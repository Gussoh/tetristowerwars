/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author Reeen
 */
public class BulletFactory {
    private final float blockSize;
    private final GameModel gameModel;

    protected BulletFactory(GameModel gameModel, float blockSize) {
        this.blockSize = blockSize;
        this.gameModel = gameModel;
    }

    public BulletBlock createBullet(CannonBlock cannon, Material material) {

        float cannonAngle = -cannon.getAngleInRadians();

        if (!cannon.isShootingToLeft()) {
            cannonAngle = (float)Math.PI - cannonAngle;
        }

        XForm xf = new XForm();
        xf.R.setAngle(cannonAngle);

        Vec2 cannonPos = cannon.getBody().getPosition();


        Vec2 impulse = XForm.mul(xf, new Vec2(-1, 0));
        // Put the bullet a bit away from the tower body to avoid auto-translation by the physics engine.
        Vec2 bulletPosition = new Vec2(cannonPos.x + impulse.x * blockSize * 2, cannonPos.y + impulse.y * blockSize * 2);
        
        Body body = createBody(bulletPosition);
        addShape(blockSize * 0.5f, material, body);

        impulse.x *= cannon.getForce();
        impulse.y *= cannon.getForce();

        body.applyImpulse(impulse, body.getPosition());

        BulletBlock block = new BulletBlock(body, cannon);
        gameModel.fireBodyCreationNotification(block);

        return block;
    }

    private Body createBody(Vec2 pos) {

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.allowSleep = false;
        boxBodyDef.position.set(pos.x, pos.y);

        return gameModel.getWorld().createBody(boxBodyDef);
    }

    private void addShape(float radius, Material mat, Body body) {

        CircleDef shapeDef = new CircleDef();
        shapeDef.density = mat.getDensity();
        shapeDef.radius = radius;
        shapeDef.isSensor = true;
        shapeDef.friction = 0.8f;
        shapeDef.restitution = 0.1f;
        body.createShape(shapeDef);
        body.setMassFromShapes();
    }
}
