/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.Settings;
import org.tetristowerwars.MainFrame;
import org.tetristowerwars.gui.gl.BackgroundAnimationRenderer;
import org.tetristowerwars.gui.gl.BackgroundRenderer;
import org.tetristowerwars.gui.gl.EffectRenderer;
import org.tetristowerwars.gui.gl.BulletRenderer;
import org.tetristowerwars.gui.gl.CannonRenderer;

import org.tetristowerwars.gui.gl.JointRenderer;
import org.tetristowerwars.gui.gl.Pointer;
import org.tetristowerwars.gui.gl.PointerRenderer;
import org.tetristowerwars.gui.gl.RectangularBuildingBlockRenderer;
import org.tetristowerwars.gui.gl.MessageRenderer;
import org.tetristowerwars.gui.gl.TriggerRenderer;
import org.tetristowerwars.gui.gl.WinRenderer;
import org.tetristowerwars.gui.gl.WinningHeightRenderer;
import org.tetristowerwars.gui.gl.animation.BackgroundAnimationFactory;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.BulletBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.GameModelListener;
import org.tetristowerwars.model.Player;
import static javax.media.opengl.GL.*;
import org.tetristowerwars.model.WinningCondition;

/**
 *
 * @author Andreas
 */
public class GLRenderer extends Renderer implements GLEventListener, GameModelListener {

    private final GLCanvas glCanvas;
    private Map<Integer, Pointer> id2Pointers = new LinkedHashMap<Integer, Pointer>();
    private BackgroundRenderer backgroundRenderer;
    private JointRenderer jointRenderer;
    private BulletRenderer bulletRenderer;
    private CannonRenderer cannonRenderer;
    private PointerRenderer pointerRenderer;
    private EffectRenderer effectRenderer;
    private MessageRenderer messageRenderer;
    private WinRenderer winRenderer;
    private TriggerRenderer triggerRenderer;
    private WinningHeightRenderer winningHeightRenderer;
    private RectangularBuildingBlockRenderer rectangularBuildingBlockRenderer;
    private BackgroundAnimationRenderer backgroundAnimationRenderer;
    private BackgroundAnimationFactory backgroundAnimationFactory;
    private float renderWorldHeight;
    private float lineWidthFactor = 1.0f;
    private float[] ambientLight = {0.2f, 0.2f, 0.2f, 1.0f};
    private float[] mainLightColor = {1.0f, 0.9f, 0.9f, 1.0f};
    //private float[] mainLightPosition = {0.0f, 0.7071f, 0.7071f, 0.0f};
    private float[] mainLightPosition = {0.2f, 0.7f, 0.7f, 0.0f};
    private long lastTimeMillis;
    private final MainFrame mainFrame;
    private final boolean lightingEffects;
    private final boolean sceneAntiAliasing;
    private long frameCounter = 0;
    private long performanceTimer = 0;
    private Semaphore renderingSemaphore = new Semaphore(0);
    private final boolean useParticleEffects;

    /**
     * 
     * @param gameModel The game model.
     * @param mainFrame The main Frame.
     */
    public GLRenderer(GameModel gameModel, MainFrame mainFrame) {
        super(gameModel);
        this.mainFrame = mainFrame;

        Settings settings = mainFrame.getSettings();

        this.lightingEffects = settings.isLightingEnabled();
        this.useParticleEffects = settings.isParticlesEnabled();

        // Anti-aliasing is needed when using lighting for good looking gfx :)
        this.sceneAntiAliasing = lightingEffects;


        GLCapabilities capabilities = new GLCapabilities();

        // Double buffered and hardware accelerated.
        capabilities.setDoubleBuffered(true);
        capabilities.setHardwareAccelerated(true);
        capabilities.setStereo(false);
        capabilities.setSampleBuffers(sceneAntiAliasing);
        capabilities.setNumSamples(4);

        // Use 32-bit RGBA
        capabilities.setRedBits(8);
        capabilities.setGreenBits(8);
        capabilities.setBlueBits(8);
        capabilities.setAlphaBits(8);



        glCanvas = new GLCanvas(capabilities); //, null, null, graphicsDevice);

        glCanvas.addGLEventListener(this);
        glCanvas.setAutoSwapBufferMode(true);
        glCanvas.setPreferredSize(new Dimension(settings.getWindowWidth(), settings.getWindowHeight()));

        mainFrame.openComponent(glCanvas, false);

    }

