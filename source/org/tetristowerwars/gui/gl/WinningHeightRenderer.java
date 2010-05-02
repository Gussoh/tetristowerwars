/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl;

import javax.media.opengl.GL;
import static javax.media.opengl.GL.*;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;

/**
 *
 * @author Administrator
 */
public class WinningHeightRenderer {

    public void render(GL gl, GameModel gameModel, float lineWidthFactor) {
        gl.glLineWidth(lineWidthFactor * 2.0f);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
        gl.glLineStipple(3, Short.valueOf("0000111100001111", 2));
        gl.glEnable(GL_LINE_STIPPLE);

        gl.glBegin(GL_LINES);
        for (Player player : gameModel.getPlayers()) {
            float y = gameModel.getWinningCondition().getWinningHeight(player) + gameModel.getGroundLevel();
            float leftX = player.getLeftLimit();
            float rightX = player.getRightLimit();

            gl.glVertex2f(leftX, y);
            gl.glVertex2f(rightX, y);
        }
        gl.glEnd();
        
        gl.glDisable(GL_LINE_STIPPLE);

    }
}
