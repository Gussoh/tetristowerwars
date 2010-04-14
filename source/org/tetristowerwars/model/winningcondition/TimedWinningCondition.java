/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.winningcondition;

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
     * @param gameTime Game time in ms.
     */
    public TimedWinningCondition(GameModel model, long gameTime) {
        super(model);
        endTime = System.currentTimeMillis() + gameTime;
    }

    @Override
    public boolean gameIsOver() {
        return System.currentTimeMillis() > endTime;
    }

    @Override
    public String getStatusMessage() {
        long time = endTime - System.currentTimeMillis();
        return "Time remaining: " + time/1000;
    }

}
