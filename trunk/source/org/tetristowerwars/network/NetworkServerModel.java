/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.tetristowerwars.Settings;
import org.tetristowerwars.network.message.AllReadyMessage;
import org.tetristowerwars.network.message.ChatMessage;
import org.tetristowerwars.network.message.ClientConnectedMessage;
import org.tetristowerwars.network.message.ClientDisconnectedMessage;
import org.tetristowerwars.network.message.ClientIdAssignMessage;
import org.tetristowerwars.network.message.Message;
import org.tetristowerwars.network.message.ServerMessage;
import org.tetristowerwars.network.message.SettingsMessage;
import org.tetristowerwars.network.message.StartGameMessage;
import org.tetristowerwars.util.ImmutableEntry;

/**
 *
 * @author Andreas
 */
public class NetworkServerModel implements NetworkMessageListener {

    private List<Connection> connectionsNotLoggedIn = new ArrayList<Connection>();
    private Map<Connection, ImmutableEntry<Short, String>> clients = new LinkedHashMap<Connection, ImmutableEntry<Short, String>>();
    private short nextClientId = 0;
    private int numClientsReady = 0;

    protected NetworkServerModel() {
    }

    @Override
    public synchronized void handleReceivedMessage(Connection connection, Message message) {
        if (message instanceof ServerMessage) {
            ((ServerMessage) message).processServerMessage(this, connection);
        }
    }

    @Override
    public synchronized void onConnectionError(Connection connection, String message) {
    }

    @Override
    public synchronized void onConnectionClosed(Connection connection) {
        if (connection != null) {
            ImmutableEntry<Short, String> removedEntry = clients.remove(connection);
            if (removedEntry != null) {
                ClientDisconnectedMessage cdm = new ClientDisconnectedMessage(removedEntry.getKey());
                for (Connection c : clients.keySet()) {
                    c.send(cdm);
                }
            }
        }
    }

    public synchronized void distributeChatMessage(Connection fromConnection, String message) {
        ImmutableEntry<Short, String> entry = clients.get(fromConnection);
        ChatMessage chatMessage = new ChatMessage(entry.getKey(), message);
        distributeMessage(chatMessage);
    }

    public synchronized void newClientConnection(Connection c) {
        connectionsNotLoggedIn.add(c);
    }

    public synchronized void loginClient(Connection connection, String name) {
        if (connectionsNotLoggedIn.remove(connection)) {
            // The newly connected client needs a list of all clients already connected

            short newClientId = nextClientId++;

            for (ImmutableEntry<Short, String> entry : clients.values()) {
                ClientConnectedMessage clientConnectedMessage = new ClientConnectedMessage(entry.getKey(), entry.getValue());
                connection.send(clientConnectedMessage);
            }
            
            clients.put(connection, new ImmutableEntry<Short, String>(newClientId, name));

            // Report the new client to everybody already connected including the new client as well.
            ClientConnectedMessage clientConnectedMessage = new ClientConnectedMessage(newClientId, name);
            for (Connection otherConnection : clients.keySet()) {
                otherConnection.send(clientConnectedMessage);
            }


            ClientIdAssignMessage assignMessage = new ClientIdAssignMessage(newClientId);
            connection.send(assignMessage);

        } else {
            connection.close();
        }
    }

    public synchronized void distributeMessage(Message message) {
        for (Connection connection : clients.keySet()) {
            connection.send(message);
        }
    }

    public synchronized void startGame(Settings settings) {
        numClientsReady = 0;
        SettingsMessage settingsMessage = new SettingsMessage(settings);
        distributeMessage(settingsMessage);

        StartGameMessage gameMessage = new StartGameMessage();
        distributeMessage(gameMessage);
    }

    public synchronized void clientReady(Connection connection) {
        numClientsReady++;
        if (numClientsReady == clients.size()) {
            AllReadyMessage allReadyMessage = new AllReadyMessage();
            distributeMessage(allReadyMessage);
        }
    }

    public synchronized void clear() {
        for (Connection connection : clients.keySet()) {
            connection.close();
        }
        clients.clear();

        for (Connection connection : connectionsNotLoggedIn) {
            connection.close();
        }
        connectionsNotLoggedIn.clear();

        nextClientId = 0;
    }
}
