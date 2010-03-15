/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.control;

/**
 *
 * @author Andreas
 */
public interface InputListener {

    public void onInputDevicePressed(InputEvent event);

    public void onInputDeviceReleased(InputEvent event);

    public void onInputDeviceDragged(InputEvent event);
}
