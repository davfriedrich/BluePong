package de.fh_kl.bluepong;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import de.fh_kl.bluepong.util.BluetoothListAdapter;
import de.fh_kl.bluepong.util.BluetoothService;

/**
 * User: #empty
 * Date: 08.08.14
 */
public class BluetoothActivity extends Activity implements ListView.OnItemClickListener{

    private Typeface team401;
    BluetoothService bluetoothService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_bluetooth);

        bluetoothService = new BluetoothService();

        team401 = Typeface.createFromAsset(getAssets(), "fonts/Team401.ttf");

        TextView gameTitle = (TextView) findViewById(R.id.gameTitle);
        TextView bluetoothTitle = (TextView) findViewById(R.id.bluetoothTitle);
        Button hostGameButton = (Button) findViewById(R.id.btn_bluetooth_hostGame);
        Button joinGameButton = (Button) findViewById(R.id.btn_bluetooth_joinGame);

        Animation animation = new AlphaAnimation(1, 0.33f);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        gameTitle.setTypeface(team401);
        bluetoothTitle.setTypeface(team401);
        hostGameButton.setTypeface(team401);
        hostGameButton.startAnimation(animation);
        joinGameButton.setTypeface(team401);
        joinGameButton.startAnimation(animation);
    }

    public void hostGame(View v) {

    }

    public void joinGame(View v) {
        setContentView(R.layout.activity_bluetooth_join_fragment);

        TextView joinTitle = (TextView) findViewById(R.id.lbl_bluetooth_joinGame);
        ListView joinListview = (ListView) findViewById(R.id.lv_bluetooth_devices);

        ListAdapter adapter = new BluetoothListAdapter(this, bluetoothService.getPairedDevices(), team401);

        joinTitle.setTypeface(team401);
        joinListview.setAdapter(adapter);

        joinListview.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        BluetoothDevice deviceToConnect = (BluetoothDevice) adapterView.getItemAtPosition(i);

        if (!bluetoothService.connect(deviceToConnect)) {
            AlertDialog failDialog = new AlertDialog.Builder(this).setMessage(R.string.bluetoothConnectionFailed).show();
            TextView message = (TextView) failDialog.findViewById(android.R.id.message);
            message.setTypeface(team401);
            message.setGravity(Gravity.CENTER_HORIZONTAL);
            message.setTextColor(Color.RED);
        }
    }
}