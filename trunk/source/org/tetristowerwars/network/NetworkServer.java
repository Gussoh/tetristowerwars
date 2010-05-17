/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.Settings;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.network.message.EndOfFrameMessage;
import org.tetristowerwars.network.message.SpawnBuildingBlockMessage;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class NetworkServer {

    private final int port;
    private ServerSocketThread serverSocketThread = null;
    private NetworkServerModel networkServerModel = new NetworkServerModel();

    public NetworkServer(int port) {
        this.port = port;
    }

    public void start() {
        if (serverSocketThread == null) {
            serverSocketThread = new ServerSocketThread();
            serverSocketThread.start();
        }
    }

    public void stop() {
        serverSocketThread.close();
        networkServerModel.clear();
    }

    public void startGame(Settings settings) {
        if (serverSocketThread != null) {
            serverSocketThread.close();
        }

        networkServerModel.startGame(settings);
    }

    public void createRandomBuildingBlock(float leftLimit, float rightLimit, float yPos) {
        float xPos = MathUtil.random(leftLimit, rightLimit);
        Vec2 pos = new Vec2(xPos, yPos);
        Material material = Material.createRandomMaterial();
        SpawnBuildingBlockMessage blockMessage = new SpawnBuildingBlockMessage(pos, material);
        networkServerModel.distributeMessage(blockMessage);
    }

    public void sendEndOfFrame() {
        EndOfFrameMessage endOfFrameMessage = new EndOfFrameMessage();
        networkServerModel.distributeMessage(endOfFrameMessage);
    }

    private class ServerSocketThread extends Thread {

        private boolean alive = true;

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            while (alive) {
                try {
                    synchronized (this) {
                        if (serverSocket == null) {
                            serverSocket = new ServerSocket(port);
                            serverSocket.setSoTimeout(5000);
                        }
                    }
                    final Socket socket = serverSocket.accept();
                    Connection c = new Connection(socket, networkServerModel);
                    networkServerModel.newClientConnection(c);

                } catch (SocketTimeoutException ex) {
                    // This is normal
                } catch (IOException ex) {
                    synchronized (this) {
                        if (alive) {
                            Logger.getLogger(NetworkServer.class.getName()).log(Level.SEVERE, null, ex);
                            networkServerModel.onConnectionError(null, "Server socket error: " + ex.getMessage());
                        }
                        if (serverSocket != null) {
                            try {
                                serverSocket.close();
                            } catch (IOException ex1) {
                            }
                        }
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException ex1) {
                        }
                    }
                }
            }

            synchronized (this) {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }

        public void close() {
            alive = false;
        }
    }
}
