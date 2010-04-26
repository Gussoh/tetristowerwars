/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StartPanel.java
 *
 * Created on 2010-apr-26, 10:19:34
 */
package org.tetristowerwars;

/**
 *
 * @author Andreas
 */
public class StartPanel extends javax.swing.JPanel {

    private final MainFrame mainFrame;

    /** Creates new form StartPanel
     * @param mainFrame 
     */
    public StartPanel(MainFrame mainFrame) {
        initComponents();
        this.mainFrame = mainFrame;


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        touchButton = new javax.swing.JButton();
        networkButton = new javax.swing.JButton();
        emulatorCheckBox = new javax.swing.JCheckBox();

        touchButton.setText("Play using a multi-touch screen");
        touchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                touchButtonActionPerformed(evt);
            }
        });

        networkButton.setText("Play network game");

        emulatorCheckBox.setText("Enable TUIO mouse emulation");
        emulatorCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emulatorCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emulatorCheckBox)
                    .addComponent(touchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addComponent(networkButton, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(touchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(networkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(emulatorCheckBox)
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void touchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_touchButtonActionPerformed
        mainFrame.openComponent(new TouchGamePanel(mainFrame));
    }//GEN-LAST:event_touchButtonActionPerformed

    private void emulatorCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emulatorCheckBoxActionPerformed
        if (emulatorCheckBox.isSelected()) {
            mainFrame.enableMouseEmulation();
            mainFrame.getSettings().setProperty(Settings.KEY_MOUSE_EMULATION, "true");
        } else {
            mainFrame.disableMouseEmulation();
            mainFrame.getSettings().setProperty(Settings.KEY_MOUSE_EMULATION, "false");
        }
    }//GEN-LAST:event_emulatorCheckBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox emulatorCheckBox;
    private javax.swing.JButton networkButton;
    private javax.swing.JButton touchButton;
    // End of variables declaration//GEN-END:variables
}