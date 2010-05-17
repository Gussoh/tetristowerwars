/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network;

import org.tetristowerwars.model.Player;

/**
 *
 * @author Andreas
 */
public class ClientEntry {

    private String name;
    private final ClientInputManager networkClientInputManager = new ClientInputManager();
    private short player;

    public ClientEntry(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ClientInputManager getNetworkClientInputManager() {
        return networkClientInputManager;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer(short player) {
        this.player = player;
    }

    public short getPlayerIndex() {
        return player;
    }
}
