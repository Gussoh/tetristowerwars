/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;

/**
 *
 * @author Andreas
 */
public class TutorialTriggerBlock extends TriggerBlock {

    public TutorialTriggerBlock(Body body, TriggerListener triggerListener) {
        super(body, "Start!", triggerListener);
    }


}
