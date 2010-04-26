/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.GLDrawable;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.CannonBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.WinningCondition;
import org.tetristowerwars.model.WinningCondition.MessageEntry;

/**
 *
 * @author Andreas
 */
public class TextInformationRenderer {

    private final TextRenderer textRenderer;
    private final float textScale = 0.08f;

    public TextInformationRenderer(GL gl) {
        textRenderer = new TextRenderer(new Font("Sans-serif", Font.BOLD, 64), true, true, null, true);
    }

    public void render(GLDrawable drawable, GameModel gameModel, float renderWorldHeight) {

        
        textRenderer.begin3DRendering();

        float centerXPos = gameModel.getWorldBoundries().upperBound.x * 0.5f;

        if (gameModel.checkWinningConditions()) {
            ArrayList<TextEntry> leaderEntry = new ArrayList<TextEntry>();
            leaderEntry.add(new TextEntry(gameModel.getLeader().getName() + " wins!"));
            textRenderer.setColor(1.0f, 1.0f, 0.7f, 1.0f);
            renderText(leaderEntry, centerXPos, renderWorldHeight * 0.5f, true, true, 4.0f);
        }

        List<TextEntry> winningConditionTexts = new LinkedList<TextEntry>();
        textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.6f);
        for (WinningCondition winningCondition : gameModel.getWinningConditions()) {

            List<MessageEntry> message = winningCondition.getStatusMessage();

            if (message != null) {
                for (MessageEntry messageEntry : message) {
                    if (messageEntry.getPlayer() == null) {
                        winningConditionTexts.add(new TextEntry(messageEntry.getText()));
                    }
                }
            }
        }
        renderText(winningConditionTexts, centerXPos, gameModel.getGroundLevel() - 7, true, true, 1.0f);

        for (Player player : gameModel.getPlayers()) {
            textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.6f);
            float playerCenterPos = (player.getRightLimit() + player.getLeftLimit()) * 0.5f;

            List<TextEntry> texts = new LinkedList<TextEntry>();

            texts.add(new TextEntry(player.getName()));
            texts.add(new TextEntry("Tower height: " + Math.round(player.getTowerHeight()) + " m"));
            texts.add(new TextEntry("Blocks: " + player.getBuildingBlocks().size()));

            for (WinningCondition winningCondition : gameModel.getWinningConditions()) {
                List<MessageEntry> message = winningCondition.getStatusMessage();
                if (message != null) {
                    for (MessageEntry messageEntry : message) {
                        if (messageEntry.getPlayer() == player) {
                            texts.add(new TextEntry(messageEntry.getText()));
                        }
                    }
                }
            }

            renderText(texts, playerCenterPos, gameModel.getGroundLevel() - 7, false, true, 1.0f);

            for (CannonBlock cannonBlock : player.getCannons()) {
                if (cannonBlock.isCannonLoaded()) {
                    textRenderer.setColor(1.0f, 0.0f, 0.0f, 0.6f);
                    texts.clear();
                    texts.add(new TextEntry((int)Math.ceil(cannonBlock.getTimeUntilShooting()) + ""));
                    Vec2 pos = cannonBlock.getBody().getPosition();
                    renderText(texts, pos.x, pos.y, true, true, 2.0f);
                }
            }
        }
        textRenderer.end3DRendering();
    }

    private float renderText(List<TextEntry> textEntries, float centerXPos, float yPos, boolean centered, boolean equalLineSpace, float size) {

        float lineSpace = 0;
        float finalSize = textScale * size;
        
        if (equalLineSpace) {

            for (TextEntry textEntry : textEntries) {
                lineSpace = Math.max(lineSpace, (float) textEntry.bounds.getHeight());
            }

            lineSpace *= finalSize;
        }


        if (centered) {
            for (TextEntry entry : textEntries) {
                float xPos = centerXPos - (float) entry.bounds.getWidth() * 0.5f * finalSize;
                yPos -= equalLineSpace ? lineSpace : (float) entry.bounds.getHeight() * finalSize;

                textRenderer.draw3D(entry.text, xPos, yPos, 0, finalSize);
            }
        } else {
            float maxWidth = 0;
            for (TextEntry entry : textEntries) {
                maxWidth = Math.max(maxWidth, (float) entry.bounds.getWidth());
            }

            float xPos = centerXPos - maxWidth * 0.5f * finalSize;
            for (TextEntry entry : textEntries) {
                yPos -= equalLineSpace ? lineSpace : (float) entry.bounds.getHeight() * finalSize;

                textRenderer.draw3D(entry.text, xPos, yPos, 0, finalSize);
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
