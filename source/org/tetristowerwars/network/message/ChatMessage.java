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
public class ChatMessage extends Message implements ClientMessage, ServerMessage {

    private final short clientId;
    private final String message;

    public ChatMessage(short clientId, String message) {
        this.clientId = clientId;
        if (message == null) {
            this.message = "";
        } else {
            this.message = message;
        }
    }

    public ChatMessage(DataInputStream dataInputStream) throws IOException {
        clientId = dataInputStream.readShort();
        message = dataInputStream.readUTF();
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(clientId);
        dataOutputStream.writeUTF(message);
    }

    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.postChatMessage(clientId, message);
    }

    @Override
    public void processServerMessage(NetworkServerModel networkServerModel, Connection connection) {
        networkServerModel.distributeChatMessage(connection, message);
    }
}
