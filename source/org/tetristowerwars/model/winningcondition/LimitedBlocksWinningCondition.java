/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model.winningcondition;

import java.util.ArrayList;
import java.util.List;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.WinningCondition;

/**
 *
 * @author Reeen
 */
public class LimitedBlocksWinningCondition extends WinningCondition {

    private final int maxBlocks;
    private float startTimeS;
    private final float winningTimeS;
    private BuildingBlock highestBlock = null;
    private boolean candidateFound;


    public LimitedBlocksWinningCondition(GameModel model, int maxBlocks, float winningTimeS) {
        super(model);
        this.maxBlocks = maxBlocks;
        this.winningTimeS = winningTimeS;
    }

    @Override
    public void reset() {
        startTimeS = 0;
    }

    @Override
    public void update() {
        int numOwnedBlocks = maxBlocks;
        candidateFound = false;
        for (Player player : model.getPlayers()) {
            if (player.getBuildingBlocks().size() > numOwnedBlocks) {
                numOwnedBlocks = player.getBuildingBlocks().size();
                candidateFound = true;
                BuildingBlock block = getLeader().getPlayer().getHighestBuilingBlockInTower();
                if (block != highestBlock) {
                    highestBlock = block;
                    startTimeS = model.getElapsedGameTimeS();
                }
            }
        }
    }


    @Override
    public List<MessageEntry> getStatusMessages() {
        ArrayList<MessageEntry> messages = new ArrayList<MessageEntry>();

        for (Player player : model.getPlayers()) {
            int blocksLeft = maxBlocks - player.getBuildingBlocks().size();
            MessageType type = blocksLeft < 10 ? MessageType.CRITICAL : MessageType.NORMAL;
            messages.add(new MessageEntry(blocksLeft + " blocks left", type, player));
        }
        return messages;
    }

    public boolean hasCandidate() {
        return candidateFound;
    }
    @Override
    public int timeLeftUntilGameOver() {
        if (hasCandidate()) {
            int timeLeftS = (int) (startTimeS + winningTimeS - model.getElapsedGameTimeS());

            if (timeLeftS < 0) {
                return 0;
            } else if (timeLeftS > winningTimeS - 2) {
                return UNKNOWN_TIME_LEFT;
            } else {
                return timeLeftS + 1;
            }
        } else {
            return UNKNOWN_TIME_LEFT;
        }
    }

    @Override
    public float getWinningHeight(Player player) {
        float highest = 0;
        for (Player otherPlayer : model.getPlayers()) {
            if (otherPlayer != player) {
                highest = Math.max(highest, otherPlayer.getTowerHeight());
            }
        }

        return highest;
    }
}
