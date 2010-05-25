/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network;

import java.io.DataOutputStream;
import org.tetristowerwars.control.InputEvent;
import org.tetristowerwars.control.InputListener;
import org.tetristowerwars.control.InputManager;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.network.message.InputEventMessage;

/**
 *
 * @author Andreas
 */
public class EventTransmitter implements InputListener {

    private final Connection connection;
    private final InputManager inputManager;
    private final short ownClientId;

    public EventTransmitter(Connection connection, InputManager inputManager, short ownClientId) {
        this.connection = connection;
        this.inputManager = inputManager;
        this.ownClientId = ownClientId;

        inputManager.addInputListener(this);
    }

    @Override
    public void onInputDevicePressed(InputEvent event) {
        sendEventMessage(event);
    }

    @Override
    public void onInputDeviceReleased(InputEvent event) {
        sendEventMessage(event);
    }

    @Override
    public void onInputDeviceDragged(InputEvent event) {
        sendEventMessage(event);
    }

    private void sendEventMessage(InputEvent event) {
        InputEvent newEvent = new InputEvent(event.getType(), event.getPosition(), event.getActionId() + ownClientId * 1000000);
        InputEventMessage eventMessage = new InputEventMessage(ownClientId, newEvent);
        connection.send(eventMessage);
    }

    public void stop() {
        inputManager.removeInputListener(this);
    }
}
