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
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.collision.Shape;
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
    private FloatBuffer baseSecondaryColorBuffer;
    private FloatBuffer topSecondaryColorBuffer;
    private FloatBuffer pipeSecondaryColorBuffer;
    private final Vec2 pipeLeftBottom;
    private final Vec2 pipeRightBottom;
    private final Vec2 pipeRightTop;
    private final Vec2 pipeLeftTop;
    private final int NUM_VERTICES_PER_PART = 4;
    private final boolean lightingEffects;
    private final float[] color = {0.9f, 0.9f, 0.9f, 1.0f};
    //private final float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};

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

        baseSecondaryColorBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 4);
        topSecondaryColorBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 4);
        pipeSecondaryColorBuffer = BufferUtil.newFloatBuffer(2 * NUM_VERTICES_PER_PART * 4);

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
            baseSecondaryColorBuffer = BufferUtil.newFloatBuffer(numCoords * 4);
            topSecondaryColorBuffer = BufferUtil.newFloatBuffer(numCoords * 4);
            pipeSecondaryColorBuffer = BufferUtil.newFloatBuffer(numCoords * 4);


        }

        float blockSize = gameModel.getBlockSize();
        XForm xf = new XForm();

        for (Player player : gameModel.getPlayers()) {
            for (CannonBlock cannonBlock : player.getCannons()) {
                Vec2 pos = cannonBlock.getBody().getPosition();
                PolygonShape ps = null;
                for (Shape shape = cannonBlock.getBody().getShapeList(); shape != null; shape = shape.getNext()) {
                    if (shape instanceof PolygonShape) {
                        ps = (PolygonShape) shape;
                    }
                }

                if (ps == null) {
                    System.out.println("WARNING: cannon did not contain a polygonShape");
                    continue;
                }
                Vec2[] vertices = ps.getVertices();



                baseVertexBuffer.put(new float[]{
                            pos.x + vertices[0].x, pos.y + vertices[0].y,
                            pos.x + vertices[1].x, pos.y + vertices[1].y,
                            pos.x + vertices[2].x, pos.y + vertices[2].y,
                            pos.x + vertices[3].x, pos.y + vertices[3].y
                        });
                topVertexBuffer.put(new float[]{
                            pos.x + vertices[3].x, pos.y + vertices[3].y,
                            pos.x + vertices[2].x, pos.y + vertices[2].y,
                            pos.x + vertices[2].x, pos.y + vertices[2].y + blockSize,
                            pos.x + vertices[3].x, pos.y + vertices[3].y + blockSize
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

                float[] hilightColor;
                if (cannonBlock.isHilighted()) {
                    if (lightingEffects) {
                        hilightColor = new float[]{0.3f, 0.3f, 0.3f, 1.0f};
                    } else {
                        hilightColor = new float[]{0.3f, 0.3f, 0.3f};
                    }
                } else {
                    if (lightingEffects) {
                        hilightColor = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
                    } else {
                        hilightColor = new float[]{0.0f, 0.0f, 0.0f};
                    }
                }

                pipeSecondaryColorBuffer.put(hilightColor);
                pipeSecondaryColorBuffer.put(hilightColor);
                pipeSecondaryColorBuffer.put(hilightColor);
                pipeSecondaryColorBuffer.put(hilightColor);

                baseSecondaryColorBuffer.put(hilightColor);
                baseSecondaryColorBuffer.put(hilightColor);
                baseSecondaryColorBuffer.put(hilightColor);
                baseSecondaryColorBuffer.put(hilightColor);

                topSecondaryColorBuffer.put(hilightColor);
                topSecondaryColorBuffer.put(hilightColor);
                topSecondaryColorBuffer.put(hilightColor);
                topSecondaryColorBuffer.put(hilightColor);

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
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        if (lightingEffects) {
            gl.glEnable(GL_LIGHTING);
            gl.glEnableClientState(GL_NORMAL_ARRAY);
        }

        baseVertexBuffer.rewind();
        topVertexBuffer.rewind();
        pipeVertexBuffer.rewind();

        baseTexCoordBuffer.rewind();
        topTexCoordBuffer.rewind();
        pipeTexCoordBuffer.rewind();
        pipeSecondaryColorBuffer.rewind();
        baseSecondaryColorBuffer.rewind();
        topSecondaryColorBuffer.rewind();

        if (lightingEffects) {
            baseNormalBuffer.rewind();
            topNormalBuffer.rewind();
            pipeNormalBuffer.rewind();

            gl.glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, color, 0);
            gl.glColorMaterial(GL_FRONT, GL_SPECULAR);
            gl.glEnable(GL_COLOR_MATERIAL);
            gl.glEnableClientState(GL_COLOR_ARRAY);
        } else {
            gl.glColor4fv(color, 0);
            gl.glEnableClientState(GL_SECONDARY_COLOR_ARRAY);
        }

        gl.glVertexPointer(2, GL_FLOAT, 0, pipeVertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, pipeTexCoordBuffer);

        if (lightingEffects) {
            gl.glNormalPointer(GL_FLOAT, 0, pipeNormalBuffer);
            gl.glColorPointer(4, GL_FLOAT, 0, pipeSecondaryColorBuffer);
        } else {
            gl.glSecondaryColorPointer(3, GL_FLOAT, 0, pipeSecondaryColorBuffer);
        }
        pipeTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);


        gl.glVertexPointer(2, GL_FLOAT, 0, baseVertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, baseTexCoordBuffer);
        if (lightingEffects) {
            gl.glNormalPointer(GL_FLOAT, 0, baseNormalBuffer);
            gl.glColorPointer(4, GL_FLOAT, 0, baseSecondaryColorBuffer);
        } else {
            gl.glSecondaryColorPointer(3, GL_FLOAT, 0, baseSecondaryColorBuffer);
        }
        baseTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);

        gl.glVertexPointer(2, GL_FLOAT, 0, topVertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, topTexCoordBuffer);
        if (lightingEffects) {
            gl.glNormalPointer(GL_FLOAT, 0, topNormalBuffer);
            gl.glColorPointer(4, GL_FLOAT, 0, topSecondaryColorBuffer);
        } else {
            gl.glSecondaryColorPointer(3, GL_FLOAT, 0, topSecondaryColorBuffer);
        }
        topTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);

        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        // Draw everything again if lighting is enabled


        if (lightingEffects) {
            gl.glDisable(GL_LIGHTING);
            gl.glDisableClientState(GL_NORMAL_ARRAY);
            gl.glDisable(GL_COLOR_MATERIAL);
        } else {
            gl.glDisableClientState(GL_SECONDARY_COLOR_ARRAY);
            gl.glSecondaryColor3fv(new float[]{0, 0, 0}, 0);
        }

    }
}
