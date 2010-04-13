/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl;

import java.util.Set;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.BuildingBlockJoint;
import static javax.media.opengl.GL.*;
/**
 *
 * @author Andreas
 */
public class JointRenderer {


    private final float[] color = {1.0f, 0.0f, 0.0f, 0.5f};

    public void renderLines(GL gl, Set<BuildingBlockJoint> blockJoints) {

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gl.glColor4fv(color, 0);
        
        gl.glBegin(GL.GL_LINES);

        for (BuildingBlockJoint buildingBlockJoint : blockJoints) {
            Vec2 v1 = buildingBlockJoint.getBodyPosition();
            Vec2 v2 = buildingBlockJoint.getPointerPosition();
            gl.glVertex2f(v1.x, v1.y);
            gl.glVertex2f(v2.x, v2.y);
        }

        gl.glEnd();
    }
}
