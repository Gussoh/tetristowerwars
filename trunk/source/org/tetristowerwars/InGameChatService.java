/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.network.NetworkClient;

/**
 *
 * @author Andreas
 */
public class InGameChatService {

    private final NetworkClient networkClient;
    private final Renderer renderer;
    private final KeyListenerImpl keyListenerImpl = new KeyListenerImpl();
    private StringBuilder chatMessageBuilder = null;

    public InGameChatService(NetworkClient networkClient, Renderer renderer) {
        this.networkClient = networkClient;
        this.renderer = renderer;
    }

    public void startChatService() {
        renderer.getInputComponent().addKeyListener(keyListenerImpl);
    }

    public void stopChatService() {
        renderer.getInputComponent().removeKeyListener(keyListenerImpl);
    }

    private class KeyListenerImpl implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            char key = e.getKeyChar();
            if (key == KeyEvent.CHAR_UNDEFINED) {
                return;
            }
            if (chatMessageBuilder != null) {
                chatMessageBuilder.append(key);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                chatMessageBuilder = null;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (chatMessageBuilder == null) {
                    chatMessageBuilder = new StringBuilder();
                } else {
                    networkClient.sendChatMessage(chatMessageBuilder.toString().trim());
                    chatMessageBuilder = null;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
