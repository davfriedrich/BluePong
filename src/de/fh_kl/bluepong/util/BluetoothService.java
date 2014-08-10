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

public class BluetoothService {

    private static BluetoothService instance;

    private int clientCount;
    private boolean isServer;

    BluetoothAdapter adapter;
    BluetoothSocket socket;
    BluetoothServerSocket serverSocket;
    UUID uuid;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;


    public static BluetoothService getInstance() {

        if (instance == null) {
            instance = new BluetoothService();
        }
        return instance;
    }

    public void start() {
        clientCount++;
        Log.v("bluetoothService", "start: " + clientCount);
    }

    public void stop() {
        clientCount--;
        Log.v("bluetoothService", "stop: " + clientCount);

        if (clientCount == 0) {
            shutdown();
        }
    }

    public boolean isServer() {
        return isServer;
    }

    private BluetoothService () {
        adapter = BluetoothAdapter.getDefaultAdapter();
        uuid = UUID.fromString(Constants.BLUETOOTH_UUID);
    }

    public List<BluetoothDevice> getPairedDevices() {

        Set<BluetoothDevice> deviceSet = adapter.getBondedDevices();

        return new ArrayList<BluetoothDevice>(deviceSet);
    }

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

    private void shutdown() {
        closeIOStream();
        closeSockets();
    }

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

    public int receiveInt() {
        try {
            return dataInputStream.readShort();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

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

    public double[] syncDisplaySize(int width, int height) {

        send(width);
        send(height);

        double opponentWidth = receiveInt();
        double opponentHeight = receiveInt();

        return new double[] {width/opponentWidth, height/opponentHeight};
    }

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
