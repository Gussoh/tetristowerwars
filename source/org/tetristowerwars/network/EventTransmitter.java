/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network;

import org.tetristowerwars.control.InputEvent;
import org.tetristowerwars.control.InputListener;
import org.tetristowerwars.control.InputManager;
import org.tetristowerwars.network.message.InputEventMessage;

/**
 *
 * @author Andreas
 */
public class EventTransmitter implements InputListener {

    private final Connection connection;
    private final InputManager inputManager;
    private final short ownClientId;
    private long lastEventMessageTimeStamp;
    private static final long MIN_MS_BETWEEN_EVENTS = (long) ((1f / 50f) * 1000f);

    public EventTransmitter(Connection connection, InputManager inputManager, short ownClientId) {
        this.connection = connection;
        this.inputManager = inputManager;
        this.ownClientId = ownClientId;

        inputManager.addInputListener(this);
    }

    @Override
    public void onInputDevicePressed(InputEvent event) {
        lastEventMessageTimeStamp = System.currentTimeMillis();
        sendEventMessage(event);
    }

    @Override
    public void onInputDeviceReleased(InputEvent event) {
        lastEventMessageTimeStamp = System.currentTimeMillis();
        sendEventMessage(event);
    }

    @Override
    public void onInputDeviceDragged(InputEvent event) {
        if (System.currentTimeMillis() - lastEventMessageTimeStamp >= MIN_MS_BETWEEN_EVENTS) {
            lastEventMessageTimeStamp = System.currentTimeMillis();
            sendEventMessage(event);
        }
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
