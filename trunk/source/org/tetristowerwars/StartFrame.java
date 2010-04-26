/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Andreas
 */
public class StartFrame {

    private JFrame frame = new JFrame("Tetris Tower Wars");
    private TouchGamePanel startPanel = new TouchGamePanel(this);
    private SettingsPanel settingsPanel = new SettingsPanel(this);
    private JPanel currentPanel;

    public StartFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.add(startPanel);
        currentPanel = startPanel;
        frame.pack();
        frame.setVisible(true);
    }

    public void currentPanelDone() {
        frame.remove(currentPanel);
        if (currentPanel == settingsPanel) {
            frame.add(startPanel);
            currentPanel = startPanel;
            frame.pack();
        } else {
            // TODO: Start the game
        }
    }

    public void openSettingsPanel() {
        frame.remove(currentPanel);
        frame.add(settingsPanel);
        currentPanel = settingsPanel;
        frame.pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                new StartFrame();
            }
        });

    }
}
