/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import javax.swing.SwingUtilities;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.control.Controller;
import org.tetristowerwars.control.InputManager;
import org.tetristowerwars.control.MouseInputManager;
import org.tetristowerwars.control.TouchInputManager;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.model.BuildingBlockFactory;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.CannonFactory;
import org.tetristowerwars.model.TriggerBlock;
import org.tetristowerwars.model.TriggerListener;
import org.tetristowerwars.model.WinningCondition;
import org.tetristowerwars.model.material.BrickMaterial;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;
import org.tetristowerwars.model.material.WoodMaterial;
import org.tetristowerwars.model.winningcondition.CompoundWinningCondition;
import org.tetristowerwars.model.winningcondition.HeightWinningCondition;
import org.tetristowerwars.model.winningcondition.LimitedBlocksWinningCondition;
import org.tetristowerwars.model.winningcondition.TimedWinningCondition;
import org.tetristowerwars.sound.SoundPlayer;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class TouchGameLogic {

    private boolean resetGame = false;
    private boolean timedReset = true;
    private boolean alive = true;
    private long resetTime = 0;

    public TouchGameLogic(final MainFrame mainFrame) {



        DisplayMode displayMode = mainFrame.getJFrame().getGraphicsConfiguration().getDevice().getDisplayMode();
        Dimension screenDimensions = new Dimension(displayMode.getWidth(), displayMode.getHeight());
        final Settings settings = mainFrame.getSettings();

        final GameModel gameModel = new GameModel(settings.getWorldWidth(), settings.getWorldHeight(), settings.getGroundHeight(), settings.getBlockSize());
        final Renderer glRenderer = new org.tetristowerwars.gui.GLRenderer(gameModel, mainFrame);

        final SoundPlayer soundPlayer = new SoundPlayer(settings.isPlayMusicEnabled(), settings.isPlaySoundEffectsEnabled(), settings.getWorldTheme());
        gameModel.addGameModelListener(soundPlayer);

        final InputManager mouseInputManager = new MouseInputManager(glRenderer);
        final InputManager touchInputManager = new TouchInputManager(mainFrame.getTuioClient(), screenDimensions, glRenderer);


        final float playerAreaWidth = settings.getWorldWidth() * (settings.getPlayerArea() * 0.005f);

        final Player player1 = gameModel.createPlayer(settings.getLeftTeamName(), 0, playerAreaWidth);
        final Player player2 = gameModel.createPlayer(settings.getRightTeamName(), settings.getWorldWidth() - playerAreaWidth, settings.getWorldWidth());

        final Controller mouseController = new Controller(gameModel, mouseInputManager, glRenderer);
        final Controller touchController = new Controller(gameModel, touchInputManager, glRenderer);

        CannonFactory cannonFactory = gameModel.getCannonFactory();
        cannonFactory.createBasicCannon(player1, new Vec2(playerAreaWidth, settings.getGroundHeight()), false);
        cannonFactory.createBasicCannon(player2, new Vec2(settings.getWorldWidth() - playerAreaWidth, settings.getGroundHeight()), true);

        LinkedList<WinningCondition> winningConditions = new LinkedList<WinningCondition>();
        if (settings.isHeightConditionEnabled()) {
            winningConditions.add(new HeightWinningCondition(gameModel, settings.getHeightCondition(), 16));
        }

        if (settings.isNumBlocksConditionEnabled()) {
            winningConditions.add(new LimitedBlocksWinningCondition(gameModel, settings.getNumBlocksCondition(), 16));
        }

        if (settings.isTimeConditionEnabled()) {
            winningConditions.add(new TimedWinningCondition(gameModel, settings.getTimeCondition(), 16));
        }

        CompoundWinningCondition.LogicType logicType = settings.mustAllWinningConditionsBeMet() ? CompoundWinningCondition.LogicType.AND : CompoundWinningCondition.LogicType.OR;
        CompoundWinningCondition cwc = new CompoundWinningCondition(gameModel, winningConditions, logicType);
        cwc.setWinningCondition();

        mainFrame.setMousePosition(screenDimensions.height, screenDimensions.width);
        mainFrame.disableMouseEmulation();

        glRenderer.getInputComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    resetGame = true;
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    alive = false;
                    mainFrame.back();
                }
            }
        });

        new Thread() {

            @Override
            public void run() {
                final float constantStepTimeS = 1f / 60f;
                long lastStepTimeNano = System.nanoTime();

                gameModel.getTriggerBlockFactory().createRoundTrigger(new Vec2(settings.getWorldWidth() / 2.0f, 0f), 15.0f, "Exit", new TriggerListener() {

                    private long timePressed;

                    @Override
                    public void onTriggerPressed(TriggerBlock triggerBlock) {
                        timePressed = System.currentTimeMillis();
                        triggerBlock.setText("Hold");
                    }

                    @Override
                    public void onTriggerReleased(TriggerBlock triggerBlock) {
                        triggerBlock.setText("Exit");
                    }

                    @Override
                    public void onTriggerHold(TriggerBlock triggerBlock) {
                        if (timePressed + 2000 < System.currentTimeMillis()) {
                            alive = false;
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    mainFrame.back();
                                }
                            });
                        }
                    }
                }).setVisible(true);

                TriggerBlock restartTrigger = gameModel.getTriggerBlockFactory().createRoundTrigger(new Vec2(settings.getWorldWidth() / 2.0f, gameModel.getGroundLevel() * 4.0f), 25.0f, "Restart", new TriggerListener() {

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

                int loopCount = 0;
                while (alive) {
                    Thread.yield();

                    long currentTimeNano = System.nanoTime();
                    long stepTimeNano = currentTimeNano - lastStepTimeNano;
                    int numTimesStepped = 0;

                    if (stepTimeNano < 0) { // in case of nanoTime wrapping around
                        stepTimeNano = (long) (constantStepTimeS * 1000000000.0f);
                    }



                    while (stepTimeNano > (long) (constantStepTimeS * 1000000000.0f) && numTimesStepped < 2) {
                        mouseController.pumpEvents();
                        touchController.pumpEvents();

                        gameModel.update();
                        numTimesStepped++;
                        stepTimeNano -= (long) (constantStepTimeS * 1000000000.0f);

                        if (resetGame) {
                            restartTrigger.setVisible(false);
                            gameModel.reset();
                            resetGame = false;
                            timedReset = true;
                        }

                        if (gameModel.isGameOver() && !restartTrigger.isVisible()) {
                            restartTrigger.setVisible(true);
                        }

                        if (gameModel.getBuildingBlockPool().size() <= 2) {
                            for (int i = 0; i < 7; i++) {

                                createRandomBuildingBlock(gameModel, playerAreaWidth, settings.getWorldWidth() - playerAreaWidth);
                            }
                        }

                        if (settings.isPowerups() && loopCount % (settings.getSecondsBetweenPowerups() * 60) == 0) {

                            gameModel.getPowerupFactory().createPowerUp(player1, true);
                            gameModel.getPowerupFactory().createPowerUp(player2, false);

                        }

                        loopCount++;
                    }

                    lastStepTimeNano = currentTimeNano - stepTimeNano; // Save remaining step time

                    if (numTimesStepped > 0) {
                        glRenderer.renderFrame();
                    }

                }
                soundPlayer.stopAllMusic();
                soundPlayer.unloadAllSounds();
            }
        }.start();

    }

    public void createRandomBuildingBlock(GameModel gameModel, float left, float right) {

        BuildingBlockFactory bbf = gameModel.getBuildingBlockFactory();
        double randomValue = Math.random() * 3;

        Vec2 pos = new Vec2(MathUtil.random(left, right), gameModel.getWorldBoundries().upperBound.y - gameModel.getBlockSize() * 5);
        Material material;


        if (randomValue < 1.0) {
            material = new WoodMaterial();
        } else if (randomValue < 2.0) {
            material = new BrickMaterial();
        } else {
            material = new SteelMaterial();
        }

        bbf.createRandomRectangularBuildingBlock(pos, material);
    }
}
