package de.fh_kl.bluepong.util;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import de.fh_kl.bluepong.constants.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    BluetoothAdapter adapter;
    BluetoothSocket socket;
    UUID uuid;
    InputStream inputStream;
    OutputStream outputStream;

    public BluetoothService () {
        adapter = BluetoothAdapter.getDefaultAdapter();
        uuid = UUID.randomUUID();
    }


    public List<BluetoothDevice> getPairedDevices() {

        Set<BluetoothDevice> deviceSet = adapter.getBondedDevices();

        return new ArrayList<BluetoothDevice>(deviceSet);
    }

    public boolean startServer() {

        try {
            BluetoothServerSocket serverSocket = adapter.listenUsingInsecureRfcommWithServiceRecord(Constants.BLUETOOTH_SERVER_NAME, uuid);

            socket = serverSocket.accept(30000);
        } catch (IOException e) {
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

    public boolean openIOStream() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            closeIOStream();
            return false;
        }
        return true;
    }

    public void closeIOStream() {
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
