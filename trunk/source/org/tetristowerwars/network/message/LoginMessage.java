/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.tetristowerwars.network.Connection;
import org.tetristowerwars.network.NetworkServerModel;

/**
 *
 * @author Andreas
 */
public class LoginMessage extends Message implements ServerMessage {

    private final String name;
    private final static String APPLICATION_ID = "Tetris Tower Wars";

    public LoginMessage(String name) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name;
        }
    }

    public LoginMessage(DataInputStream dataInputStream) throws IOException {
        String appId = dataInputStream.readUTF();
        if (!appId.equals(APPLICATION_ID)) {
            throw new IOException("Incorrect application id");
        }
        name = dataInputStream.readUTF();
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF(APPLICATION_ID);
        dataOutputStream.writeUTF(name);
    }

    @Override
    public void processServerMessage(NetworkServerModel networkServerModel, Connection connection) {
        networkServerModel.loginClient(connection, name);
    }

}
