/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network.message;

import org.tetristowerwars.network.Connection;
import org.tetristowerwars.network.NetworkServerModel;

/**
 *
 * @author Andreas
 */
public interface ServerMessage {
    public void processServerMessage(NetworkServerModel networkServerModel, Connection connection);
}
