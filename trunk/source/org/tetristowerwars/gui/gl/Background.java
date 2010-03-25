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
import static javax.media.opengl.GL.*;


/**
 *
 * @author Andreas
 */
public class Background {

    private final Texture skyTexture;
    private final Texture cityTexture;
    private final Texture groundTexture;
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer texCoordBuffer;

    public Background(GL gl, float renderWorldWidth, float renderWorldHeight) throws IOException {
        skyTexture = TextureIO.newTexture(new File("res/gfx/sky.png"), true);
        cityTexture = TextureIO.newTexture(new File("res/gfx/citysilhuette.png"), true);
        groundTexture = TextureIO.newTexture(new File("res/gfx/ground.png"), true);

        

        vertexBuffer = BufferUtil.newFloatBuffer(3 * 4 * 2); // 3 quads, 4 vertices each, 2 floats per vertex
        texCoordBuffer = BufferUtil.newFloatBuffer(3 * 4 * 2);

        float horizonHeight = renderWorldHeight * 0.2f;

        float cityRepeats = 4.0f * renderWorldWidth / cityTexture.getWidth();

        cityTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);

        // remember counter-clockwise order
        // First the sky

        vertexBuffer.put(new float[]{0.0f, horizonHeight}); // left-bottom
        texCoordBuffer.put(new float[]{0.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, horizonHeight}); // right-bottom
        texCoordBuffer.put(new float[]{1.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, renderWorldHeight}); // right-top
        texCoordBuffer.put(new float[]{1.0f, 0.0f});
        
        vertexBuffer.put(new float[]{0.0f, renderWorldHeight}); // left-top
        texCoordBuffer.put(new float[]{0.0f, 0.0f});

        // City

        vertexBuffer.put(new float[]{0.0f, horizonHeight}); // left-bottom
        texCoordBuffer.put(new float[]{0.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, horizonHeight}); // right-bottom
        texCoordBuffer.put(new float[]{cityRepeats, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, horizonHeight + cityTexture.getHeight() / 4}); // right-top
        texCoordBuffer.put(new float[]{cityRepeats, 0.0f});

        vertexBuffer.put(new float[]{0.0f, horizonHeight + cityTexture.getHeight() / 4}); // left-top
        texCoordBuffer.put(new float[]{0.0f, 0.0f});


        // Ground
        vertexBuffer.put(new float[]{0.0f, 0.0f}); // left-bottom
        texCoordBuffer.put(new float[]{0.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, 0.0f}); // right-bottom
        texCoordBuffer.put(new float[]{1.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, horizonHeight}); // right-top
        texCoordBuffer.put(new float[]{1.0f, 0.0f});

        vertexBuffer.put(new float[]{0.0f, horizonHeight}); // left-top
        texCoordBuffer.put(new float[]{0.0f, 0.0f});

        // jogl seems to like rewinded buffers.
        vertexBuffer.rewind();
        texCoordBuffer.rewind();
    }


    public void render(GL gl) {

        gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);

        gl.glVertexPointer(2, GL.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, texCoordBuffer);

        skyTexture.bind();
        gl.glDrawArrays(GL.GL_QUADS, 0, 4);

        //gl.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glEnable(GL.GL_BLEND);
        cityTexture.bind();
        gl.glDrawArrays(GL.GL_QUADS, 4, 4);
        gl.glDisable(GL.GL_BLEND);

        groundTexture.bind();
        gl.glDrawArrays(GL.GL_QUADS, 8, 4);

        gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);

    }
}
