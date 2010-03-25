/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
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

    private final Map<String, List<Clip>> clipDB = new HashMap<String, List<Clip>>();
    private GameModel gameModel;
    private String soundLocation = "res/sound/";
    private Mixer.Info mixer = null;
    private static final int MAX_CLIPS = 10;

    public SoundPlayer(GameModel gameModel) {
        this.gameModel = gameModel;
        for (javax.sound.sampled.Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().contains("Java Sound Audio Engine")) {
                mixer = info;
                break;
            }
        }
        gameModel.addGameModelListener(this);
    }

    public static void main(String[] args) {
        SoundPlayer sp = new SoundPlayer(null);
        for (float i = 0; i < 1.1; i += .1) {
            sp.playSound("collisionHard1.wav", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Clip loadSound(String filename) {
        System.out.println("loading " + filename);
        try {
            Clip clip;
            if (mixer != null) {
                clip = AudioSystem.getClip(mixer);
            } else {
                clip = AudioSystem.getClip();
            }
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

    public Clip getFirstAvailable(String filename) {
        List<Clip> list = clipDB.get(filename);

        if (list == null) {
            list = new ArrayList<Clip>();
            clipDB.put(filename, list);
        }

        for (;;) {
            for (int i = 0; i < list.size(); i++) {
                Clip clip = clipDB.get(filename).get(i);
                if (!clip.isActive()) {
                    return clip;
                }
            }

            if (list.size() >= MAX_CLIPS) {
                return null;
            }
            
            list.add(loadSound(filename));
        }
    }

    public void playSound(String filename, float volume, boolean repeat) {


        System.out.println("playing " + filename + " at volume " + volume);
        Clip clip = getFirstAvailable(filename);

        if (clip != null) {
            if (!clip.isActive()) {
                clip.stop();
                clip.flush();

                FloatControl gainControl = null;
                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                }

                FloatControl panControl = null;
                if (clip.isControlSupported(FloatControl.Type.PAN)) {
                    panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
                }

                if (gainControl != null) {
                    float min = gainControl.getMinimum();
                    float max = gainControl.getMaximum();
                    float value = gainControl.getMinimum() + (max - min) * (float) Math.sqrt(volume);
                    value = Math.max(min, Math.min(max, value));
                    //System.out.println("Found gain control: " + gainControl);
                    gainControl.setValue(value);
                }
                clip.loop(repeat ? Clip.LOOP_CONTINUOUSLY : 0);
                System.out.println("Active: " + clip.isActive());
            }
        }
    }

    public void playSound(String filename, float volume) {
        playSound(filename, volume, false);
    }

    @Override
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed) {
        //System.out.println("collision: " + collisionSpeed);
        if (collisionSpeed > 1.0) {
            if (collisionSpeed > 10) {
                playSound("collisionHard1.wav", (collisionSpeed - 10) / 8);
            } else {
                playSound("collision1.wav", collisionSpeed / 4);
            }
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
