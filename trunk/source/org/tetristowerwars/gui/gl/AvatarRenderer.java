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
import java.util.List;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.*;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;

/**
 *
 * @author Andreas
 */
public class AvatarRenderer {

    private final Texture player1Texture;
    private final Texture player2Texture;
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer texCoordBuffer;


    public AvatarRenderer(GL gl, int themeId) throws IOException {
        String basePath = "res/gfx/THEME" + themeId + "/";

        player1Texture = TextureIO.newTexture(new File(basePath + "player1.png"), true);
        player2Texture = TextureIO.newTexture(new File(basePath + "player2.png"), true);
        vertexBuffer = BufferUtil.newFloatBuffer(2 * 4 * 2); // 2 texture, 4 per quad, 2 floats per vertex
        texCoordBuffer = BufferUtil.newFloatBuffer(2 * 4 * 2); // 2 texture, 4 per quad, 2 floats per vertex
    }

    public void render(GL gl, GameModel gameModel, float lineWidthFactor) {


        List<Player> players = gameModel.getPlayers();
        float avatar1CenterX = players.get(0).getRightLimit();
        float avatar2CenterX = players.get(1).getLeftLimit();
        float half = gameModel.getGroundLevel() / 3;
        
        float centerY = gameModel.getGroundLevel() * 0.4f;

        vertexBuffer.put(new float[] {
            avatar1CenterX - half, centerY - half,
            avatar1CenterX + half, centerY - half,
            avatar1CenterX + half, centerY + half,
            avatar1CenterX - half, centerY + half,

            avatar2CenterX - half, centerY - half,
            avatar2CenterX + half, centerY - half,
            avatar2CenterX + half, centerY + half,
            avatar2CenterX - half, centerY + half,
        });

        texCoordBuffer.put(new float[]{
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
        });

        vertexBuffer.rewind();
        texCoordBuffer.rewind();

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, texCoordBuffer);
        gl.glColor4fv(new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);

        player1Texture.bind();
        gl.glDrawArrays(GL_QUADS, 0, 4);

        player2Texture.bind();
        gl.glDrawArrays(GL_QUADS, 4, 4);

        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);

//        gl.glLineWidth(lineWidthFactor * 2);
//        gl.glColor4fv(new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
//        gl.glDrawArrays(GL_LINE_LOOP, 0, 4);
//        gl.glDrawArrays(GL_LINE_LOOP, 4, 4);
    }
}
