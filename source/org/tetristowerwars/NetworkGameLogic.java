/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.control.Controller;
import org.tetristowerwars.control.InputManager;
import org.tetristowerwars.control.MouseInputManager;
import org.tetristowerwars.gui.GLRenderer;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.model.BuildingBlockFactory;
import org.tetristowerwars.model.CannonFactory;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.TriggerBlock;
import org.tetristowerwars.model.TriggerListener;
import org.tetristowerwars.model.WinningCondition;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.winningcondition.CompoundWinningCondition;
import org.tetristowerwars.model.winningcondition.HeightWinningCondition;
import org.tetristowerwars.model.winningcondition.LimitedBlocksWinningCondition;
import org.tetristowerwars.model.winningcondition.TimedWinningCondition;
import org.tetristowerwars.network.ClientEntry;
import org.tetristowerwars.network.NetworkClient;
import org.tetristowerwars.network.NetworkClientListener;
import org.tetristowerwars.network.NetworkServer;
import org.tetristowerwars.network.message.SpawnBuildingBlockMessage;
import org.tetristowerwars.sound.SoundPlayer;

/**
 *
 * @author Andreas
 */
public class NetworkGameLogic {

    private final static int MAX_NUM_UNPROCESSED_FRAMES = 12;
    private final static float NANO_FACTOR = 1000000000.0f;
    private final MainFrame mainFrame;
    private final float playerAreaWidth;
    private final GameModel gameModel;
    private final Renderer renderer;
    private final SoundPlayer soundPlayer;
    private final NetworkClient networkClient;
    private final NetworkServer networkServer;
    private final ProgressMonitor progressMonitor;

    public NetworkGameLogic(final MainFrame mainFrame, final NetworkClient networkClient, final NetworkServer networkServer) {
        this.mainFrame = mainFrame;
        this.networkClient = networkClient;
        this.networkServer = networkServer;
        final Settings networkSettings = networkClient.getSettings();
        final Settings personalSettings = mainFrame.getSettings();

        gameModel = new GameModel(networkSettings.getWorldWidth(), networkSettings.getWorldHeight(), networkSettings.getGroundHeight(), networkSettings.getBlockSize());
        renderer = new GLRenderer(gameModel, mainFrame);
        this.progressMonitor = new ProgressMonitor(mainFrame.getJFrame(), "Starting game...", "Loading...", 0, 5);
        progressMonitor.setMillisToPopup(1);
        progressMonitor.setMillisToDecideToPopup(1);
        progressMonitor.setProgress(1);

        soundPlayer = new SoundPlayer(personalSettings.isPlayMusicEnabled(), personalSettings.isPlaySoundEffectsEnabled(), personalSettings.getWorldTheme());
        gameModel.addGameModelListener(soundPlayer);


        playerAreaWidth = networkSettings.getWorldWidth() * (networkSettings.getPlayerArea() * 0.005f);

        final Player player1 = gameModel.createPlayer(networkSettings.getLeftTeamName(), 0, playerAreaWidth);
        final Player player2 = gameModel.createPlayer(networkSettings.getRightTeamName(), networkSettings.getWorldWidth() - playerAreaWidth, networkSettings.getWorldWidth());

        CannonFactory cannonFactory = gameModel.getCannonFactory();
        cannonFactory.createBasicCannon(player1, new Vec2(playerAreaWidth, networkSettings.getGroundHeight()), false);
        cannonFactory.createBasicCannon(player2, new Vec2(networkSettings.getWorldWidth() - playerAreaWidth, networkSettings.getGroundHeight()), true);

        new GameLoop().start();
    }

    private class GameLoop extends Thread implements NetworkClientListener {

        private boolean resetGame = false;
        private boolean alive = true;
        private Semaphore syncedStartSemaphore = new Semaphore(0);
        private int numBlockSpawning = 0;

        public GameLoop() {
            networkClient.addNetworkClientListener(this);
        }

