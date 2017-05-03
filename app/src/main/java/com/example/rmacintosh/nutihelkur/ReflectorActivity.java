package com.example.rmacintosh.nutihelkur;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmacintosh.nutihelkur.applications.ScannerApplication;
import com.example.rmacintosh.nutihelkur.bluetooth.Transmitter;
import com.example.rmacintosh.nutihelkur.helpers.BluetoothHelper;
import com.example.rmacintosh.nutihelkur.models.Sensor;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.BleNotAvailableException;

import io.realm.Realm;
import io.realm.RealmResults;

import com.example.rmacintosh.nutihelkur.ReflectorActivity;

public class ReflectorActivity extends AppCompatActivity {

    protected static final String TAG = "ReflectorActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private BeaconManager beaconManager2;
    private Transmitter transmitter2;
    private String id = "5a4bcfce-174e-4bac-a814-092e77f6b7e5";
    private String linn ="lammas";
    TextView textViewStatsData;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*
        Realm.init(this);
    RealmConfiguration config = new RealmConfiguration.Builder().build();
    Realm.setDefaultConfiguration(config);
         */
        realm = Realm.getDefaultInstance();

        setr();

        setContentView(R.layout.activity_reflector);

        textViewStatsData = (TextView) findViewById(R.id.textViewStatsData);
        textViewStatsData.setMovementMethod(new ScrollingMovementMethod());


        //writeSensorToDatabase("5a4bcfce-174e-4bac-a814-092e77f6b7e5", "Annelinn");
        bluetoothStatus();
        beaconManager2 = BeaconManager.getInstanceForApplication(this);
        logToDisplay("Test sussesful");


        // ONLY FOR NEWER THAN LOLLIPOP
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            startBeaconTransmitService();
            startTransmitting(); // checki õigust igaks juks, vb peaks väljas olema

        }
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStats();
                Snackbar.make(view, "Did data appeared?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });



    }

    public void setr() {
        realm.beginTransaction();
        Sensor sensor1 = realm.createObject(Sensor.class); // Create a new object
        sensor1.setSensorId(id);
        sensor1.setLocation(linn);
        realm.commitTransaction();
    }

    public void getStats() {
        RealmResults<Sensor> result = realm.where(Sensor.class).findAll();

        textViewStatsData.setText("");

        for(Sensor sensor : result){
            textViewStatsData.append("SensorId " + sensor.getSensorId() + "\n" + "Location" + sensor.getLocation() + "\n");
        }
    }

 /*   private void writeSensorToDatabase(final String sensorId, final String location) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Sensor sensor = bgRealm.createObject(Sensor.class);
                sensor.setSensorId(sensorId);
                sensor.setLocation(location);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "DataBase execute onSuccess()");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "DataBase execute onError: " + error.getMessage());
            }
        });
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ScannerApplication) this.getApplicationContext()).setReflectorActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((ScannerApplication) this.getApplicationContext()).setReflectorActivity(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reflector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void bluetoothStatus() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("BLUETOOTH IS TURNED OFF");
                builder.setMessage("PLEASE ENABLE BLUETOOTH");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        } catch (RuntimeException e) {
            String bleProblem = BluetoothHelper.bleCompatibility(
                    BeaconTransmitter.checkTransmissionSupported(this));
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("SERIOUS BLE PROBLEM");
            builder.setMessage("PROBLEM MAY BE:" + bleProblem);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }

            });
            builder.show();

        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startBeaconTransmitService() {
        transmitter2 = new Transmitter(this);
    }

    private void notifyTransmittingNotSupported() {
        String bleProblem2 = BluetoothHelper.bleCompatibility(
                BeaconTransmitter.checkTransmissionSupported(this));
        new AlertDialog.Builder(this)
                .setTitle("HOW YOU MADE SO FAR?")
                .setMessage("BLE ERROR: " + bleProblem2)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void startTransmitting() {
        try {
            if (!beaconManager2.checkAvailability()) {
                Toast.makeText(this, "BLE NOT ON!",
                        Toast.LENGTH_SHORT).show();            }
            else {
                if (!(BeaconTransmitter.checkTransmissionSupported(this) == BeaconTransmitter.SUPPORTED)) {
                    Toast.makeText(this, "BLE advert no support!",
                            Toast.LENGTH_SHORT).show();
                    notifyTransmittingNotSupported();
                } else if (transmitter2 != null) {

                    transmitter2.startTransmitting();
                }
            }
        } catch (BleNotAvailableException bleNotAvailableException) {
            notifyTransmittingNotSupported();
        }
    }

    public void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView textView = (TextView)ReflectorActivity.this
                        .findViewById(R.id.monitoringText);
                textView.append(line+"\n");
            }
        });
    }
}
