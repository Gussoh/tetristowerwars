/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.control.Controller;
import org.tetristowerwars.model.GameModel;

/**
 *
 * @author Andreas
 */
public class SwingRenderer extends Renderer {

    private final JFrame frame;

    public SwingRenderer(GameModel dataModel, Controller controller) {
        super(dataModel, controller);
        frame = new JFrame("Awesomeness");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new RenderPanel());
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void renderFrame() {
        frame.repaint();
    }

    private class RenderPanel extends JPanel {

        public RenderPanel() {
            setPreferredSize(new Dimension(640, 480));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            drawBody(g2, gameModel.getGroundBody());

            for (Body body : gameModel.getBodies()) {
                drawBody(g2, body);
            }
        }

        private void drawBody(Graphics2D g, Body body) {
            PolygonShape groundShape = (PolygonShape) body.getShapeList();

            Vec2[] vertices = groundShape.getVertices();
            AffineTransform currentTransform = g.getTransform();
            g.translate(body.getPosition().x, getHeight() - body.getPosition().y);
            g.rotate(-body.getAngle());
            for (int i = 0; i < vertices.length - 1; i++) {
                g.drawLine((int) vertices[i].x,
                        ((int) vertices[i].y),
                        (int) vertices[i + 1].x,
                        ((int) vertices[i + 1].y));
            }
            
            g.drawLine((int) vertices[0].x,
                        ((int) vertices[0].y),
                        (int) vertices[vertices.length - 1].x,
                        ((int) vertices[vertices.length - 1].y));

            g.setTransform(currentTransform);
        }
    }
}
