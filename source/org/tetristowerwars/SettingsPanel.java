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
import java.io.IOException;
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

    
    private final MainFrame mainFrame;
    private final Settings settings;

    /** Creates new form SettingsPanel */
    public SettingsPanel(MainFrame mainFrame) {
        initComponents();
        settings = mainFrame.getSettings();

        buildingBlockSizeField.setText(Float.toString(settings.getBlockSize()));
        gravityField.setText(Float.toString(settings.getGravity()));
        worldWidthField.setText(Float.toString(settings.getWorldWidth()));
        worldHeightField.setText(Float.toString(settings.getWorldHeight()));
        playerAreaField.setText(Float.toString(settings.getPlayerArea()));
        groundHeightField.setText(Float.toString(settings.getGroundHeight()));
        powerupCheckBox.setSelected(settings.isPowerups());
        secondsBetweenPowerupsSlider.setValue(settings.getSecondsBetweenPowerups());

        musicCheckBox.setSelected(settings.isPlayMusicEnabled());
        soundCheckBox.setSelected(settings.isPlaySoundEffectsEnabled());
        
        windowWidthField.setText(Integer.toString(settings.getWindowWidth()));
        windowHeightField.setText(Integer.toString(settings.getWindowHeight()));
        lightingCheckBox.setSelected(settings.isLightingEnabled());
        particleCheckBox.setSelected(settings.isParticlesEnabled());
        fullscreenCheckBox.setSelected(settings.isFullscreen());
        antiAliasingCheckBox.setSelected(settings.isAntiAliasingEnabled());
        
        this.mainFrame = mainFrame;
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
        groundHeightField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        powerupCheckBox = new javax.swing.JCheckBox();
        secondsBetweenPowerupsSlider = new javax.swing.JSlider();
        jLabel10 = new javax.swing.JLabel();
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
        fullscreenCheckBox = new javax.swing.JCheckBox();
        antiAliasingCheckBox = new javax.swing.JCheckBox();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Advanced Game Options"));

        jLabel1.setText("Building block size (m):");

        jLabel2.setText("Gravity (m/s^2):");

        jLabel3.setText("World width (m):");

        jLabel4.setText("World height (m):");

        jLabel5.setText("Player area (%):");

        jLabel9.setText("Ground height (m):");

        powerupCheckBox.setText("Powerups");

        secondsBetweenPowerupsSlider.setMajorTickSpacing(30);
        secondsBetweenPowerupsSlider.setMaximum(180);
        secondsBetweenPowerupsSlider.setMinorTickSpacing(10);
        secondsBetweenPowerupsSlider.setPaintLabels(true);
        secondsBetweenPowerupsSlider.setPaintTicks(true);
        secondsBetweenPowerupsSlider.setValue(30);
        secondsBetweenPowerupsSlider.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel10.setText("Seconds between:");

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
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(powerupCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gravityField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(buildingBlockSizeField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(worldWidthField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(worldHeightField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(playerAreaField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(groundHeightField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(secondsBetweenPowerupsSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groundHeightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(powerupCheckBox)
                        .addGap(7, 7, 7)
                        .addComponent(jLabel10)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(secondsBetweenPowerupsSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Sound Options"));

        musicCheckBox.setText("Music");

        soundCheckBox.setText("Sound effects");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(musicCheckBox)
                    .addComponent(soundCheckBox))
                .addContainerGap(232, Short.MAX_VALUE))
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

        jLabel6.setText("Window size:");

        jLabel7.setText("x");

        jLabel8.setText("pixels.");

        lightingCheckBox.setText("Lighting effects");

        particleCheckBox.setText("Particle effects");

        fullscreenCheckBox.setText("Fullscreen");

        antiAliasingCheckBox.setText("Anti-aliasing");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(antiAliasingCheckBox)
                    .addComponent(fullscreenCheckBox)
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
                .addGap(130, 130, 130))
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
                .addComponent(particleCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fullscreenCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, Short.MAX_VALUE)
                .addComponent(antiAliasingCheckBox))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                        .addGap(194, 194, 194)
                        .addComponent(saveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(cancelButton)
                    .addComponent(saveButton))
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

        if (!checkFloatField(groundHeightField, 1, Float.MAX_VALUE, statusLabel)) {
            return;
        }

        // If correct, save to settings
        settings.setProperty(Settings.KEY_BUILDING_BLOCK_SIZE, buildingBlockSizeField.getText());
        settings.setProperty(Settings.KEY_GRAVITY, gravityField.getText());
        settings.setProperty(Settings.KEY_WORLD_WIDTH, worldWidthField.getText());
        settings.setProperty(Settings.KEY_WORLD_HEIGHT, worldHeightField.getText());
        settings.setProperty(Settings.KEY_PLAYER_AREA, playerAreaField.getText());
        settings.setProperty(Settings.KEY_GROUND_HEIGHT, groundHeightField.getText());
        settings.setProperty(Settings.KEY_POWERUPS, Boolean.toString(powerupCheckBox.isSelected()));
        settings.setProperty(Settings.KEY_SECONDS_BETWEEN_POWERUPS, secondsBetweenPowerupsSlider.getValue() + "");

        settings.setProperty(Settings.KEY_PLAY_MUSIC, Boolean.toString(musicCheckBox.isSelected()));
        settings.setProperty(Settings.KEY_PLAY_SOUND_EFFECTS, Boolean.toString(soundCheckBox.isSelected()));

        settings.setProperty(Settings.KEY_WINDOW_WIDTH, windowWidthField.getText());
        settings.setProperty(Settings.KEY_WINDOW_HEIGHT, windowHeightField.getText());
        settings.setProperty(Settings.KEY_LIGHTING_EFFECTS, Boolean.toString(lightingCheckBox.isSelected()));
        settings.setProperty(Settings.KEY_PARTICLE_EFFECTS, Boolean.toString(particleCheckBox.isSelected()));
        settings.setProperty(Settings.KEY_FULLSCREEN, Boolean.toString(fullscreenCheckBox.isSelected()));
        settings.setProperty(Settings.KEY_ANTI_ALIASING, Boolean.toString(antiAliasingCheckBox.isSelected()));
        try {
            settings.save();
        } catch (IOException ex) {
            Logger.getLogger(SettingsPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Unable to save settings to file: " + Settings.SETTINGS_FILE + "\nReason: " + ex.getMessage());
        }

        mainFrame.back();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        mainFrame.back();
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox antiAliasingCheckBox;
    private javax.swing.JTextField buildingBlockSizeField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox fullscreenCheckBox;
    private javax.swing.JTextField gravityField;
    private javax.swing.JTextField groundHeightField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JCheckBox lightingCheckBox;
    private javax.swing.JCheckBox musicCheckBox;
    private javax.swing.JCheckBox particleCheckBox;
    private javax.swing.JTextField playerAreaField;
    private javax.swing.JCheckBox powerupCheckBox;
    private javax.swing.JButton saveButton;
    private javax.swing.JSlider secondsBetweenPowerupsSlider;
    private javax.swing.JCheckBox soundCheckBox;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField windowHeightField;
    private javax.swing.JTextField windowWidthField;
    private javax.swing.JTextField worldHeightField;
    private javax.swing.JTextField worldWidthField;
    // End of variables declaration//GEN-END:variables
}
