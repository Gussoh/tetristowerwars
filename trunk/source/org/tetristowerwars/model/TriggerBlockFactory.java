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

/**
 *
 * @author Administrator
 */
public class TriggerBlockFactory {

    private final GameModel gameModel;

    protected TriggerBlockFactory(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void createRoundTrigger(Vec2 pos, float radius, String text, Runnable runnable) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pos);
        bodyDef.allowSleep = true;

        Body body = gameModel.getWorld().createBody(bodyDef);

        CircleDef circleDef = new CircleDef();
        circleDef.isSensor = true;
        circleDef.radius = radius;

        body.createShape(circleDef);
        TriggerBlock triggerBlock = new TriggerBlock(body, text, runnable);
        gameModel.addTriggerBlock(triggerBlock);
    }


    public void createSquareTrigger(Vec2 pos, float width, float height, String text, Runnable runnable) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.allowSleep = true;
        bodyDef.position.set(pos);

        Body body = gameModel.getWorld().createBody(bodyDef);

        List<Vec2> vertices = new ArrayList<Vec2>();

        vertices.add(new Vec2(pos.x - width / 2, pos.y - height / 2));
        vertices.add(new Vec2(pos.x + width / 2, pos.y - height / 2));
        vertices.add(new Vec2(pos.x + width / 2, pos.y + height / 2));
        vertices.add(new Vec2(pos.x - width / 2, pos.y + height / 2));

        PolygonDef polygonDef = new PolygonDef();
        polygonDef.isSensor = true;
        polygonDef.vertices = vertices;

        body.createShape(polygonDef);

        TriggerBlock triggerBlock = new TriggerBlock(body, text, runnable);
        gameModel.addTriggerBlock(triggerBlock);
    }
}