        @Override
        public void run() {
            final Settings networkSettings = networkClient.getSettings();
            final List<Controller> controllers = networkClient.createControllers(gameModel, renderer);

            final LinkedList<WinningCondition> winningConditions = new LinkedList<WinningCondition>();
            if (networkSettings.isHeightConditionEnabled()) {
                winningConditions.add(new HeightWinningCondition(gameModel, networkSettings.getHeightCondition(), 16));
            }

            if (networkSettings.isNumBlocksConditionEnabled()) {
                winningConditions.add(new LimitedBlocksWinningCondition(gameModel, networkSettings.getNumBlocksCondition(), 16));
            }

            if (networkSettings.isTimeConditionEnabled()) {
                winningConditions.add(new TimedWinningCondition(gameModel, networkSettings.getTimeCondition(), 16));
            }

            CompoundWinningCondition.LogicType logicType = networkSettings.mustAllWinningConditionsBeMet() ? CompoundWinningCondition.LogicType.AND : CompoundWinningCondition.LogicType.OR;
            CompoundWinningCondition cwc = new CompoundWinningCondition(gameModel, winningConditions, logicType);
            cwc.setWinningCondition();



            TriggerBlock restartTrigger = gameModel.getTriggerBlockFactory().createRoundTrigger(new Vec2(networkSettings.getWorldWidth() / 2.0f, gameModel.getGroundLevel() * 4.0f), 25.0f, "Restart", new TriggerListener() {

                @Override
                public void onTriggerPressed(TriggerBlock triggerBlock) {
                    resetGame = true;
                }

                @Override
                public void onTriggerReleased(TriggerBlock triggerBlock) {
                }

                @Override
                public void onTriggerHold(TriggerBlock triggerBlock) {
                }
            });

            try {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        progressMonitor.setProgress(2);
                        progressMonitor.setNote("Loading graphics...");
                    }
                });

