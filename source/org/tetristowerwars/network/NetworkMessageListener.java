/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network;

import org.tetristowerwars.network.message.Message;

/**
 *
 * @author Andreas
 */
public interface NetworkMessageListener {

    public void handleReceivedMessage(Connection connection, Message message);

    public void onConnectionError(Connection connection, String message);

    public void onConnectionClosed(Connection connection);
}
