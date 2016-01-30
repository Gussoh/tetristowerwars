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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.gl.animation.Path;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class BackgroundAnimationRenderer {

    private final Map<Integer, TextureEntry> textures = new HashMap<Integer, TextureEntry>();
    private final List<Animation> animations = new LinkedList<Animation>();
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    public final static int GROUNDVEHICLE1 = 0, GROUNDVEHICLE2 = 1, GROUNDVEHICLE3 = 2, GROUNDVEHICLE4 = 3, AIRVEHICLE1 = 4, AIRVEHICLE2 = 5, AIRVEHICLE3 = 6, TRAJECTORYVEHICLE = 7;
    public final static int NUM_VERTICES_PER_ANIMATION = 4;

    public BackgroundAnimationRenderer(GL gl, int themeIndex) throws IOException {
        String texturePath = "res/gfx/THEME" + themeIndex + "/";
        textures.put(GROUNDVEHICLE1, new TextureEntry(TextureIO.newTexture(new File(texturePath + "groundvehicle1.png"), true), true));
        textures.put(GROUNDVEHICLE2, new TextureEntry(TextureIO.newTexture(new File(texturePath + "groundvehicle2.png"), true), true));
        textures.put(GROUNDVEHICLE3, new TextureEntry(TextureIO.newTexture(new File(texturePath + "groundvehicle3.png"), true), true));
        textures.put(GROUNDVEHICLE4, new TextureEntry(TextureIO.newTexture(new File(texturePath + "groundvehicle4.png"), true), true));
        textures.put(AIRVEHICLE1, new TextureEntry(TextureIO.newTexture(new File(texturePath + "airvehicle1.png"), true), true));
        textures.put(AIRVEHICLE2, new TextureEntry(TextureIO.newTexture(new File(texturePath + "airvehicle2.png"), true), true));
        textures.put(AIRVEHICLE3, new TextureEntry(TextureIO.newTexture(new File(texturePath + "airvehicle3.png"), true), true));
        textures.put(TRAJECTORYVEHICLE, new TextureEntry(TextureIO.newTexture(new File(texturePath + "trajectoryvehicle1.png"), true), true));

        for (TextureEntry entry : textures.values()) {
            GLUtil.fixTextureParameters(entry.texture);
        }

        vertexBuffer = BufferUtil.newFloatBuffer(10 * NUM_VERTICES_PER_ANIMATION * 2);
        texCoordBuffer = BufferUtil.newFloatBuffer(10 * NUM_VERTICES_PER_ANIMATION * 2);
    }

    public void render(GL gl, float timeElapsedS) {

        processAnimation(animations, timeElapsedS);
        int numAnimations = animations.size();

        int numCoords = numAnimations * NUM_VERTICES_PER_ANIMATION;

        if (numCoords * 2 > vertexBuffer.capacity()) {
            vertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            texCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
        }

        Collections.sort(animations); // render back-to-front
        constructBuffers(animations);


        vertexBuffer.rewind();
        texCoordBuffer.rewind();

        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);

        int startPos = 0;

        Texture lastTexture = null;
        for (Animation animation : animations) {
            if (lastTexture != animation.textureEntry.texture) {
                lastTexture = animation.textureEntry.texture;
                lastTexture.bind();
            }
            gl.glDrawArrays(GL_QUADS, startPos, NUM_VERTICES_PER_ANIMATION);
            startPos += NUM_VERTICES_PER_ANIMATION;
        }

        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    private void constructBuffers(List<Animation> animations) {
        for (Animation animation : animations) {

            Vec2 pos = animation.path.getCurrentPosition();
            float halfWidth = animation.width * 0.5f;
            float halfHeight = halfWidth / animation.textureEntry.texture.getAspectRatio();
            boolean hMirror = !(animation.path.isLeftToright() ^ animation.textureEntry.isGoingRight);

            vertexBuffer.put(new float[]{
                        pos.x - halfWidth, pos.y - halfHeight,
                        pos.x + halfWidth, pos.y - halfHeight,
                        pos.x + halfWidth, pos.y + halfHeight,
                        pos.x - halfWidth, pos.y + halfHeight,});
            if (hMirror) {
                texCoordBuffer.put(new float[]{
                            0.0f, 1.0f,
                            1.0f, 1.0f,
                            1.0f, 0.0f,
                            0.0f, 0.0f
                        });
            } else {
                texCoordBuffer.put(new float[]{
                            1.0f, 1.0f,
                            0.0f, 1.0f,
                            0.0f, 0.0f,
                            1.0f, 0.0f
                        });
            }
        }
    }

    public void addAnimation(Path path, float width, int image) {
        animations.add(new Animation(path, width, textures.get(image)));
    }

    private void processAnimation(List<Animation> list, float timeElapsed) {
        for (Iterator<Animation> it = list.iterator(); it.hasNext();) {
            Path path = it.next().path;
            path.addTime(timeElapsed);
            if (path.isDone()) {
                it.remove();
            }
        }
    }

    private class Animation implements Comparable<Animation> {

        private final Path path;
        private final float width;
        private final TextureEntry textureEntry;

        public Animation(Path path, float width, TextureEntry textureEntry) {
            this.path = path;
            this.width = width;
            this.textureEntry = textureEntry;
        }

        @Override
        public int compareTo(Animation o) {
            float diff = width - o.width;
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private class TextureEntry {

        private final Texture texture;
        private final boolean isGoingRight;

        public TextureEntry(Texture texture, boolean isGoingRight) {
            this.texture = texture;
            this.isGoingRight = isGoingRight;
        }
    }
}
