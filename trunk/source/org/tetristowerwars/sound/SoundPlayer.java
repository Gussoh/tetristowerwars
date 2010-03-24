/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.sound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.GameModelListener;

/**
 *
 * @author Andreas
 */
public class SoundPlayer implements GameModelListener {

    private final Map<String, Clip> filename2Clip = new HashMap<String, Clip>();
    private GameModel gameModel;
    private String soundLocation = "res/sound/";

    public SoundPlayer(GameModel gameModel) {
        this.gameModel = gameModel;
        gameModel.addGameModelListener(this);
    }

    public Clip loadSound(String filename) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream in = AudioSystem.getAudioInputStream(new File(soundLocation + filename));

            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED, // Encoding to use
                    baseFormat.getSampleRate(), // sample rate (same as base format)
                    16, // sample size in bits (thx to Javazoom)
                    baseFormat.getChannels(), // # of Channels
                    baseFormat.getChannels() * 2, // Frame Size
                    baseFormat.getSampleRate(), // Frame Rate
                    false // Big Endian
                    );

            AudioInputStream decodedIn = AudioSystem.getAudioInputStream(decodedFormat, in);
            clip.open(decodedIn);

            filename2Clip.put(filename, clip);
            return clip;
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void playSound(String filename, boolean repeat) {

        Clip clip = filename2Clip.get(filename);
        if (clip == null) {
            loadSound(filename);
        }
        if (clip != null) {
            clip.loop(repeat ? Clip.LOOP_CONTINUOUSLY : 1);
        }
    }

    public void playSound(String filename) {
        playSound(filename, false);
    }

    @Override
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed) {
        if (collisionSpeed > 100) {
            playSound("collisionHard1.wav");
        } else {
            playSound("collision1.wav");
        }
    }

    @Override
    public void onJointCreation(BuildingBlockJoint blockJoint) {
    }

    @Override
    public void onBlockDestruction(Block block) {
    }

    @Override
    public void onJointDestruction(BuildingBlockJoint blockJoint) {
    }
}
