/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.tetristowerwars.network.Connection;
import org.tetristowerwars.network.NetworkClientModel;
import org.tetristowerwars.network.NetworkServerModel;

/**
 *
 * @author Andreas
 */
public class SetPlayerIndexMessage extends Message implements ClientMessage, ServerMessage {

    private final short clientId;
    private final short playerIndex;

    public SetPlayerIndexMessage(short clientId, short playerIndex) {
        this.clientId = clientId;
        this.playerIndex = playerIndex;
    }

    public SetPlayerIndexMessage(DataInputStream dataInputStream) throws IOException {
        clientId = dataInputStream.readShort();
        playerIndex = dataInputStream.readShort();
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(clientId);
        dataOutputStream.writeShort(playerIndex);
    }

    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.setPlayerIndex(clientId, playerIndex);
    }

    @Override
    public void processServerMessage(NetworkServerModel networkServerModel, Connection connection) {
        networkServerModel.distributeMessage(this);
    }
}
