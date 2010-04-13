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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
    private final Map<Integer, List<Animation>> animations = new LinkedHashMap<Integer, List<Animation>>();
    
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    public final static int TANK1 = 0, TANK2 = 1, SCUD = 2, ZEPPELIN1 = 3, ZEPPELIN2 = 4, SPUTNIK = 5;
    public final static int NUM_VERTICES_PER_ANIMATION = 4;

    public BackgroundAnimationRenderer(GL gl) throws IOException {
        textures.put(TANK1, new TextureEntry(TextureIO.newTexture(new File("res/gfx/decoration/tank1.png"), true), true));
        textures.put(TANK2, new TextureEntry(TextureIO.newTexture(new File("res/gfx/decoration/tank2.png"), true), true));
        textures.put(SCUD, new TextureEntry(TextureIO.newTexture(new File("res/gfx/decoration/scud-grey.png"), true), false));
        textures.put(ZEPPELIN1, new TextureEntry(TextureIO.newTexture(new File("res/gfx/decoration/zeppelin1.png"), true), false));
        textures.put(ZEPPELIN2, new TextureEntry(TextureIO.newTexture(new File("res/gfx/decoration/zeppelin2.png"), true), false));
        textures.put(SPUTNIK, new TextureEntry(TextureIO.newTexture(new File("res/gfx/decoration/sputnik.png"), true), true));
        
        for (TextureEntry entry : textures.values()) {
            GLUtil.fixTextureParameters(entry.texture);
        }

        vertexBuffer = BufferUtil.newFloatBuffer(10 * NUM_VERTICES_PER_ANIMATION * 2);
        texCoordBuffer = BufferUtil.newFloatBuffer(10 * NUM_VERTICES_PER_ANIMATION * 2);
    }

    public void render(GL gl, float timeElapsed) {

        int numAnimations = 0;
        for (List<Animation> list : animations.values()) {
            processAnimationIterator(list, timeElapsed);
            numAnimations += list.size();
        }


        int numCoords = numAnimations * NUM_VERTICES_PER_ANIMATION;

        if (numCoords * 2 > vertexBuffer.capacity()) {
            vertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            texCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
        }

        for (Map.Entry<Integer, List<Animation>> entry : animations.entrySet()) {
            constructBuffers(entry.getValue(), textures.get(entry.getKey()));
        }

        vertexBuffer.rewind();
        texCoordBuffer.rewind();

        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        // Draw background
        // This blend function is needed for photoshop-like blend.
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);

        int startPos = 0;

        for (Map.Entry<Integer, List<Animation>> entry : animations.entrySet()) {
            int image = entry.getKey();
            List<Animation> list = entry.getValue();
            
            Texture texture = textures.get(image).texture;
            texture.bind();
            gl.glDrawArrays(GL_QUADS, startPos, list.size() * NUM_VERTICES_PER_ANIMATION);
            startPos += list.size() * NUM_VERTICES_PER_ANIMATION;
        }

        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    private void constructBuffers(List<Animation> animations, TextureEntry textureEntry) {
        for (Animation animation : animations) {

            Vec2 pos = animation.path.getCurrentPosition();
            float halfWidth = animation.width * 0.5f;
            float halfHeight = halfWidth / textureEntry.texture.getAspectRatio();
            boolean hMirror = !(animation.path.isLeftToright() ^ textureEntry.isGoingRight);

            vertexBuffer.put(new float[]{
                        pos.x - halfWidth, pos.y - halfHeight,
                        pos.x + halfWidth, pos.y - halfHeight,
                        pos.x + halfWidth, pos.y + halfHeight,
                        pos.x - halfWidth, pos.y + halfHeight
                    });
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

        List<Animation> list = animations.get(image);

        if (list == null) {
            list = new LinkedList<Animation>();
            animations.put(image, list);
        }

        list.add(new Animation(path, width));
        
    }

    private void processAnimationIterator(List<Animation> list, float timeElapsed) {
        for (Iterator<Animation> it = list.iterator(); it.hasNext();) {
            Path path = it.next().path;
            path.addTime(timeElapsed);
            if (path.isDone()) {
                it.remove();
            }
        }
    }

    private class Animation {

        private final Path path;
        private final float width;

        public Animation(Path path, float width) {
            this.path = path;
            this.width = width;
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
