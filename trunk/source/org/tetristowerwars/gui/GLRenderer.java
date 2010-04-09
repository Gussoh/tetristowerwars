/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui;

import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import org.jbox2d.collision.AABB;
import org.tetristowerwars.gui.gl.BackgroundAnimationRenderer;
import org.tetristowerwars.gui.gl.BackgroundRenderer;
import org.tetristowerwars.gui.gl.BulletRenderer;
import org.tetristowerwars.gui.gl.CannonRenderer;
import org.tetristowerwars.gui.gl.RectangularBuildingBlockRenderer;
import org.tetristowerwars.gui.gl.JointRenderer;
import org.tetristowerwars.gui.gl.animation.BackgroundAnimationFactory;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.GameModelListener;
import org.tetristowerwars.model.RectangularBuildingBlock;
import static javax.media.opengl.GL.*;
import org.tetristowerwars.model.WinningCondition;

/**
 *
 * @author Andreas
 */
public class GLRenderer extends Renderer implements GLEventListener, GameModelListener {

    private final GLCanvas glCanvas;
    private final JFrame frame;
    private Map<Integer, Point> id2windowPoints = new LinkedHashMap<Integer, Point>();

    private BackgroundRenderer backgroundRenderer;
    private JointRenderer jointRenderer;
    private BulletRenderer bulletRenderer;
    private CannonRenderer cannonRenderer;
    private BackgroundAnimationRenderer animationRenderer;
    private BackgroundAnimationFactory animationFactory;

    private LinkedHashMap<RectangularBuildingBlock, RectangularBuildingBlockRenderer> rectBlock2renderers = new LinkedHashMap<RectangularBuildingBlock, RectangularBuildingBlockRenderer>();
    private float renderWorldHeight;
    private float[] blockOutlineColor = {0.0f, 0.0f, 0.0f, 1.0f};
    private float[] jointColor = {0.0f, 0.0f, 0.0f, 1.0f};
    private long lastTimeMillis;

