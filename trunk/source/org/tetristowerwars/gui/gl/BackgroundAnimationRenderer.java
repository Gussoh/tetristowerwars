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
import java.util.LinkedList;
import java.util.List;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.gl.animation.Path;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class BackgroundAnimationRenderer {

    private final Texture tank1Texture;
    private final Texture tank2Texture;
    private final Texture zeppelinTexture;
    private final List<Animation> tank1Animations = new LinkedList<Animation>();
    private final List<Animation> tank2Animations = new LinkedList<Animation>();
    private final List<Animation> zeppelinAnimations = new LinkedList<Animation>();
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    public final static int TANK1 = 0, TANK2 = 1, ZEPPELIN = 2;
    public final static int NUM_VERTICES_PER_ANIMATION = 4;

    public BackgroundAnimationRenderer(GL gl) throws IOException {
        tank1Texture = TextureIO.newTexture(new File("res/gfx/decoration/tank1.png"), true);
        tank2Texture = TextureIO.newTexture(new File("res/gfx/decoration/tank2.png"), true);
        zeppelinTexture = TextureIO.newTexture(new File("res/gfx/decoration/zeppelin1.png"), true);

        vertexBuffer = BufferUtil.newFloatBuffer(10 * NUM_VERTICES_PER_ANIMATION * 2);
        texCoordBuffer = BufferUtil.newFloatBuffer(10 * NUM_VERTICES_PER_ANIMATION * 2);
    }

    public void render(GL gl, float timeElapsed) {

        processAnimationIterator(tank1Animations.iterator(), timeElapsed);
        processAnimationIterator(tank2Animations.iterator(), timeElapsed);
        processAnimationIterator(zeppelinAnimations.iterator(), timeElapsed);

        int numAnimation = tank1Animations.size() + tank2Animations.size() + zeppelinAnimations.size();

        int numCoords = numAnimation * NUM_VERTICES_PER_ANIMATION;

        if (numCoords * 2 > vertexBuffer.capacity()) {
            vertexBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
            texCoordBuffer = BufferUtil.newFloatBuffer(numCoords * 2);
        }

        constructBuffers(tank1Animations, 0.5f, true);
        constructBuffers(tank2Animations, 0.5f, true);
        constructBuffers(zeppelinAnimations, 0.5f, false);

        vertexBuffer.rewind();
        texCoordBuffer.rewind();

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);

        int startPos = 0;

        tank1Texture.bind();
        gl.glDrawArrays(GL_QUADS, startPos, tank1Animations.size() * NUM_VERTICES_PER_ANIMATION);
        startPos += tank1Animations.size() * NUM_VERTICES_PER_ANIMATION;

        tank2Texture.bind();
        gl.glDrawArrays(GL_QUADS, startPos, tank2Animations.size() * NUM_VERTICES_PER_ANIMATION);
        startPos += tank2Animations.size() * NUM_VERTICES_PER_ANIMATION;

        zeppelinTexture.bind();
        gl.glDrawArrays(GL_QUADS, startPos, zeppelinAnimations.size() * NUM_VERTICES_PER_ANIMATION);
    }

    private void constructBuffers(List<Animation> animations, float heightRatio, boolean textureIsLeftToRight) {
        for (Animation animation : animations) {

            Vec2 pos = animation.path.getCurrentPosition();
            float halfWidth = animation.width * 0.5f;
            float halfHeight = halfWidth * heightRatio;
            boolean hMirror = !(animation.path.isLeftToright() ^ textureIsLeftToRight);

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
        switch (image) {
            case TANK1:
                tank1Animations.add(new Animation(path, width));
                break;
            case TANK2:
                tank2Animations.add(new Animation(path, width));
                break;
            case ZEPPELIN:
                zeppelinAnimations.add(new Animation(path, width));
                break;
        }
    }

    private void processAnimationIterator(Iterator<Animation> it, float timeElapsed) {
        while (it.hasNext()) {
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
}
