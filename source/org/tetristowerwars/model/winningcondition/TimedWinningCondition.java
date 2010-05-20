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

    private final float endTimeS;
    private final float winningTimeS;

    /**
     *
     * @param model
     * @param endTimeS Game time in seconds.
     */
    public TimedWinningCondition(GameModel model, float endTimeS, float winningTimeS) {
        super(model);
        this.endTimeS = endTimeS;
        this.winningTimeS = winningTimeS;
    }

    @Override
    public void reset() {
    }

    @Override
    public void update() {
    }



    @Override
    public boolean isGameOver() {
        return model.getElapsedGameTimeS() > endTimeS;
    }

    @Override
    public int timeLeftUntilGameOver() {

            int timeLeftS = (int) (endTimeS - model.getElapsedGameTimeS());

            if (timeLeftS < 0) {
                return 0;
            } else if (timeLeftS < winningTimeS) {
                return timeLeftS + 1;
            } else {
                return UNKNOWN_TIME_LEFT;
            }
    }

    @Override
    public List<MessageEntry> getStatusMessages() {
        int timeLeft = (int) (endTimeS - model.getElapsedGameTimeS());
        ArrayList<MessageEntry> message = new ArrayList<MessageEntry>(1);
        MessageType type = timeLeft < winningTimeS ? MessageType.CRITICAL : MessageType.NORMAL;
        message.add(new MessageEntry("" + Math.max(timeLeft + 1, 0), type, null));
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
