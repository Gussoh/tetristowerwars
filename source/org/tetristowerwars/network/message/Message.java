/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andreas
 */
public abstract class Message {

    private final static List<Class<? extends Message>> type2Class = new ArrayList<Class<? extends Message>>();
    private final static Map<Class<? extends Message>, Short> class2Type = new HashMap<Class<? extends Message>, Short>();

    static {
        type2Class.add(AllReadyMessage.class);
        type2Class.add(ChatMessage.class);
        type2Class.add(ClientConnectedMessage.class);
        type2Class.add(ClientDisconnectedMessage.class);
        type2Class.add(ClientIdAssignMessage.class);
        type2Class.add(EndOfFrameMessage.class);
        type2Class.add(InputEventMessage.class);
        type2Class.add(LoginMessage.class);
        type2Class.add(ReadyMessage.class);
        type2Class.add(SetPlayerIndexMessage.class);
        type2Class.add(SettingsMessage.class);
        type2Class.add(SpawnBuildingBlockMessage.class);
        type2Class.add(SpawnPowerUpMessage.class);
        type2Class.add(StartGameMessage.class);

        for (short i = 0; i < type2Class.size(); i++) {
            class2Type.put(type2Class.get(i), i);
        }
    }

    public static Message read(DataInputStream dataInputStream) throws IOException {
        short type = dataInputStream.readShort();

        if (type < 0 || type >= type2Class.size()) {
            throw new IOException("Unknown message type: " + type);
        }

        Class<? extends Message> messageClass = type2Class.get(type);
        try {
            Constructor<? extends Message> constructor = messageClass.getConstructor(DataInputStream.class);
            return constructor.newInstance(dataInputStream);
        } catch (InstantiationException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        }
    }

    public void write(DataOutputStream dataOutputStream) throws IOException  {
        Short type = class2Type.get(getClass());
        if (type == null) {
            throw new IOException("Message class not registered: " + getClass().getName());
        }

        dataOutputStream.writeShort(type);
        writeContent(dataOutputStream);
    }

    protected abstract void writeContent(DataOutputStream dataOutputStream) throws IOException;
}
