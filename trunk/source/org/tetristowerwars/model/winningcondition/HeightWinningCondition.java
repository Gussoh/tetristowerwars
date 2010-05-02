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
    private long startTimeMs;
    private final long winningTimeMs;
    private Player leader;
    private int leaderHeight;

    public HeightWinningCondition(GameModel model, int height, long winningTimeS) {
        super(model);
        this.height = height;
        this.winningTimeMs = winningTimeS * 1000;
        reset();
    }

    @Override
    public void reset() {
        startTimeMs = 0;
        highestBlock = null;
    }
   
    public boolean hasCandidate() {
        ScoreEntry score = getLeader();
        leader = score.getPlayer();
        leaderHeight = score.getScore();

        if (leaderHeight >= height) {
            BuildingBlock block = leader.getHighestBuilingBlockInTower();
            if (highestBlock != block) {
                highestBlock = block;
                startTimeMs = System.currentTimeMillis();
            }
            return true;
        } else {
            highestBlock = null;
            return false;
        }
    }

    @Override
    public List<MessageEntry> getStatusMessages() {
        ArrayList<MessageEntry> message = new ArrayList<MessageEntry>();
        for (ScoreEntry scoreEntry : getScores()) {
            MessageType type = height - scoreEntry.getScore() < 20 ? MessageType.CRITICAL : MessageType.NORMAL;
            message.add(new MessageEntry(Math.max(height - scoreEntry.getScore(), 0) + "m left to win!", type, scoreEntry.getPlayer()));
        }
        return message;
    }

    @Override
    public int timeLeftUntilGameOver() {
        if (hasCandidate()) {
            long timeLeftMs = startTimeMs + winningTimeMs - System.currentTimeMillis();

            if (timeLeftMs < 0) {
                return 0;
            } else if(timeLeftMs > winningTimeMs - 2000) {
                return -1;
            } else {
                return (int) (timeLeftMs / 1000) + 1;
            }
        } else {
            return -1;
        }
    }
}
