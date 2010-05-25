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
public class SpawnPowerUpMessage extends Message implements ClientMessage, EventQueueMessage {

    public SpawnPowerUpMessage() {
    }

    public SpawnPowerUpMessage(DataInputStream dataInputStream) {
    }



    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
    }

    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.addEventQueueMessage(this);
    }

    @Override
    public void executeMessage(NetworkClientModel networkClientModel) {
        networkClientModel.executeCreatePowerUpMessage();
    }

}
