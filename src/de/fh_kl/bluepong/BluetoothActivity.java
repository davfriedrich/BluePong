package de.fh_kl.bluepong;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.fh_kl.bluepong.constants.Constants;
import de.fh_kl.bluepong.util.BluetoothListAdapter;
import de.fh_kl.bluepong.util.BluetoothService;

/**
 * User: #empty
 * Date: 08.08.14
 */
public class BluetoothActivity extends Activity implements ListView.OnItemClickListener, Constants{

    private Typeface team401;
    private BluetoothService bluetoothService;
    protected BluetoothActivity self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_bluetooth);

        self = this;

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

    public void hostGame(final View v) {

        setContentView(R.layout.activity_bluetooth_host_fragment);

        TextView wait = (TextView) findViewById(R.id.lbl_bluetooth_wait);
        wait.setTypeface(team401);

        new StartServer().execute();
    }

    public void test() {
                Log.v("asyncTask", "test");

    }

    public boolean startServer() {
        return bluetoothService.startServer();
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

    private class StartServer extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Object[] objects) {
            return startServer();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (success) {
                AlertDialog winDialog = new AlertDialog.Builder(self).setMessage("Connection established").create();                
                winDialog.setButton(DialogInterface.BUTTON_POSITIVE, getText(R.string.btn_bluetooth_startGame), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(self, GameActivity.class);
//						intent.putExtra(BLUETOOTH_SERVICE, bluetoothService);
						startActivity(intent);
					}
				});
                winDialog.show();
                TextView message = (TextView) winDialog.findViewById(android.R.id.message);
                message.setTypeface(team401);
                message.setGravity(Gravity.CENTER_HORIZONTAL);
                message.setTextColor(Color.GREEN);
            } else {

                self.setContentView(R.layout.activity_bluetooth);

                AlertDialog failDialog = new AlertDialog.Builder(self).setMessage(R.string.bluetoothServerFailed).show();
                TextView message = (TextView) failDialog.findViewById(android.R.id.message);
                message.setTypeface(team401);
                message.setGravity(Gravity.CENTER_HORIZONTAL);
                message.setTextColor(Color.RED);
            }
        }
    }
}