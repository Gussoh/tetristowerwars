/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import org.tetristowerwars.Settings;
import org.tetristowerwars.network.NetworkClientModel;

/**
 *
 * @author Andreas
 */
public class SettingsMessage extends Message implements ClientMessage {

    private final Settings settings;

    public SettingsMessage(Settings settings) {
        this.settings = settings;
    }

    public SettingsMessage(DataInputStream dataInputStream) throws IOException {
        Properties properties = new Properties();
        int numEntries = dataInputStream.readInt();

        for (int i = 0; i < numEntries; i++) {
            String key = dataInputStream.readUTF();
            String value = dataInputStream.readUTF();

            properties.put(key, value);
        }
        settings = new Settings();
    }

    @Override
    protected void writeContent(DataOutputStream dataOutputStream) throws IOException {
        Properties properties = settings.getProperties();
        dataOutputStream.writeInt(properties.size());
        for (Entry<Object, Object> entry : properties.entrySet()) {
            dataOutputStream.writeUTF(entry.getKey().toString());
            dataOutputStream.writeUTF(entry.getValue().toString());
        }
    }

    @Override
    public void processClientMessage(NetworkClientModel networkClientModel) {
        networkClientModel.setGameSettings(settings);
    }
}