    @Override
    public void renderFrame() {
        renderingSemaphore.release();
        glCanvas.display();
    }

    @Override
    public Component getInputComponent() {
        return glCanvas;
    }

    @Override
    public Vec2 convertWindowToWorldCoordinates(Point windowCoord) {
        AABB aabb = gameModel.getWorldBoundries();

        return new Vec2((float) windowCoord.x * (aabb.upperBound.x / (float) glCanvas.getWidth()), (float) (glCanvas.getHeight() - windowCoord.y) * (renderWorldHeight / (float) glCanvas.getHeight()));
    }

    @Override
    public void putCursorPoint(int id, Point point, boolean hit) {
        id2Pointers.put(id, new Pointer(convertWindowToWorldCoordinates(point), hit));
    }

    @Override
    public void removeCursorPoint(int id) {
        id2Pointers.remove(id);
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();
        gl.setSwapInterval(1);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        jointRenderer = new JointRenderer();
        try {
            bulletRenderer = new BulletRenderer(gl, lightingEffects);
            cannonRenderer = new CannonRenderer(gl, gameModel.getBlockSize(), lightingEffects);
            pointerRenderer = new PointerRenderer(gl);
            effectRenderer = new EffectRenderer(gl, gameModel);
            messageRenderer = new MessageRenderer(gl);
            backgroundAnimationRenderer = new BackgroundAnimationRenderer(gl, mainFrame.getSettings().getWorldTheme());
            rectangularBuildingBlockRenderer = new RectangularBuildingBlockRenderer(gl, lightingEffects);
            backgroundAnimationFactory = new BackgroundAnimationFactory(backgroundAnimationRenderer, gameModel.getGroundLevel(), gameModel.getGroundLevel() + 30, gameModel.getWorldBoundries().upperBound.x);
            winRenderer = new WinRenderer(gl, gameModel);
            winningHeightRenderer = new WinningHeightRenderer();
            triggerRenderer = new TriggerRenderer(gl);
        } catch (IOException ex) {
            Logger.getLogger(GLRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }


        gameModel.addGameModelListener(this);

        gl.glEnable(GL_NORMALIZE);
        gl.glEnable(GL_BLEND);

        gl.glEnable(GL_LINE_SMOOTH);
        gl.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, mainLightColor, 0);
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, mainLightColor, 0);
        gl.glLightfv(GL_LIGHT0, GL_POSITION, mainLightPosition, 0);

        gl.glEnable(GL_LIGHT0);

        gl.glEnableClientState(GL_VERTEX_ARRAY);

