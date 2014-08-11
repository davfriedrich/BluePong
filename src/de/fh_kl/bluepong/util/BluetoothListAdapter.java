package de.fh_kl.bluepong.util;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.fh_kl.bluepong.R;

import java.util.List;

/**
 * Custom list adapter to list {@link android.bluetooth.BluetoothDevice}s
 */
public class BluetoothListAdapter extends ArrayAdapter<BluetoothDevice> {

    Context context;
    List<BluetoothDevice> deviceList;
    Typeface typeface;

    /**
     * Constructor
     * @param context
     * @param deviceList {@link android.bluetooth.BluetoothDevice} list
     * @param typeface custom typeface
     */
    public BluetoothListAdapter(Context context, List<BluetoothDevice> deviceList, Typeface typeface) {
        super(context, android.R.layout.simple_list_item_1, deviceList);

        this.context = context;
        this.deviceList = deviceList;

        this.typeface = typeface;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView deviceName = (TextView) rowView.findViewById(android.R.id.text1);
        deviceName.setTypeface(typeface);
        deviceName.setTextSize(context.getResources().getDimension(R.dimen.listText));
        deviceName.setText(deviceList.get(position).getName());

        return  rowView;
    }
}