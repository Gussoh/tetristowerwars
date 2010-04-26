/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import java.awt.Font;
import javax.media.opengl.GL;
import org.tetristowerwars.model.GameModel;

/**
 *
 * @author Andreas
 */
public class WinRenderer {

    

    public WinRenderer(GL gl) {
        
    }

    public void render(GL gl, GameModel gameModel) {
        if (gameModel.getWinningCondition().gameIsOver()) {

        }
    }

    public void triggerWinAnimation(long atTimeMs) {
    }
}

