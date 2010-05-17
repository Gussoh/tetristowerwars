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
public class ReadyMessage extends Message implements ServerMessage {

    public ReadyMessage() {
    }

    public ReadyMessage(DataInputStream dataInputStream) {
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
    }

    @Override
    public void processServerMessage(NetworkServerModel networkServerModel, Connection connection) {
        networkServerModel.clientReady(connection);
    }

}
