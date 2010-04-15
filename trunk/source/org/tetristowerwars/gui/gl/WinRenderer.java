/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import javax.media.opengl.GL;

/**
 *
 * @author Andreas
 */
public class WinRenderer {

   private final TextRenderer textRenderer;
    private final float textScale = 0.12f;

    public WinRenderer(GL gl) {
        textRenderer = new TextRenderer(new Font("Sans-serif", Font.BOLD, 32), true, true, null, true);
    }
}

