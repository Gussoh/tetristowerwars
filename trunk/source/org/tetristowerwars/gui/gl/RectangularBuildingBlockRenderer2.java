/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.RectangularBuildingBlock;
import org.tetristowerwars.model.material.AluminiumMaterial;
import org.tetristowerwars.model.material.ConcreteMaterial;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;
import org.tetristowerwars.model.material.WoodMaterial;
import org.tetristowerwars.util.MathUtil;
import org.tetristowerwars.util.Vec3;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class RectangularBuildingBlockRenderer2 {

    private final Map<Class<? extends Material>, BufferEntry> bufferEntries = new LinkedHashMap<Class<? extends Material>, BufferEntry>();
    private final int NUM_VERTICES_PER_SQUARE;
    private final float flatSquareRatio = 0.8f;
    private final boolean lightingEffects;
    private FloatBuffer lineVertexBuffer;
    private int numLines;
    private final float[] outlineColor = new float[]{0.0f, 0.0f, 0.0f, 1.0f};

    public RectangularBuildingBlockRenderer2(GL gl, boolean lightingEffects) throws IOException {
        this.lightingEffects = lightingEffects;

        if (lightingEffects) {
            NUM_VERTICES_PER_SQUARE = 20;
        } else {
            NUM_VERTICES_PER_SQUARE = 4;
        }

        createBufferEntry(WoodMaterial.class, "res/gfx/singleblocks/tree01.png", new float[] {1.0f, 1.0f, 1.0f, 1.0f}, 0.0f);
        createBufferEntry(ConcreteMaterial.class, "res/gfx/singleblocks/patch_green.png", new float[] {1.0f, 1.0f, 1.0f, 1.0f}, 0.0f);
        createBufferEntry(AluminiumMaterial.class, "res/gfx/singleblocks/mosaic_orange.png", new float[] {1.0f, 1.0f, 1.0f, 1.0f}, 0.0f);
        createBufferEntry(SteelMaterial.class, "res/gfx/singleblocks/patch_blue.png", new float[] {1.0f, 1.0f, 1.0f, 1.0f}, 0.0f);

        lineVertexBuffer = BufferUtil.newFloatBuffer(100);
    }

    private void createBufferEntry(Class<? extends Material> materialClass, String textureFile, float[] materialColor, float shinyFactor) throws IOException {
        Texture texture = TextureIO.newTexture(new File(textureFile), true);
        bufferEntries.put(materialClass, new BufferEntry(texture, materialColor, shinyFactor));
        GLUtil.fixTextureParameters(texture);
    }

    private void ensureBufferCapacity(GameModel gameModel) {
        // Ensure buffer capacities
        for (BufferEntry bufferEntry : bufferEntries.values()) {
            bufferEntry.numSquares = 0;
        }

        numLines = 0;
        countSquaresAndLines(gameModel.getBuildingBlockPool());

        for (Player player : gameModel.getPlayers()) {
            countSquaresAndLines(player.getBuildingBlocks());
        }

        for (BufferEntry bufferEntry : bufferEntries.values()) {

            int numVertices = bufferEntry.numSquares * NUM_VERTICES_PER_SQUARE;

            if (numVertices * 2 > bufferEntry.vertexBuffer.capacity()) {
                bufferEntry.vertexBuffer = BufferUtil.newFloatBuffer(numVertices * 2);
                bufferEntry.texCoordBuffer = BufferUtil.newFloatBuffer(numVertices * 2);
                if (lightingEffects) {
                    bufferEntry.normalBuffer = BufferUtil.newFloatBuffer(numVertices * 3);
                }
            }
        }


        int numVertices = numLines * 2;

        if (numVertices * 2 > lineVertexBuffer.capacity()) {
            lineVertexBuffer = BufferUtil.newFloatBuffer(numVertices * 2);
        }
    }

    private void countSquaresAndLines(Set<BuildingBlock> buildingBlocks) {

        for (BuildingBlock buildingBlock : buildingBlocks) {
            if (buildingBlock instanceof RectangularBuildingBlock) {
                RectangularBuildingBlock rbb = (RectangularBuildingBlock) buildingBlock;
                bufferEntries.get(buildingBlock.getMaterial().getClass()).numSquares += rbb.getRectangles().length;
                numLines += rbb.getOutline().length;
            }
        }
    }

    private void putDataIntoBuffers(BufferEntry bufferEntry, Vec2 bottomLeft, Vec2 bottomRight, Vec2 topRight, Vec2 topLeft, Vec3 normal) {
        bufferEntry.vertexBuffer.put(new float[]{
                    bottomLeft.x, bottomLeft.y,
                    bottomRight.x, bottomRight.y,
                    topRight.x, topRight.y,
                    topLeft.x, topLeft.y
                });

        bufferEntry.texCoordBuffer.put(new float[]{
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 0.0f
                });

        if (normal != null) {
            bufferEntry.normalBuffer.put(new float[]{
                        normal.x, normal.y, normal.z,
                        normal.x, normal.y, normal.z,
                        normal.x, normal.y, normal.z,
                        normal.x, normal.y, normal.z
                    });
        }
    }

    private void createBufferData(Set<BuildingBlock> buildingBlocks) {
        for (BuildingBlock buildingBlock : buildingBlocks) {
            if (buildingBlock instanceof RectangularBuildingBlock) {
                RectangularBuildingBlock rbb = (RectangularBuildingBlock) buildingBlock;

                BufferEntry bufferEntry = bufferEntries.get(rbb.getMaterial().getClass());
                XForm xForm = rbb.getBody().getXForm();
                Vec2[] outline = rbb.getOutline();

                for (Rectangle2D r : rbb.getRectangles()) {

                    float minX = (float) r.getX();
                    float minY = (float) -(r.getY() + r.getHeight());
                    float maxX = (float) (r.getX() + r.getWidth());
                    float maxY = (float) -r.getY();

                    Vec2 bl = XForm.mul(xForm, new Vec2(minX, minY));
                    Vec2 br = XForm.mul(xForm, new Vec2(maxX, minY));
                    Vec2 tr = XForm.mul(xForm, new Vec2(maxX, maxY));
                    Vec2 tl = XForm.mul(xForm, new Vec2(minX, maxY));

                    if (lightingEffects) {
                        float internalMinX = MathUtil.lerp(1f - flatSquareRatio, minX, maxX);
                        float internalMinY = MathUtil.lerp(1f - flatSquareRatio, minY, maxY);
                        float internalMaxX = MathUtil.lerp(flatSquareRatio, minX, maxX);
                        float internalMaxY = MathUtil.lerp(flatSquareRatio, minY, maxY);


                        // create the Middle
                        Vec2 ibl = XForm.mul(xForm, new Vec2(internalMinX, internalMinY));
                        Vec2 ibr = XForm.mul(xForm, new Vec2(internalMaxX, internalMinY));
                        Vec2 itr = XForm.mul(xForm, new Vec2(internalMaxX, internalMaxY));
                        Vec2 itl = XForm.mul(xForm, new Vec2(internalMinX, internalMaxY));

                        Vec3 normal = new Vec3(0.0f, 0.0f, 1.0f);
                        putDataIntoBuffers(bufferEntry, ibl, ibr, itr, itl, normal);

                        // create top
                        normal = MathUtil.rotateNormal(xForm, new Vec3(0.0f, 0.7071f, 0.7071f));
                        putDataIntoBuffers(bufferEntry, itl, itr, tr, tl, normal);

                        // create right
                        normal = MathUtil.rotateNormal(xForm, new Vec3(0.7071f, 0.0f, 0.7071f));
                        putDataIntoBuffers(bufferEntry, ibr, br, tr, itr, normal);

                        // create bottom
                        normal = MathUtil.rotateNormal(xForm, new Vec3(0.0f, -0.7071f, 0.7071f));
                        putDataIntoBuffers(bufferEntry, bl, br, ibr, ibl, normal);

                        // create left
                        normal = MathUtil.rotateNormal(xForm, new Vec3(-0.7071f, 0.0f, 0.7071f));
                        putDataIntoBuffers(bufferEntry, bl, ibl, itl, tl, normal);

                    } else {
                        putDataIntoBuffers(bufferEntry, bl, br, tr, tl, null);
                    }
                }

                Vec2 start = XForm.mul(xForm, outline[outline.length - 1]);
                for (int i = 0; i < outline.length; i++) {

                    Vec2 end = XForm.mul(xForm, outline[i]);
                    lineVertexBuffer.put(new float[]{
                                start.x, start.y,
                                end.x, end.y
                            });

                    start = end;
                }
            }
        }
    }

    public void render(GL gl, GameModel gameModel) {
        ensureBufferCapacity(gameModel);

        createBufferData(gameModel.getBuildingBlockPool());
        for (Player player : gameModel.getPlayers()) {
            createBufferData(player.getBuildingBlocks());
        }

        lineVertexBuffer.rewind();

        for (BufferEntry bufferEntry : bufferEntries.values()) {

            bufferEntry.vertexBuffer.rewind();
            bufferEntry.texCoordBuffer.rewind();
            gl.glVertexPointer(2, GL_FLOAT, 0, bufferEntry.vertexBuffer);
            gl.glTexCoordPointer(2, GL_FLOAT, 0, bufferEntry.texCoordBuffer);

            if (lightingEffects) {
                bufferEntry.normalBuffer.rewind();
                gl.glNormalPointer(GL_FLOAT, 0, bufferEntry.normalBuffer);
                
                gl.glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, bufferEntry.color, 0);
                gl.glMaterialfv(GL_FRONT, GL_SPECULAR, bufferEntry.specular, 0);
            } else {
                gl.glColor4fv(bufferEntry.color, 0);
            }

            bufferEntry.texture.bind();
            gl.glDrawArrays(GL_QUADS, 0, bufferEntry.numSquares * NUM_VERTICES_PER_SQUARE);
        }

    }

    public void renderLines(GL gl) {
        gl.glColor4fv(outlineColor, 0);
        gl.glVertexPointer(2, GL_FLOAT, 0, lineVertexBuffer);
        gl.glDrawArrays(GL_LINES, 0, numLines * 2);
    }

    private class BufferEntry {

        private FloatBuffer vertexBuffer;
        private FloatBuffer texCoordBuffer;
        private FloatBuffer normalBuffer;
        private final Texture texture;
        private int numSquares = 0;
        private final float[] color;
        private final float[] specular;
        // TODO: Define material properties

        public BufferEntry(Texture texture, float[] materialColor, float shinyFactor) {
            this.texture = texture;
            this.color = materialColor;
            specular = new float[]{shinyFactor, shinyFactor, shinyFactor, 1.0f};

            vertexBuffer = BufferUtil.newFloatBuffer(32 * NUM_VERTICES_PER_SQUARE * 2);
            texCoordBuffer = BufferUtil.newFloatBuffer(32 * NUM_VERTICES_PER_SQUARE * 2);
            if (lightingEffects) {
                normalBuffer = BufferUtil.newFloatBuffer(32 * NUM_VERTICES_PER_SQUARE * 3);
            }
        }
    }
}
