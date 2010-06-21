/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NetworkPanel.java
 *
 * Created on 2010-maj-24, 11:53:42
 */
package org.tetristowerwars;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.network.ClientEntry;
import org.tetristowerwars.network.NetworkClient;
import org.tetristowerwars.network.NetworkClientListener;
import org.tetristowerwars.network.NetworkServer;

/**
 *
 * @author Andreas
 */
public class NetworkPanel extends javax.swing.JPanel implements NetworkClientListener {

    private final MainFrame mainFrame;
    private NetworkLobby lobby = null;
    private final Color defaultTextColor;
    private NetworkServer networkServer = null;
    private NetworkClient networkClient = null;

    /** Creates new form NetworkPanel
     * @param mainFrame
     */
    public NetworkPanel(MainFrame mainFrame) {
        initComponents();
        this.mainFrame = mainFrame;

        Settings settings = mainFrame.getSettings();
        nameTextField.setText(settings.getPlayerName());
        hostnameTextField.setText(settings.getNetworkHostname());
        int port = settings.getNetworkPort();
        serverPortNoteLabel.setText("Note: Port " + port + " is used. You might need to adjust you firewall settings.");
        defaultTextColor = statusLabel.getForeground();


    }

    private void saveSettings() {
        String playerName = nameTextField.getText();
        String hostName = hostnameTextField.getText();

        Settings settings = mainFrame.getSettings();
        settings.setProperty(Settings.KEY_PLAYER_NAME, playerName);
        settings.setProperty(Settings.KEY_NETWORK_HOSTNAME, hostName);
        try {
            settings.save();
        } catch (IOException ex) {
            Logger.getLogger(NetworkPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void allClientsReady() {
    }

    @Override
    public void chatMessageReceive(ClientEntry clientEntry, String message) {
    }

    @Override
    public void clientConnected(ClientEntry clientEntry) {
        
    }

    @Override
    public void clientDisconnected(ClientEntry clientEntry) {
    }

    @Override
    public void endOfFramePosted(int unprocessedFrames) {
    }

    @Override
    public void gameStarted() {
    }

    @Override
    public void gameStopped() {
    }

    @Override
    public void onConnectionClosed() {
    }

    @Override
    public void onConnectionError(final String message) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                statusLabel.setText(message);
                statusLabel.setForeground(Color.RED);
                createGameButton.setEnabled(true);
                connectButton.setEnabled(true);

                if (networkClient != null) {
                    if (lobby != null) {
                        networkClient.removeNetworkClientListener(lobby);
                        lobby = null;
                    }
                    networkClient.removeNetworkClientListener(NetworkPanel.this);
                    networkClient.stop();
                    networkClient = null;
                }

                if (networkServer != null) {
                    networkServer.stop();
                    networkServer = null;
                }
                
            }
        });

    }

    @Override
    public void onOwnClientIdSet(short ownClientId) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                mainFrame.openComponent(lobby, false);
            }
        });
        
    }

    @Override
    public void spawnBuildingBlock(Vec2 position, Material material, short shape) {
    }

    @Override
    public void spawnPowerUpBlock() {
    }

    @Override
    public void onClientPropertyChanged(ClientEntry clientEntry) {
    }

    @Override
    public void onSettingsReceived(Settings settings) {
    }



    private void tryConnect(NetworkServer networkServer) {
        
        createGameButton.setEnabled(false);
        connectButton.setEnabled(false);
        statusLabel.setForeground(defaultTextColor);
        statusLabel.setText("Status: Connecting...");
        Settings settings = mainFrame.getSettings();
        String host;
        if (networkServer == null) {
            host = settings.getNetworkHostname();
        } else {
            host = "localhost";
        }
        networkClient = new NetworkClient(settings.getPlayerName(), host, settings.getNetworkPort());
        lobby = new NetworkLobby(mainFrame, networkClient, networkServer);
        networkClient.addNetworkClientListener(lobby);
        networkClient.addNetworkClientListener(this);
        networkClient.start();
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
        createGameButton = new javax.swing.JButton();
        serverPortNoteLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        hostnameTextField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        backButton = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Host game"));

        createGameButton.setText("Create new game");
        createGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createGameButtonActionPerformed(evt);
            }
        });

        serverPortNoteLabel.setText("Note: Port 25001 is used. You might need to adjust you firewall settings.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serverPortNoteLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(242, Short.MAX_VALUE)
                .addComponent(createGameButton)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serverPortNoteLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(createGameButton)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Join game"));

        jLabel1.setText("Hostname:");

        hostnameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostnameTextFieldActionPerformed(evt);
            }
        });

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        statusLabel.setText("Status:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                            .addComponent(hostnameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)))
                    .addComponent(connectButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(hostnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(connectButton)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));

        jLabel3.setText("Player name:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(backButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void createGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createGameButtonActionPerformed
        saveSettings();

        Settings settings = mainFrame.getSettings();
        networkServer = new NetworkServer(settings.getNetworkPort());
        networkServer.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        tryConnect(networkServer);

    }//GEN-LAST:event_createGameButtonActionPerformed

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        saveSettings();
        tryConnect(null);
    }//GEN-LAST:event_connectButtonActionPerformed

    private void hostnameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostnameTextFieldActionPerformed
        saveSettings();
        tryConnect(null);
    }//GEN-LAST:event_hostnameTextFieldActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        if (networkClient != null) {
            networkClient.removeNetworkClientListener(this);
        }
        mainFrame.back();
    }//GEN-LAST:event_backButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton connectButton;
    private javax.swing.JButton createGameButton;
    private javax.swing.JTextField hostnameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel serverPortNoteLabel;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}
