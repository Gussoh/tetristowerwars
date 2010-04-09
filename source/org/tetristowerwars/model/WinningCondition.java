/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Reeen
 */
public abstract class WinningCondition {
    protected final GameModel model;

    public WinningCondition(GameModel model) {
        this.model = model;
        setWinningCondition();
    }

    protected void setWinningCondition() {
        model.addWinningCondition(this);
    }

    public abstract boolean gameIsOver();

    public Player getLeader() {
        List<Player> players = model.getPlayers();
        Player leader = null;
        float highest = 0;
        for (Player player : players) {
            float pHeight = player.getTowerHeight();
            if(pHeight > highest) {
                highest = pHeight;
                leader = player;
            }
        }
        return leader;
    }

    public ArrayList getScores() {
        ArrayList<ScoreEntry<Player, Float>> scores = new ArrayList<ScoreEntry<Player, Float>>();
        List<Player> players = model.getPlayers();
        for (Player player : players) {
            scores.add(new ScoreEntry(player, player.getTowerHeight()));
        }
        Collections.sort(scores);

        return scores;
    }
    
    public class ScoreEntry<Player, Float> implements Comparable<ScoreEntry>{
        private final Player player;
        private final float score;

        private ScoreEntry (Player player, float score) {
            this.player = player;
            this.score = score;

        }

        public Player getPlayer() {
            return player;
        }

        public float getScore() {
            return score;
        }

        @Override
        public int compareTo(ScoreEntry e) {
            return (int) (e.score - this.score);
        }
    }

}