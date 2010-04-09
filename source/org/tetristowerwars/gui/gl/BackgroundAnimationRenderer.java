/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import org.tetristowerwars.gui.gl.animation.Path;

/**
 *
 * @author Andreas
 */
public class BackgroundAnimationRenderer {

    private final Texture tank1Texture;
    private final Texture tank2Texture;
    private final Texture zeppelinTexture;

    private final List<Path> tank1Animations = new ArrayList<Path>();
    private final List<Path> tank2Animations = new ArrayList<Path>();
    private final List<Path> zeppelinAnimations = new ArrayList<Path>();

    public final static int TANK1 = 0, TANK2 = 1, ZEPPELIN = 2;

    public BackgroundAnimationRenderer(GL gl) throws IOException {
        tank1Texture = TextureIO.newTexture(new File("res/gfx/decoration/tank1.png"), true);
        tank2Texture = TextureIO.newTexture(new File("res/gfx/decoration/tank2.png"), true);
        zeppelinTexture = TextureIO.newTexture(new File("res/gfx/decoration/zeppelin1.png"), true);
    }


    public void render(GL gl, float timeElapsed) {

    }

    public void addAnimation(Path path, int image) {
        switch (image) {
            case TANK1:
                
                break;
            case TANK2:
                break;
            case ZEPPELIN:
                break;
        }
    }

}
