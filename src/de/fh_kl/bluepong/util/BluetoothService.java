package de.fh_kl.bluepong.util;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;
import de.fh_kl.bluepong.constants.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service class to handle bluetooth connections and transmissions
 *
 * implemented as Singleton
 */
public class BluetoothService {

    private static BluetoothService instance;

    // keep track of the number of clients using this service
    private int clientCount;

    private boolean isServer;

    BluetoothAdapter adapter;
    BluetoothSocket socket;
    BluetoothServerSocket serverSocket;
    UUID uuid;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    /**
     * returns singleton-instance of BluetoothService
     * @return singleton-instance of BluetoothService
     */
    public static BluetoothService getInstance() {

        if (instance == null) {
            instance = new BluetoothService();
        }
        return instance;
    }

    /**
     * invisible constructor
     */
    private BluetoothService () {
        adapter = BluetoothAdapter.getDefaultAdapter();
        uuid = UUID.fromString(Constants.BLUETOOTH_UUID);
    }


    /**
     * increments {@link de.fh_kl.bluepong.util.BluetoothService#clientCount}
     *
     * IMPORTANT! call this in every activity you use this service
     */
    public void start() {
        clientCount++;
    }

    /**
     * decrements {@link de.fh_kl.bluepong.util.BluetoothService#clientCount}
     *
     * shuts the service down if {@link de.fh_kl.bluepong.util.BluetoothService#clientCount} == 0
     *
     * IMPORTANT! call this in {@link android.app.Activity#onStop()} in every activity you use this service
     */
    public void stop() {
        clientCount--;
        if (clientCount == 0) {
            shutdown();
        }
    }

    /**
     * returns whether the service acts as server or not
     * @return whether the service acts as server or not
     */
    public boolean isServer() {
        return isServer;
    }

    /**
     * returns a list with all paired devices
     * @return a list with all paired devices
     */
    public List<BluetoothDevice> getPairedDevices() {

        Set<BluetoothDevice> deviceSet = adapter.getBondedDevices();

        return new ArrayList<BluetoothDevice>(deviceSet);
    }

    /**
     * starts a bluetooth server
     * @return whether the start was successful or not
     */
    public boolean startServer() {

        isServer = true;

        if (serverSocket != null) {
            return true;
        }

        try {
            serverSocket = adapter.listenUsingInsecureRfcommWithServiceRecord(Constants.BLUETOOTH_SERVER_NAME, uuid);

            socket = serverSocket.accept(30000);
        } catch (IOException e) {

            serverSocket = null;
            return false;
        }

        if (!openIOStream()) {
            return false;
        }

        return true;
    }

    /**
     * connect to given bluetooth device}
     * @param deviceToConnect  device to connect to
     * @return whether connect was successful or not
     */
    public boolean connect(BluetoothDevice deviceToConnect) {

        isServer = false;

        try {
            socket = deviceToConnect.createInsecureRfcommSocketToServiceRecord(uuid);

            socket.connect();

        } catch (IOException e) {
            return false;
        }

        if (!openIOStream()) {
            return false;
        }

        return true;
    }

    /**
     * opens an input and output stream
     * @return whether both streams could be opened or not
     */
    private boolean openIOStream() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            closeIOStream();
            return false;
        }
        return true;
    }

    /**
     * closes the input and output stream
     */
    private void closeIOStream() {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }

            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * closes the sockets
     */
    private void closeSockets() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }

            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * shuts the service down
     */
    private void shutdown() {
        closeIOStream();
        closeSockets();
    }

    /**
     * send an int to connected device
     * @param i
     * @return returns whether transmission was successful or not
     */
    public boolean send(int i) {
        try {
            dataOutputStream.writeShort(i);
            dataOutputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * send an boolean to connected device
     * @param b
     * @return returns whether transmission was successful or not
     */
    private boolean send(boolean b) {
        try {
            dataOutputStream.writeBoolean(b);
            dataOutputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * receive next int in queue
     * @return next int in queue
     */
    public int receiveInt() {
        try {
            return dataInputStream.readShort();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * receive next boolean in queue
     * @return next boolean in queue
     */
    public boolean receiveBoolean() {
        try {
            return dataInputStream.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean sendPosition(int xPosition) {
        return send(xPosition);
    }

    public int getOpponentPosition() {
        return receiveInt();
    }

    /**
     * get display ratio between own display and opponents display
     * @param width own display width
     * @param height own display height
     * @return display ratio as array
     *
     * sends own display size to opponent and receives opponents display size
     */
    public double[] syncDisplaySize(int width, int height) {

        send(width);
        send(height);

        double opponentWidth = receiveInt();
        double opponentHeight = receiveInt();

        return new double[] {width/opponentWidth, height/opponentHeight};
    }

    /**
     * sync settings with opponent. settings from host will be used
     *
     * @param preferences
     * @return synced settings
     */
    public SharedPreferences syncPreferences(SharedPreferences preferences) {
        if (isServer()) {
            send(preferences.getInt(Constants.BALL_SPEED_SETTING, 4));
            send(preferences.getBoolean(Constants.BALL_SPEED_INCREASE_SETTING, true));
            send(preferences.getInt(Constants.BALL_SIZE_SETTING, 4));
            send(preferences.getInt(Constants.PADDLE_SPEED_SETTING, 4));
            send(preferences.getInt(Constants.PADDLE_SIZE_SETTING, 4));
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(Constants.BALL_SPEED_SETTING, receiveInt());
            editor.putBoolean(Constants.BALL_SPEED_INCREASE_SETTING, receiveBoolean());
            editor.putInt(Constants.BALL_SIZE_SETTING, receiveInt());
            editor.putInt(Constants.PADDLE_SPEED_SETTING, receiveInt());
            editor.putInt(Constants.PADDLE_SIZE_SETTING, receiveInt());
            editor.apply();
        }
        return preferences;
    }

    public boolean sendBallPosition(Point position) {
        return send(position.x) && send(position.y);
    }

    public Point getBallPosition() {
        int x = receiveInt();
        int y = receiveInt();
        return new Point(x, y);
    }

    public boolean sendIsBallOut(boolean newRound) {
        return send(newRound);
    }

    public boolean receiveIsBallOut() {
        return receiveBoolean();
    }
}
