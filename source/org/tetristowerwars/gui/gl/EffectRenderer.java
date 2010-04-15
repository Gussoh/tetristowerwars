/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.*;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.gl.animation.Path;
import org.tetristowerwars.gui.gl.particle.Color;
import org.tetristowerwars.gui.gl.particle.ColorStepFunction;
import org.tetristowerwars.gui.gl.particle.FadeOutStepFunction;
import org.tetristowerwars.gui.gl.particle.GravityStepFunction;
import org.tetristowerwars.gui.gl.particle.Particle;
import org.tetristowerwars.gui.gl.particle.ParticleEngine;
import org.tetristowerwars.gui.gl.particle.PointSourceParticleEngine;
import org.tetristowerwars.gui.gl.particle.VelocityDampStepFunction;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class EffectRenderer {

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private FloatBuffer particleVertexBuffer;
    private FloatBuffer particleTexCoordBuffer;
    private FloatBuffer particleColorBuffer;
    private final PointSourceParticleEngine explosionParticleEngine;
    private final PointSourceParticleEngine frictionParticleEngine;
    private final PointSourceParticleEngine smokeParticleEngine;
    private final Texture particleTexture;
    private final static int NUM_VERTICES_PER_LINE = 2;
    private final static int NUM_VERTICES_PER_PARTICLE = 4;
    private final static float START_INTENSITY = 0.9f;
    private final static float END_INTENSITY = 0.0f;
    private final static float START_LENGTH = 20.0f;
    private final static float END_LENGTH = 20.0f;
    private final static float ANIMATION_TIME_MS = 750.0f;
    private final Map<Path, Vec2> animations = new LinkedHashMap<Path, Vec2>();

    public EffectRenderer(GL gl) throws IOException {
        vertexBuffer = BufferUtil.newFloatBuffer(8 * NUM_VERTICES_PER_LINE * 2);
        colorBuffer = BufferUtil.newFloatBuffer(8 * NUM_VERTICES_PER_LINE * 4);

        particleVertexBuffer = BufferUtil.newFloatBuffer(40 * NUM_VERTICES_PER_PARTICLE * 2);
        particleTexCoordBuffer = BufferUtil.newFloatBuffer(40 * NUM_VERTICES_PER_PARTICLE * 2);
        particleColorBuffer = BufferUtil.newFloatBuffer(40 * NUM_VERTICES_PER_PARTICLE * 4);

        particleTexture = TextureIO.newTexture(new File("res/gfx/particle.png"), true);

        explosionParticleEngine = new PointSourceParticleEngine();
        explosionParticleEngine.setTimeToLive(1.5f, 2.0f);
        explosionParticleEngine.setDirection(0, MathUtil.PI * 2.0f);
        explosionParticleEngine.setRotationSpeed(0, 0);
        explosionParticleEngine.setSpeed(0.0f, 40.0f);
        explosionParticleEngine.setColor(new Color(0.6f, 0.3f, 0.0f, 0.3f), new Color(1.0f, 0.6f, 0.2f, 0.6f), false);
        explosionParticleEngine.setRadius(10.0f, 12.0f);
        explosionParticleEngine.addStepFunction(new FadeOutStepFunction(0.4f));
        //explosionParticleEngine.addStepFunction(new ColorStepFunction(0.6f, new Color(0, 0, 0, 0)));
        explosionParticleEngine.addStepFunction(new VelocityDampStepFunction(1.5f));


        frictionParticleEngine = new PointSourceParticleEngine();
        frictionParticleEngine.setTimeToLive(1.0f, 2.0f);
        frictionParticleEngine.setDirection(0, MathUtil.PI * 2.0f);
        frictionParticleEngine.setRotationSpeed(0, 0);
        frictionParticleEngine.setSpeed(1.0f, 10.0f);
        frictionParticleEngine.setColor(new Color(0.6f, 0.3f, 0.0f, 0.6f), new Color(1.0f, 0.6f, 0.2f, 1.0f), false);
        frictionParticleEngine.setRadius(0.3f, 0.6f);
        frictionParticleEngine.addStepFunction(new GravityStepFunction());
        frictionParticleEngine.addStepFunction(new FadeOutStepFunction(0.3f));
        //frictionParticleEngine.addStepFunction(new ColorStepFunction(0.6f, new Color(0, 0, 0, 0)));


        smokeParticleEngine = new PointSourceParticleEngine();
        smokeParticleEngine.setTimeToLive(5.0f, 10.0f);
        smokeParticleEngine.setDirection(0, MathUtil.PI * 2.0f);
        smokeParticleEngine.setRotationSpeed(0, 0);
        smokeParticleEngine.setSpeed(0, 7.0f);
        smokeParticleEngine.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f), new Color(0.3f, 0.3f, 0.3f, 0.5f), true);
        smokeParticleEngine.setRadius(20.0f, 30.0f);
        smokeParticleEngine.addStepFunction(new FadeOutStepFunction(0.5f));
        //smokeParticleEngine.addStepFunction(new VelocityDampStepFunction(1.5f));
        smokeParticleEngine.addStepFunction(new GravityStepFunction(1.5f));
    }

    public void render(GL gl, GameModel gameModel, float elapsedTime) {
        for (Iterator<Path> it = animations.keySet().iterator(); it.hasNext();) {
            Path p = it.next();

            if (p.isDone()) {
                it.remove();
            } else {
                p.addTime(elapsedTime);
            }
        }

        int numVertices = 2 * animations.size() * NUM_VERTICES_PER_LINE;

        if (numVertices * 2 > vertexBuffer.capacity()) {
            vertexBuffer = BufferUtil.newFloatBuffer(numVertices * 2);
            colorBuffer = BufferUtil.newFloatBuffer(numVertices * 4);
        }

        for (Map.Entry<Path, Vec2> entry : animations.entrySet()) {
            Path p = entry.getKey();
            Vec2 v = entry.getValue();

            Vec2 temp = p.getCurrentPosition();

            float intensity = temp.x;
            float length = temp.y;

            vertexBuffer.put(new float[]{
                        v.x, v.y - length,
                        v.x, v.y,
                        v.x, v.y,
                        v.x, v.y + length
                    });

            colorBuffer.put(new float[]{
                        0.0f, 1.0f, 0.0f, 0.0f,
                        1.0f, 1.0f, 1.0f, intensity,
                        1.0f, 1.0f, 1.0f, intensity,
                        0.0f, 1.0f, 0.0f, 0.0f,});
        }

        vertexBuffer.rewind();
        colorBuffer.rewind();

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        gl.glEnableClientState(GL_COLOR_ARRAY);

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL_FLOAT, 0, colorBuffer);

        gl.glDrawArrays(GL_LINES, 0, numVertices);

        gl.glDisableClientState(GL_COLOR_ARRAY);
    }

    public void createBufferData(ParticleEngine particleEngine) {

        for (Particle particle : particleEngine.getParticles()) {
            Vec2 pos = particle.getPosition();
            float radius = particle.getRadius();
            float lx = pos.x - radius;
            float rx = pos.x + radius;
            float by = pos.y - radius;
            float ty = pos.y + radius;
            particleVertexBuffer.put(new float[]{
                        lx, by,
                        rx, by,
                        rx, ty,
                        lx, ty
                    });

            particleTexCoordBuffer.put(new float[]{
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 0.0f
                    });

            Color c = particle.getCurrentColor();

            particleColorBuffer.put(new float[]{
                        c.r, c.g, c.b, c.a,
                        c.r, c.g, c.b, c.a,
                        c.r, c.g, c.b, c.a,
                        c.r, c.g, c.b, c.a
                    });
        }
    }

    public void renderParticles(GL gl, float elapsedTime) {

        explosionParticleEngine.update(elapsedTime * 0.001f);
        frictionParticleEngine.update(elapsedTime * 0.001f);
        smokeParticleEngine.update(elapsedTime * 0.001f);

        int numLightVertices = (explosionParticleEngine.getParticles().size() + frictionParticleEngine.getParticles().size()) * NUM_VERTICES_PER_PARTICLE;
        int numSolidVertices = (smokeParticleEngine.getParticles().size() * NUM_VERTICES_PER_PARTICLE);
        int totalNumVertices = numLightVertices + numSolidVertices;
        if (totalNumVertices * 2 > particleTexCoordBuffer.capacity()) {
            particleVertexBuffer = BufferUtil.newFloatBuffer(totalNumVertices * 2);
            particleTexCoordBuffer = BufferUtil.newFloatBuffer(totalNumVertices * 2);
            particleColorBuffer = BufferUtil.newFloatBuffer(totalNumVertices * 4);
        }

        createBufferData(smokeParticleEngine);
        createBufferData(explosionParticleEngine);
        createBufferData(frictionParticleEngine);

        particleVertexBuffer.rewind();
        particleTexCoordBuffer.rewind();
        particleColorBuffer.rewind();

        particleTexture.bind();
        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL_COLOR_ARRAY);

        gl.glVertexPointer(2, GL_FLOAT, 0, particleVertexBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, particleTexCoordBuffer);
        gl.glColorPointer(4, GL_FLOAT, 0, particleColorBuffer);

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glDrawArrays(GL_QUADS, 0, numSolidVertices);

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        gl.glDrawArrays(GL_QUADS, numSolidVertices, numLightVertices);

        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL_COLOR_ARRAY);
    }

    public void createBorderLitAnimation(Vec2 v) {
        Path p = new Path(new Vec2(START_INTENSITY, START_LENGTH), new Vec2(END_INTENSITY, END_LENGTH), ANIMATION_TIME_MS);
        animations.put(p, v);


    }

    public void createExplosionEffect(Vec2 v) {
        explosionParticleEngine.setPosition(v);
        explosionParticleEngine.createParticles(50);
    }

    public void createFrictionEffect(Vec2 v, int numParticles) {
        frictionParticleEngine.setPosition(v);
        frictionParticleEngine.createParticles(numParticles);
    }

    public void createSmokeEffect(Vec2 v) {
        smokeParticleEngine.setPosition(v);
        smokeParticleEngine.createParticles(10);
    }
}
