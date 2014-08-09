package de.fh_kl.bluepong.util;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import de.fh_kl.bluepong.constants.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    private static BluetoothService instance;

    private int clientCount;

    BluetoothAdapter adapter;
    BluetoothSocket socket;
    BluetoothServerSocket serverSocket;
    UUID uuid;
    InputStream inputStream;
    OutputStream outputStream;

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

    private BluetoothService () {
        adapter = BluetoothAdapter.getDefaultAdapter();
        uuid = UUID.fromString(Constants.BLUETOOTH_UUID);
    }

    public List<BluetoothDevice> getPairedDevices() {

        Set<BluetoothDevice> deviceSet = adapter.getBondedDevices();

        return new ArrayList<BluetoothDevice>(deviceSet);
    }

    public boolean startServer() {

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
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            closeIOStream();
            return false;
        }
        return true;
    }

    private void closeIOStream() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
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

    public void send(int i) {
        try {
            outputStream.write(i);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int receive() {
        try {
            return inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
