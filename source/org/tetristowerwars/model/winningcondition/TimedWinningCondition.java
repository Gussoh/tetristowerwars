/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.winningcondition;

import java.util.ArrayList;
import java.util.List;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.WinningCondition;

/**
 *
 * @author Reeen
 */
public class TimedWinningCondition extends WinningCondition {

    private long endTime;

    /**
     *
     * @param model
     * @param gameTime Game time in s.
     */
    public TimedWinningCondition(GameModel model, long gameTime) {
        super(model);
        endTime = System.currentTimeMillis() + gameTime * 1000;
    }

    @Override
    public boolean gameIsOver() {
        return System.currentTimeMillis() > endTime;
    }

    @Override
    public List<MessageEntry> getStatusMessage() {
        long time = endTime - System.currentTimeMillis();
        ArrayList<MessageEntry> message = new ArrayList<MessageEntry>(1);
        MessageType type = time < 10000 ? MessageType.CRITICAL : MessageType.NORMAL;
        message.add(new MessageEntry("Time remaining: " + Math.max(Math.round(time/1000), 0), type, null));
        return message;
    }

}
