/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NetworkLobby.java
 *
 * Created on 2010-maj-24, 13:33:51
 */
package org.tetristowerwars;

import java.util.LinkedHashSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
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
public class NetworkLobby extends javax.swing.JPanel implements NetworkClientListener {

    private final MainFrame mainFrame;
    private final NetworkClient networkClient;
    private final NetworkServer networkServer;
    private final LinkedHashSet<ClientEntry> clients = new LinkedHashSet<ClientEntry>();

    /** Creates new form NetworkLobby
     * @param mainFrame
     * @param networkClient
     * @param networkServer
     */
    public NetworkLobby(MainFrame mainFrame, NetworkClient networkClient, NetworkServer networkServer) {
        initComponents();
        this.mainFrame = mainFrame;
        this.networkClient = networkClient;
        this.networkServer = networkServer;

        if (networkServer == null) {
            timeLimitCheckBox.setEnabled(false);
            timeLimitTextField.setEnabled(false);
            heightCheckBox.setEnabled(false);
            heightTextField.setEnabled(false);
            startGameButton.setEnabled(false);
            conditionComboBox1.setEnabled(false);
        } else {
            Settings settings = mainFrame.getSettings();
            timeLimitCheckBox.setSelected(settings.isTimeConditionEnabled());
            timeLimitTextField.setText(settings.getTimeCondition() + "");
            heightCheckBox.setEnabled(settings.isHeightConditionEnabled());
            heightTextField.setText(settings.getHeightCondition() + "");
        }

        chatHistoryTextArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                chatHistoryTextArea.setCaretPosition(e.getLength());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void updateTeamLists() {
        Vector<String> team1Vector = new Vector<String>();
        Vector<String> team2Vector = new Vector<String>();

        for (ClientEntry clientEntry : clients) {
            short playerIndex = clientEntry.getPlayerIndex();
            if (playerIndex == 0) {
                team1Vector.add(clientEntry.getName());
            } else if (playerIndex == 1) {
                team2Vector.add(clientEntry.getName());
            }
        }

        team1List.setListData(team1Vector);
        team2List.setListData(team2Vector);

        System.out.println("Team1: " + team1Vector);
        System.out.println("Team2: " + team2Vector);
    }

    @Override
    public void allClientsReady() {
    }

    @Override
    public void chatMessageReceive(final ClientEntry clientEntry, final String message) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                int length = chatHistoryTextArea.getDocument().getLength();
                try {
                    chatHistoryTextArea.getDocument().insertString(length, "<" + clientEntry.getName() + "> " + message + "\n", null);
                } catch (BadLocationException ex) {
                    Logger.getLogger(NetworkLobby.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    @Override
    public void clientConnected(final ClientEntry clientEntry) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                clients.add(clientEntry);
                updateTeamLists();
            }
        });
    }

    @Override
    public void clientDisconnected(final ClientEntry clientEntry) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                clients.remove(clientEntry);
                updateTeamLists();
            }
        });
    }

    @Override
    public void endOfFramePosted(int unprocessedFrames) {
    }

    @Override
    public void gameStarted() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                NetworkGameLogic networkGameLogic = new NetworkGameLogic(mainFrame, networkClient, networkServer);
            }
        });
    }

    @Override
    public void onConnectionClosed() {
    }

    @Override
    public void onConnectionError(String message) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JOptionPane.showMessageDialog(NetworkLobby.this, "Lost connection with the server.");
                networkClient.removeNetworkClientListener(NetworkLobby.this);
                mainFrame.back();
            }
        });

    }

    @Override
    public void onOwnClientIdSet(short ownClientId) {
    }

    @Override
    public void spawnBuildingBlock(Vec2 position, Material material, short shape) {
    }

    @Override
    public void onClientPropertyChanged(ClientEntry clientEntry) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                updateTeamLists();
            }
        });
    }

    private void sendChatMessage() {
        String message = chatMessageTextField.getText();
        if (message.length() > 0) {
            networkClient.sendChatMessage(message);
            chatMessageTextField.setText("");
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
        jScrollPane1 = new javax.swing.JScrollPane();
        team1List = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        team2List = new javax.swing.JList();
        joinTeam1Button = new javax.swing.JButton();
        joinTeam2Button = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        timeLimitCheckBox = new javax.swing.JCheckBox();
        timeLimitTextField = new javax.swing.JTextField();
        heightCheckBox = new javax.swing.JCheckBox();
        heightTextField = new javax.swing.JTextField();
        conditionComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        startGameButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        chatHistoryTextArea = new javax.swing.JTextArea();
        chatMessageTextField = new javax.swing.JTextField();
        sendMessageButton = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Teams"));

        jLabel1.setText("[SOVIET]");

        jLabel2.setText("[USA]");

        jScrollPane1.setViewportView(team1List);

        jScrollPane2.setViewportView(team2List);

        joinTeam1Button.setText("Join");
        joinTeam1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinTeam1ButtonActionPerformed(evt);
            }
        });

        joinTeam2Button.setText("Join");
        joinTeam2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinTeam2ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(joinTeam1Button, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                    .addComponent(joinTeam2Button, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, 0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(joinTeam1Button)
                    .addComponent(joinTeam2Button))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Settings"));

        timeLimitCheckBox.setText("Time limit (seconds)");

        heightCheckBox.setText("Height limit (meters)");

        conditionComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All selected conditions are fulfilled", "Any selected condition is fulfilled" }));

        jLabel3.setText("Game over conditions:");

        startGameButton.setText("Start game!");
        startGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startGameButtonActionPerformed(evt);
            }
        });

        settingsButton.setText("More settings...");
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(conditionComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timeLimitCheckBox)
                            .addComponent(heightCheckBox))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(heightTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(timeLimitTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLimitCheckBox)
                    .addComponent(timeLimitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(heightCheckBox)
                    .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(conditionComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startGameButton)
                    .addComponent(settingsButton))
                .addGap(18, 18, 18))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Chat"));

        chatHistoryTextArea.setColumns(20);
        chatHistoryTextArea.setRows(5);
        jScrollPane3.setViewportView(chatHistoryTextArea);

        chatMessageTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatMessageTextFieldActionPerformed(evt);
            }
        });

        sendMessageButton.setText("Send");
        sendMessageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessageButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(chatMessageTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendMessageButton))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chatMessageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendMessageButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void joinTeam1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinTeam1ButtonActionPerformed
        networkClient.setPlayerIndex((short) 0);
    }//GEN-LAST:event_joinTeam1ButtonActionPerformed

    private void joinTeam2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinTeam2ButtonActionPerformed
        networkClient.setPlayerIndex((short) 1);
    }//GEN-LAST:event_joinTeam2ButtonActionPerformed

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        mainFrame.openComponent(new SettingsPanel(mainFrame), false);
    }//GEN-LAST:event_settingsButtonActionPerformed

    private void startGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startGameButtonActionPerformed
        networkServer.startGame(mainFrame.getSettings());
    }//GEN-LAST:event_startGameButtonActionPerformed

    private void sendMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMessageButtonActionPerformed
        sendChatMessage();
    }//GEN-LAST:event_sendMessageButtonActionPerformed

    private void chatMessageTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chatMessageTextFieldActionPerformed
        sendChatMessage();
    }//GEN-LAST:event_chatMessageTextFieldActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatHistoryTextArea;
    private javax.swing.JTextField chatMessageTextField;
    private javax.swing.JComboBox conditionComboBox1;
    private javax.swing.JCheckBox heightCheckBox;
    private javax.swing.JTextField heightTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton joinTeam1Button;
    private javax.swing.JButton joinTeam2Button;
    private javax.swing.JButton sendMessageButton;
    private javax.swing.JButton settingsButton;
    private javax.swing.JButton startGameButton;
    private javax.swing.JList team1List;
    private javax.swing.JList team2List;
    private javax.swing.JCheckBox timeLimitCheckBox;
    private javax.swing.JTextField timeLimitTextField;
    // End of variables declaration//GEN-END:variables
}
