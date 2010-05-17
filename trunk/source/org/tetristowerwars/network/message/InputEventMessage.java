/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network.message;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.control.InputEvent;
import org.tetristowerwars.network.Connection;
import org.tetristowerwars.network.NetworkClientModel;
import org.tetristowerwars.network.NetworkServerModel;

/**
 *
 * @author Andreas
 */
public class InputEventMessage extends Message implements ClientMessage, ServerMessage, EventQueueMessage {

    private final short clientId;
    private final InputEvent inputEvent;

    public InputEventMessage(short clientId, InputEvent inputEvent) {
        this.clientId = clientId;
        this.inputEvent = inputEvent;
    }

    public InputEventMessage(DataInputStream dataInputStream) throws IOException {
        clientId = dataInputStream.readShort();
        short eventType = dataInputStream.readShort();
        float x = dataInputStream.readFloat();
        float y = dataInputStream.readFloat();
        int id = dataInputStream.readInt();

        inputEvent = new InputEvent(eventType, new Vec2(x, y), id);
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
        Vec2 p = inputEvent.getPosition();
        dataOutputStream.writeShort(clientId);
        dataOutputStream.writeShort(inputEvent.getType());
        dataOutputStream.writeFloat(p.x);
        dataOutputStream.writeFloat(p.y);
        dataOutputStream.writeInt(inputEvent.getActionId());
    }

    public short getClientId() {
        return clientId;
    }

    public InputEvent getInputEvent() {
        return inputEvent;
    }

    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.addEventQueueMessage(this);
    }

    @Override
    public void processServerMessage(NetworkServerModel networkServerModel, Connection connection) {
        networkServerModel.distributeMessage(this);
    }

    @Override
    public void executeMessage(NetworkClientModel networkClientModel) {
        networkClientModel.executeInputEvent(clientId, inputEvent);
    }
}
