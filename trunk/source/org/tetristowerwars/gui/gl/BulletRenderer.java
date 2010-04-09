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
import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.BulletBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class BulletRenderer {

    private final Texture texture;
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private final int NUM_VERTICES_PER_BULLET = 4;

    public BulletRenderer(GL gl) throws IOException {
        texture = TextureIO.newTexture(new File("res/gfx/bullet.png"), true);
        vertexBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_BULLET * 2 * 16);
        texCoordBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_BULLET * 2 * 16);

        GLUtil.fixTextureParameters(texture);
    }

    public void render(GL gl, GameModel gameModel) {
        int numBullets = 0;

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        for (Player player : gameModel.getPlayers()) {
            numBullets += player.getBullets().size();
        }
        int numCoords = numBullets * NUM_VERTICES_PER_BULLET;
        if (numCoords * 2 > vertexBuffer.capacity()) {
            vertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            texCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
        } 

        for (Player player : gameModel.getPlayers()) {
            for (BulletBlock bulletBlock : player.getBullets()) {
                Vec2 pos = bulletBlock.getBody().getPosition();
                Shape shape = (CircleShape) bulletBlock.getBody().getShapeList();
                if (shape instanceof CircleShape) {
                    float radius = ((CircleShape) shape).getRadius();
                    vertexBuffer.put(new float[]{
                                pos.x - radius, pos.y - radius,
                                pos.x + radius, pos.y - radius,
                                pos.x + radius, pos.y + radius,
                                pos.x - radius, pos.y + radius
                    });

                    texCoordBuffer.put(new float[]{
                                0.0f, 0.0f,
                                1.0f, 0.0f,
                                1.0f, 1.0f,
                                0.0f, 1.0f
                    });
                }
            }
        }

        vertexBuffer.rewind();
        texCoordBuffer.rewind();

        texture.bind();
        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);

        gl.glDrawArrays(GL_QUADS, 0, numBullets * NUM_VERTICES_PER_BULLET);
    }
}
