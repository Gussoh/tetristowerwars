/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.tetristowerwars.network.ClientEntry;
import org.tetristowerwars.network.NetworkClientListener;
import org.tetristowerwars.network.NetworkClientModel;

/**
 *
 * @author Andreas
 */
public class ClientConnectedMessage extends Message implements ClientMessage {

    private final short clientId;
    private final String name;

    public ClientConnectedMessage(short clientId, String name) {
        this.clientId = clientId;
        if (name == null) {
            this.name = "";
        } else {
            this.name = name;
        }
    }

    public ClientConnectedMessage(DataInputStream dataInputStream) throws IOException {
        clientId = dataInputStream.readShort();
        name = dataInputStream.readUTF();
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(clientId);
        dataOutputStream.writeUTF(name);
    }


    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.addClient(clientId, name);
    }
}
