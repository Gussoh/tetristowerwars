/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.BufferUtil;
import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.RectangularBuildingBlock;

/**
 *
 * @author Andreas
 */
public class BuildingBlockOutlineRenderer {

    private FloatBuffer vertexBuffer;

    public BuildingBlockOutlineRenderer(GL gl) {
        vertexBuffer = BufferUtil.newFloatBuffer(100);
    }

    public void render(GL gl, GameModel gameModel) {

        int numLines = 0;

        for (BuildingBlock buildingBlock : gameModel.getBuildingBlockPool()) {
            numLines += ((RectangularBuildingBlock) buildingBlock).getOutline().length;
        }

        for (Player player : gameModel.getPlayers()) {

        }
    }
}
