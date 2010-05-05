/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.winningcondition;

import java.util.ArrayList;
import java.util.List;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.WinningCondition;

/**
 *
 * @author Reeen
 */
public class TimedWinningCondition extends WinningCondition {

    private long endTimeMs;
    private final long gameTime;

    /**
     *
     * @param model
     * @param gameTime Game time in s.
     */
    public TimedWinningCondition(GameModel model, long gameTime) {
        super(model);
        this.gameTime = gameTime;
        reset();
    }

    @Override
    public void reset() {
        endTimeMs = System.currentTimeMillis() + gameTime * 1000;
    }

    @Override
    public boolean isGameOver() {
        return System.currentTimeMillis() > endTimeMs;
    }

    @Override
    public int timeLeftUntilGameOver() {

            long timeLeftMs = endTimeMs - System.currentTimeMillis();

            if (timeLeftMs < 0) {
                return 0;
            } else if (timeLeftMs < 10000) {
                return (int) (timeLeftMs / 1000) + 1;
            } else {
                return -1;
            }
    }

    @Override
    public List<MessageEntry> getStatusMessages() {
        long time = endTimeMs - System.currentTimeMillis();
        ArrayList<MessageEntry> message = new ArrayList<MessageEntry>(1);
        MessageType type = time < 10000 ? MessageType.CRITICAL : MessageType.NORMAL;
        message.add(new MessageEntry("" + Math.max(time / 1000 + 1, 0), type, null));
        return message;
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
