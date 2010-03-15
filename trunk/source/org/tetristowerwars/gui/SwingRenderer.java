/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui;

import java.awt.Color;
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
import org.tetristowerwars.control.MouseEventController;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.building.BuildingBlock;
import org.tetristowerwars.model.material.ConcreteMaterial;
import org.tetristowerwars.model.material.GroundMaterial;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author Andreas
 */
public class SwingRenderer extends Renderer {

    private final JFrame frame;
    private final MouseEventController mouseEvent;

    public SwingRenderer(GameModel gameModel, Controller controller) {
        super(gameModel, controller);
        frame = new JFrame("Awesomeness");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new RenderPanel());
        frame.pack();
        frame.setVisible(true);

        // Jättefulhaxx för gameModel för referens till MouseEventController.

        // Add mouse listener to the frame
        mouseEvent = new MouseEventController(gameModel);
        frame.addMouseListener(mouseEvent);
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
            drawBody(g2, gameModel.getGroundBody(), new GroundMaterial());

            for (BuildingBlock block : gameModel.getBlockPool()) {
                drawBlock(g2, block);
            }
        }

        private void drawBlock(Graphics2D g, BuildingBlock block)
        {

            for (Body body : block.getBodies()) {
                drawBody(g, body, block.getMaterial());
            }
        }

        private void drawBody(Graphics2D g, Body body, Material mat) {
            for(PolygonShape shape = (PolygonShape) body.getShapeList(); shape != null; shape = (PolygonShape) shape.m_next) {

                Vec2[] vertices = shape.getVertices();
                AffineTransform currentTransform = g.getTransform();

                g.translate(body.getPosition().x, getHeight() - body.getPosition().y);
                g.rotate(-body.getAngle());
          
                //fillPolygon implementation
                //draws every shape as a rectangles
                int nPoints = 4;
                int[] xpoints = new int[nPoints];
                int[] ypoints = new int[nPoints];
                
                for (int i = 0; i < vertices.length; i++) {
                    xpoints[i] = (int)vertices[i].x;
                    ypoints[i] = -(int)vertices[i].y;
                }

                g.setColor(mat.getColor()); //color defined by material
                g.fillPolygon(xpoints, ypoints, nPoints);

                g.setTransform(currentTransform);
            }
        }
    }
}
