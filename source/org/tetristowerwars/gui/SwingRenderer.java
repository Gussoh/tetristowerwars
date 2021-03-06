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
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BulletBlock;
import org.tetristowerwars.model.CannonBlock;

/**
 *
 * @author Andreas
 */
public class SwingRenderer extends Renderer {

    private final JFrame frame;
    private final RenderPanel renderPanel;
    private double scale = 5.0;
    private LinkedHashMap<Integer, Vec2> cursorPoints = new LinkedHashMap<Integer, Vec2>();
    private final List<Color> niceColors = new ArrayList<Color>();
    private final NumberFormat realNumberFormat = NumberFormat.getNumberInstance();

    public SwingRenderer(GameModel gameModel) {
        super(gameModel);
        renderPanel = new RenderPanel();
        frame = new JFrame("Awesomeness");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(renderPanel);
        frame.pack();
        frame.setVisible(true);

        realNumberFormat.setMaximumFractionDigits(2);
        realNumberFormat.setMinimumFractionDigits(0);

        niceColors.add(Color.red);
        niceColors.add(Color.green);
        niceColors.add(Color.yellow);
    }

    @Override
    public Component getInputComponent() {
        return renderPanel;
    }

    @Override
    public void renderFrame() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    renderPanel.paintImmediately(renderPanel.getVisibleRect());
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(SwingRenderer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(SwingRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Vec2 convertWindowToWorldCoordinates(Point windowCoord) {
        return new Vec2((float) (windowCoord.x / scale), (float) ((renderPanel.getHeight() - windowCoord.y) / scale));
    }

    private Vec2 convertWorldToWindowCoordinates(Vec2 worldCoord) {
        return new Vec2((float) (scale * worldCoord.x), (float) (renderPanel.getHeight() - scale * worldCoord.y));
    }

    @Override
    public void putCursorPoint(int id, Vec2 point, boolean hit) {
        cursorPoints.put(id, point);
    }

    @Override
    public void removeCursorPoint(int id) {
        cursorPoints.remove(id);
    }

    @Override
    public float getRenderWorldHeight() {
        throw new UnsupportedOperationException("Not supported yet.");
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
            drawBody(g2, gameModel.getGroundBlock().getBody(), true);

            //draw blocks
            g2.setColor(Color.GRAY);
            for (BuildingBlock block : gameModel.getBuildingBlockPool()) {
                drawBuildingBlock(g2, block);
            }

            //draw cannons
            for (int i = 0; i < gameModel.getPlayers().size(); i++) {
                g2.setColor(niceColors.get(i));

                for (BuildingBlock buildingBlock : gameModel.getPlayers().get(i).getBuildingBlocks()) {
                    drawBuildingBlock(g2, buildingBlock);
                }

                for (CannonBlock cannonBlock : gameModel.getPlayers().get(i).getCannons()) {
                    drawCannonBlock(g2, cannonBlock);
                }

                for (BulletBlock bulletBlock : gameModel.getPlayers().get(i).getBullets()) {
                    drawBullet(g2, bulletBlock);
                }
            }

            //draw joints
            for (BuildingBlockJoint joint : gameModel.getBuildingBlockJoints()) {
                drawJoint(g2, joint);
            }

            // Draw markers
            g2.setColor(Color.RED);
            for (Vec2 point : cursorPoints.values()) {
                Vec2 windowCoord = convertWorldToWindowCoordinates(point);
                Ellipse2D ellipse = new Ellipse2D.Double(windowCoord.x / scale - 1, windowCoord.y / scale - 1, 3, 3);
                g2.fill(ellipse);
            }

            // Draw tower heights
            g2.setColor(Color.BLACK);
            for (Player player : gameModel.getPlayers()) {
                BuildingBlock bb = player.getHighestBuilingBlockInTower();

                if (bb != null) {
                    drawBody(g2, bb.getBody(), false);
                }
            }
            g2.scale(1 / scale, 1 / scale);
            g2.drawString("Physics Engine - time to update: " + realNumberFormat.format(gameModel.getTimeTakenToExecuteUpdateMs()) + " ms", 10, 10);
        }

        private void drawBuildingBlock(Graphics2D g2, BuildingBlock block) {
            drawBody(g2, block.getBody(), true);
        }

        private void drawCannonBlock(Graphics2D g2, CannonBlock block) {
            drawBody(g2, block.getBody(), true);
        }

        private void drawBody(Graphics2D g2, Body body, boolean fill) {
            for (Shape abstractShape = body.getShapeList(); abstractShape != null; abstractShape = abstractShape.m_next) {

                if (abstractShape instanceof PolygonShape) {

                    PolygonShape shape = (PolygonShape) abstractShape;
                    Vec2[] vertices = shape.getVertices();
                    AffineTransform currentTransform = g2.getTransform();

                    g2.translate(body.getPosition().x, getHeight() / scale - body.getPosition().y);
                    g2.rotate(-body.getAngle());

                    Path2D path = new Path2D.Float();

                    path.moveTo(vertices[0].x, -vertices[0].y);
                    for (int i = 1; i < vertices.length; i++) {
                        path.lineTo(vertices[i].x, -vertices[i].y);
                    }
                    path.closePath();

                    if (fill) {
                        g2.fill(path);
                    } else {
                        g2.draw(path);
                    }

                    g2.setTransform(currentTransform);
                }
            }
        }

        private void drawJoint(Graphics2D g2, BuildingBlockJoint joint) {
            g2.setColor(Color.BLACK);
            g2.draw(new Line2D.Float(joint.getPointerPosition().x, (getHeight() / (float) scale) - joint.getPointerPosition().y, joint.getBodyPosition().x, (getHeight() / (float) scale) - joint.getBodyPosition().y));
        }

        private void drawBullet(Graphics2D g2, BulletBlock bullet) {

            g2.setColor(Color.red);

            Body body = bullet.getBody();
            CircleShape shape = (CircleShape) body.m_shapeList;

            float radius = shape.getRadius();
            Vec2 pos = body.getPosition();

            g2.fill(new Ellipse2D.Float(pos.x - radius, (getHeight() / (float) scale - pos.y - radius), radius * 2, radius * 2));

        }
    }
}
