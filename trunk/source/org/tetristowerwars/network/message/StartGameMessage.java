/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.tetristowerwars.network.NetworkClientListener;
import org.tetristowerwars.network.NetworkClientModel;

/**
 *
 * @author Andreas
 */
public class StartGameMessage extends Message implements ClientMessage {

    public StartGameMessage() {
    }

    public StartGameMessage(DataInputStream dataInputStream) {
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
    }

    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.fireGameStartedEvent();
    }

}
