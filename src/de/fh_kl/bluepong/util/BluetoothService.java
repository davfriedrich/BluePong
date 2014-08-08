package de.fh_kl.bluepong.util;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    BluetoothAdapter adapter;
    BluetoothSocket socket;
    UUID uuid;

    public BluetoothService () {
        adapter = BluetoothAdapter.getDefaultAdapter();
        uuid = UUID.randomUUID();
    }


    public List<BluetoothDevice> getPairedDevices() {

        Set<BluetoothDevice> deviceSet = adapter.getBondedDevices();

        return new ArrayList<BluetoothDevice>(deviceSet);
    }


    public boolean connect(BluetoothDevice deviceToConnect) {

        try {
            socket = deviceToConnect.createInsecureRfcommSocketToServiceRecord(uuid);

            socket.connect();

        } catch (IOException e) {
            return false;
        }
        return true;
    }
}