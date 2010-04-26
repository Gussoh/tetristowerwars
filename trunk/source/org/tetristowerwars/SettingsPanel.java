/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SettingsPanel.java
 *
 * Created on 2010-apr-25, 22:51:13
 */
package org.tetristowerwars;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *
 * @author Andreas
 */
public class SettingsPanel extends javax.swing.JPanel {

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
    private final Properties properties;
    private final StartFrame startFrame;

    /** Creates new form SettingsPanel */
    public SettingsPanel(StartFrame startFrame) {
        initComponents();

        File settingsFile = new File(SETTINGS_FILE);
        properties = new Properties();

        if (settingsFile.isFile() && settingsFile.canRead()) {
            try {
                System.out.println("Loading properties from file " + settingsFile.getCanonicalPath());
                InputStream inputStream = new FileInputStream(settingsFile);
                properties.load(inputStream);
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(SettingsPanel.class.getName()).log(Level.SEVERE, null, ex);
                statusLabel.setText("Unable to load settings file: " + SETTINGS_FILE);
            }
        }


        buildingBlockSizeField.setText(properties.getProperty(KEY_BUILDING_BLOCK_SIZE, "5"));
        gravityField.setText(properties.getProperty(KEY_GRAVITY, "9.82"));
        worldWidthField.setText(properties.getProperty(KEY_WORLD_WIDTH, "300"));
        worldHeightField.setText(properties.getProperty(KEY_WORLD_HEIGHT, "600"));
        playerAreaField.setText(properties.getProperty(KEY_PLAYER_AREA, "70"));
        musicCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(KEY_PLAY_MUSIC, "true")));
        soundCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(KEY_PLAY_SOUND_EFFECTS, "true")));
        windowWidthField.setText(properties.getProperty(KEY_WINDOW_WIDTH, "1024"));
        windowHeightField.setText(properties.getProperty(KEY_WINDOW_HEIGHT, "768"));
        lightingCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(KEY_LIGHTING_EFFECTS, "true")));
        particleCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(KEY_PARTICLE_EFFECTS, "true")));
        this.startFrame = startFrame;
    }

    private boolean checkIntField(JTextField textField, int min, int max, JLabel statusField) {

        try {
            int value = Integer.parseInt(textField.getText());
            if (value < min) {
                statusField.setText("Must be larger than " + min);
            } else if (value > max) {
                statusField.setText("Must be smaller than " + max);
            } else {
                statusField.setBackground(UIManager.getColor("Textfield.background"));
                return true;
            }
        } catch (NumberFormatException e) {
            statusField.setText("Must be an integer");
        }

        textField.setBackground(Color.red.brighter());
        return false;
    }

    private boolean checkFloatField(JTextField textField, float min, float max, JLabel statusField) {
        try {
            float value = Float.parseFloat(textField.getText());
            if (value < min) {
                statusField.setText("Must be larger than " + min);
            } else if (value > max) {
                statusField.setText("Must be smaller than " + max);
            } else {
                statusField.setBackground(UIManager.getColor("Textfield.background"));
                return true;
            }
        } catch (Exception e) {
            statusField.setText("Must be a real number");
        }

        textField.setBackground(Color.red.brighter());
        return false;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        buildingBlockSizeField = new javax.swing.JTextField();
        gravityField = new javax.swing.JTextField();
        worldWidthField = new javax.swing.JTextField();
        worldHeightField = new javax.swing.JTextField();
        playerAreaField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        musicCheckBox = new javax.swing.JCheckBox();
        soundCheckBox = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        windowWidthField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        windowHeightField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        lightingCheckBox = new javax.swing.JCheckBox();
        particleCheckBox = new javax.swing.JCheckBox();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Options"));

        jLabel1.setText("Building block size (m):");

        jLabel2.setText("Gravity (m/s^2):");

        jLabel3.setText("World width (m):");

        jLabel4.setText("World height (m):");

        jLabel5.setText("Player area (%):");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gravityField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(buildingBlockSizeField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(worldWidthField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(worldHeightField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(playerAreaField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(buildingBlockSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(gravityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(worldWidthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(worldHeightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(playerAreaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Sound Options"));

        musicCheckBox.setText("Play music");

        soundCheckBox.setText("Play sound effects");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(musicCheckBox)
                    .addComponent(soundCheckBox))
                .addContainerGap(247, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(musicCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(soundCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Graphics Options"));

        jLabel6.setText("Window dimensions:");

        jLabel7.setText("x");

        jLabel8.setText("pixels.");

        lightingCheckBox.setText("Enable lighting effects");

        particleCheckBox.setText("Enable particle effects");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(particleCheckBox)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(windowWidthField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(windowHeightField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addComponent(lightingCheckBox))
                .addGap(133, 133, 133))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(windowWidthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(windowHeightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lightingCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(particleCheckBox))
        );

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        statusLabel.setForeground(java.awt.Color.red);
        statusLabel.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(saveButton)
                    .addComponent(statusLabel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // Validate all fields
        if (!checkFloatField(buildingBlockSizeField, 0.1f, Float.MAX_VALUE, statusLabel)) {
            return;
        }

        if (!checkFloatField(gravityField, Float.MIN_VALUE, Float.MAX_VALUE, statusLabel)) {
            return;
        }

        if (!checkFloatField(worldWidthField, 1, Float.MAX_VALUE, statusLabel)) {
            return;
        }

        if (!checkFloatField(worldHeightField, 1, Float.MAX_VALUE, statusLabel)) {
            return;
        }

        if (!checkFloatField(playerAreaField, 0.1f, 99.9f, statusLabel)) {
            return;
        }

        if (!checkIntField(windowWidthField, 1, Integer.MAX_VALUE, statusLabel)) {
            return;
        }

        if (!checkIntField(windowHeightField, 1, Integer.MAX_VALUE, statusLabel)) {
            return;
        }


        // If correct, save to properties, write to file, then dispose
        properties.setProperty(KEY_BUILDING_BLOCK_SIZE, buildingBlockSizeField.getText());
        properties.setProperty(KEY_GRAVITY, gravityField.getText());
        properties.setProperty(KEY_WORLD_WIDTH, worldWidthField.getText());
        properties.setProperty(KEY_WORLD_HEIGHT, worldHeightField.getText());
        properties.setProperty(KEY_PLAYER_AREA, playerAreaField.getText());

        properties.setProperty(KEY_PLAY_MUSIC, Boolean.toString(musicCheckBox.isSelected()));
        properties.setProperty(KEY_PLAY_SOUND_EFFECTS, Boolean.toString(soundCheckBox.isSelected()));

        properties.setProperty(KEY_WINDOW_WIDTH, windowWidthField.getText());
        properties.setProperty(KEY_WINDOW_HEIGHT, windowHeightField.getText());
        properties.setProperty(KEY_LIGHTING_EFFECTS, Boolean.toString(lightingCheckBox.isSelected()));
        properties.setProperty(KEY_PARTICLE_EFFECTS, Boolean.toString(particleCheckBox.isSelected()));
        try {
            File file = new File(SETTINGS_FILE);
            System.out.println("Writing properties to file " + file.getCanonicalPath());
            FileOutputStream fos = new FileOutputStream(file);
            properties.store(fos, "Settings file for Tetris Tower Wars");
            fos.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Unable to save settings to " + SETTINGS_FILE + "\nReason: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        startFrame.currentPanelDone();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        startFrame.currentPanelDone();
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField buildingBlockSizeField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField gravityField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JCheckBox lightingCheckBox;
    private javax.swing.JCheckBox musicCheckBox;
    private javax.swing.JCheckBox particleCheckBox;
    private javax.swing.JTextField playerAreaField;
    private javax.swing.JButton saveButton;
    private javax.swing.JCheckBox soundCheckBox;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField windowHeightField;
    private javax.swing.JTextField windowWidthField;
    private javax.swing.JTextField worldHeightField;
    private javax.swing.JTextField worldWidthField;
    // End of variables declaration//GEN-END:variables
}
