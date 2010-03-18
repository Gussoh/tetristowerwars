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
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.building.BuildingBlock;
import org.tetristowerwars.model.cannon.BulletBlock;
import org.tetristowerwars.model.cannon.CannonBlock;
import org.tetristowerwars.model.material.GroundMaterial;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;

/**
 *
 * @author Andreas
 */
public class SwingRenderer extends Renderer {

    private final JFrame frame;
    private final RenderPanel renderPanel;
    private double scale = 5.0;
    private LinkedHashMap<Integer, Point> cursorPoints = new LinkedHashMap<Integer, Point>();

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
        return new Point2D.Double(screenCoord.x / scale, (renderPanel.getHeight() - screenCoord.y) / scale);
    }

    @Override
    public void putCursorPoint(int id, Point point) {
        cursorPoints.put(id, point);
    }

    @Override
    public void removeCursorPoint(int id) {
        cursorPoints.remove(id);
    }

    private class RenderPanel extends JPanel {

        public RenderPanel() {
            setPreferredSize(new Dimension(640, 480));
            setDoubleBuffered(true);

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.scale(scale, scale);

            //draw ground
            drawBody(g2, gameModel.getGroundBody(), new GroundMaterial());

            //draw blocks
            for (BuildingBlock block : gameModel.getBlockPool()) {
                drawBuildingBlock(g2, block);
            }

            //draw cannons
            for (Player player : gameModel.getPlayers()) {
                for (CannonBlock cannonBlock : player.getCannons()) {
                    drawCannonBlock(g2, cannonBlock);
                }

                for (BulletBlock bulletBlock : player.getBullets()) {
                    drawBullet(g2, bulletBlock);
                }
            }

            //draw joints
            for (BuildingBlockJoint joint : gameModel.getBuildingBlockJoints()) {
                drawJoint(g2, joint);
            }

            g2.setColor(Color.RED);
            for (Point point : cursorPoints.values()) {
                g2.fillOval((int) (point.x / scale - 1), (int) (point.y / scale - 1), 3, 3);
            }
        }

        private void drawBuildingBlock(Graphics2D g2, BuildingBlock block) {
            for (Body body : block.getBodies()) {
                drawBody(g2, body, block.getMaterial());
            }
        }

        private void drawCannonBlock(Graphics2D g2, CannonBlock block) {
            for (Body body : block.getBodies()) {
                drawBody(g2, body, new SteelMaterial());
            }
        }

        private void drawBody(Graphics2D g2, Body body, Material mat) {
            for (PolygonShape shape = (PolygonShape) body.getShapeList(); shape != null; shape = (PolygonShape) shape.m_next) {

                Vec2[] vertices = shape.getVertices();
                AffineTransform currentTransform = g2.getTransform();

                g2.translate(body.getPosition().x, getHeight() / scale - body.getPosition().y);
                g2.rotate(-body.getAngle());

                g2.setColor(mat.getColor()); //color defined by material

                Path2D path = new Path2D.Float();

                path.moveTo(vertices[0].x, -vertices[0].y);
                for (int i = 1; i < vertices.length; i++) {
                    path.lineTo(vertices[i].x, -vertices[i].y);
                }
                g2.fill(path);

                g2.setTransform(currentTransform);
            }
        }

        private void drawJoint(Graphics2D g2, BuildingBlockJoint joint) {
            g2.setColor(Color.BLACK);
            g2.draw(new Line2D.Float(joint.getPointerPosition().x, (getHeight() / (float) scale) - joint.getPointerPosition().y, joint.getBodyPosition().x, (getHeight() / (float) scale) - joint.getBodyPosition().y));
        }

        private void drawBullet(Graphics2D g2, BulletBlock bullet) {

            g2.setColor(Color.red);

            Body body = bullet.getBodies()[0];
            CircleShape shape = (CircleShape) body.m_shapeList;

            float radius = shape.getRadius();
            Vec2 pos = body.getPosition();

            g2.fill(new Ellipse2D.Float(pos.x - radius, (getHeight() / (float) scale - pos.y - radius), radius * 2, radius * 2));

        }
    }
}
