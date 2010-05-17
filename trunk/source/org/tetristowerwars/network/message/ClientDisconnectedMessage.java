/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.tetristowerwars.network.NetworkClientModel;

/**
 *
 * @author Andreas
 */
public class ClientDisconnectedMessage extends Message implements ClientMessage {

    private final short clientId;

    public ClientDisconnectedMessage(short clientId) {
        this.clientId = clientId;
    }

    public ClientDisconnectedMessage(DataInputStream dataInputStream) throws IOException {
        clientId = dataInputStream.readShort();
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(clientId);
    }

    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.removeClient(clientId);
    }
}
