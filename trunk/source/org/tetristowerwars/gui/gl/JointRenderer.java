/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.BufferUtil;
import java.nio.FloatBuffer;
import java.util.Set;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.BuildingBlockJoint;

/**
 *
 * @author Andreas
 */
public class JointRenderer {

   

    public void render(GL gl, Set<BuildingBlockJoint> blockJoints) {
        
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
