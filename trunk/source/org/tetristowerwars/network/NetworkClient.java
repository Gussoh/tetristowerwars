/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tetristowerwars.Settings;
import org.tetristowerwars.control.Controller;
import org.tetristowerwars.control.InputManager;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.network.message.LoginMessage;
import org.tetristowerwars.network.message.ReadyMessage;
import org.tetristowerwars.network.message.SetPlayerIndexMessage;

/**
 *
 * @author Andreas
 */
public class NetworkClient {

    protected Connection connection;
    protected NetworkClientModel clientModel = new NetworkClientModel();
    protected final String name;
    protected final String host;
    protected final int port;
    protected EventTransmitter eventTransmitter;

    public NetworkClient(String name, String host, int port) throws IOException {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public void start() {
        new Thread() {

            @Override
            public void run() {
                try {
                    Socket socket = new Socket(host, port);
                    connection = new Connection(socket, clientModel);
                    connection.send(new LoginMessage(name));
                } catch (UnknownHostException ex) {
                    Logger.getLogger(NetworkClient.class.getName()).log(Level.SEVERE, null, ex);
                    clientModel.onConnectionError(null, "Unknown host: " + ex.getMessage());
                } catch (IOException ex) {
                    Logger.getLogger(NetworkClient.class.getName()).log(Level.SEVERE, null, ex);
                    clientModel.onConnectionError(null, "Network error: " + ex.getMessage());
                }
            }
        }.start();
    }

    public void addNetworkClientListener(NetworkClientListener listener) {
        clientModel.addNetworkClientListeners(listener);
    }

    public void removeNetworkClientListener(NetworkClientListener listener) {
        clientModel.removeNetworkClientListeners(listener);
    }

    public void startSendingUserInput(InputManager inputManager) {
        if (eventTransmitter != null) {
            eventTransmitter.stop();
        }
        eventTransmitter = new EventTransmitter(connection, inputManager, clientModel.getOwnClientId());
    }

    public void stopSendingUserInput() {
        if (eventTransmitter != null) {
            eventTransmitter.stop();
        }

        eventTransmitter = null;
    }

    public Settings getSettings() {
        return clientModel.getSettings();
    }

    public List<Controller> createControllers(GameModel gameModel, Renderer renderer) {
        return clientModel.createControllers(gameModel, renderer);
    }

    public int getNumUnprocessedFrames() {
        return clientModel.getNumUnprocessedFrames();
    }

    public void processNextFrame() {
        clientModel.processNextFrame();
    }

    public void stop() {
        if (connection != null) {
            connection.close();
        }
        stopSendingUserInput();
    }

    public void ready() throws IOException {
        connection.send(new ReadyMessage());
    }

    public void setPlayerIndex(short playerIndex) {
        connection.send(new SetPlayerIndexMessage(clientModel.getOwnClientId(), playerIndex));
    }
}
