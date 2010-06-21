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
public class StopGameMessage extends Message implements ClientMessage {

    public StopGameMessage() {
    }

    public StopGameMessage(DataInputStream dataInputStream) {

    }

    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.fireGameStoppedEvent();
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
    }

}
