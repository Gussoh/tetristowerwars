/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import java.util.ArrayList;
import java.util.List;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

/**
 *
 * @author Reeen
 */
public class CannonFactory {
    
    private final GameModel gameModel;
    private float blockSize;

    public CannonFactory(GameModel gameModel, float blockSize) {
        this.gameModel = gameModel;    
        this.blockSize = blockSize;
    }

    public CannonBlock createBasicCannon(Player player, Vec2 pos) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize, -blockSize));
        vertices1.add(new Vec2(blockSize, -blockSize));
        vertices1.add(new Vec2(blockSize, blockSize));
        vertices1.add(new Vec2(-blockSize, blockSize));

        List<Vec2> vertices2 = new ArrayList<Vec2>(3);
        vertices2.add(new Vec2(-blockSize, blockSize));
        vertices2.add(new Vec2(blockSize, blockSize));
        vertices2.add(new Vec2(0, blockSize*2));

        addShape(vertices1, body);
        addShape(vertices2, body);

        CannonBlock cannonBlock = new CannonBlock(body, 10000, 5, player);
        
        return cannonBlock;
    }


    private Body createBody(Vec2 pos) {

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.allowSleep = true;
        boxBodyDef.position.set(pos.x, pos.y);

        return gameModel.getWorld().createBody(boxBodyDef);
    }

    private void addShape(List<Vec2> vertices, Body body) {

        PolygonDef shapeDef = new PolygonDef();
        shapeDef.vertices = vertices;
        shapeDef.isSensor = false;
        body.createShape(shapeDef);
    }

}
