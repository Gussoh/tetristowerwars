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
import java.util.Map;
import javax.media.opengl.GL;
import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.BulletBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.material.BrickMaterial;
import org.tetristowerwars.model.material.GhostMaterial;
import org.tetristowerwars.model.material.InvulnerableMaterial;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.RubberMaterial;
import org.tetristowerwars.model.material.SteelMaterial;
import org.tetristowerwars.model.material.WoodMaterial;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class BulletRenderer {

    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private FloatBuffer normalBuffer;
    private final int NUM_VERTICES_PER_BULLET = 4;
    private final boolean lightingEffects;
    private final float[] color = {1.0f, 1.0f, 1.0f, 1.0f};
    private final float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};

    private final Map<Class<? extends Material>, Texture> textures = new HashMap<Class<? extends Material>, Texture>();

    public BulletRenderer(GL gl, boolean lightingEffects) throws IOException {
        String basePath = "res/gfx/bullets/";
        textures.put(WoodMaterial.class, TextureIO.newTexture(new File(basePath + "wood.png"), true));
        textures.put(BrickMaterial.class, TextureIO.newTexture(new File(basePath + "brick.png"), true));
        textures.put(SteelMaterial.class, TextureIO.newTexture(new File(basePath + "steel.png"), true));
        textures.put(RubberMaterial.class, TextureIO.newTexture(new File(basePath + "rubber.png"), true));
        textures.put(GhostMaterial.class, TextureIO.newTexture(new File(basePath + "ghost.png"), true));
        textures.put(InvulnerableMaterial.class, TextureIO.newTexture(new File(basePath + "invulnerable.png"), true));

        for (Texture texture : textures.values()) {
            GLUtil.fixTextureParameters(texture);
        }

        vertexBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_BULLET * 2 * 16);
        texCoordBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_BULLET * 2 * 16);

        if (lightingEffects) {
            normalBuffer = BufferUtil.newFloatBuffer(NUM_VERTICES_PER_BULLET * 3 * 16);
        }

        this.lightingEffects = lightingEffects;
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

            if (lightingEffects) {
                normalBuffer = BufferUtil.newFloatBuffer(numCoords * 3);
            }
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

                    if (lightingEffects) {
                        normalBuffer.put(new float[]{
                                    -1.0f, -1.0f, 0.01f,
                                    1.0f, -1.0f, 0.01f,
                                    1.0f, 1.0f, 0.01f,
                                    -1.0f, 1.0f, 0.01f
                                });
                    }
                }
            }
        }

        vertexBuffer.rewind();
        texCoordBuffer.rewind();

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        if (lightingEffects) {
            gl.glEnableClientState(GL_NORMAL_ARRAY);
            gl.glEnable(GL_LIGHTING);

            normalBuffer.rewind();
            gl.glNormalPointer(GL_FLOAT, 0, normalBuffer);
            gl.glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, color, 0);
            gl.glMaterialfv(GL_FRONT, GL_SPECULAR, specular, 0);
        } else {
            gl.glColor4fv(color, 0);
        }

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);

        int currentPos = 0;
        for (Player player : gameModel.getPlayers()) {
            for (BulletBlock bulletBlock : player.getBullets()) {
                textures.get(bulletBlock.getMaterial().getClass()).bind();
                gl.glDrawArrays(GL_QUADS, currentPos, 4);
                currentPos += 4;
            }
        }

        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL_TEXTURE_2D);

        if (lightingEffects) {
            gl.glDisable(GL_LIGHTING);
            gl.glDisableClientState(GL_NORMAL_ARRAY);
        }
    }
}
