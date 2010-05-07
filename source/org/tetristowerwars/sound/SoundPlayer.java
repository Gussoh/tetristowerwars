/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.sound;

import java.io.ByteArrayOutputStream;
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
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.BulletBlock;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.GameModelListener;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.WinningCondition;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class SoundPlayer implements GameModelListener {

    private final Map<String, List<Clip>> clipDB = new HashMap<String, List<Clip>>();
    private final Map<String, AudioData> audioDataDB = new HashMap<String, AudioData>();
    private String soundLocation = "res/sound/";
    private String musicLocation = "res/sound/";
    private Mixer.Info mixer = null;
    private static final int MAX_CLIPS = 4;
    private static final String[] collisionSounds = new String[]{"collision1.wav"};
    private static final String[] collisionHardSounds = new String[]{"collisionHard1.wav", "collisionHard2.wav", "collisionHard3.wav", "collisionHard4.wav", "collisionHard4.wav"};
    private static final String[] explosionSounds = new String[]{"explosion.mp3"};
    private static final String[] cannonFireSounds = new String[]{"cannonfire.mp3"};
    private static final String[] music = new String[]{"music1.mp3", "music2.mp3", "music3.mp3", "music4.mp3", "music5.mp3"};
    private static final String[] winningMusic = new String[]{"soviet.mp3", "usa.mp3"};
    private static final String select = "select.wav";
    private static final String deselect = "deselect.wav";
    private static final String ownerChanged = "ownerchanged.mp3";
    private static final String zap = "zap.wav";
    private static final String beep = "beep.wav";
    PlayThread playThread;
    private boolean triggerWin = true;
    private boolean musicEnabled;
    private boolean soundEffectsEnabled;

    public SoundPlayer(boolean musicEnabled, boolean soundEffectsEnabled, int themeIndex) {
        this.musicEnabled = musicEnabled;
        this.soundEffectsEnabled = soundEffectsEnabled;
        musicLocation = musicLocation.concat("THEME" + themeIndex + "/");
        System.out.println("soundlocation: " + soundLocation);
        for (javax.sound.sampled.Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().contains("Java Sound Audio Engine")) {
                mixer = info;
                break;
            }
        }
        if (soundEffectsEnabled) {
            System.out.println("- Preloading sounds");
            getFirstAvailable(select);
            getFirstAvailable(deselect);
            getFirstAvailable(zap);
            getFirstAvailable(ownerChanged);
            for (int i = 0; i < collisionSounds.length; i++) {
                getFirstAvailable(collisionSounds[i]);
            }
            for (int i = 0; i < collisionHardSounds.length; i++) {
                getFirstAvailable(collisionHardSounds[i]);
            }
            for (int i = 0; i < cannonFireSounds.length; i++) {
                getFirstAvailable(cannonFireSounds[i]);
            }
            for (int i = 0; i < explosionSounds.length; i++) {
                getFirstAvailable(explosionSounds[i]);
            }
        }
        if (musicEnabled) {
            playMusic(new File(musicLocation + getRandomIndex(music)));
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

            AudioData audioData = audioDataDB.get(filename);

            if (audioData == null) {

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
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] data = new byte[1024];
                int numBytes = 0;
                for (;;) {
                    numBytes = decodedIn.read(data);
                    if (numBytes == -1) {
                        break;
                    } else {
                        baos.write(data, 0, numBytes);
                    }
                }

                decodedIn.close();
                in.close();

                audioData = new AudioData(baos.toByteArray(), decodedFormat);
                audioDataDB.put(filename, audioData);
            }

            clip.open(audioData.audioFormat, audioData.data, 0, audioData.data.length);

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
        if (soundEffectsEnabled) {
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
                        float value = MathUtil.lerp(volume, 0.0f, 1.0f, MathUtil.lerpNoCap(0.5f, gainControl.getMinimum(), gainControl.getMaximum()), MathUtil.lerpNoCap(0.9f, gainControl.getMinimum(), gainControl.getMaximum()));

                        //System.out.println("Found gain control: " + gainControl);
                        gainControl.setValue(value);
                    }

                    clip.loop(repeat ? Clip.LOOP_CONTINUOUSLY : 0);
                    //System.out.println("Active: " + clip.isActive());
                }
            }
        }
    }

    public void playSound(String filename, float volume) {
        playSound(filename, volume, false);
    }

    private String getRandomIndex(String[] data) {
        return data[(int) (Math.random() * data.length)];
    }

    private synchronized void playMusic(File musicFile) {
        if (musicEnabled) {
            AudioInputStream in;
            try {
                in = AudioSystem.getAudioInputStream(musicFile);
            } catch (UnsupportedAudioFileException ex) {
                System.out.println("Sound file format not supported!");
                return;
            } catch (IOException ex) {
                System.out.println("Error when reading file!");
                return;
            }

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

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(decodedFormat, in);

            if (playThread != null) {
                playThread.stopMusic();
            }
            playThread = new PlayThread(audioInputStream);
            playThread.start();
        }
    }

    public synchronized void stopAllMusic() {
        if (playThread != null) {
            playThread.stopMusic();
        }
    }

    public void unloadAllSounds() {
        for (List<Clip> clipList : clipDB.values()) {
            for (Clip clip : clipList) {
                clip.close();
            }
        }
    }

    @Override
    public void onBlockCreation(Block block) {
        if (block instanceof BulletBlock) {
            playSound(getRandomIndex(cannonFireSounds), 1.0f);
        }
    }

    @Override
    public void onWinningConditionFulfilled(WinningCondition condition) {
        if (triggerWin) {
            triggerWin = false;
            System.out.println("playing winningsound!");
            playMusic(new File(musicLocation + winningMusic[condition.getLeader().getPlayer().getPlayerIndex() - 1]));
        }
    }

    @Override
    public void onGameReset() {
        System.out.println("sound reset");
        playMusic(new File(musicLocation + getRandomIndex(music)));
        triggerWin = true;
    }

    @Override
    public void onLeaderChanged(Player leader) {
    }

    @Override
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed, Vec2 contactPoint) {
        //System.out.println("collision: " + collisionSpeed);
        if (collisionSpeed > 1.0) {
            if (collisionSpeed > 10) {
                playSound(getRandomIndex(collisionHardSounds), MathUtil.lerp((collisionSpeed - 10.0f) / 8.0f, 0.0f, 1.0f, 0.0f, 0.9f));
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
        if (block instanceof BulletBlock) {
            playSound(getRandomIndex(explosionSounds), 1.0f);
        } else {
            playSound(zap, .6f);
        }
    }

    @Override
    public void onJointDestruction(BuildingBlockJoint blockJoint) {
        playSound(deselect, 1.0f);
    }

    @Override
    public void onBuildingBlockOwnerChanged(BuildingBlock block) {
        playSound(ownerChanged, 0.95f);
    }

    @Override
    public void onTimerBeat(int beatsLeft) {
        System.out.println("BEAT");
        playSound(beep, 0.5f);
    }

    @Override
    public void onPowerupHoverChanged(BuildingBlock hoveredBlock) {
        
    }



    private class AudioData {

        private final byte[] data;
        private final AudioFormat audioFormat;

        public AudioData(byte[] data, AudioFormat audioFormat) {
            this.data = data;
            this.audioFormat = audioFormat;
        }
    }

    class PlayThread extends Thread {

        private byte tempBuffer[] = new byte[5000];
        private boolean keepPlaying = true;
        private final AudioInputStream audioInputStream;
        private SourceDataLine sourceDataLine = null;

        public PlayThread(AudioInputStream audioInputStream) {
            this.audioInputStream = audioInputStream;
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioInputStream.getFormat());
            try {
                sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceDataLine.open(audioInputStream.getFormat());
                sourceDataLine.start();
            } catch (LineUnavailableException ex) {
                System.out.println("Sound card not available!");
            }
        }

        public void stopMusic() {
            keepPlaying = false;
        }

        @Override
        public void run() {
            try {
                int cnt;
                //Keep looping until the input read method returns -1 for empty stream.
                while (keepPlaying && (cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        //Write data to the internal buffer of the data line
                        //where it will be delivered to the speaker.
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }
                }
                //Block and wait for internal buffer of the data line to empty.
                sourceDataLine.drain();
                sourceDataLine.close();

                if (keepPlaying) {
                    System.out.println("playing next song");
                    playMusic(new File(musicLocation + getRandomIndex(music)));
                }

            } catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }
}
