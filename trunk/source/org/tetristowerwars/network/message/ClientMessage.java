/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network.message;

import org.tetristowerwars.network.NetworkClientModel;

/**
 *
 * @author Andreas
 */
public interface ClientMessage {
    public void processClientMessage(NetworkClientModel networkClientModel);
}
