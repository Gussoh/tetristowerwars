/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.winningcondition;

import java.util.List;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.WinningCondition;

/**
 *
 * @author Reeen
 */
public class HeightWinningCondition extends WinningCondition {
    private final float height;

    public HeightWinningCondition(GameModel model, float height) {
        super(model);
        this.height = height;
    }

    @Override
    public boolean gameIsOver() {
        List<Player> players = model.getPlayers();
        for (Player player : players) {
            if (player.getTowerHeight() >= height) {
                return true;
            }
        }
        return false;
    }

}
