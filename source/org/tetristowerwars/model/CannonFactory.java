/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.util.ArrayList;
import java.util.List;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;

/**
 *
 * @author Reeen
 */
public class CannonFactory {

    private final GameModel gameModel;
    private float blockSize;
    private static final float CANNON_COOLDOWN = 5.0f;
    private static final float CANNON_FORCE = 1500f;

    protected CannonFactory(GameModel gameModel, float blockSize) {
        this.gameModel = gameModel;
        this.blockSize = blockSize;
    }

    public CannonBlock createBasicCannon(Player player, Vec2 pos, boolean shootingToLeft) {

        Body body = createBody(new Vec2(pos.x, pos.y + blockSize * 4));

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize, -blockSize * 4));
        vertices1.add(new Vec2(blockSize, -blockSize * 4));
        vertices1.add(new Vec2(blockSize, 0));
        vertices1.add(new Vec2(-blockSize, 0));

        addPolyShape(vertices1, body);
        addCircleShape(blockSize, new SteelMaterial(), body);

        CannonBlock cannonBlock = new CannonBlock(body, CANNON_FORCE, CANNON_COOLDOWN, player, shootingToLeft, gameModel.getBulletFactory());

        return cannonBlock;
    }

    private Body createBody(Vec2 pos) {

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.allowSleep = true;
        boxBodyDef.position.set(pos.x, pos.y);

        return gameModel.getWorld().createBody(boxBodyDef);
    }

    private void addPolyShape(List<Vec2> vertices, Body body) {

        PolygonDef shapeDef = new PolygonDef();
        shapeDef.vertices = vertices;
        shapeDef.isSensor = false;
        body.createShape(shapeDef);
    }

    private void addCircleShape(float radius, Material mat, Body body) {

        CircleDef shapeDef = new CircleDef();
        shapeDef.radius = radius;
        shapeDef.isSensor = false;
        body.createShape(shapeDef);
        body.setMassFromShapes();
    }
}
