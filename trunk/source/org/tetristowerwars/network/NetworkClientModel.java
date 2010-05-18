/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.Settings;
import org.tetristowerwars.control.Controller;
import org.tetristowerwars.control.InputEvent;
import org.tetristowerwars.control.PlayerRestrictedController;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.network.message.ClientMessage;
import org.tetristowerwars.network.message.EventQueueMessage;
import org.tetristowerwars.network.message.InputEventMessage;
import org.tetristowerwars.network.message.Message;

/**
 *
 * @author Andreas
 */
public class NetworkClientModel implements NetworkMessageListener {

    private final List<NetworkClientListener> networkClientListeners = new ArrayList<NetworkClientListener>();
    private final SortedMap<Short, ClientEntry> clients = new TreeMap<Short, ClientEntry>();
    private final Queue<Queue<EventQueueMessage>> eventQueues = new LinkedList<Queue<EventQueueMessage>>();
    private Queue<EventQueueMessage> currentEventQueue = new LinkedList<EventQueueMessage>();
    private short ownClientId = -1;
    private Settings gameSettings;

    protected NetworkClientModel() {
    }

    @Override
    public synchronized void handleReceivedMessage(Connection connection, Message message) {
        if (message instanceof ClientMessage) {
            ((ClientMessage) message).processClientMessage(this);
        }
    }

    @Override
    public synchronized void onConnectionClosed(Connection connection) {
        for (NetworkClientListener networkClientListener : networkClientListeners) {
            networkClientListener.onConnectionClosed();
        }
    }

    @Override
    public synchronized void onConnectionError(Connection connection, String message) {
        for (NetworkClientListener networkClientListener : networkClientListeners) {
            networkClientListener.onConnectionError(message);
        }
    }



    public synchronized void addNetworkClientListeners(NetworkClientListener listener) {
        networkClientListeners.add(listener);
    }

    public synchronized void removeNetworkClientListeners(NetworkClientListener listener) {
        networkClientListeners.remove(listener);
    }

    public synchronized void addClient(short clientId, String name) {
        ClientEntry clientEntry = new ClientEntry(name);
        clients.put(clientId, clientEntry);

        for (NetworkClientListener networkClientListener : networkClientListeners) {
            networkClientListener.clientConnected(clientEntry);
        }
    }

    public synchronized void removeClient(short clientId) {
        ClientEntry clientEntry = clients.remove(clientId);
        for (NetworkClientListener networkClientListener : networkClientListeners) {
            networkClientListener.clientDisconnected(clientEntry);
        }
    }

    public synchronized void addEventQueueMessage(EventQueueMessage eventQueueMessage) {
        currentEventQueue.add(eventQueueMessage);
    }

    public synchronized void endCurrentFrame() {
        eventQueues.add(currentEventQueue);
        currentEventQueue = new LinkedList<EventQueueMessage>();

        for (NetworkClientListener networkClientListener : networkClientListeners) {
            networkClientListener.endOfFramePosted(eventQueues.size());
        }
    }

    public synchronized void setOwnClientId(short clientId) {
        if (ownClientId != -1) {
            throw new IllegalStateException("Own client id already set.");
        }
        
        this.ownClientId = clientId;
        for (NetworkClientListener networkClientListener : networkClientListeners) {
            networkClientListener.onOwnClientIdSet(ownClientId);
        }
    }

    public synchronized void postChatMessage(short clientId, String message) {
        ClientEntry entry = clients.get(clientId);
        for (NetworkClientListener networkClientListener : networkClientListeners) {
            networkClientListener.chatMessageReceive(entry, message);
        }
    }

    public synchronized void setGameSettings(Settings settings) {
        this.gameSettings = settings;
    }

    public synchronized void fireGameStartedEvent() {
        List<NetworkClientListener> copy = new ArrayList<NetworkClientListener>(networkClientListeners);
        for (NetworkClientListener networkClientListener : copy) {
            networkClientListener.gameStarted();
        }
    }

    public synchronized short getOwnClientId() {
        return ownClientId;
    }

    public synchronized Settings getSettings() {
        return gameSettings;
    }

    public synchronized List<Controller> createControllers(GameModel gameModel, Renderer renderer) {
        List<Controller> controllers = new ArrayList<Controller>();
        for (Map.Entry<Short, ClientEntry> entry : clients.entrySet()) {
            ClientEntry ce = entry.getValue();
            Player player = gameModel.getPlayers().get(ce.getPlayerIndex());
            Controller controller = new PlayerRestrictedController(gameModel, ce.getNetworkClientInputManager(), renderer, player);
            controllers.add(controller);
        }
        return controllers;
    }

    public synchronized int getNumUnprocessedFrames() {
        return eventQueues.size();
    }

    public synchronized void processNextFrame() {
        Queue<EventQueueMessage> frame = eventQueues.poll();
        for (EventQueueMessage eventQueueMessage : frame) {
            eventQueueMessage.executeMessage(this);
        }
    }

    public synchronized void allClientsReady() {
        for (NetworkClientListener networkClientListener : networkClientListeners) {
            networkClientListener.allClientsReady();
        }
    }

    public synchronized void executeInputEvent(short clientId, InputEvent inputEvent) {
        ClientEntry ce = clients.get(clientId);
        ce.getNetworkClientInputManager().handleIncomingEvent(inputEvent);
    }

    public synchronized void executeCreateBuildingBlock(Vec2 position, Material material, short shape) {
        for (NetworkClientListener networkClientListener : networkClientListeners) {
            networkClientListener.spawnBuildingBlock(position, material, shape);
        }
    }

    public synchronized void setPlayerIndex(short clientId, short playerIndex) {
        ClientEntry clientEntry = clients.get(clientId);
        clientEntry.setPlayerIndex(playerIndex);
    }
}
