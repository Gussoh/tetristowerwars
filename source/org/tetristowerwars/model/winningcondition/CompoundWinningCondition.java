/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.winningcondition;

import java.util.List;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.WinningCondition;

/**
 * Used to group several WinningConditions into one.<p>
 * Can be set to check if the WinningCondition is fulfilled either by
 * AND or OR logic.
 * @author Reeen
 */
public class CompoundWinningCondition extends WinningCondition {
    private final List<WinningCondition> conditions;
    private final LogicType logicType;
/**
 *
 * @param model The GameModel
 * @param conditions A List of the WinningConditions
 * @param logicType An Enum controlling the logic to check the WinningConditions
 */
    public CompoundWinningCondition (GameModel model, List<WinningCondition> conditions, LogicType logicType) {
        super(model);
        this.conditions = conditions;
        this.logicType = logicType;
    }

    /**
     * Checks if the game is over.<br>
     * Depending on the LogicType set for this object the WinningConditions are
     * checked with AND-logic (all must be true) or OR-logic (any must be true).
     *
     * @return true if the game is over, else false
     */
    @Override
    public boolean gameIsOver() {
        switch (logicType) {
            case AND:
                for (WinningCondition winningCondition : conditions) {
                    if  (!winningCondition.gameIsOver()) {
                        return false;
                    }
                }
                return true;

            case OR:
                for (WinningCondition winningCondition : conditions) {
                    if  (winningCondition.gameIsOver()) {
                        return true;
                    }
                }
                return false;

            default: return false;
        }
    }

    public LogicType getLogicType() {
        return logicType;
    }

    @Override
    public String getStatusMessage() {
        String status = "";
        for (WinningCondition winningCondition : conditions) {
            status.concat(winningCondition.getStatusMessage()).concat("\n");
        }
        return status;
    }

    /**
     * Can be either AND or OR.
     */
    public enum LogicType {
        AND, OR
    }

}