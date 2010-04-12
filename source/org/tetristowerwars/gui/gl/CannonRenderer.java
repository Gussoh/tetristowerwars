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
import org.jbox2d.common.XForm;
import org.tetristowerwars.model.CannonBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.util.MathUtil;
import org.tetristowerwars.util.Vec3;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class CannonRenderer {

    private final Texture baseTexture;
    private final Texture topTexture;
    private final Texture pipeTexture;
    private FloatBuffer baseVertexBuffer;
    private FloatBuffer topVertexBuffer;
    private FloatBuffer pipeVertexBuffer;
    private FloatBuffer baseTexCoordBuffer;
    private FloatBuffer topTexCoordBuffer;
    private FloatBuffer pipeTexCoordBuffer;
    private FloatBuffer baseNormalBuffer;
    private FloatBuffer topNormalBuffer;
    private FloatBuffer pipeNormalBuffer;
    private final Vec2 pipeLeftBottom;
    private final Vec2 pipeRightBottom;
    private final Vec2 pipeRightTop;
    private final Vec2 pipeLeftTop;
    private final int NUM_VERTICES_PER_PART = 4;
    private final boolean lightingEffects;
    private final float[] color = {0.9f, 0.9f, 0.9f, 1.0f};
    private final float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};

    public CannonRenderer(GL gl, float blockSize, boolean lightingEffects) throws IOException {
        baseTexture = TextureIO.newTexture(new File("res/gfx/cannon_base.png"), true);
        topTexture = TextureIO.newTexture(new File("res/gfx/cannon_top.png"), true);
        pipeTexture = TextureIO.newTexture(new File("res/gfx/cannon_pipe.png"), true);

        GLUtil.fixTextureParameters(baseTexture);
        GLUtil.fixTextureParameters(topTexture);
        GLUtil.fixTextureParameters(pipeTexture);

        baseVertexBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 2);
        topVertexBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 2);
        pipeVertexBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 2);

        baseTexCoordBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 2);
        topTexCoordBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 2);
        pipeTexCoordBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 2);

        if (lightingEffects) {
            baseNormalBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 3);
            topNormalBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 3);
            pipeNormalBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 3);
        }

        pipeLeftBottom = new Vec2(-2 * blockSize, -blockSize * 0.5f);
        pipeRightBottom = new Vec2(0, -blockSize * 0.5f);
        pipeRightTop = new Vec2(0, blockSize * 0.5f);
        pipeLeftTop = new Vec2(-2 * blockSize, blockSize * 0.5f);
        this.lightingEffects = lightingEffects;
    }

    public void render(GL gl, GameModel gameModel) {
        int numCannons = 0;

        for (Player player : gameModel.getPlayers()) {
            numCannons += player.getCannons().size();
        }

        int numCoords = numCannons * NUM_VERTICES_PER_PART;

        if (numCoords * 2 > baseVertexBuffer.capacity()) {
            baseVertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            topVertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            pipeVertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);

            baseTexCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            topTexCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            pipeTexCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);

            if (lightingEffects) {
                baseNormalBuffer = BufferUtil.newFloatBuffer(numCoords * 3);
                topNormalBuffer = BufferUtil.newFloatBuffer(numCoords * 3);
                pipeNormalBuffer = BufferUtil.newFloatBuffer(numCoords * 3);
            }
        }

        float blockSize = gameModel.getBlockSize();
        XForm xf = new XForm();

        for (Player player : gameModel.getPlayers()) {
            for (CannonBlock cannonBlock : player.getCannons()) {
                Vec2 pos = cannonBlock.getBody().getPosition();

                baseVertexBuffer.put(new float[]{
                            pos.x - blockSize, pos.y - blockSize * 2,
                            pos.x + blockSize, pos.y - blockSize * 2,
                            pos.x + blockSize, pos.y,
                            pos.x - blockSize, pos.y
                        });
                topVertexBuffer.put(new float[]{
                            pos.x - blockSize, pos.y,
                            pos.x + blockSize, pos.y,
                            pos.x + blockSize, pos.y + blockSize,
                            pos.x - blockSize, pos.y + blockSize
                        });

                baseTexCoordBuffer.put(new float[]{
                            0.0f, 1.0f, // Base begins
                            1.0f, 1.0f,
                            1.0f, 0.0f,
                            0.0f, 0.0f, // Base ends
                        });
                topTexCoordBuffer.put(new float[]{
                            0.0f, 1.0f, // Base begins
                            1.0f, 1.0f,
                            1.0f, 0.0f,
                            0.0f, 0.0f, // Base ends
                        });

                if (lightingEffects) {
                    baseNormalBuffer.put(new float[]{
                                -0.7071f, 0.0f, 0.7071f,
                                0.7071f, 0.0f, 0.7071f,
                                0.7071f, 0.0f, 0.7071f,
                                -0.7071f, 0.0f, 0.7071f,});

                    topNormalBuffer.put(new float[]{
                                -0.7071f, 0.0f, 0.7071f,
                                0.7071f, 0.0f, 0.7071f,
                                0.7071f, 0.7071f, 0.1f,
                                -0.7071f, 0.7071f, 0.1f,});
                }

                float cannonAngle = -cannonBlock.getAngleInRadians();

                if (!cannonBlock.isShootingToLeft()) {
                    cannonAngle = (float) Math.PI - cannonAngle;
                }

                xf.position = pos;
                xf.R.setAngle(cannonAngle);

                Vec2 leftBottom = XForm.mul(xf, pipeLeftBottom);
                Vec2 rightBottom = XForm.mul(xf, pipeRightBottom);
                Vec2 rightTop = XForm.mul(xf, pipeRightTop);
                Vec2 leftTop = XForm.mul(xf, pipeLeftTop);


                pipeVertexBuffer.put(new float[]{
                            leftBottom.x, leftBottom.y,
                            rightBottom.x, rightBottom.y,
                            rightTop.x, rightTop.y,
                            leftTop.x, leftTop.y
                        });

                pipeTexCoordBuffer.put(new float[]{
                            0.0f, 1.0f,
                            1.0f, 1.0f,
                            1.0f, 0.0f,
                            0.0f, 0.0f
                        });

                if (lightingEffects) {
                    Vec3 nBottom = MathUtil.rotateNormal(xf, new Vec3(0.0f, -0.9f, 0.4f));
                    Vec3 nTop = MathUtil.rotateNormal(xf, new Vec3(0.0f, 0.9f, 0.4f));

                    pipeNormalBuffer.put(new float[]{
                                nBottom.x, nBottom.y, nBottom.z,
                                nBottom.x, nBottom.y, nBottom.z,
                                nTop.x, nTop.y, nTop.z,
                                nTop.x, nTop.y, nTop.z,});
                }
            }

        }

        baseVertexBuffer.rewind();
        topVertexBuffer.rewind();
        pipeVertexBuffer.rewind();

        baseTexCoordBuffer.rewind();
        topTexCoordBuffer.rewind();
        pipeTexCoordBuffer.rewind();

        if (lightingEffects) {
            baseNormalBuffer.rewind();
            topNormalBuffer.rewind();
            pipeNormalBuffer.rewind();

            gl.glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, color, 0);
            gl.glMaterialfv(GL_FRONT, GL_SPECULAR, specular, 0);
        } else {
            gl.glColor4fv(color, 0);
        }

        gl.glVertexPointer(2, GL_FLOAT, 0, pipeVertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, pipeTexCoordBuffer);
        if (lightingEffects) {
            gl.glNormalPointer(GL_FLOAT, 0, pipeNormalBuffer);
        }
        pipeTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);

        gl.glVertexPointer(2, GL_FLOAT, 0, baseVertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, baseTexCoordBuffer);
        if (lightingEffects) {
            gl.glNormalPointer(GL_FLOAT, 0, baseNormalBuffer);
        }
        baseTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);

        gl.glVertexPointer(2, GL_FLOAT, 0, topVertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, topTexCoordBuffer);
        if (lightingEffects) {
            gl.glNormalPointer(GL_FLOAT, 0, topNormalBuffer);
        }
        topTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);
    }
}
