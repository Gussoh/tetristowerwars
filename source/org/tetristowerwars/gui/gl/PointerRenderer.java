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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.gl.animation.Path;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class PointerRenderer {

    private final Texture texture;
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private FloatBuffer colorBuffer;
    private static final int NUM_VERTICES_PER_POINTER = 4;
    private static final float POINTER_RADIUS = 3.0f;
    private Map<Integer, Path> intensityPaths = new LinkedHashMap<Integer, Path>();

    public PointerRenderer(GL gl) throws IOException {
        texture = TextureIO.newTexture(new File("res/gfx/marker.png"), true);
        GLUtil.fixTextureParameters(texture);

        vertexBuffer = BufferUtil.newFloatBuffer(4 * NUM_VERTICES_PER_POINTER * 2);
        texCoordBuffer = BufferUtil.newFloatBuffer(4 * NUM_VERTICES_PER_POINTER * 2);
        colorBuffer = BufferUtil.newFloatBuffer(4 * NUM_VERTICES_PER_POINTER * 4);
    }

    public void render(GL gl, Map<Integer, Vec2> pointers, float elapsedTime) {

        int numCoords = pointers.size() * NUM_VERTICES_PER_POINTER;


        if (numCoords * 2 > vertexBuffer.capacity()) {
            vertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            texCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            colorBuffer = BufferUtil.newFloatBuffer(numCoords * 4);
        }

        for (Iterator<Map.Entry<Integer, Path>> it = intensityPaths.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, Path> entry = it.next();
            if (!pointers.containsKey(entry.getKey())) {
                it.remove();
            }
        }

        for (Map.Entry<Integer, Vec2> entry : pointers.entrySet()) {
            Vec2 v = entry.getValue();

            Path p = intensityPaths.get(entry.getKey());

            if (p == null) {
                p = new Path(new Vec2(1.0f, 1.0f), new Vec2(0.5f, 0.0f), 300.0f);
                intensityPaths.put(entry.getKey(), p);
            } else {
                p.addTime(elapsedTime);
            }

            float alpha = p.getCurrentPosition().x;
            float r = POINTER_RADIUS + p.getCurrentPosition().y;



            vertexBuffer.put(new float[]{
                        v.x - r, v.y - r,
                        v.x + r, v.y - r,
                        v.x + r, v.y + r,
                        v.x - r, v.y + r
                    });

            texCoordBuffer.put(new float[]{
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 0.0f
                    });



            colorBuffer.put(new float[]{
                        1.0f, 0.0f, 0.0f, alpha,
                        1.0f, 0.0f, 0.0f, alpha,
                        1.0f, 0.0f, 0.0f, alpha,
                        1.0f, 0.0f, 0.0f, alpha,});
        }




        vertexBuffer.rewind();
        texCoordBuffer.rewind();
        colorBuffer.rewind();

        gl.glEnableClientState(GL_COLOR_ARRAY);

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);
        gl.glColorPointer(4, GL_FLOAT, 0, colorBuffer);

        texture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numCoords);

        gl.glDisableClientState(GL_COLOR_ARRAY);
    }
}
