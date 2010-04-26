/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Andreas
 */
public class MainFrame {


    private final JFrame frame = new JFrame("Tetris Tower Wars");
    private final Deque<Component> componentStack = new ArrayDeque<Component>();
    private final Settings settings;

    public MainFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);

        openComponent(new StartPanel(this));
        frame.setVisible(true);
        settings = new Settings();
        try {
            settings.load();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(componentStack.peek(), "Unable to load settings file: " + Settings.SETTINGS_FILE + "\nReason: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                new MainFrame();
            }
        });

    }

    public void openComponent(Component component) {
        Component currentComponent = componentStack.peek();

        if (currentComponent != null) {
            frame.remove(currentComponent);
        }

        componentStack.push(component);
        frame.add(component);
        frame.pack();
    }



    public JFrame getJFrame() {
        return frame;
    }

    public void back() {
        frame.remove(componentStack.pop());
        frame.add(componentStack.peek());
        frame.pack();
    }

    public Settings getSettings() {
        return settings;
    }
}
