package de.fh_kl.bluepong.util;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import de.fh_kl.bluepong.R;

import java.util.List;

/**
 * User: #empty
 * Date: 08.08.14
 */
public class BluetoothListAdapter extends ArrayAdapter<BluetoothDevice> {

    Context context;
    List<BluetoothDevice> deviceList;
    Typeface typeface;

    public BluetoothListAdapter(Context context, List<BluetoothDevice> deviceList, Typeface team401) {
        super(context, android.R.layout.simple_list_item_1, deviceList);

        this.context = context;
        this.deviceList = deviceList;

        typeface = team401;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView deviceName = (TextView) rowView.findViewById(android.R.id.text1);
        deviceName.setTypeface(typeface);
        deviceName.setTextSize(7 * context.getResources().getDisplayMetrics().density);
        deviceName.setText(deviceList.get(position).getName());

        return  rowView;
    }
}