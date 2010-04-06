/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    public Player getWinner() {
        List<Player> players = model.getPlayers();
        Player winner = null;
        float highest = 0;
        for (Player player : players) {
            float pHeight = player.getTowerHeight();
            if(pHeight > highest) {
                highest = pHeight;
                winner = player;
            }
        }
        return winner;
    }

    public Map getScores() {
        TreeMap<Player, Integer> scores = new TreeMap<Player, Integer>();
        List<Player> players = model.getPlayers();
        for (Player player : players) {
            scores.put(player, player.getScore());
        }
        return scores;
    }

}