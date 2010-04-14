/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import javax.media.opengl.GL;
import javax.media.opengl.GLDrawable;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;

/**
 *
 * @author Andreas
 */
public class TextInformationRenderer {
    private final TextRenderer textRenderer;

    public TextInformationRenderer(GL gl) {
       textRenderer = new TextRenderer(new Font("Sans-serif", Font.BOLD, 14), true, true, null, true);
    }


    public void render(GLDrawable drawable, GameModel gameModel, float renderWorldHeight) {
        float widthFactor = gameModel.getWorldBoundries().upperBound.x / (float) drawable.getWidth();
        float heightFactor = renderWorldHeight / (float) drawable.getHeight();

        textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        for (Player player : gameModel.getPlayers()) {
            float playerCenterPos = (player.getRightLimit() + player.getLeftLimit()) * 0.5f * widthFactor;
            Rectangle2D bounds = textRenderer.getBounds(player.getName());
            textRenderer.draw(player.getName(), (int) (playerCenterPos - bounds.getWidth()), (int) (drawable.getHeight() - bounds.getHeight()));
        }
        textRenderer.endRendering();
    }
}
