/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author entrance
 */
public class JavaFXWindow extends Application {

    private final JComponent component;

    
    
    public JavaFXWindow(JComponent component) {
        this.component = component;
        launch(new String[]{});
    }
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        final SwingNode swingNode = new SwingNode();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(component);
            }
        });

        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);

        stage.setTitle("Swing in JavaFX");
        stage.setScene(new Scene(pane, 250, 150));
        stage.show();
        System.out.println("Is it me?");
    }
}
