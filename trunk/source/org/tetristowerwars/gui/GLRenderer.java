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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import org.jbox2d.collision.AABB;
import org.tetristowerwars.gui.gl.Background;
import org.tetristowerwars.gui.gl.BuildingBlockRenderer;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.GameModelListener;
import org.tetristowerwars.model.RectangularBuildingBlock;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class GLRenderer extends Renderer implements GLEventListener, GameModelListener {

    private final GLCanvas glCanvas;
    private final JFrame frame;
    private Map<Integer, Point> id2windowPoints = new LinkedHashMap<Integer, Point>();
    private Background background;
    private LinkedHashMap<RectangularBuildingBlock, BuildingBlockRenderer> blockRenderers = new LinkedHashMap<RectangularBuildingBlock, BuildingBlockRenderer>();
    private float renderWorldHeight;
    

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
        capabilities.setSampleBuffers(true);
        capabilities.setNumSamples(2);

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
        Point2D point = new Point2D.Double((float)windowCoord.x * (aabb.upperBound.x / (float)glCanvas.getWidth()), (float)(glCanvas.getHeight() - windowCoord.y) * (renderWorldHeight / (float)glCanvas.getHeight()));

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
        // TODO: Remove debugGl after testing is done.
        //drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glEnable(GL.GL_TEXTURE_2D);
        // Draw background
        if (background != null) {
            background.render(gl);
        }


        // Update block renderers if necessary
        for (BuildingBlock buildingBlock : gameModel.getBuildingBlockPool()) {
            if (buildingBlock instanceof RectangularBuildingBlock) {
                RectangularBuildingBlock rbb = (RectangularBuildingBlock) buildingBlock;
                if (!blockRenderers.containsKey(rbb)) {
                    blockRenderers.put(rbb, new BuildingBlockRenderer(gl, rbb));
                }
            }
        }


        // Draw building blocks
        gl.glEnableClientState(GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        for (BuildingBlockRenderer bbr : blockRenderers.values()) {
            bbr.render(gl);
        }

        gl.glDisable(GL_TEXTURE_2D);
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
            background = new Background(gl, aabb.upperBound.x, renderWorldHeight);
        } catch (IOException ex) {
            Logger.getLogger(GLRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            blockRenderers.remove(rbb);
        }
    }

    @Override
    public void onJointDestruction(BuildingBlockJoint blockJoint) {

    }
}
