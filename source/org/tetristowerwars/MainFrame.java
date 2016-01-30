/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import TUIO.TuioClient;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.tetristowerwars.control.TouchInputManager;
import org.tetristowerwars.gui.JavaFXWindow;

/**
 *
 * @author Andreas
 */
public final class MainFrame extends Application {

    //private JFrame frame = new JFrame("Tetris Tower Wars");
    private final Deque<Component> componentStack = new ArrayDeque<Component>();
    private final Settings settings;
    private final TuioClient tuioClient = new TuioClient();
    private MouseEmulator emulator;
    private boolean isFullscreen = false;
    private JComponent rootPanel = new JPanel();
    SwingNode swingNode;

    public MainFrame() {
        tuioClient.connect();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLocationByPlatform(true);
        settings = new Settings();
        try {
            settings.load();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(componentStack.peek(), "Unable to load settings file: " + Settings.SETTINGS_FILE + "\nReason: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        openComponent(new StartPanel(this), false);
        //frame.setVisible(true);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        swingNode = new SwingNode();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(rootPanel);
            }
        });
        
        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);

        stage.setTitle("Swing in JavaFX");
        stage.setScene(new Scene(pane, 1250, 1150));
        stage.show();
        System.out.println("Is it me?");
    }

    public TuioClient getTuioClient() {
        return tuioClient;
    }

    public static void main(String[] args) {
        

        launch(args);
    }

    public void openComponent(Component component, boolean fullscreen) {
        Component currentComponent = componentStack.peek();

        if (currentComponent != null) {
            //frame.remove(currentComponent);
            rootPanel.remove(currentComponent);
        }
        
        
        
        if(fullscreen) {
            
            /*isFullscreen = true;
            frame.dispose();
            frame = new JFrame("uni Tetris Tower Wars");
            frame.setUndecorated(true);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            GraphicsDevice gDev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int width = gDev.getDisplayMode().getWidth();
            int height = gDev.getDisplayMode().getHeight();
*/
            componentStack.push(component);
            rootPanel.add(component);
            //frame.add(component);
            //frame.setSize(width, height);
        }
        else {
            componentStack.push(component);
            rootPanel.add(component);
            //frame.add(component);
            //frame.pack();
        }
        rootPanel.validate();

        if (settings.isMouseEmulationEnabled()) {
            enableMouseEmulation();
        } else {
            disableMouseEmulation();
        }
    }

    public JComponent getRootPanel() {
        return rootPanel;
    }

    public void back() {
        if (isFullscreen) {
            /*isFullscreen = false;
            frame.dispose();
            frame = new JFrame("Tetris Tower Wars");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            */

            componentStack.pop();
        }
        else {
            //frame.remove(componentStack.pop());
            rootPanel.remove(componentStack.pop());
        }
        //frame.add(componentStack.peek());
        //frame.pack();
        rootPanel.add(componentStack.peek());

        if (settings.isMouseEmulationEnabled()) {
            enableMouseEmulation();
        } else {
            disableMouseEmulation();
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public void setMousePosition(int x, int y) {
        if (emulator != null) {
            emulator.setMousePosition(x, y);
        }
    }

    public void enableMouseEmulation() {
        /*if (emulator == null) {
            DisplayMode displayMode = frame.getGraphicsConfiguration().getDevice().getDisplayMode();
            Dimension dimension = new Dimension(displayMode.getWidth(), displayMode.getHeight());
            try {
                emulator = new MouseEmulator(new TouchInputManager(tuioClient, dimension, null));
                emulator.enable();
            } catch (AWTException ex) {
                JOptionPane.showMessageDialog(frame, "Unable to emulate mouse.\nReason: " + ex.getMessage());
            }
        }*/
    }

    public void disableMouseEmulation() {
        if (emulator != null) {
            emulator.disable();
            emulator = null;
        }
    }

    public Component getCurrentComponent() {
        return componentStack.peek();
    }


}
