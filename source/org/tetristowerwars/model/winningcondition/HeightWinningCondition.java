/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.winningcondition;

import java.util.ArrayList;
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
    private float leaderScore;

    public HeightWinningCondition(GameModel model, float height) {
        super(model);
        this.height = height+model.getGroundLevel();
    }

    @Override
    public boolean gameIsOver() {
        ArrayList<ScoreEntry<Player, Float>> scores = getScores();
        leader = scores.get(0).getPlayer();
        leaderScore = scores.get(0).getScore();

        if (leaderScore >= height) {
            BuildingBlock block = leader.getHighestBuilingBlockInTower();
            if(highestBlock != block) {
                highestBlock = block;
                startTime = System.currentTimeMillis();
            }
            else if (System.currentTimeMillis() >= startTime+winningTimer && highestBlock == block) {
                return true;
            }
        }
        else {
            highestBlock = null;
        }
        return false;
    }

    @Override
    public String getStatusMessage() {
        ArrayList<ScoreEntry<Player, Float>> scores = getScores();
        leaderScore = scores.get(0).getScore();
        return (height - leaderScore) + "m left to win!";
    }

}
