/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.building.BuildingBlock;
import org.tetristowerwars.model.material.GroundMaterial;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author Andreas
 */
public class SwingRenderer extends Renderer {

    private final JFrame frame;
    private final RenderPanel renderPanel;

    public SwingRenderer(GameModel gameModel) {
        super(gameModel);
        renderPanel = new RenderPanel();
        frame = new JFrame("Awesomeness");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(renderPanel);
        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public Component getMouseInputComponent() {
        return renderPanel;
    }

    @Override
    public void renderFrame() {
        frame.repaint();
    }

    @Override
    public Point2D convertScreenToWorldCoordinates(Point screenCoord) {
        return new Point2D.Float((float)screenCoord.x, (float)(renderPanel.getHeight() - screenCoord.y));
    }

    private class RenderPanel extends JPanel {

        public RenderPanel() {
            setPreferredSize(new Dimension(640, 480));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            //draw ground
            drawBody(g2, gameModel.getGroundBody(), new GroundMaterial());

            //draw blocks
            for (BuildingBlock block : gameModel.getBlockPool()) {
                drawBlock(g2, block);
            }

            //draw joints
            for (BuildingBlockJoint joint : gameModel.getBuildingBlockJoints()) {
                drawJoint(g2, joint);
            }
        }

        private void drawBlock(Graphics2D g2, BuildingBlock block)
        {

            for (Body body : block.getBodies()) {
                drawBody(g2, body, block.getMaterial());
            }
        }

        private void drawBody(Graphics2D g2, Body body, Material mat) {
            for(PolygonShape shape = (PolygonShape) body.getShapeList(); shape != null; shape = (PolygonShape) shape.m_next) {

                Vec2[] vertices = shape.getVertices();
                AffineTransform currentTransform = g2.getTransform();

                g2.translate(body.getPosition().x, getHeight() - body.getPosition().y);
                g2.rotate(-body.getAngle());
          
                //fillPolygon implementation
                int[] xpoints = new int[vertices.length];
                int[] ypoints = new int[vertices.length];
                
                for (int i = 0; i < vertices.length; i++) {
                    xpoints[i] = (int)vertices[i].x;
                    ypoints[i] = -(int)vertices[i].y;
                }

                g2.setColor(mat.getColor()); //color defined by material
                g2.fillPolygon(xpoints, ypoints, vertices.length);

                g2.setTransform(currentTransform);
            }
        }

        private void drawJoint(Graphics2D g2, BuildingBlockJoint joint) {
            g2.setColor(Color.BLACK);
            g2.drawLine((int)joint.getPointerPosition().x, getHeight()-(int)joint.getPointerPosition().y, (int)joint.getBodyPosition().x, getHeight()-(int)joint.getBodyPosition().y);
        }

    }
}
