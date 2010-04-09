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
import java.util.Collection;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class PointerRenderer {

    private final Texture texture;
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private static final int NUM_VERTICES_PER_POINTER = 4;
    private static final float pointerRadius = 3;

    public PointerRenderer(GL gl) throws IOException {
        texture = TextureIO.newTexture(new File("res/gfx/bullet.png"), true);
        GLUtil.fixTextureParameters(texture);

        vertexBuffer = BufferUtil.newFloatBuffer(4 * NUM_VERTICES_PER_POINTER * 2);
        texCoordBuffer = BufferUtil.newFloatBuffer(4 * NUM_VERTICES_PER_POINTER * 2);
    }

    public void render(GL gl, Collection<Vec2> pointers) {

        int numCoords = pointers.size() * NUM_VERTICES_PER_POINTER;


        if (numCoords * 2 > vertexBuffer.capacity()) {
            vertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            texCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
        }

        for (Vec2 vec2 : pointers) {
            vertexBuffer.put(new float[]{
                vec2.x - pointerRadius, vec2.y - pointerRadius,
                vec2.x + pointerRadius, vec2.y - pointerRadius,
                vec2.x + pointerRadius, vec2.y + pointerRadius,
                vec2.x - pointerRadius, vec2.y + pointerRadius
            });

            texCoordBuffer.put(new float[] {
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f
            });
        }

        vertexBuffer.rewind();
        texCoordBuffer.rewind();

        gl.glColor3f(1.0f, 0.0f, 0.0f);

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);
        texture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);
    }
}
