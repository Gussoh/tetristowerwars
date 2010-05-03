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
import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.gui.gl.animation.Path;
import org.tetristowerwars.model.CannonBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.TriggerBlock;
import org.tetristowerwars.model.WinningCondition.MessageEntry;

/**
 *
 * @author Andreas
 */
public class MessageRenderer {

    private final TextRenderer textRenderer;
    private final float textScale = 0.07f;
    private int lastTimeLeft = -1;
    private Path timeLeftSizeColorAnimation;

    public MessageRenderer(GL gl) {
        textRenderer = new TextRenderer(new Font("Sans-serif", Font.BOLD, 72), true, true, null, true);
    }

    public void render(GLDrawable drawable, GameModel gameModel, float renderWorldHeight, float elapsedTimeS) {

        int timeLeft = gameModel.getWinningCondition().timeLeftUntilGameOver();

        if (timeLeft == -1) {
            timeLeftSizeColorAnimation = null;
        } else if (timeLeft != lastTimeLeft) {
            timeLeftSizeColorAnimation = new Path(new Vec2(1.1f, 1.0f), new Vec2(1.0f, 0.0f), 1.0f);
            lastTimeLeft = timeLeft;
        } else if (timeLeftSizeColorAnimation != null) {
            timeLeftSizeColorAnimation.addTime(elapsedTimeS);
            if (timeLeftSizeColorAnimation.isDone()) {
                timeLeftSizeColorAnimation = null;
            }
        }

        textRenderer.begin3DRendering();

        float centerXPos = gameModel.getWorldBoundries().upperBound.x * 0.5f;
        float centerYPos = renderWorldHeight * 0.5f;

        if (gameModel.getWinningCondition().gameIsOver()) {
            ArrayList<TextEntry> leaderEntry = new ArrayList<TextEntry>();
            leaderEntry.add(new TextEntry(gameModel.getLeader().getName() + " wins!"));
            textRenderer.setColor(1.0f, 1.0f, 0.7f, 1.0f);
            renderText(leaderEntry, centerXPos, centerYPos, true, true, 4.0f);
        } else if (timeLeftSizeColorAnimation != null) {
            ArrayList<TextEntry> timeLeftEntry = new ArrayList<TextEntry>();
            timeLeftEntry.add(new TextEntry(gameModel.getWinningCondition().getLeader().getPlayer().getName() + " wins in " + timeLeft + " seconds."));
            Vec2 sizeColor = timeLeftSizeColorAnimation.getCurrentPosition();
            textRenderer.setColor(1.0f, sizeColor.y, sizeColor.y, 1.0f);
            renderText(timeLeftEntry, centerXPos, centerYPos, true, true, sizeColor.x * 2);
        }

        List<TextEntry> winningConditionTexts = new LinkedList<TextEntry>();
        textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.8f);

        List<MessageEntry> messages = gameModel.getWinningCondition().getStatusMessages();

        if (messages != null) {
            for (MessageEntry messageEntry : messages) {
                if (messageEntry.getPlayer() == null) {
                    winningConditionTexts.add(new TextEntry(messageEntry.getText()));
                }
            }
        }

        renderText(winningConditionTexts, centerXPos, renderWorldHeight * 0.7f, true, true, 6.0f);

        for (Player player : gameModel.getPlayers()) {
            textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.8f);
            float playerCenterPos = (player.getRightLimit() + player.getLeftLimit()) * 0.5f;

            List<TextEntry> texts = new LinkedList<TextEntry>();

            texts.add(new TextEntry(player.getName()));
            texts.add(new TextEntry("Tower height: " + Math.round(player.getTowerHeight()) + " m"));
            texts.add(new TextEntry("Blocks: " + player.getBuildingBlocks().size()));

            if (messages != null) {
                for (MessageEntry messageEntry : messages) {
                    if (messageEntry.getPlayer() == player) {
                        texts.add(new TextEntry(messageEntry.getText()));
                    }
                }
            }

            renderText(texts, playerCenterPos, gameModel.getGroundLevel() - 7, false, true, 1.0f);

            for (CannonBlock cannonBlock : player.getCannons()) {
                if (cannonBlock.isCannonLoaded()) {
                    textRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
                    texts.clear();
                    texts.add(new TextEntry((int) Math.ceil(cannonBlock.getTimeUntilShooting()) + ""));
                    Vec2 pos = cannonBlock.getBody().getPosition();
                    renderText(texts, pos.x, pos.y, true, true, 2.0f);
                }
            }

        }

        List<TextEntry> triggerTexts = new LinkedList<TextEntry>();
        textRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
        for (TriggerBlock triggerBlock : gameModel.getTriggerBlocks()) {
            if (triggerBlock.isVisible()) {
                triggerTexts.add(new TextEntry(triggerBlock.getText()));
                Body body = triggerBlock.getBody();
                Shape s = body.getShapeList();

                if (s instanceof CircleShape) {
                    CircleShape circleShape = (CircleShape) s;
                    Vec2 pos = body.getPosition();
                    renderText(triggerTexts, pos.x, pos.y + 10, true, true, 1.5f);
                }
                triggerTexts.clear();
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
