/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network;

import org.jbox2d.common.Vec2;
import org.tetristowerwars.Settings;
import org.tetristowerwars.model.material.Material;


/**
 *
 * @author Andreas
 */
public interface NetworkClientListener {

    public void chatMessageReceive(ClientEntry clientEntry, String message);

    public void clientConnected(ClientEntry clientEntry);

    public void spawnBuildingBlock(Vec2 position, Material material, short shape);

    public void gameStarted();

    public void endOfFramePosted(int unprocessedFrames);

    public void clientDisconnected(ClientEntry clientEntry);

    public void onConnectionError(String message);

    public void onConnectionClosed();

    public void allClientsReady();

    public void onOwnClientIdSet(short ownClientId);

    public void onClientPropertyChanged(ClientEntry clientEntry);

    public void spawnPowerUpBlock();

    public void onSettingsReceived(Settings settings);

    public void gameStopped();
}
