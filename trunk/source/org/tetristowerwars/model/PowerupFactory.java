/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.PowerupMaterial;

/**
 *
 * @author Andreas
 */
public class PowerupFactory {

    private final GameModel gameModel;

    protected PowerupFactory(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public PowerupBlock createPowerUp(Player player, boolean leftSide) {
        
        float blockSize = gameModel.getBlockSize();
        float x = leftSide ? player.getLeftLimit() + blockSize : player.getRightLimit() - blockSize;
        float y = gameModel.getGroundLevel();
        Vec2 pos = new Vec2(x, y);
        Material mat = new PowerupMaterial();


        Body body = createBody(pos);

        List<Vec2> vertices = new ArrayList<Vec2>(4);

        float left = -blockSize * 0.5f;
        float right = blockSize * 0.5f;
        float bottom = -blockSize * 0.5f;
        float top = blockSize * 0.5f;

        vertices.add(new Vec2(left, bottom));
        vertices.add(new Vec2(right, bottom));
        vertices.add(new Vec2(right, top));
        vertices.add(new Vec2(left, top));

        addShape(vertices, mat, body);

        Rectangle2D[] recs = new Rectangle2D[1];
        recs[0] = new Rectangle2D.Float(left, bottom, blockSize, blockSize);

        PowerupBlock block = new PowerupBlock(body, mat, recs, vertices.toArray(new Vec2[0]));
        player.addBuildingBlock(block);

        gameModel.fireBodyCreationNotification(block);

        return block;
    }

     private Body createBody(Vec2 pos) {

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.allowSleep = true;
        boxBodyDef.position.set(pos.x, pos.y);

        return gameModel.getWorld().createBody(boxBodyDef);
    }

    private void addShape(List<Vec2> vertices, Material mat, Body body) {

        PolygonDef shapeDef = new PolygonDef();
        shapeDef.density = mat.getDensity();
        shapeDef.vertices = vertices;
        shapeDef.isSensor = false;
        shapeDef.friction = 0.8f;
        shapeDef.restitution = 0.1f;
        body.createShape(shapeDef);

    }
}
