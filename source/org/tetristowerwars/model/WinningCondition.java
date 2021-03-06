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

    public final static int UNKNOWN_TIME_LEFT = -1;
    protected final GameModel model;
    private int lastTimeLeftUntilGameOver = UNKNOWN_TIME_LEFT;

    public WinningCondition(GameModel model) {
        this.model = model;
    }

    public void setWinningCondition() {
        model.setWinningCondition(this);
    }

    public abstract float getWinningHeight(Player player);
        

    public boolean isGameOver() {
        int timeLeft = timeLeftUntilGameOver();
        if (timeLeft != lastTimeLeftUntilGameOver) {
            lastTimeLeftUntilGameOver = timeLeft;
            model.fireTimeLeftBeat(timeLeft);
        }
        
        return timeLeftUntilGameOver() == 0;
    }

    public abstract List<MessageEntry> getStatusMessages();

    /**
     *
     * @return UNKNOWN_TIME_LEFT if unknown, otherwise the number of seconds.
     */
    public abstract int timeLeftUntilGameOver();

    public ScoreEntry getLeader() {
        List<ScoreEntry> scores = getScores();
        return scores.get(0);
    }

    public List<ScoreEntry> getScores() {
        ArrayList<ScoreEntry> scores = new ArrayList<ScoreEntry>();
        List<Player> players = model.getPlayers();
        for (Player player : players) {
            scores.add(new ScoreEntry(player, (int) Math.ceil(player.getTowerHeight())));
        }
        Collections.sort(scores);

        return scores;
    }

    public abstract void reset();
    public abstract void update();

    public class ScoreEntry implements Comparable<ScoreEntry> {

        private final Player player;
        private final int score;

        private ScoreEntry(Player player, int score) {
            this.player = player;
            this.score = score;
        }

        public Player getPlayer() {
            return player;
        }

        public int getScore() {
            return score;
        }

        @Override
        public int compareTo(ScoreEntry e) {
            return e.score - this.score;
        }
    }

    public class MessageEntry {

        private final String text;
        private final MessageType type;
        private final Player player;

        /**
         *
         * @param text The message
         * @param type For formatting
         * @param player Null for a general message
         */
        public MessageEntry(String text, MessageType type, Player player) {
            this.text = text;
            this.type = type;
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }

        public String getText() {
            return text;
        }

        public MessageType getType() {
            return type;
        }
    }

    public enum MessageType {

        NORMAL, CRITICAL
    }
}
