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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;
import org.tetristowerwars.gui.gl.animation.Path;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.RectangularBuildingBlock;
import org.tetristowerwars.model.material.BrickMaterial;
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
public class RectangularBuildingBlockRenderer {

    private final Map<Class<? extends Material>, BufferEntry> bufferEntries = new LinkedHashMap<Class<? extends Material>, BufferEntry>();
    private final int NUM_VERTICES_PER_SQUARE;
    private final float flatSquareRatio = 0.8f;
    private final boolean lightingEffects;
    private FloatBuffer lineVertexBuffer;
    private FloatBuffer animationVertexBuffer;
    private FloatBuffer animationColorBuffer;
    private int numLines;
    private int numAnimationVertices;
    private final float[] outlineColor = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] overlayColor = new float[]{0.9f, 1.0f, 0.9f};
    private static final float TEXTURE_COORD_FACTOR = 0.08f;
    private final static float START_INTENSITY = 1.0f;
    private final static float END_INTENSITY = 0.0f;
    private final static float ANIMATION_TIME_S = 1.5f;
    private final static float XY_NORMAL = 0.8f;
    private final static float NON_OWNED_BLOCK_COLOR_FACTOR = 1.0f;
    private final static float NON_OWNED_BLOCK_ALPHA_FACTOR = 0.6f;
    private final Map<BuildingBlock, Path> animations = new LinkedHashMap<BuildingBlock, Path>();

    public RectangularBuildingBlockRenderer(GL gl, boolean lightingEffects) throws IOException {
        this.lightingEffects = lightingEffects;
        if (lightingEffects) {
            NUM_VERTICES_PER_SQUARE = 20;
        } else {
            NUM_VERTICES_PER_SQUARE = 4;
        }

        float color = 0.9f;
        createBufferEntry(WoodMaterial.class, "res/gfx/textures/wood2.png", new float[]{color, color, color, 1.0f}, 0.0f);
        createBufferEntry(SteelMaterial.class, "res/gfx/textures/steel2.png", new float[]{color, color, color, 1.0f}, 0.0f);
        createBufferEntry(BrickMaterial.class, "res/gfx/textures/brick1.png", new float[]{color, color, color, 1.0f}, 0.0f);

        lineVertexBuffer = BufferUtil.newFloatBuffer(100);
        animationVertexBuffer = BufferUtil.newFloatBuffer(4 * 2 * 20);
        animationColorBuffer = BufferUtil.newFloatBuffer(4 * 4 * 20);
    }

    private void createBufferEntry(Class<? extends Material> materialClass, String textureFile, float[] materialColor, float shinyFactor) throws IOException {
        Texture texture = TextureIO.newTexture(new File(textureFile), true);
        bufferEntries.put(materialClass, new BufferEntry(texture, materialColor, shinyFactor));
        GLUtil.fixTextureParameters(texture);
        GLUtil.mirrorTexture(texture);
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
                bufferEntry.colorBuffer = BufferUtil.newFloatBuffer(numVertices * 4);
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

    private void putTextureData(BufferEntry bufferEntry, Vec2 bottomLeft, Vec2 bottomRight, Vec2 topRight, Vec2 topLeft) {
        if (bufferEntry.texture != null) {
            bufferEntry.texCoordBuffer.put(new float[]{
                        bottomLeft.x, bottomLeft.y,
                        bottomRight.x, bottomRight.y,
                        topRight.x, topRight.y,
                        topLeft.x, topLeft.y
                    });
        }
    }

    private void putDataIntoBuffers(BufferEntry bufferEntry, Vec2 bottomLeft, Vec2 bottomRight, Vec2 topRight, Vec2 topLeft, Vec3 normal, float[] color) {
        bufferEntry.vertexBuffer.put(new float[]{
                    bottomLeft.x, bottomLeft.y,
                    bottomRight.x, bottomRight.y,
                    topRight.x, topRight.y,
                    topLeft.x, topLeft.y
                });



        if (normal != null) {
            bufferEntry.normalBuffer.put(new float[]{
                        normal.x, normal.y, normal.z,
                        normal.x, normal.y, normal.z,
                        normal.x, normal.y, normal.z,
                        normal.x, normal.y, normal.z
                    });
        }
        bufferEntry.colorBuffer.put(color);
        bufferEntry.colorBuffer.put(color);
        bufferEntry.colorBuffer.put(color);
        bufferEntry.colorBuffer.put(color);
    }

    private void createBufferData(Set<BuildingBlock> buildingBlocks) {
        for (BuildingBlock buildingBlock : buildingBlocks) {
            if (buildingBlock instanceof RectangularBuildingBlock) {
                RectangularBuildingBlock rbb = (RectangularBuildingBlock) buildingBlock;

                BufferEntry bufferEntry = bufferEntries.get(rbb.getMaterial().getClass());
                XForm xForm = rbb.getBody().getXForm();
                Vec2[] outline = rbb.getOutline();
                Path p = animations.get(buildingBlock);

                float[] color;
                if (rbb.getOwner() == null) {
                    color = new float[] {
                        bufferEntry.color[0] * NON_OWNED_BLOCK_COLOR_FACTOR,
                        bufferEntry.color[1] * NON_OWNED_BLOCK_COLOR_FACTOR,
                        bufferEntry.color[2] * NON_OWNED_BLOCK_COLOR_FACTOR,
                        bufferEntry.color[3] * NON_OWNED_BLOCK_ALPHA_FACTOR
                    };
                } else {
                    color = bufferEntry.color;
                }

                for (Rectangle2D r : rbb.getRectangles()) {

                    float minX = (float) r.getX();
                    float minY = (float) -(r.getY() + r.getHeight());
                    float maxX = (float) (r.getX() + r.getWidth());
                    float maxY = (float) -r.getY();

                    Vec2 bl = XForm.mul(xForm, new Vec2(minX, minY));
                    Vec2 br = XForm.mul(xForm, new Vec2(maxX, minY));
                    Vec2 tr = XForm.mul(xForm, new Vec2(maxX, maxY));
                    Vec2 tl = XForm.mul(xForm, new Vec2(minX, maxY));

                    Vec2 texBl = new Vec2(minX * TEXTURE_COORD_FACTOR, minY * TEXTURE_COORD_FACTOR);
                    Vec2 texBr = new Vec2(maxX * TEXTURE_COORD_FACTOR, minY * TEXTURE_COORD_FACTOR);
                    Vec2 texTr = new Vec2(maxX * TEXTURE_COORD_FACTOR, maxY * TEXTURE_COORD_FACTOR);
                    Vec2 texTl = new Vec2(minX * TEXTURE_COORD_FACTOR, maxY * TEXTURE_COORD_FACTOR);


                    if (lightingEffects) {
                        float internalMinX = MathUtil.lerpNoCap(1f - flatSquareRatio, minX, maxX);
                        float internalMinY = MathUtil.lerpNoCap(1f - flatSquareRatio, minY, maxY);
                        float internalMaxX = MathUtil.lerpNoCap(flatSquareRatio, minX, maxX);
                        float internalMaxY = MathUtil.lerpNoCap(flatSquareRatio, minY, maxY);

                        Vec2 ibl = XForm.mul(xForm, new Vec2(internalMinX, internalMinY));
                        Vec2 ibr = XForm.mul(xForm, new Vec2(internalMaxX, internalMinY));
                        Vec2 itr = XForm.mul(xForm, new Vec2(internalMaxX, internalMaxY));
                        Vec2 itl = XForm.mul(xForm, new Vec2(internalMinX, internalMaxY));
                       

                        Vec2 texIntBl = new Vec2(internalMinX * TEXTURE_COORD_FACTOR, internalMinY * TEXTURE_COORD_FACTOR);
                        Vec2 texIntBr = new Vec2(internalMaxX * TEXTURE_COORD_FACTOR, internalMinY * TEXTURE_COORD_FACTOR);
                        Vec2 texIntTr = new Vec2(internalMaxX * TEXTURE_COORD_FACTOR, internalMaxY * TEXTURE_COORD_FACTOR);
                        Vec2 texIntTl = new Vec2(internalMinX * TEXTURE_COORD_FACTOR, internalMaxY * TEXTURE_COORD_FACTOR);

                         // create the Middle
                        Vec3 normal = new Vec3(0.0f, 0.0f, 1.0f);
                        putDataIntoBuffers(bufferEntry, ibl, ibr, itr, itl, normal, color);
                        putTextureData(bufferEntry, texIntBl, texIntBr, texIntTr, texIntTl);

                        // create top
                        normal = MathUtil.rotateNormal(xForm, new Vec3(0.0f, XY_NORMAL, 1.0f));
                        putDataIntoBuffers(bufferEntry, itl, itr, tr, tl, normal, color);
                        putTextureData(bufferEntry, texIntTl, texIntTr, texTr, texTl);

                        // create right
                        normal = MathUtil.rotateNormal(xForm, new Vec3(XY_NORMAL, 0.0f, 1.0f));
                        putDataIntoBuffers(bufferEntry, ibr, br, tr, itr, normal, color);
                        putTextureData(bufferEntry, texIntBr, texBr, texTr, texIntTr);

                        // create bottom
                        normal = MathUtil.rotateNormal(xForm, new Vec3(0.0f, -XY_NORMAL, 1.0f));
                        putDataIntoBuffers(bufferEntry, bl, br, ibr, ibl, normal, color);
                        putTextureData(bufferEntry, texBl, texBr, texIntBr, texIntBl);

                        // create left
                        normal = MathUtil.rotateNormal(xForm, new Vec3(-XY_NORMAL, 0.0f, 1.0f));
                        putDataIntoBuffers(bufferEntry, bl, ibl, itl, tl, normal, color);
                        putTextureData(bufferEntry, texBl, texIntBl, texIntTl, texTl);

                    } else {
                        putDataIntoBuffers(bufferEntry, bl, br, tr, tl, null, color);
                        putTextureData(bufferEntry, texBl, texBr, texTr, texTl);
                    }

                    if (p != null) {
                        float intensity = p.getCurrentPosition().x;
                        animationVertexBuffer.put(new float[]{
                                    bl.x, bl.y,
                                    br.x, br.y,
                                    tr.x, tr.y,
                                    tl.x, tl.y
                                });

                        animationColorBuffer.put(new float[]{
                                    overlayColor[0], overlayColor[1], overlayColor[2], intensity,
                                    overlayColor[0], overlayColor[1], overlayColor[2], intensity,
                                    overlayColor[0], overlayColor[1], overlayColor[2], intensity,
                                    overlayColor[0], overlayColor[1], overlayColor[2], intensity
                                });
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

    public void render(GL gl, GameModel gameModel, float elapsedTimeS) {
        ensureBufferCapacity(gameModel);
        updateAnimationsAndBufferCapacity(elapsedTimeS);

        createBufferData(gameModel.getBuildingBlockPool());
        for (Player player : gameModel.getPlayers()) {
            createBufferData(player.getBuildingBlocks());
        }

        lineVertexBuffer.rewind();
        animationVertexBuffer.rewind();
        animationColorBuffer.rewind();

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL_TEXTURE_2D);

        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL_COLOR_ARRAY);
        if (lightingEffects) {
            gl.glEnableClientState(GL_NORMAL_ARRAY);
            gl.glEnable(GL_LIGHTING);
            gl.glEnable(GL_COLOR_MATERIAL);
            gl.glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
        }



        for (BufferEntry bufferEntry : bufferEntries.values()) {

            bufferEntry.vertexBuffer.rewind();
            bufferEntry.texCoordBuffer.rewind();
            bufferEntry.colorBuffer.rewind();
            gl.glVertexPointer(2, GL_FLOAT, 0, bufferEntry.vertexBuffer);
            gl.glTexCoordPointer(2, GL_FLOAT, 0, bufferEntry.texCoordBuffer);
            gl.glColorPointer(4, GL_FLOAT, 0, bufferEntry.colorBuffer);

            if (lightingEffects) {
                bufferEntry.normalBuffer.rewind();
                gl.glNormalPointer(GL_FLOAT, 0, bufferEntry.normalBuffer);
              //  gl.glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, bufferEntry.color, 0);
                gl.glMaterialfv(GL_FRONT, GL_SPECULAR, bufferEntry.specular, 0);
            } 

            bufferEntry.texture.bind();
            gl.glDrawArrays(GL_QUADS, 0, bufferEntry.numSquares * NUM_VERTICES_PER_SQUARE);
        }

        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL_COLOR_ARRAY);
        if (lightingEffects) {
            gl.glDisable(GL_COLOR_MATERIAL);
            gl.glDisableClientState(GL_NORMAL_ARRAY);
            gl.glDisable(GL_LIGHTING);
        }
    }

    public void renderLines(GL gl, float lineWidth) {

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glLineWidth(lineWidth);
        gl.glColor4fv(outlineColor, 0);
        gl.glVertexPointer(2, GL_FLOAT, 0, lineVertexBuffer);
        gl.glDrawArrays(GL_LINES, 0, numLines * 2);
    }

    public void renderOverlay(GL gl) {
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        gl.glEnableClientState(GL_COLOR_ARRAY);

        gl.glVertexPointer(2, GL_FLOAT, 0, animationVertexBuffer);
        gl.glColorPointer(4, GL_FLOAT, 0, animationColorBuffer);
        gl.glDrawArrays(GL_QUADS, 0, numAnimationVertices);

        gl.glDisableClientState(GL_COLOR_ARRAY);
    }

    public void addBuildingBlockOverlayAnimation(BuildingBlock bb) {
        animations.put(bb, new Path(new Vec2(START_INTENSITY, 0), new Vec2(END_INTENSITY, 0), ANIMATION_TIME_S));
    }

    private void updateAnimationsAndBufferCapacity(float elapsedTimeS) {

        int numRects = 0;

        for (Iterator<Map.Entry<BuildingBlock, Path>> it = animations.entrySet().iterator(); it.hasNext();) {
            Map.Entry<BuildingBlock, Path> entry = it.next();

            Path p = entry.getValue();

            if (p.isDone()) {
                it.remove();
            } else {
                p.addTime(elapsedTimeS);
                if (entry.getKey() instanceof RectangularBuildingBlock) {
                    RectangularBuildingBlock rbb = (RectangularBuildingBlock) entry.getKey();
                    numRects += rbb.getRectangles().length;
                }
            }
        }


        numAnimationVertices = numRects * 4;

        if (numAnimationVertices * 2 > animationVertexBuffer.capacity()) {
            animationVertexBuffer = BufferUtil.newFloatBuffer(numAnimationVertices * 2);
            animationColorBuffer = BufferUtil.newFloatBuffer(numAnimationVertices * 4);
        }
    }

    private class BufferEntry {

        private FloatBuffer vertexBuffer;
        private FloatBuffer texCoordBuffer;
        private FloatBuffer normalBuffer;
        private FloatBuffer colorBuffer;
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
            if (texture != null) {
                texCoordBuffer = BufferUtil.newFloatBuffer(32 * NUM_VERTICES_PER_SQUARE * 2);
            }

            if (lightingEffects && materialColor != null) {
                normalBuffer = BufferUtil.newFloatBuffer(32 * NUM_VERTICES_PER_SQUARE * 3);
            }

            colorBuffer = BufferUtil.newFloatBuffer(32 * NUM_VERTICES_PER_SQUARE * 4);
        }
    }
}
