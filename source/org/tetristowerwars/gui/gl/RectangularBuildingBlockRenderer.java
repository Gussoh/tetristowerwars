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
import java.util.HashMap;
import java.util.Map;
import javax.media.opengl.GL;
import javax.media.opengl.GLException;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.RectangularBuildingBlock;
import org.tetristowerwars.model.material.AluminiumMaterial;
import org.tetristowerwars.model.material.ConcreteMaterial;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;
import org.tetristowerwars.model.material.WoodMaterial;
import static javax.media.opengl.GL.*;

/**
 *
 * @author Andreas
 */
public class RectangularBuildingBlockRenderer implements Comparable<RectangularBuildingBlockRenderer> {

    private final BuildingBlock buildingBlock;
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer texCoordsBuffer;
    private final FloatBuffer normalBuffer;
    private final int numQuads;
    private final int numPoints;
    private static final Map<Class<? extends Material>, Texture> material2Texture = new HashMap<Class<? extends Material>, Texture>();
    private final Texture texture;
    private final String materialName;

    public RectangularBuildingBlockRenderer(GL gl, RectangularBuildingBlock buildingBlock) {
        this.buildingBlock = buildingBlock;
        numQuads = buildingBlock.getRectangles().length;
        numPoints = buildingBlock.getOutline().length;


        Class<? extends Material> mat = buildingBlock.getMaterial().getClass();

        if (!material2Texture.containsKey(mat)) {
            createTexture(mat);
        }

        texture = material2Texture.get(mat);
        materialName = buildingBlock.getMaterial().getClass().getSimpleName();

        vertexBuffer = BufferUtil.newFloatBuffer(numQuads * 4 * 2 + numPoints * 2); // 4 vertices per quad * 2 floats per vertex
        texCoordsBuffer = BufferUtil.newFloatBuffer(numQuads * 4 * 2);
        normalBuffer = BufferUtil.newFloatBuffer(numQuads * 4 * 3); // 3 floats per vertex for normals.

        for (Rectangle2D r : buildingBlock.getRectangles()) {
            vertexBuffer.put(new float[]{(float) r.getX(), (float) -r.getY()}); // left-top
            texCoordsBuffer.put(new float[]{0.0f, 0.0f});

            vertexBuffer.put(new float[]{(float) r.getX(), (float) -(r.getY() + r.getHeight())}); // left-bottom
            texCoordsBuffer.put(new float[]{0.0f, 1.0f});

            vertexBuffer.put(new float[]{(float) (r.getX() + r.getWidth()), (float) -(r.getY() + r.getHeight())}); // right-bottom
            texCoordsBuffer.put(new float[]{1.0f, 1.0f});

            vertexBuffer.put(new float[]{(float) (r.getX() + r.getWidth()), (float) -r.getY()}); // right-top
            texCoordsBuffer.put(new float[]{1.0f, 0.0f});
        }

        for (Vec2 vec2 : buildingBlock.getOutline()) {
            vertexBuffer.put(vec2.x);
            vertexBuffer.put(vec2.y);
        }

        vertexBuffer.rewind();
        texCoordsBuffer.rewind();
    }

    public void bindTexture() {
        texture.bind();
    }

    private void createTexture(Class<? extends Material> mat) throws GLException {
        try {
            Texture tex = null;
            if (mat.equals(SteelMaterial.class)) {
                tex = TextureIO.newTexture(new File("res/gfx/singleblocks/patch_blue.png"), true);
            } else if (mat.equals(WoodMaterial.class)) {
                tex = TextureIO.newTexture(new File("res/gfx/singleblocks/tree01.png"), true);
            } else if (mat.equals(ConcreteMaterial.class)) {
                tex = TextureIO.newTexture(new File("res/gfx/singleblocks/patch_green.png"), true);
            } else if (mat.equals(AluminiumMaterial.class)) {
                tex = TextureIO.newTexture(new File("res/gfx/singleblocks/mosaic_orange.png"), true);
            }
            if (tex != null) {
                GLUtil.fixTextureParameters(tex);
                material2Texture.put(mat, tex);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void render(GL gl) {
        gl.glPushMatrix();
        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordsBuffer);
        setupMatrix(gl);
        gl.glDrawArrays(GL_QUADS, 0, numQuads * 4);
        gl.glPopMatrix();
    }

    public void renderLines(GL gl) {
        gl.glPushMatrix();
        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        setupMatrix(gl);
        gl.glDrawArrays(GL_LINE_LOOP, numQuads * 4, numPoints);
        gl.glPopMatrix();
    }

    private void setupMatrix(GL gl) {
        Vec2 pos = buildingBlock.getBody().getPosition();
        gl.glTranslatef(pos.x, pos.y, 0);
        gl.glRotatef((float) Math.toDegrees(buildingBlock.getBody().getAngle()), 0, 0, 1.0f);
    }

    @Override
    public int compareTo(RectangularBuildingBlockRenderer o) {
        return materialName.compareTo(o.materialName);
    }

    public String getMaterialName() {
        return materialName;
    }
}
