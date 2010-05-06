/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

/**
 *
 * @author Administrator
 */
public interface TriggerListener {

    public void onTriggerPressed(TriggerBlock triggerBlock);

    public void onTriggerReleased(TriggerBlock triggerBlock);

    public void onTriggerHold(TriggerBlock triggerBlock);
}
