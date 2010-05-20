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
    private float startTimeS;
    private final float winningTimeS;
    private Player leader;
    private int leaderHeight;

    public HeightWinningCondition(GameModel model, int height, float winningTimeS) {
        super(model);
        this.height = height;
        this.winningTimeS = winningTimeS;
        reset();
    }

    @Override
    public void reset() {
        startTimeS = 0;
        highestBlock = null;
    }

    @Override
    public void update() {
        ScoreEntry score = getLeader();
        leader = score.getPlayer();
        leaderHeight = score.getScore();

        if (leaderHeight >= height) {
            BuildingBlock block = leader.getHighestBuilingBlockInTower();
            if (highestBlock != block) {
                highestBlock = block;
                startTimeS = model.getElapsedGameTimeS();
            }
        } else {
            highestBlock = null;
        }
    }

    public boolean hasCandidate() {
        return leaderHeight >= height;
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
        return height;
    }
}
