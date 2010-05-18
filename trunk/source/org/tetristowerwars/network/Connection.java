/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tetristowerwars.network.message.Message;

/**
 *
 * @author Andreas
 */
public class Connection {

    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final BlockingQueue<Message> sendQueue = new LinkedBlockingQueue<Message>();
    private final NetworkMessageListener messageListener;
    private final Semaphore aliveSemaphore = new Semaphore(1);
    private final boolean alwaysFlush;

    public Connection(Socket socket, NetworkMessageListener messageListener, boolean alwaysFlush) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.messageListener = messageListener;
        this.alwaysFlush = alwaysFlush;

        new SendThread().start();
        new ReceiveThread().start();
    }

    public Connection(Socket socket, NetworkMessageListener messageListener) throws IOException {
        this(socket, messageListener, true);
    }

    public boolean isAlive() {
        return aliveSemaphore.availablePermits() > 0;
    }

    public void send(Message message) {
        try {
            sendQueue.put(message);
        } catch (InterruptedException ex) {
        }
    }

    public void close() {
        if (aliveSemaphore.tryAcquire()) {
            messageListener.onConnectionClosed(this);
            new Thread() {

                @Override
                public void run() {
                    try {
                        dataInputStream.close();
                    } catch (IOException ex) {
                    }
                    try {
                        dataOutputStream.close();
                    } catch (IOException ex) {
                    }
                    try {
                        socket.close();
                    } catch (IOException ex) {
                    }

                    
                }
            }.start();
        }
    }

    @Override
    public String toString() {
        return socket.toString();
    }

    private class SendThread extends Thread {

        public SendThread() {
            super("Send thread");
        }


        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            try {
                int numMessagesSent = 0;
                while (isAlive()) {
                    try {
                        Message message = sendQueue.take();
                        message.write(dataOutputStream);
                        numMessagesSent++;

                        if (numMessagesSent % 100 == 0) {
                            System.out.println("Messages sent: " + numMessagesSent);
                        }
                        if (alwaysFlush) {
                            dataOutputStream.flush();
                        } else if (sendQueue.isEmpty()) {
                            dataOutputStream.flush();
                        }


                    } catch (InterruptedException ex) {
                    }
                }
            } catch (IOException ex) {
                synchronized (this) {
                    if (isAlive()) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                        messageListener.onConnectionError(Connection.this, ex.getMessage());
                        close();
                    }
                }
            }
        }
    }

    private class ReceiveThread extends Thread {

        public ReceiveThread() {
            super("Receive thread");
        }


        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            try {
                while (isAlive()) {
                    Message message = Message.read(dataInputStream);
                    messageListener.handleReceivedMessage(Connection.this, message);
                }
            } catch (IOException ex) {
                synchronized (this) {
                    if (isAlive()) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                        messageListener.onConnectionError(Connection.this, ex.getMessage());
                        close();
                    }
                }
            }
        }
    }
}
