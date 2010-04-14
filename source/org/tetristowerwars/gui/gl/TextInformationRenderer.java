/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
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
    private final float textScale = 0.12f;

    public TextInformationRenderer(GL gl) {
        textRenderer = new TextRenderer(new Font("Sans-serif", Font.BOLD, 32), true, true, null, true);
    }

    public void render(GLDrawable drawable, GameModel gameModel, float renderWorldHeight) {

        textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.8f);
        textRenderer.begin3DRendering();
        for (Player player : gameModel.getPlayers()) {
            float playerCenterPos = (player.getRightLimit() + player.getLeftLimit()) * 0.5f;
            
            List<TextEntry> texts = new LinkedList<TextEntry>();

            texts.add(new TextEntry(player.getName()));
            texts.add(new TextEntry("Tower height: " + Math.round(player.getTowerHeight()) + " m"));
            texts.add(new TextEntry("Blocks: " + player.getBuildingBlocks().size()));
            
            renderText(texts, renderWorldHeight, playerCenterPos, false, true);
        }
        textRenderer.end3DRendering();
    }

    private float renderText(List<TextEntry> textEntries, float yPos, float centerXPos, boolean centered, boolean equalLineSpace) {

        float lineSpace = 0;

        if (equalLineSpace) {

            for (TextEntry textEntry : textEntries) {
                lineSpace = Math.max(lineSpace, (float) textEntry.bounds.getHeight());
            }

            lineSpace *= textScale;
        }


        if (centered) {
            for (TextEntry entry : textEntries) {
                float xPos = centerXPos - (float) entry.bounds.getWidth() * 0.5f * textScale;
                yPos -= equalLineSpace ? lineSpace : (float) entry.bounds.getHeight() * textScale;

                textRenderer.draw3D(entry.text, xPos, yPos, 0, textScale);
            }
        } else {
            float maxWidth = 0;
            for (TextEntry entry : textEntries) {
                maxWidth = Math.max(maxWidth, (float) entry.bounds.getWidth());
            }

            float xPos = centerXPos - maxWidth * 0.5f * textScale;
            for (TextEntry entry : textEntries) {
                yPos -= equalLineSpace ? lineSpace : (float) entry.bounds.getHeight() * textScale;

                textRenderer.draw3D(entry.text, xPos, yPos, 0, textScale);
            }
        }


        return yPos;
    }

    private class TextEntry {

        private final String text;
        private final Rectangle2D bounds;

        public TextEntry(String text) {
            this.text = text;
            this.bounds = textRenderer.getBounds(text);
        }
    }
}
