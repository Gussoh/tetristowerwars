/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.tetristowerwars.gui.gl.GLUtil;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;

/**
 *
 * @author Reeen
 */
public class BulletFactory {
    private final float blockSize;
    private final GameModel gameModel;

    public BulletFactory(GameModel gameModel, float blockSize) {
        this.blockSize = blockSize;
        this.gameModel = gameModel;
    }

    public BulletBlock createBullet(CannonBlock cannon) {

        float radians = cannon.getAngleInRadians();

        if (cannon.isShootingToLeft()) {
            radians = (float)Math.PI - radians;
        }
        Vec2 impulse = GLUtil.rotate(new Vec2(1, 0), -radians);
        Vec2 pos = cannon.getBody().getPosition();
        pos.x += impulse.x * blockSize * 2;
        pos.y += impulse.y * blockSize * 2;

        Body body = createBody(pos);
        addShape(blockSize / 2, new SteelMaterial(), body);

        impulse.x *= cannon.getForce();
        impulse.y *= cannon.getForce();

        body.applyImpulse(impulse, body.getPosition());

        BulletBlock block = new BulletBlock(body, cannon);
        gameModel.fireBodyCreationNotification(block);

        return block;
    }

    private Body createBody(Vec2 pos) {

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.allowSleep = true;
        boxBodyDef.position.set(pos.x, pos.y);


        return gameModel.getWorld().createBody(boxBodyDef);
    }

    private void addShape(float radius, Material mat, Body body) {

        CircleDef shapeDef = new CircleDef();
        shapeDef.density = mat.getDensity();
        shapeDef.radius = radius;
        shapeDef.isSensor = false;
        shapeDef.friction = 0.8f;
        shapeDef.restitution = 0.1f;
        body.createShape(shapeDef);
        body.setMassFromShapes();
    }
}
