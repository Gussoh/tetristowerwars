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
public class LimitedBlocksWinningCondition extends WinningCondition {
    private final int maxBlocks;

    public LimitedBlocksWinningCondition(GameModel model, int maxBlocks) {
        super(model);
        this.maxBlocks = maxBlocks;
    }

    @Override
    public boolean gameIsOver() {
       //TODO: which pieces to count? Global? Player?
       for (Player player : model.getPlayers()) {
           if (player.getBuildingBlocks().size() > maxBlocks) {
               return true;
           }
       }
       return false;
    }

    @Override
    public List<MessageEntry> getStatusMessage() {
        ArrayList<MessageEntry> messages = new ArrayList<MessageEntry>();

        for (Player player : model.getPlayers()) {
            int blocksLeft = maxBlocks - player.getBuildingBlocks().size();
            MessageType type = blocksLeft < 10 ? MessageType.CRITICAL : MessageType.NORMAL;
            messages.add(new MessageEntry(blocksLeft + " blocks left", type, player));
        }
        return messages;
    }

}
