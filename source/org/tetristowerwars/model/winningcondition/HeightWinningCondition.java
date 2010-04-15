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

    private final float height;
    private BuildingBlock highestBlock = null;
    private long startTime;
    private final long winningTimer = 5000;
    private Player leader;
    private float leaderHeight;
    private final NumberFormat numForm;

    public HeightWinningCondition(GameModel model, float height) {
        super(model);
        this.height = height;
        numForm = NumberFormat.getNumberInstance();
        numForm.setMaximumFractionDigits(1);
        numForm.setMinimumFractionDigits(1);
    }

    @Override
    public boolean gameIsOver() {
        ScoreEntry<Player, Float> score = getLeader();
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
        for (ScoreEntry<Player, Float> scoreEntry : getScores()) {
            MessageType type = height - scoreEntry.getScore() < 20 ? MessageType.CRITICAL : MessageType.NORMAL;
            message.add(new MessageEntry(numForm.format(Math.max(height - scoreEntry.getScore(), 0)) + "m left to win!", type, scoreEntry.getPlayer()));
        }
        return message;
    }
}
