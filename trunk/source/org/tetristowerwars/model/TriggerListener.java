/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.tetristowerwars.control.Controller;

/**
 *
 * @author Administrator
 */
public interface TriggerListener {

    public void onTriggerPressed(TriggerBlock triggerBlock, Controller controller);

    public void onTriggerReleased(TriggerBlock triggerBlock, Controller controller);

    public void onTriggerHold(TriggerBlock triggerBlock, Controller controller);
}
