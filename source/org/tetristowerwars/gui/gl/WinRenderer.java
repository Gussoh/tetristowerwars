/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.gl.animation.Path;
import org.tetristowerwars.gui.gl.particle.Color;
import org.tetristowerwars.gui.gl.particle.FadeOutStepFunction;
import org.tetristowerwars.gui.gl.particle.GravityStepFunction;
import org.tetristowerwars.gui.gl.particle.Particle;
import org.tetristowerwars.gui.gl.particle.PointSourceParticleEngine;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.util.MathUtil;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class WinRenderer {

    private final Texture flagTexture;
    private final Texture particleTexture;
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private FloatBuffer colorBuffer;
    private final static int NUM_VERTICES_PER_OBJECT = 4;
    private final static float FLAG_ANIMATION_TIME = 2.0f;
    private Path flagAnimation = null;
    private Path flagResizeAnimation = null;
    private final float widthHeightRatio;
    private final float[] color = {1.0f, 1.0f, 1.0f, 1.0f};
    private final PointSourceParticleEngine particleEngine = new PointSourceParticleEngine();
    private boolean shouldFlagExplode;

    public WinRenderer(GL gl) throws IOException {
        flagTexture = TextureIO.newTexture(new File("res/gfx/decoration/SovietFlagAndArms.png"), true);
        particleTexture = TextureIO.newTexture(new File("res/gfx/particle.png"), true);
        GLUtil.fixTextureParameters(flagTexture);
        widthHeightRatio = flagTexture.getWidth() / (float) flagTexture.getHeight();

        vertexBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_OBJECT * 2);
        texCoordBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_OBJECT * 2);
        colorBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_OBJECT * 4);

        particleEngine.addStepFunction(new GravityStepFunction());
        particleEngine.addStepFunction(new FadeOutStepFunction(0.5f));
        //particleEngine.addStepFunction(new VelocityDampStepFunction(10.0f));
    }

    public void render(GL gl, GameModel gameModel, float elapsedTimeS, float renderHeight, boolean useParticles) {
        if (gameModel.getWinningCondition().gameIsOver()) {
            if (flagAnimation == null) {
                float width = gameModel.getWorldBoundries().upperBound.x;
                float centerX = width * 0.5f;
                float centerY = renderHeight * 0.5f;
                Player winner = gameModel.getLeader();

                float playerCenterX = (winner.getLeftLimit() + winner.getRightLimit()) * 0.5f;
                float halfPlayerWidth = (winner.getRightLimit() - winner.getLeftLimit()) * 0.5f;

                flagAnimation = new Path(new Vec2(centerX, centerY), new Vec2(playerCenterX, centerY), FLAG_ANIMATION_TIME);
                flagResizeAnimation = new Path(new Vec2(width, 0), new Vec2(halfPlayerWidth, 0), FLAG_ANIMATION_TIME);

                particleEngine.setDirection(0, MathUtil.PI * 2.0f);
                particleEngine.setRotationSpeed(0, 0);
                particleEngine.setTimeToLive(0.5f, 0.6f);
                particleEngine.setSpeed(20.0f, 25.0f);
                particleEngine.setColor(new Color(0.7f, 0.6f, 0.3f, 1.0f), new Color(0.9f, 0.7f, 0.5f, 1.0f), false);
            }

            flagAnimation.addTime(elapsedTimeS);
            flagResizeAnimation.addTime(elapsedTimeS);
            particleEngine.update(elapsedTimeS);

            Vec2 flagPos = flagAnimation.getCurrentPosition();
            float halfWidth = flagResizeAnimation.getCurrentPosition().x * 0.5f;
            float halfHeight = halfWidth * widthHeightRatio;

            if (useParticles) {
                particleEngine.setPosition(flagPos, Math.min(halfWidth, halfHeight));
                if (!flagAnimation.isDone()) {
                    particleEngine.setRadius(halfWidth * 0.05f, halfWidth * 0.05f);
                    particleEngine.createParticles((int) (elapsedTimeS * 5000));
                } else if (shouldFlagExplode) {
                    shouldFlagExplode = false;
                    particleEngine.setSpeed(30.0f, 60.0f);
                    particleEngine.setTimeToLive(10.0f, 12.0f);
                    particleEngine.createParticles(5000);
                }
            }

            int numParticleVertices = particleEngine.getParticles().size() * NUM_VERTICES_PER_OBJECT;

            if (vertexBuffer.capacity() < 2 * (numParticleVertices + NUM_VERTICES_PER_OBJECT)) {
                vertexBuffer = BufferUtil.newFloatBuffer(2 * (numParticleVertices + NUM_VERTICES_PER_OBJECT));
                texCoordBuffer = BufferUtil.newFloatBuffer(2 * (numParticleVertices + NUM_VERTICES_PER_OBJECT));
                colorBuffer = BufferUtil.newFloatBuffer(4 * (numParticleVertices + NUM_VERTICES_PER_OBJECT));
            }

            vertexBuffer.put(new float[]{
                        flagPos.x - halfWidth, flagPos.y - halfHeight,
                        flagPos.x + halfWidth, flagPos.y - halfHeight,
                        flagPos.x + halfWidth, flagPos.y + halfHeight,
                        flagPos.x - halfWidth, flagPos.y + halfHeight
                    });

            texCoordBuffer.put(new float[]{
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 0.0f
                    });

            colorBuffer.put(new float[]{
                        color[0], color[1], color[2], color[3],
                        color[0], color[1], color[2], color[3],
                        color[0], color[1], color[2], color[3],
                        color[0], color[1], color[2], color[3]
                    });

            for (Particle particle : particleEngine.getParticles()) {
                Vec2 pos = particle.getPosition();
                Color c = particle.getCurrentColor();
                float r = particle.getRadius();

                vertexBuffer.put(new float[]{
                            pos.x - r, pos.y - r,
                            pos.x + r, pos.y - r,
                            pos.x + r, pos.y + r,
                            pos.x - r, pos.y + r
                        });

                texCoordBuffer.put(new float[]{
                            0.0f, 1.0f,
                            1.0f, 1.0f,
                            1.0f, 0.0f,
                            0.0f, 0.0f
                        });

                colorBuffer.put(new float[]{
                            c.r, c.g, c.b, c.a,
                            c.r, c.g, c.b, c.a,
                            c.r, c.g, c.b, c.a,
                            c.r, c.g, c.b, c.a
                        });
            }


            vertexBuffer.rewind();
            texCoordBuffer.rewind();
            colorBuffer.rewind();

            gl.glEnable(GL_TEXTURE_2D);
            gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            gl.glEnableClientState(GL_COLOR_ARRAY);
            gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);

            gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
            gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);
            gl.glColorPointer(4, GL_FLOAT, 0, colorBuffer);

            flagTexture.bind();
            gl.glDrawArrays(GL_QUADS, 0, NUM_VERTICES_PER_OBJECT);

            particleTexture.bind();
            gl.glDrawArrays(GL_QUADS, NUM_VERTICES_PER_OBJECT, numParticleVertices);


            gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL_COLOR_ARRAY);
            gl.glDisable(GL_TEXTURE_2D);
        } else { //!gameIsOver
            shouldFlagExplode = true;
            flagAnimation = null;
            flagResizeAnimation = null;
        }
    }
}

