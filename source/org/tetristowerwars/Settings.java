/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 *
 * @author Andreas
 */
public class Settings {

    public static final String SETTINGS_FILE = "tetristowerwars.ini";
    public static final String KEY_BUILDING_BLOCK_SIZE = "building_block_size";
    public static final String KEY_GRAVITY = "gravity";
    public static final String KEY_WORLD_WIDTH = "world_width";
    public static final String KEY_WORLD_HEIGHT = "world_height";
    public static final String KEY_PLAYER_AREA = "player_area";
    public static final String KEY_PLAY_MUSIC = "play_music";
    public static final String KEY_PLAY_SOUND_EFFECTS = "play_sound_effects";
    public static final String KEY_WINDOW_WIDTH = "window_width";
    public static final String KEY_WINDOW_HEIGHT = "window_height";
    public static final String KEY_LIGHTING_EFFECTS = "lighting_effects";
    public static final String KEY_PARTICLE_EFFECTS = "particle_effects";
    public static final String KEY_GROUND_HEIGHT = "ground_height";
    public static final String KEY_USE_HEIGHT_CONDITION = "use_height_condition";
    public static final String KEY_USE_NUM_BLOCKS_CONDITION = "use_num_blocks_condition";
    public static final String KEY_USE_TIME_CONDITION = "use_time_condition";
    public static final String KEY_HEIGHT_CONDITION = "height_condition";
    public static final String KEY_NUM_BLOCKS_CONDITION = "num_blocks_condition";
    public static final String KEY_TIME_CONDITION = "time_condition";
    public static final String KEY_LEFT_TEAM = "left_team";
    public static final String KEY_RIGHT_TEAM = "right_team";
    public static final String KEY_MUST_ALL_WINNING_CONDITIONS_BE_MET = "must_all_winning_conditions_be_met";
    public static final String KEY_MOUSE_EMULATION = "mouse_emulation";

    private final Properties properties = new Properties();

    public void load() throws IOException {
        File settingsFile = new File(SETTINGS_FILE);

        if (settingsFile.isFile() && settingsFile.canRead()) {

            System.out.println("Loading properties from file: " + settingsFile.getCanonicalPath());
            InputStream inputStream = new FileInputStream(settingsFile);
            properties.load(inputStream);
            inputStream.close();
        }
    }

    public void save() throws IOException {
        File file = new File(SETTINGS_FILE);
        System.out.println("Writing properties to file " + file.getCanonicalPath());
        FileOutputStream fos = new FileOutputStream(file);
        properties.store(fos, "Settings file for Tetris Tower Wars");
        fos.close();
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public float getBlockSize() {
        return getFloatProperty(KEY_BUILDING_BLOCK_SIZE, 5);
    }

    public float getGravity() {
        return getFloatProperty(KEY_GRAVITY, 9.82f);
    }

    public float getWorldWidth() {
        return getFloatProperty(KEY_WORLD_WIDTH, 300);
    }

    public float getWorldHeight() {
        return getFloatProperty(KEY_WORLD_HEIGHT, 600);
    }

    public float getPlayerArea() {
        return getFloatProperty(KEY_PLAYER_AREA, 70);
    }

    public float getGroundHeight() {
        return getFloatProperty(KEY_GROUND_HEIGHT, 30);
    }

    public boolean isPlayMusicEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_PLAY_MUSIC, "true"));
    }

    public boolean isPlaySoundEffectsEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_PLAY_SOUND_EFFECTS, "true"));
    }

    public int getWindowWidth() {
        return getIntProperty(KEY_WINDOW_WIDTH, 1024);
    }

    public int getWindowHeight() {
        return getIntProperty(KEY_WINDOW_HEIGHT, 768);
    }

    public boolean isLightingEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_LIGHTING_EFFECTS, "true"));
    }

    public boolean isParticlesEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_PARTICLE_EFFECTS, "true"));
    }

    public boolean isHeightConditionEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_USE_HEIGHT_CONDITION, "true"));
    }

    public boolean isNumBlocksConditionEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_USE_NUM_BLOCKS_CONDITION, "false"));
    }

    public boolean isTimeConditionEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_USE_TIME_CONDITION, "true"));
    }

    public int getHeightCondition() {
        return getIntProperty(KEY_HEIGHT_CONDITION, 80);
    }

    public int getNumBlocksCondition() {
        return getIntProperty(KEY_NUM_BLOCKS_CONDITION, 40);
    }

    public int getTimeCondition() {
        return getIntProperty(KEY_TIME_CONDITION, 300);
    }

    public String getLeftTeamName() {
        return getStringProperty(KEY_LEFT_TEAM, "Left Team");
    }

    public String getRightTeamName() {
        return getStringProperty(KEY_RIGHT_TEAM, "Right Team");
    }

    public boolean mustAllWinningConditionsBeMet() {
        return Boolean.parseBoolean(properties.getProperty(KEY_MUST_ALL_WINNING_CONDITIONS_BE_MET, "false"));
    }

    public boolean isMouseEmulationEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_MOUSE_EMULATION));
    }

    private float getFloatProperty(String key, float defaultValue) {
        try {
            String value = properties.getProperty(key);
            if (value == null) {
                properties.setProperty(key, Float.toString(defaultValue));
                return defaultValue;
            }
            return Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            properties.setProperty(key, Float.toString(defaultValue));
            return defaultValue;
        }
    }

    private int getIntProperty(String key, int defaultValue) {
        try {
            String value = properties.getProperty(key);
            if (value == null) {
                properties.setProperty(key, Integer.toString(defaultValue));
                return defaultValue;
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            properties.setProperty(key, Integer.toString(defaultValue));
            return defaultValue;
        }
    }

    private String getStringProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);

        if (value != null && value.length() > 0) {
            return value;
        } else {
            properties.setProperty(key, defaultValue);
            return defaultValue;
        }
    }

}
