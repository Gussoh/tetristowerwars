/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model.winningcondition;

import java.text.NumberFormat;
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
public class HeightWinningCondition extends WinningCondition {

    private final int height;
    private BuildingBlock highestBlock = null;
    private long startTime;
    private final long winningTimer = 5000;
    private Player leader;
    private int leaderHeight;

    public HeightWinningCondition(GameModel model, int height) {
        super(model);
        this.height = height;
    }

    @Override
    public boolean gameIsOver() {
        ScoreEntry score = getLeader();
        leader = score.getPlayer();
        leaderHeight = score.getScore();

        if (leaderHeight >= height) {
            BuildingBlock block = leader.getHighestBuilingBlockInTower();
            if (highestBlock != block) {
                highestBlock = block;
                startTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() >= startTime + winningTimer && highestBlock == block) {
                return true;
            }
        } else {
            highestBlock = null;
        }
        return false;
    }

    @Override
    public List<MessageEntry> getStatusMessage() {
        ArrayList<MessageEntry> message = new ArrayList<MessageEntry>();
        for (ScoreEntry scoreEntry : getScores()) {
            MessageType type = height - scoreEntry.getScore() < 20 ? MessageType.CRITICAL : MessageType.NORMAL;
            message.add(new MessageEntry(Math.max(height - scoreEntry.getScore(), 0) + "m left to win!", type, scoreEntry.getPlayer()));
        }
        return message;
    }
}
