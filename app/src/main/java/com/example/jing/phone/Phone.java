package com.example.jing.phone;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.example.jing.phone.commands.*;
import com.example.jing.phone.commands.engine.EngineLoadObdCommand;
import com.example.jing.phone.commands.engine.EngineRPMObdCommand;
import com.example.jing.phone.commands.fuel.FuelConsumptionRateObdCommand;
import com.example.jing.phone.commands.fuel.FuelLevelObdCommand;
import com.example.jing.phone.commands.protocol.EchoOffObdCommand;
import com.example.jing.phone.commands.protocol.LineFeedOffObdCommand;
import com.example.jing.phone.commands.protocol.SelectProtocolObdCommand;
import com.example.jing.phone.commands.protocol.TimeoutObdCommand;
import com.example.jing.phone.enums.*;
import com.example.jing.phone.exceptions.*;


public class Phone extends Activity {


    private static final String TAG = "Error";
    private static final String LOG = "Info";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int tank = 53;
    private static final UUID obd_uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID glass_uuid = UUID.fromString("05f2934c-1e81-4554-bb08-44aa761afbfb");
    private static final String path = "/storage/emulated/0/Android/data/com.dropbox.android/cache/";

    private Button obd_on;
    private Button glass_on;
    private TextView obd;
    private TextView glass;
    private OBDThread obd_thread;
    private GlassThread glass_thread;

    private BluetoothAdapter bluetoothAdapter;
    private String obd_device;
    private String glass_device;

    private Lock read;
    private Lock write;

    private String speed;
    private String speed_limit;
    private String fuel;
    private String fuel_rate;
    private String distance_to_empty;
    private String load;
    private String RPM;

    private Button speed_limit_15;
    private Button speed_limit_35;
    private Button speed_limit_45;
    private Button speed_limit_65;

    private Button end;

    private ArrayList<String> time = new ArrayList<String>();
    private ArrayList<String> velocity = new ArrayList<String>();


