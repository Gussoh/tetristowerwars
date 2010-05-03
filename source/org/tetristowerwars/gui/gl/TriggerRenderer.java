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
import java.util.Set;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.*;
import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.TriggerBlock;

/**
 *
 * @author Administrator
 */
public class TriggerRenderer {

    private final Texture roundTexture;
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private final static int NUM_VERTICES_PER_OBJECT = 4;

    public TriggerRenderer(GL gl) throws IOException {
        roundTexture = TextureIO.newTexture(new File("res/gfx/exit_button.png"), true);
        GLUtil.fixTextureParameters(roundTexture);

        vertexBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_OBJECT * 2);
        texCoordBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_OBJECT * 2);
    }

    public void render(GL gl, GameModel gameModel) {
        Set<TriggerBlock> triggerBlocks = gameModel.getTriggerBlocks();

        int numVertices = triggerBlocks.size() * NUM_VERTICES_PER_OBJECT;

        if (numVertices * 2 > vertexBuffer.capacity()) {
            vertexBuffer = BufferUtil.newFloatBuffer(numVertices * 2);
            texCoordBuffer = BufferUtil.newFloatBuffer(numVertices * 2);
        }

        for (TriggerBlock triggerBlock : triggerBlocks) {
            if (triggerBlock.isVisible()) {
                Vec2 pos = triggerBlock.getBody().getPosition();
                Shape shape = triggerBlock.getBody().getShapeList();

                if (shape instanceof CircleShape) {
                    CircleShape circleShape = (CircleShape) shape;
                    float r = circleShape.getRadius();
                    vertexBuffer.put(new float[]{
                                pos.x - r, pos.y - r,
                                pos.x + r, pos.y - r,
                                pos.x + r, pos.y + r,
                                pos.x - r, pos.y + r
                            });
                } else if (shape instanceof PolygonShape) {
                }

                texCoordBuffer.put(new float[]{
                            0.0f, 1.0f,
                            1.0f, 1.0f,
                            1.0f, 0.0f,
                            0.0f, 0.0f
                        });
            }
        }

        vertexBuffer.rewind();
        texCoordBuffer.rewind();

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        roundTexture.bind();
        gl.glDrawArrays(GL_QUADS, 0, numVertices);

        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL_TEXTURE_2D);

    }
}