    /**
     * 
     * @param gameModel The game model.
     * @param graphicsDevice The graphics device to use, or null to use the default device.
     */
    public GLRenderer(GameModel gameModel, GraphicsDevice graphicsDevice) {
        super(gameModel);
        gameModel.addGameModelListener(this);

        GLCapabilities capabilities = new GLCapabilities();

        // Double buffered and hardware accelerated.
        capabilities.setDoubleBuffered(true);
        capabilities.setHardwareAccelerated(true);
        capabilities.setStereo(false);
        capabilities.setSampleBuffers(false);
        capabilities.setNumSamples(4);

        // Use 32-bit RGBA
        capabilities.setRedBits(8);
        capabilities.setGreenBits(8);
        capabilities.setBlueBits(8);
        capabilities.setAlphaBits(8);

        if (graphicsDevice == null) {
            graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        }

        glCanvas = new GLCanvas(capabilities, null, null, graphicsDevice);

        glCanvas.addGLEventListener(this);
        glCanvas.setAutoSwapBufferMode(true);

        frame = new JFrame("Awesomeness");
        frame.add(glCanvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    @Override
    public void renderFrame() {
        glCanvas.display();
    }

    @Override
    public Component getMouseInputComponent() {
        return glCanvas;
    }

    @Override
    public Point2D convertWindowToWorldCoordinates(Point windowCoord) {
        AABB aabb = gameModel.getWorldBoundries();
        Point2D point = new Point2D.Double((float) windowCoord.x * (aabb.upperBound.x / (float) glCanvas.getWidth()), (float) (glCanvas.getHeight() - windowCoord.y) * (renderWorldHeight / (float) glCanvas.getHeight()));

        return point;
    }

    @Override
    public void putCursorPoint(int id, Point point) {
        id2windowPoints.put(id, point);
    }

    @Override
    public void removeCursorPoint(int id) {
        id2windowPoints.remove(id);
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        jointRenderer = new JointRenderer();
        try {
            bulletRenderer = new BulletRenderer(gl);
            cannonRenderer = new CannonRenderer(gl);
            animationRenderer = new BackgroundAnimationRenderer(gl);
            animationFactory = new BackgroundAnimationFactory(animationRenderer, gameModel.getGroundLevel(), gameModel.getGroundLevel() + 30, gameModel.getWorldBoundries().upperBound.x);
        } catch (IOException ex) {
            Logger.getLogger(GLRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        gl.glEnableClientState(GL_VERTEX_ARRAY);
        gl.glEnable(GL_BLEND);
        gl.glEnable(GL_LINE_SMOOTH);
        gl.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

        lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastTimeMillis;
        lastTimeMillis = currentTime;

        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // Draw background
        if (backgroundRenderer != null) {
            // This blend function is needed for photoshop-like blend.
            gl.glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
            backgroundRenderer.render(gl);
        }

        animationFactory.run(elapsedTime);
        animationRenderer.render(gl, elapsedTime);

        cannonRenderer.render(gl, gameModel);
        

        // Update block renderers if necessary. Since all new blocks belongs to the block pool first,
        // there is no need to iterate over player blocks
        for (BuildingBlock buildingBlock : gameModel.getBuildingBlockPool()) {
            if (buildingBlock instanceof RectangularBuildingBlock) {
                RectangularBuildingBlock rbb = (RectangularBuildingBlock) buildingBlock;
                if (!rectBlock2renderers.containsKey(rbb)) {
                    RectangularBuildingBlockRenderer rbbr = new RectangularBuildingBlockRenderer(gl, rbb);
                    rectBlock2renderers.put(rbb, rbbr);
                }
            }
        }


        // Create render list based on texture name to avoid expense texture.bind operations.
        ArrayList<RectangularBuildingBlockRenderer> renderList = new ArrayList<RectangularBuildingBlockRenderer>(rectBlock2renderers.values());
        Collections.sort(renderList);

        String currentMaterialName = "";
        for (RectangularBuildingBlockRenderer rbbr : renderList) {
            if (!currentMaterialName.equals(rbbr.getMaterialName())) {
                currentMaterialName = rbbr.getMaterialName();
                rbbr.bindTexture();
            }
            rbbr.render(gl);
        }

        bulletRenderer.render(gl, gameModel);

        // Render block outlines
        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        // This blend function is needed for good looking anti-aliased lines.
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4fv(blockOutlineColor, 0);
        for (RectangularBuildingBlockRenderer rbbr : renderList) {
            rbbr.renderLines(gl);
        }

        // Render joints between mouse/finger and block.
        // Blend function already set.
        gl.glColor4fv(jointColor, 0);
        jointRenderer.render(gl, gameModel.getBuildingBlockJoints());

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        AABB aabb = gameModel.getWorldBoundries();
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }

        float heightRatio = (float) height / (float) width;
        renderWorldHeight = (aabb.upperBound.x - aabb.lowerBound.x) * heightRatio;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(aabb.lowerBound.x,
                aabb.upperBound.x,
                aabb.lowerBound.y,
                renderWorldHeight);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        try {
            backgroundRenderer = new BackgroundRenderer(gl, aabb.upperBound.x, renderWorldHeight, gameModel.getGroundLevel(), gameModel.getGroundLevel() + 30);
        } catch (IOException ex) {
            Logger.getLogger(GLRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Scale linewidth
        gl.glLineWidth(0.2f * (float) width / 100.0f);
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        System.out.println("displayChanged triggered, do we need to do somehting here?");
    }

    @Override
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed) {
    }

    @Override
    public void onJointCreation(BuildingBlockJoint blockJoint) {
    }

    @Override
    public void onBlockDestruction(Block block) {
        if (block instanceof RectangularBuildingBlock) {
            RectangularBuildingBlock rbb = (RectangularBuildingBlock) block;
            rectBlock2renderers.remove(rbb);
        }
    }

    @Override
    public void onJointDestruction(BuildingBlockJoint blockJoint) {
    }

    @Override
    public void onBlockCreation(Block block) {
    }

    @Override
    public void onBuildingBlockOwnerChanged(BuildingBlock block) {
    }

    @Override
    public void onWinningConditionFulfilled(WinningCondition condition) {
    }

    @Override
    public void onLeaderChanged(ArrayList scoreList) {
    }
}