        gl.glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambientLight, 0);

        //gl.glLightModeli(GL_LIGHT_MODEL_LOCAL_VIEWER, GL_TRUE);
        // TODO: Check if we need to have specular color added in the fragment shader instead.
        // This might be needed for good looking shiny effects when using textures.
        //gl.glLightModeli(GL_LIGHT_MODEL_COLOR_CONTROL, GL_SEPARATE_SPECULAR_COLOR);


        lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        if (!renderingSemaphore.tryAcquire()) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        long startTime = System.nanoTime();
        float elapsedTimeS = (float) (currentTime - lastTimeMillis) / 1000.0f;
        lastTimeMillis = currentTime;
        frameCounter++;

        GL gl = drawable.getGL();

        // gl.glClear(GL.GL_COLOR_BUFFER_BIT);  // Not needed since we draw a background
        gl.glLoadIdentity();

        // background renderer depends on the shape of the window,
        // in some cases display() might be called before reshape(), this check prevent us from failing.
        if (backgroundRenderer != null) {
            backgroundRenderer.render(gl);
        }

        // Update and render tanks, zeppelins, etc.
        backgroundAnimationFactory.run(elapsedTimeS);
        backgroundAnimationRenderer.render(gl, elapsedTimeS);

        // render building blocks, except outline
        rectangularBuildingBlockRenderer.render(gl, gameModel, elapsedTimeS);
        // Render the light effect when a block changes owner
        rectangularBuildingBlockRenderer.renderOverlay(gl);

        // Render block outlines
        rectangularBuildingBlockRenderer.renderLines(gl, lineWidthFactor * gameModel.getBlockSize() * 0.75f);

        // Render the joints between mouse/finger and block.
        jointRenderer.renderLines(gl, gameModel.getBuildingBlockJoints(), lineWidthFactor * 10.0f);



        cannonRenderer.render(gl, gameModel);

        if (useParticleEffects) {
            for (Player player : gameModel.getPlayers()) {
                for (BulletBlock bulletBlock : player.getBullets()) {
                    effectRenderer.createBulletTrailEffect(bulletBlock);
                }
            }
        }

        
        // Render the light effect of the players border when a block changes owner
        effectRenderer.render(gl, gameModel, elapsedTimeS, lineWidthFactor * 4.0f);
        effectRenderer.renderParticles(gl, elapsedTimeS);

        bulletRenderer.render(gl, gameModel);

        winningHeightRenderer.render(gl, gameModel, lineWidthFactor);

        backgroundRenderer.renderBottom(gl);

        triggerRenderer.render(gl, gameModel);

        // Render the mouse/finger circles.
        pointerRenderer.render(gl, id2Pointers, elapsedTimeS);
        messageRenderer.render(drawable, gameModel, renderWorldHeight, elapsedTimeS);


        winRenderer.render(gl, gameModel, elapsedTimeS, renderWorldHeight, useParticleEffects);


        performanceTimer += System.nanoTime() - startTime;
        if (frameCounter % 60 == 0) {
            // System.out.println("Average render time: " + (performanceTimer / (60 * 1000000f)) + " ms");
            performanceTimer = 0;
        }

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
            backgroundRenderer = new BackgroundRenderer(gl, aabb.upperBound.x, renderWorldHeight, gameModel.getGroundLevel(), gameModel.getGroundLevel() + 30, mainFrame.getSettings().getWorldTheme());
        } catch (IOException ex) {
            Logger.getLogger(GLRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Scale linewidth with some nice constant
        lineWidthFactor = (float) width * 0.0004f;

    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        System.out.println("displayChanged triggered, do we need to do somehting here?");
    }

    @Override
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed, Vec2 contactPoint) {

        if (tangentSpeed > 4.0f) {
            if (useParticleEffects) {
                int numParticles = Math.min((int) tangentSpeed, 10);
                effectRenderer.createFrictionEffect(contactPoint, numParticles);
            }
        }
    }

    @Override
    public void onJointCreation(BuildingBlockJoint blockJoint) {
    }

    @Override
    public void onBlockDestruction(Block block) {
        if (useParticleEffects) {
            if (block instanceof BulletBlock) {
                effectRenderer.createExplosionEffect(block.getBody().getPosition());
                effectRenderer.createSmokeEffect(block.getBody().getPosition());
            } else if (block instanceof BuildingBlock) {
                effectRenderer.createBuildingBlockDestructionEffect((BuildingBlock) block);
            }
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
        Vec2 v = block.getBody().getPosition();
        float leftLimit = block.getOwner().getLeftLimit();
        float rightLimit = block.getOwner().getRightLimit();

        rectangularBuildingBlockRenderer.addBuildingBlockOverlayAnimation(block);
        if (Math.abs(v.x - leftLimit) < Math.abs(v.x - rightLimit)) {
            // Its the left limit!!
            effectRenderer.createBorderLitAnimation(new Vec2(leftLimit, v.y));
        } else {
            effectRenderer.createBorderLitAnimation(new Vec2(rightLimit, v.y));
        }
    }

    @Override
    public void onWinningConditionFulfilled(WinningCondition condition) {
    }

    @Override
    public void onLeaderChanged(Player leader) {
    }

    @Override
    public void onGameReset() {
    }

    public void onTimerBeat(int beatsLeft) {
    }
}