    Handler obd_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String data = bundle.getString("obd");
            obd = (TextView) findViewById(R.id.obd);
            obd.setText(data);
        }
    };


    Handler glass_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String data = bundle.getString("glass");
            glass = (TextView) findViewById(R.id.glass);
            glass.setText(data);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        speed = "0";
        speed_limit = "15";
        fuel = "0";
        fuel_rate = "0";
        distance_to_empty = "0";
        load = "0";
        RPM = "0";

    }


    protected void onStart() {
        super.onStart();
    }


    protected void onResume() {

        super.onResume();

        obd_on = (Button) findViewById(R.id.obd_on);
        glass_on = (Button) findViewById(R.id.glass_on);
        obd = (TextView) findViewById(R.id.obd);
        glass = (TextView) findViewById(R.id.glass);

        speed_limit_15 = (Button) findViewById(R.id.speed_limit_15);
        speed_limit_35 = (Button) findViewById(R.id.speed_limit_35);
        speed_limit_45 = (Button) findViewById(R.id.speed_limit_45);
        speed_limit_65 = (Button) findViewById(R.id.speed_limit_65);

        end = (Button) findViewById(R.id.end);

        ReentrantReadWriteLock read_write_lock = new ReentrantReadWriteLock();
        read = read_write_lock.readLock();
        write = read_write_lock.writeLock();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Log.d(TAG, "Device does not support bluetooth");
            finish();
        }

        else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent bluetoothIntent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);
            }
        }

        obd_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayList<String> deviceAddress = new ArrayList<String>();
                final ArrayList<String> deviceName = new ArrayList<String>();

                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        deviceName.add(device.getName() + "\n" + device.getAddress());
                        deviceAddress.add(device.getAddress());
                    }
                }
                else {
                    Log.d(TAG, "OBD: no bluetooth device found");
                    finish();
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.select_dialog_singlechoice, deviceName.toArray(new String[deviceName.size()]));

                alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        obd_device = deviceAddress.get(position);
                        obd_thread = new OBDThread();
                        obd_thread.start();
                    }
                });

                alertDialog.setTitle("Choose Device");
                alertDialog.show();

            }
        });

        glass_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayList<String> deviceAddress = new ArrayList<String>();
                final ArrayList<String> deviceName = new ArrayList<String>();

                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        deviceName.add(device.getName() + "\n" + device.getAddress());
                        deviceAddress.add(device.getAddress());
                    }
                }
                else {
                    Log.d(TAG, "Glass: no bluetooth device found");
                    finish();
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.select_dialog_singlechoice, deviceName.toArray(new String[deviceName.size()]));

                alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        glass_device = deviceAddress.get(position);
                        glass_thread = new GlassThread();
                        glass_thread.start();
                    }
                });

                alertDialog.setTitle("Choose Device");
                alertDialog.show();

            }
        });

        speed_limit_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write.lock();
                try {
                    speed_limit = "15";
                } finally {
                    write.unlock();
                }
            }
        });

        speed_limit_35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write.lock();
                try {
                    speed_limit = "35";
                } finally {
                    write.unlock();
                }
            }
        });

        speed_limit_45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write.lock();
                try {
                    speed_limit = "45";
                } finally {
                    write.unlock();
                }
            }
        });

        speed_limit_65.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write.lock();
                try {
                    speed_limit = "65";
                } finally {
                    write.unlock();
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (obd_thread != null)
                        obd_thread.cancel();
                    if (glass_thread != null)
                        glass_thread.cancel();

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
                    String name = date.format(calendar.getTime()) + ".csv";
                    Log.d(TAG, name);

                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                    File file = new File(path, name);
                    path.mkdirs();
                    OutputStream writer = new FileOutputStream(file);

                    String title = "time,speed\n";
                    writer.write(title.getBytes());

                    Log.d(TAG, "OK");

                    for (int i = 0; i < time.size(); i++) {
                        String context = time.get(i) + "," + velocity.get(i) + "\n";
                        writer.write(context.getBytes());
                    }

                    writer.close();

                } catch (IOException e) {
                    Log.d(TAG, "Write: IOException");
                    finish();
                }

            }
        });

    }


    protected void onPause() {
        super.onPause();
    }


    protected void onStop() {
        super.onStop();
    }


    protected void onDestroy() {

        super.onDestroy();

        if (obd_thread != null)
            obd_thread.cancel();
        if (glass_thread != null)
            glass_thread.cancel();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.phone, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class OBDThread extends Thread {

        private BluetoothSocket bluetoothSocket;
        private BluetoothDevice bluetoothDevice;

        public OBDThread() {

            if (obd_device == null) {
                Log.d(TAG, "OBD: bluetooth device not available");
                finish();
            }

            else {

                try {
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(obd_device);
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(obd_uuid);
                } catch (IOException e) {
                    Log.d(TAG, "OBD: socket failed");
                    finish();
                }

            }

        }

        public void run() {

            bluetoothAdapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
                OBDCommunication();
            } catch (IOException e) {
                Log.d(TAG, "OBD: socket connection failed");
                finish();
            }

        }

        public void OBDCommunication() {

            SpeedObdCommand speedObdCommand;
            FuelLevelObdCommand fuelLevelObdCommand;
            FuelConsumptionRateObdCommand fuelConsumptionRateObdCommand;
            EngineLoadObdCommand engineLoadObdCommand;
            EngineRPMObdCommand engineRPMObdCommand;

            try {

                new EchoOffObdCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                new LineFeedOffObdCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                new TimeoutObdCommand(200).run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                new SelectProtocolObdCommand(ObdProtocols.AUTO).run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());

                while (true) {

                    speedObdCommand = new SpeedObdCommand();
                    fuelLevelObdCommand = new FuelLevelObdCommand();
                    fuelConsumptionRateObdCommand = new FuelConsumptionRateObdCommand();
                    engineLoadObdCommand = new EngineLoadObdCommand();
                    engineRPMObdCommand = new EngineRPMObdCommand();

                    speedObdCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                    fuelLevelObdCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                    fuelConsumptionRateObdCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                    engineLoadObdCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                    engineRPMObdCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());

                    write.lock();

                    try {

                        speed = speedObdCommand.getFormattedResult();
                        fuel = fuelLevelObdCommand.getFormattedResult();
                        fuel_rate = fuelConsumptionRateObdCommand.getFormattedResult();
                        Double value = Double.parseDouble(speed) * Double.parseDouble(fuel) / 100 / Double.parseDouble(fuel_rate) * tank;
                        DecimalFormat format = new DecimalFormat("0.00");
                        value = Double.valueOf(format.format(value));
                        distance_to_empty = Double.toString(value);
                        load = engineLoadObdCommand.getFormattedResult();
                        RPM = engineRPMObdCommand.getFormattedResult();

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
                        String date = date_format.format(calendar.getTime());
                        time.add(date);
                        velocity.add(speed);

                        String debug = "obd: " + speed + "mph;" + distance_to_empty + "mile;" + load + "%;" + RPM + "RPM";
                        Message msg = obd_handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("obd", debug);
                        msg.setData(bundle);
                        obd_handler.sendMessage(msg);

                        Log.d(LOG, debug);

                    } finally {
                        write.unlock();
                    }

                    Thread.sleep(100);

                }


            } catch (IOException e1) {
                Log.d(TAG, "OBD: IOException");
            } catch (InterruptedException e2) {
                Log.d(TAG, "OBD: InterruptedException");
            } catch (StoppedException e3) {
                Log.d(TAG, "OBD: StoppedException");
            }

        }

        public void cancel() {

            try {
                if (bluetoothSocket != null)
                    bluetoothSocket.close();
            } catch (IOException e) { }

        }

    }


    private class GlassThread extends Thread {

        private BluetoothSocket bluetoothSocket;
        private BluetoothDevice bluetoothDevice;
        private OutputStream outputStream;

        public GlassThread() {

            if (glass_device == null) {
                Log.d(TAG, "Glass: bluetooth device not available");
                finish();
            }

            else {

                try {
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(glass_device);
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(glass_uuid);
                } catch (IOException e) {
                    Log.d(TAG, "Glass: socket failed");
                    finish();
                }

            }

        }

        public void run() {

            bluetoothAdapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                GlassCommunication();
            } catch (IOException e) {
                Log.d(TAG, "Glass: socket connection failed");
                finish();
            }

        }

        public void GlassCommunication() {

            try {

                while (true) {

                    read.lock();

                    try {

                        String out = speed + ";" + speed_limit + ";" + distance_to_empty + ";" + load + ";" + RPM + "\n";

                        outputStream.write(out.getBytes());
                        outputStream.flush();

                        String debug = "glass: " + speed + "mph;" + speed_limit + "mph;" + distance_to_empty + "mile;" + load + "%;" + RPM + "RPM";
                        Message msg = glass_handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("glass", debug);
                        msg.setData(bundle);
                        glass_handler.sendMessage(msg);

                        Log.d(LOG, debug);

                    } finally {
                        read.unlock();
                    }

                    Thread.sleep(1000);

                }

            } catch (IOException e) {
                Log.d(TAG, "Glass: IOException");
            } catch (InterruptedException e2) {
                Log.d(TAG, "Glass: InterruptedException");
            }

        }

        public void cancel() {

            try {
                if (bluetoothSocket != null)
                    bluetoothSocket.close();
            } catch (IOException e) { }

        }

    }


}