                Thread.sleep(500);
                renderer.renderFrame();

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        progressMonitor.setProgress(3);
                        progressMonitor.setNote("Loading...");
                    }
                });

                Thread.sleep(2000); // Let the resources get loaded since they might load in another thread

            } catch (InterruptedException ex) {
            }
            try {
                networkClient.ready();
            } catch (IOException ex) {
                Logger.getLogger(NetworkGameLogic.class.getName()).log(Level.SEVERE, null, ex);
                maybeGoBack(ex.getMessage());
                return;
            }

            try {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        progressMonitor.setProgress(4);
                        progressMonitor.setNote("Waiting for other players...");
                    }
                });
                syncedStartSemaphore.acquire();
            } catch (InterruptedException ex) {
                maybeGoBack("Failed when syncing the game start.");
                return;
            }


            final InputManager mouseInputManager = new MouseInputManager(renderer);
            networkClient.startSendingUserInput(mouseInputManager);

            if (networkServer != null) {
                networkServer.sendEndOfFrame();
            }

            final float constantStepTimeS = 1f / 60f;
            long lastStepTimeNano = System.nanoTime();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    progressMonitor.close();
                }
            });
            
            int loopCount = 0;
            while (alive) {
                Thread.yield();

                long currentTimeNano = System.nanoTime();
                long stepTimeNano = currentTimeNano - lastStepTimeNano;
                int numTimesStepped = 0;
                // This should hopefully make the client run smoothly
                while (networkClient.getNumUnprocessedFrames() == 0) {
                    Thread.yield();
                    currentTimeNano = System.nanoTime();
                }

                if (networkClient.getNumUnprocessedFrames() > MAX_NUM_UNPROCESSED_FRAMES) {
                    stepTimeNano += (long) (constantStepTimeS * NANO_FACTOR);
                }


                if (stepTimeNano < 0) { // in case of nanoTime wrapping around
                    stepTimeNano = (long) (constantStepTimeS * NANO_FACTOR);
                }

                while (stepTimeNano > (long) (constantStepTimeS * NANO_FACTOR) && networkClient.getNumUnprocessedFrames() > 0) {

                    networkClient.processNextFrame();

                    for (Controller controller : controllers) {
                        controller.pumpEvents();
                    }

                    gameModel.update();
                    numTimesStepped++;
                    stepTimeNano -= (long) (constantStepTimeS * NANO_FACTOR);

                    if (resetGame) {
                        restartTrigger.setVisible(false);
                        gameModel.reset();
                        resetGame = false;
                    }

                    if (gameModel.isGameOver() && !restartTrigger.isVisible()) {
                        restartTrigger.setVisible(true);
                    }

                    if (networkServer != null) {
                        // Running the server as well!
                        if (gameModel.getBuildingBlockPool().size() + numBlockSpawning <= 2) {
                            float left = gameModel.getPlayers().get(0).getRightLimit();
                            float right = gameModel.getPlayers().get(1).getLeftLimit();
                            float yPos = gameModel.getWorldBoundries().upperBound.y - gameModel.getBlockSize() * 5;
                            for (int i = 0; i < 7; i++) {
                                numBlockSpawning++;
                                networkServer.createRandomBuildingBlock(left, right, yPos);
                            }
                        }

                        int powerUpInterval = (int) (networkSettings.getSecondsBetweenPowerups() / constantStepTimeS);
                        if (networkSettings.isPowerups() && loopCount % powerUpInterval == powerUpInterval - 1) {
                            networkServer.createPowerUpBlock();
                        }
                        networkServer.sendEndOfFrame();
                    }


                    loopCount++;
                }

                lastStepTimeNano = currentTimeNano - stepTimeNano; // Save remaining step time

                if (numTimesStepped > 0) {
                    renderer.renderFrame();
                }

            }
            maybeGoBack(null);
        }

        @Override
        public void allClientsReady() {
            System.out.println("semaphore released!");
            syncedStartSemaphore.release();
        }

        @Override
        public void chatMessageReceive(ClientEntry clientEntry, String message) {
        }

        @Override
        public void clientConnected(ClientEntry clientEntry) {
        }

        @Override
        public void onOwnClientIdSet(short ownClientId) {
        }

        @Override
        public void spawnBuildingBlock(Vec2 position, Material material, short shape) {
            if (networkServer != null) {
                numBlockSpawning--;
            }
            BuildingBlockFactory bbf = gameModel.getBuildingBlockFactory();
            switch (shape) {
                case SpawnBuildingBlockMessage.CROSS:
                    bbf.createCrossBlock(position, material);
                    break;
                case SpawnBuildingBlockMessage.LEFT_L:
                    bbf.createLeftLBlock(position, material);
                    break;
                case SpawnBuildingBlockMessage.LEFT_S:
                    bbf.createLeftSBlock(position, material);
                    break;
                case SpawnBuildingBlockMessage.LINE:
                    bbf.createLineBlock(position, material);
                    break;
                case SpawnBuildingBlockMessage.PYRAMID:
                    bbf.createPyramidBlock(position, material);
                    break;
                case SpawnBuildingBlockMessage.RIGHT_L:
                    bbf.createRightLBlock(position, material);
                    break;
                case SpawnBuildingBlockMessage.RIGHT_S:
                    bbf.createRightSBlock(position, material);
                    break;
                case SpawnBuildingBlockMessage.SQUARE:
                    bbf.createSquareBlock(position, material);
                    break;

            }
        }

        @Override
        public void spawnPowerUpBlock() {
            List<Player> players = gameModel.getPlayers();
            gameModel.getPowerupFactory().createPowerUp(players.get(0), true);
            gameModel.getPowerupFactory().createPowerUp(players.get(1), false);
        }

        @Override
        public void gameStarted() {
        }

        @Override
        public void endOfFramePosted(int unprocessedFrames) {
        }

        @Override
        public void clientDisconnected(ClientEntry clientEntry) {
        }

        @Override
        public void onConnectionError(final String message) {
            maybeGoBack(message);
        }

        @Override
        public void onConnectionClosed() {
            maybeGoBack(null);
        }

        @Override
        public void onClientPropertyChanged(ClientEntry clientEntry) {
        }

        @Override
        public void onSettingsReceived(Settings settings) {
        }

        public synchronized void maybeGoBack(final String message) {
            if (alive) {
                alive = false;
                soundPlayer.stopAllMusic();
                soundPlayer.unloadAllSounds();
                if (networkServer != null) {
                    networkServer.stop();
                }
                networkClient.stop();
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        mainFrame.back();
                        if (message != null) {
                            JOptionPane.showMessageDialog(mainFrame.getJFrame(), message);
                        }
                    }
                });
            }
        }
    }
}

