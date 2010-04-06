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
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.GameModelListener;
import org.tetristowerwars.model.WinningCondition;

/**
 *
 * @author Andreas
 */
public class SoundPlayer implements GameModelListener {

    private final Map<String, List<Clip>> clipDB = new HashMap<String, List<Clip>>();
    private GameModel gameModel;
    private String soundLocation = "res/sound/";
    private Mixer.Info mixer = null;
    private static final int MAX_CLIPS = 4;
    private static final String[] collisionSounds = new String[]{"collision1.wav"};
    private static final String[] collisionHardSounds = new String[]{"collisionHard1.wav", "collisionHard2.wav", "collisionHard3.wav", "collisionHard4.wav", "collisionHard4.wav"};
    private static final String[] music = new String[]{"music1.mp3", "music2.mp3", "music3.mp3", "music4.mp3", "music5.mp3"};
    private static final String select = "select.wav";
    private static final String deselect = "deselect.wav";
    private static final String zap = "zap.wav";

    SourceDataLine sourceDataLine;
    AudioInputStream audioInputStream;

    public SoundPlayer(GameModel gameModel) {
        this.gameModel = gameModel;
        for (javax.sound.sampled.Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().contains("Java Sound Audio Engine")) {
                mixer = info;
                break;
            }
        }
        gameModel.addGameModelListener(this);

        playMusic();
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
                    16, // sample size in bits
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


        //System.out.println("playing " + filename + " at volume " + volume);
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
                //System.out.println("Active: " + clip.isActive());
            }
        }
    }

    public void playSound(String filename, float volume) {
        playSound(filename, volume, false);
    }

    private String getRandomIndex(String[] data) {
        return data[(int) (Math.random() * data.length)];
    }

    private void playMusic() {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(new File(soundLocation + getRandomIndex(music)));

            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED, // Encoding to use
                    baseFormat.getSampleRate(), // sample rate (same as base format)
                    16, // sample size in bits
                    baseFormat.getChannels(), // # of Channels
                    baseFormat.getChannels() * 2, // Frame Size
                    baseFormat.getSampleRate(), // Frame Rate
                    false // Big Endian
                    );

            audioInputStream = AudioSystem.getAudioInputStream(decodedFormat, in);

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, decodedFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(decodedFormat);
            sourceDataLine.start();

//Create a thread to play back
// the data and start it
// running. It will run until
// all the data has been played
// back.
            Thread playThread = new Thread(new PlayThread());
            playThread.start();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    @Override
    public void onBlockCreation(Block block) {
    }

    @Override
    public void onWinningConditionFulfilled(WinningCondition condition) {
    }

    @Override
    public void onLeaderChanged(Map scoreList) {
    }

    class PlayThread extends Thread {

        byte tempBuffer[] = new byte[10000];

        public void run() {
            try {
                int cnt;
//Keep looping until the input
// read method returns -1 for
// empty stream.
                while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
//Write data to the internal
// buffer of the data line
// where it will be delivered
// to the speaker.
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }
                }
//Block and wait for internal
// buffer of the data line to
// empty.
                sourceDataLine.drain();
                sourceDataLine.close();

                playMusic();
            } catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }

    @Override
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed) {
        //System.out.println("collision: " + collisionSpeed);
        if (collisionSpeed > 1.0) {
            if (collisionSpeed > 10) {
                playSound(getRandomIndex(collisionHardSounds), (collisionSpeed - 10) / 8);
            } else {
                playSound(getRandomIndex(collisionSounds), collisionSpeed / 4);
            }
        }
    }

    @Override
    public void onJointCreation(BuildingBlockJoint blockJoint) {
        playSound(select, 1);
    }

    @Override
    public void onBlockDestruction(Block block) {
        playSound(zap, 1);
    }

    @Override
    public void onJointDestruction(BuildingBlockJoint blockJoint) {
        playSound(deselect, .7f);
    }

    @Override
    public void onBuildingBlockOwnerChanged(BuildingBlock block) {
        
    }
}
