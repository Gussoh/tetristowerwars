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
 * @author Administrator
 */
public class ComboBackgroundRender {

    private final Texture skyLeftTexture;
    private final Texture skyRightTexture;
    private final Texture cityLeftTexture;
    private final Texture cityRightTexture;
    private final Texture fieldLeftTexture;
    private final Texture fieldRightTexture;
    private final Texture groundTexture;
    private final Texture bottomLeftTexture;
    private final Texture bottomRightTexture;
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer texCoordBuffer;
    private final FloatBuffer color;
    private static final float cityShrinkFactor = 10.0f;
    private static final float bottomShrinkFactor = 10.0f;
    private static final float groundShrinkFactor = 10.0f;

    public ComboBackgroundRender(GL gl, float renderWorldWidth, float renderWorldHeight, float groundLevel, float horizontLevel, int leftThemeIndex, int rightThemeIndex) throws IOException {
        String leftTexturePath = "../../res/gfx/THEME" + leftThemeIndex + "/";
        String rightTexturePath = "../../res/gfx/THEME" + rightThemeIndex + "/";
        skyLeftTexture = TextureIO.newTexture(new File(leftTexturePath + "sky.png"), true);
        skyRightTexture = TextureIO.newTexture(new File(rightTexturePath + "sky.png"), true);
        cityLeftTexture = TextureIO.newTexture(new File(leftTexturePath + "citysilhuette.png"), true);
        cityRightTexture = TextureIO.newTexture(new File(rightTexturePath + "citysilhuette.png"), true);

        fieldLeftTexture = null;
        fieldRightTexture = null;

        groundTexture = TextureIO.newTexture(new File(leftTexturePath + "ground.png"), true);
        bottomLeftTexture = TextureIO.newTexture(new File(leftTexturePath + "bottom.png"), true);
        bottomRightTexture = TextureIO.newTexture(new File(rightTexturePath + "bottom.png"), true);



        GLUtil.fixTextureParameters(skyLeftTexture);
        GLUtil.fixTextureParameters(skyRightTexture);
        GLUtil.fixTextureParameters(cityLeftTexture);
        GLUtil.fixTextureParameters(cityRightTexture);
        GLUtil.fixTextureParameters(groundTexture);
        GLUtil.fixTextureParameters(bottomLeftTexture);
        GLUtil.fixTextureParameters(bottomRightTexture);

        color = BufferUtil.newFloatBuffer(4);
        color.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        color.rewind();

        vertexBuffer = BufferUtil.newFloatBuffer(8 * 4 * 2); // 8 quads, 4 vertices each, 2 floats per vertex
        texCoordBuffer = BufferUtil.newFloatBuffer(8 * 4 * 2);

        float halfRenderWidth = renderWorldWidth * 0.5f;

        float cityLeftRepeats = cityShrinkFactor * halfRenderWidth / cityLeftTexture.getWidth();
        float cityRightRepeats = cityShrinkFactor * halfRenderWidth / cityRightTexture.getWidth();
        float bottomLeftRepeats = bottomShrinkFactor * halfRenderWidth / bottomLeftTexture.getWidth();
        float bottomRightRepeats = bottomShrinkFactor * halfRenderWidth / bottomRightTexture.getWidth();
        float groundRepeats = groundShrinkFactor * renderWorldWidth / groundTexture.getWidth();

        cityLeftTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        cityRightTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        bottomLeftTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_MIRRORED_REPEAT);
        bottomRightTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_MIRRORED_REPEAT);

        // 1.Left sky

        vertexBuffer.put(new float[]{
                    0.0f, horizontLevel,
                    halfRenderWidth, horizontLevel,
                    halfRenderWidth, renderWorldHeight,
                    0.0f, renderWorldHeight
                });

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 0.0f
                });

        // 2. Right sky
        vertexBuffer.put(new float[]{
                    halfRenderWidth, horizontLevel,
                    renderWorldWidth, horizontLevel,
                    renderWorldWidth, renderWorldHeight,
                    halfRenderWidth, renderWorldHeight
                });

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 0.0f
                });

        // 3. Left City
        vertexBuffer.put(new float[]{
                    0.0f, horizontLevel,
                    halfRenderWidth, horizontLevel,
                    halfRenderWidth, horizontLevel + cityLeftTexture.getHeight() / cityShrinkFactor,
                    0.0f, horizontLevel + cityLeftTexture.getHeight() / cityShrinkFactor
                });

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    cityLeftRepeats, 1.0f,
                    cityLeftRepeats, 0.0f,
                    0.0f, 0.0f
                });

        // 4. Right City
        vertexBuffer.put(new float[]{
                    halfRenderWidth, horizontLevel,
                    renderWorldWidth, horizontLevel,
                    renderWorldWidth, horizontLevel + cityRightTexture.getHeight() / cityShrinkFactor,
                    halfRenderWidth, horizontLevel + cityRightTexture.getHeight() / cityShrinkFactor,});

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    cityRightRepeats, 1.0f,
                    cityRightRepeats, 0.0f,
                    0.0f, 0.0f
                });

        // 5. Left field
        vertexBuffer.put(new float[]{
                    0.0f, 0.0f,
                    halfRenderWidth, 0.0f,
                    halfRenderWidth, horizontLevel,
                    0.0f, horizontLevel
                });

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 0.0f
                });

        // 6. Right field
        vertexBuffer.put(new float[]{
                    halfRenderWidth, 0.0f,
                    renderWorldWidth, 0.0f,
                    renderWorldWidth, horizontLevel,
                    halfRenderWidth, horizontLevel
                });

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 0.0f
                });


        // 7. Left Bottom
        vertexBuffer.put(new float[]{
                    0.0f, 0.0f,
                    halfRenderWidth, 0.0f,
                    halfRenderWidth, groundLevel,
                    0.0f, groundLevel
                });

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    bottomLeftRepeats, 1.0f,
                    bottomLeftRepeats, 0.0f,
                    0.0f, 0.0f
                });


        // 8. Right Bottom
        vertexBuffer.put(new float[]{
                    halfRenderWidth, 0.0f,
                    renderWorldWidth, 0.0f,
                    renderWorldWidth, groundLevel,
                    halfRenderWidth, groundLevel
                });

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    bottomRightRepeats, 1.0f,
                    bottomRightRepeats, 0.0f,
                    0.0f, 0.0f
                });

        // 9. Ground
        vertexBuffer.put(new float[]{
                    0.0f, groundLevel - groundTexture.getHeight() / groundShrinkFactor,
                    renderWorldWidth, groundLevel - groundTexture.getHeight() / groundShrinkFactor,
                    renderWorldWidth, groundLevel,
                    0.0f, groundLevel
                });

        texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    groundRepeats, 1.0f,
                    groundRepeats, 0.0f,
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

        skyLeftTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, 4);

        skyRightTexture.bind();
        gl.glDrawArrays(GL_QUADS, 4, 4);

        cityLeftTexture.bind();
        gl.glDrawArrays(GL_QUADS, 8, 4);

        cityRightTexture.bind();
        gl.glDrawArrays(GL_QUADS, 12, 4);

        fieldLeftTexture.bind();
        gl.glDrawArrays(GL_QUADS, 16, 4);

        fieldRightTexture.bind();
        gl.glDrawArrays(GL_QUADS, 20, 4);

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

        bottomLeftTexture.bind();
        gl.glDrawArrays(GL_QUADS, 24, 4);

        bottomRightTexture.bind();
        gl.glDrawArrays(GL_QUADS, 28, 4);

        groundTexture.bind();
        gl.glDrawArrays(GL_QUADS, 32, 4);


        gl.glEnable(GL_BLEND);
        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }
}
