/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StartPanel.java
 *
 * Created on 2010-apr-25, 11:36:01
 */
package org.tetristowerwars;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Andreas
 */
public class TouchGamePanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;

    /** Creates new form StartPanel
     * @param mainFrame 
     */
    public TouchGamePanel(MainFrame mainFrame) {
        initComponents();
        this.mainFrame = mainFrame;
        Settings settings = mainFrame.getSettings();

        leftTeamName.setText(settings.getLeftTeamName());
        rightTeamName.setText(settings.getRightTeamName());

        heightTextField.setText(Integer.toString(settings.getHeightCondition()));
        numBlocksTextField.setText(Integer.toString(settings.getNumBlocksCondition()));
        timeTextField.setText(Integer.toString(settings.getTimeCondition()));

        heightConditionCheckBox.setSelected(settings.isHeightConditionEnabled());
        numBlocksCheckBox.setSelected(settings.isNumBlocksConditionEnabled());
        timeCheckBox.setSelected(settings.isTimeConditionEnabled());

        if (settings.mustAllWinningConditionsBeMet()) {
            anyAllComboBox.setSelectedIndex(1);
        } else {
            anyAllComboBox.setSelectedIndex(0);
        }

        updateSlider(heightSlider, heightTextField);
        updateSlider(numBlocksSlider, numBlocksTextField);
        updateSlider(timeSlider, timeTextField);

        heightTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSlider(heightSlider, heightTextField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSlider(heightSlider, heightTextField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        numBlocksTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSlider(numBlocksSlider, numBlocksTextField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSlider(numBlocksSlider, numBlocksTextField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        timeTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSlider(timeSlider, timeTextField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSlider(timeSlider, timeTextField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void updateSlider(JSlider slider, JTextField field) {
        try {
            slider.setValue((int) Integer.parseInt(field.getText()));
            field.setBackground(UIManager.getColor("TextField.background"));
        } catch (NumberFormatException ex) {
            field.setBackground(Color.red.brighter().brighter());
        }
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
        leftTeamName = new javax.swing.JTextField();
        rightTeamName = new javax.swing.JTextField();
        playButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        heightConditionCheckBox = new javax.swing.JCheckBox();
        numBlocksCheckBox = new javax.swing.JCheckBox();
        timeCheckBox = new javax.swing.JCheckBox();
        timeTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        anyAllComboBox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        heightSlider = new javax.swing.JSlider();
        numBlocksSlider = new javax.swing.JSlider();
        timeSlider = new javax.swing.JSlider();
        heightTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        numBlocksTextField = new javax.swing.JTextField();
        settingsButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Team names"));

        jLabel1.setText("Left team:");

        jLabel2.setText("Right team:");

        leftTeamName.setText("Left team");

        rightTeamName.setText("Right team");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rightTeamName)
                    .addComponent(leftTeamName, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(leftTeamName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(rightTeamName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        playButton.setText("Play!");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Winning conditions"));

        heightConditionCheckBox.setSelected(true);
        heightConditionCheckBox.setText("A tower is higher than");

        numBlocksCheckBox.setText("Number of blocks owned by a player is");

        timeCheckBox.setSelected(true);
        timeCheckBox.setText("Play time reaches");

        timeTextField.setText("600");

        jLabel4.setText("seconds.");

        anyAllComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Any condition", "All conditions" }));

        jLabel6.setText("must be fulfilled.");

        heightSlider.setMajorTickSpacing(50);
        heightSlider.setMaximum(200);
        heightSlider.setMinorTickSpacing(10);
        heightSlider.setPaintLabels(true);
        heightSlider.setPaintTicks(true);
        heightSlider.setSnapToTicks(true);
        heightSlider.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        heightSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                heightSliderStateChanged(evt);
            }
        });

        numBlocksSlider.setMajorTickSpacing(25);
        numBlocksSlider.setMinorTickSpacing(5);
        numBlocksSlider.setPaintLabels(true);
        numBlocksSlider.setPaintTicks(true);
        numBlocksSlider.setSnapToTicks(true);
        numBlocksSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numBlocksSliderStateChanged(evt);
            }
        });

        timeSlider.setMajorTickSpacing(200);
        timeSlider.setMaximum(600);
        timeSlider.setMinorTickSpacing(50);
        timeSlider.setPaintLabels(true);
        timeSlider.setPaintTicks(true);
        timeSlider.setSnapToTicks(true);
        timeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeSliderStateChanged(evt);
            }
        });

        jLabel3.setText("meters.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(anyAllComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(heightConditionCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(timeCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(numBlocksCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numBlocksTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(heightSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(numBlocksSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(timeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(heightSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numBlocksSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(timeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(heightConditionCheckBox)
                            .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(numBlocksCheckBox)
                            .addComponent(numBlocksTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(timeCheckBox)
                            .addComponent(timeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(anyAllComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addContainerGap())
        );

        settingsButton.setText("Change settings...");
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                        .addComponent(settingsButton)
                        .addGap(18, 18, 18)
                        .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(playButton, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                        .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        mainFrame.openComponent(new SettingsPanel(mainFrame));
    }//GEN-LAST:event_settingsButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        mainFrame.back();
    }//GEN-LAST:event_backButtonActionPerformed

    private void heightSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_heightSliderStateChanged
        JSlider slider = (JSlider) evt.getSource();
        if (slider.hasFocus()) {
            heightTextField.setText(Integer.toString(slider.getValue()));
        }
    }//GEN-LAST:event_heightSliderStateChanged

    private void numBlocksSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numBlocksSliderStateChanged
        JSlider slider = (JSlider) evt.getSource();
        if (slider.hasFocus()) {
            numBlocksTextField.setText(Integer.toString(slider.getValue()));
        }
    }//GEN-LAST:event_numBlocksSliderStateChanged

    private void timeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timeSliderStateChanged
        JSlider slider = (JSlider) evt.getSource();
        if (slider.hasFocus()) {
            timeTextField.setText(Integer.toString(slider.getValue()));
        }
    }//GEN-LAST:event_timeSliderStateChanged

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed

        Settings settings = mainFrame.getSettings();

        settings.setProperty(Settings.KEY_LEFT_TEAM, leftTeamName.getText());
        settings.setProperty(Settings.KEY_RIGHT_TEAM, rightTeamName.getText());

        settings.setProperty(Settings.KEY_USE_HEIGHT_CONDITION, Boolean.toString(heightConditionCheckBox.isSelected()));
        settings.setProperty(Settings.KEY_USE_NUM_BLOCKS_CONDITION, Boolean.toString(numBlocksCheckBox.isSelected()));
        settings.setProperty(Settings.KEY_USE_TIME_CONDITION, Boolean.toString(timeCheckBox.isSelected()));

        settings.setProperty(Settings.KEY_HEIGHT_CONDITION, heightTextField.getText());
        settings.setProperty(Settings.KEY_NUM_BLOCKS_CONDITION, numBlocksTextField.getText());
        settings.setProperty(Settings.KEY_TIME_CONDITION, timeTextField.getText());

        
        if (anyAllComboBox.getSelectedIndex() == 0) {
            settings.setProperty(Settings.KEY_MUST_ALL_WINNING_CONDITIONS_BE_MET, "false");
        } else {
            settings.setProperty(Settings.KEY_MUST_ALL_WINNING_CONDITIONS_BE_MET, "true");
        }

        try {
            settings.save();
        } catch (IOException ex) {
            Logger.getLogger(TouchGamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        new TouchGameLogic(mainFrame);
    }//GEN-LAST:event_playButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox anyAllComboBox;
    private javax.swing.JButton backButton;
    private javax.swing.JCheckBox heightConditionCheckBox;
    private javax.swing.JSlider heightSlider;
    private javax.swing.JTextField heightTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField leftTeamName;
    private javax.swing.JCheckBox numBlocksCheckBox;
    private javax.swing.JSlider numBlocksSlider;
    private javax.swing.JTextField numBlocksTextField;
    private javax.swing.JButton playButton;
    private javax.swing.JTextField rightTeamName;
    private javax.swing.JButton settingsButton;
    private javax.swing.JCheckBox timeCheckBox;
    private javax.swing.JSlider timeSlider;
    private javax.swing.JTextField timeTextField;
    // End of variables declaration//GEN-END:variables
}
