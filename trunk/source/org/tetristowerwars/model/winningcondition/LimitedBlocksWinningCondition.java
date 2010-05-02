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
    private long startTimeMs;
    private final long winningTimeMs;
    private BuildingBlock highestBlock = null;

    public LimitedBlocksWinningCondition(GameModel model, int maxBlocks, long winningTimeS) {
        super(model);
        this.maxBlocks = maxBlocks;
        this.winningTimeMs = winningTimeS * 1000;
    }

    @Override
    public void reset() {
        startTimeMs = 0;
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
        for (Player player : model.getPlayers()) {
            if (player.getBuildingBlocks().size() > maxBlocks) {
                BuildingBlock block = getLeader().getPlayer().getHighestBuilingBlockInTower();
                if (block != highestBlock) {
                    highestBlock = block;
                    startTimeMs = System.currentTimeMillis();
                }
                return true;
            }
        }

        return false;
    }
    @Override
    public int timeLeftUntilGameOver() {
        if (hasCandidate()) {
            long timeLeftMs = startTimeMs + winningTimeMs - System.currentTimeMillis();

            if (timeLeftMs < 0) {
                return 0;
            } else if (timeLeftMs > winningTimeMs - 2000) {
                return -1;
            } else {
                return (int) (timeLeftMs / 1000) + 1;
            }
        } else {
            return -1;
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
