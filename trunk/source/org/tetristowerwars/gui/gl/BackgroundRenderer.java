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
import org.tetristowerwars.Settings;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class BackgroundRenderer {

    private final Texture skyTexture;
    private final Texture cityTexture;
    private final Texture groundTexture;
    private final Texture bottomTexture;
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer texCoordBuffer;
    private final FloatBuffer color;
    private static final float cityShrinkFactor = 10.0f;
    private static final float bottomShrinkFactor = 10.0f;

    public BackgroundRenderer(GL gl, float renderWorldWidth, float renderWorldHeight, float groundLevel, float horizontLevel, int themeIndex) throws IOException {
        String texturePath = "res/gfx/THEME" + themeIndex + "/";
        skyTexture = TextureIO.newTexture(new File(texturePath + "sky.png"), true);
        cityTexture = TextureIO.newTexture(new File(texturePath + "citysilhuette.png"), true);
        groundTexture = TextureIO.newTexture(new File(texturePath + "ground.png"), true);
        bottomTexture = TextureIO.newTexture(new File(texturePath + "bottom.png"), true);

        GLUtil.fixTextureParameters(skyTexture);
        GLUtil.fixTextureParameters(cityTexture);
        GLUtil.fixTextureParameters(groundTexture);
        GLUtil.fixTextureParameters(bottomTexture);

        color = BufferUtil.newFloatBuffer(4);
        color.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        color.rewind();

        vertexBuffer = BufferUtil.newFloatBuffer(4 * 4 * 2); // 4 quads, 4 vertices each, 2 floats per vertex
        texCoordBuffer = BufferUtil.newFloatBuffer(4 * 4 * 2);

        float cityRepeats = cityShrinkFactor * renderWorldWidth / cityTexture.getWidth();
        float bottomRepeats = bottomShrinkFactor * renderWorldWidth / bottomTexture.getWidth();

        cityTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        bottomTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);

        // remember counter-clockwise order
        // First the sky

        vertexBuffer.put(new float[]{0.0f, horizontLevel}); // left-bottom
        texCoordBuffer.put(new float[]{0.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, horizontLevel}); // right-bottom
        texCoordBuffer.put(new float[]{1.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, renderWorldHeight}); // right-top
        texCoordBuffer.put(new float[]{1.0f, 0.0f});

        vertexBuffer.put(new float[]{0.0f, renderWorldHeight}); // left-top
        texCoordBuffer.put(new float[]{0.0f, 0.0f});

        // City

        vertexBuffer.put(new float[]{0.0f, horizontLevel}); // left-bottom
        texCoordBuffer.put(new float[]{0.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, horizontLevel}); // right-bottom
        texCoordBuffer.put(new float[]{cityRepeats, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, horizontLevel + cityTexture.getHeight() / cityShrinkFactor}); // right-top
        texCoordBuffer.put(new float[]{cityRepeats, 0.0f});

        vertexBuffer.put(new float[]{0.0f, horizontLevel + cityTexture.getHeight() / cityShrinkFactor}); // left-top
        texCoordBuffer.put(new float[]{0.0f, 0.0f});


        // Ground
        vertexBuffer.put(new float[]{0.0f, 0.0f}); // left-bottom
        texCoordBuffer.put(new float[]{0.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, 0.0f}); // right-bottom
        texCoordBuffer.put(new float[]{1.0f, 1.0f});

        vertexBuffer.put(new float[]{renderWorldWidth, horizontLevel}); // right-top
        texCoordBuffer.put(new float[]{1.0f, 0.0f});

        vertexBuffer.put(new float[]{0.0f, horizontLevel}); // left-top
        texCoordBuffer.put(new float[]{0.0f, 0.0f});


        // Bottom / ground to stand on
        vertexBuffer.put(new float[]{
                    0, 0,
                    renderWorldWidth, 0,
                    renderWorldWidth, groundLevel,
                    0, groundLevel
                });

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    bottomRepeats, 1.0f,
                    bottomRepeats, 0.0f,
                    0.0f, 0.0f
                });

        vertexBuffer.rewind();
        texCoordBuffer.rewind();
    }

    public void render(GL gl) {

        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // Draw background
        // This blend function is needed for photoshop-like blend.
        gl.glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);


        gl.glColor4fv(color);

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);

        skyTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, 4);

        cityTexture.bind();
        gl.glDrawArrays(GL_QUADS, 4, 4);

        groundTexture.bind();
        gl.glDrawArrays(GL_QUADS, 8, 4);

        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void renderBottom(GL gl) {

        gl.glColor4fv(color);
        gl.glDisable(GL_BLEND);
        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);

        bottomTexture.bind();
        gl.glDrawArrays(GL_QUADS, 12, 4);

        gl.glEnable(GL_BLEND);
        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }
}
