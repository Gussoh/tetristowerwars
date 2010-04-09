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
import org.tetristowerwars.model.CannonBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
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
    private FloatBuffer baseTexCoordBuffer;
    private FloatBuffer topTexCoordBuffer;
    private final int NUM_VERTICES_PER_PART = 4;

    public CannonRenderer(GL gl) throws IOException {
        baseTexture = TextureIO.newTexture(new File("res/gfx/cannon_base.png"), true);
        topTexture = TextureIO.newTexture(new File("res/gfx/cannon_top.png"), true);
        pipeTexture = TextureIO.newTexture(new File("res/gfx/cannon_pipe.png"), true);

        GLUtil.fixTextureParameters(baseTexture);
        GLUtil.fixTextureParameters(topTexture);
        GLUtil.fixTextureParameters(pipeTexture);

        baseVertexBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_PART * 2);
        baseTexCoordBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_PART * 2);
        topVertexBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_PART * 2);
        topTexCoordBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_PART * 2);
    }

    public void render(GL gl, GameModel gameModel) {
        int numCannons = 0;

        for (Player player : gameModel.getPlayers()) {
            numCannons += player.getCannons().size();
        }

        int numCoords = numCannons * NUM_VERTICES_PER_PART;

        if (numCoords * 2 > baseVertexBuffer.capacity()) {
            baseVertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            baseTexCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            topVertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            topTexCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
        }

        float blockSize = gameModel.getBlockSize();

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
            }
        }

        baseVertexBuffer.rewind();
        baseTexCoordBuffer.rewind();
        topVertexBuffer.rewind();
        topTexCoordBuffer.rewind();

        gl.glVertexPointer(2, GL_FLOAT, 0, baseVertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, baseTexCoordBuffer);
        baseTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);

        gl.glVertexPointer(2, GL_FLOAT, 0, topVertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, topTexCoordBuffer);
        topTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);

    }
}
