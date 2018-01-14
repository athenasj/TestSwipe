package com.example.makati.testswipe;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    Button sendBtn;
    EditText txtphoneNo;
    EditText txtMessage;
    //String message;
    ImageButton selCon;
    Button locButton;
    private String SimState = "";
    private String phoneNo = ""; // Recipient Phone Number
    private String message = ""; // Message Body

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        locButton = (Button)findViewById(R.id.locButton);
        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        txtphoneNo = (EditText) findViewById(R.id.editText);
        txtMessage = (EditText) findViewById(R.id.editText2);
        forLocationOffline();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //sendSMSMessage();
                sendSms();
            }
        });
        locButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //sendSMSMessage();

            }
        });

        checkPermission();

        selCon = (ImageButton)findViewById(R.id.selCon);
        selCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactTest();
            }
        });


        contactDet();
    }

    public void ContactTest(){
        Intent fragIntent = new Intent(Main2Activity.this,FragmentActivity.class);
        startActivity(fragIntent);
    }

    public void forLocationOffline(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //if not, go here
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);}
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {

            }*/
        else{
            GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
            Location loc = null;
            double lat = 0 ;
            double longitude = 0;
            while (loc == null) {
                loc = gpsTracker.getLocation();
                if (loc != null) {
                    lat = loc.getLatitude();
                    longitude = loc.getLongitude();
                    Toast.makeText(this, "lat:" + lat + "long" + longitude, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Location is null", Toast.LENGTH_LONG).show();

                }
            }
            message = "https://www.google.com/maps/@"+lat+","+longitude+",17z";
        }
    }

    //String phoneNo = null;
    String name = null;
    SharedPreferences appPref;
    SharedPreferences.Editor edit;

    public void contactDet(){
        appPref = getSharedPreferences("basicSettings", Context.MODE_PRIVATE);
        edit = appPref.edit();
        //get phonename

        name = appPref.getString("contactName","");
        if (!name.equals("")){
            //add exception

        }else{
            edit.putString("contactName","Default");
            edit.commit();
        }
        //get phonenum
        phoneNo = appPref.getString("contactNumber","");
        if (!phoneNo.equals("")){
            Toast.makeText(this, "setphone num", Toast.LENGTH_SHORT).show();
            txtphoneNo.setText(phoneNo);
        }
    }


    public void checkPermission() {
    /*start || check whether permission granted*/
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //if not, go here
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //if not, go here
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);}
        /*end*/
    }

    protected void sendSMSMessage() {
        /*phoneNo = txtphoneNo.getText().toString();*/
        message = txtMessage.getText().toString();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }else{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        }
    }
    /*PERMISSION FOR SMS*/
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "SMS PERMISSION GRANTED.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*ALTERNATIVE SMS SENDER*/


    protected void sendSms() {
        checkPermission();

        if (phoneNo.equals("")){
            phoneNo = txtphoneNo.getText().toString();
            edit.putString("contactNumber",phoneNo);
            edit.commit();
        }
        Toast.makeText(this, phoneNo, Toast.LENGTH_SHORT).show();
        if (isSimExists()) {
            try {
                String SENT = "SMS_SENT";
                //message = txtMessage.getText().toString();
                PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        int resultCode = getResultCode();
                        switch (resultCode) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new IntentFilter(SENT));

                SmsManager smsMgr = SmsManager.getDefault();
                smsMgr.sendTextMessage(phoneNo, null, message, sentPI, null);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage() + "!\n" + "Failed to send SMS", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, SimState + " " + "Cannot send SMS", Toast.LENGTH_LONG).show();
        }
    }





    /*TEST FOR SIM CHECKER*/
    /**
     * @return true if SIM card exists
     * false if SIM card is locked or doesn't exists <br/><br/>
     * <b>Note:</b> This method requires permissions <b> "android.permission.READ_PHONE_STATE" </b>
     */
    public boolean isSimExists()
    {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int SIM_STATE = telephonyManager.getSimState();

        if(SIM_STATE == TelephonyManager.SIM_STATE_READY)
            return true;
        else
        {
            switch(SIM_STATE)
            {
                case TelephonyManager.SIM_STATE_ABSENT: //SimState = "No Sim Found!";
                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED: //SimState = "Network Locked!";
                    break;
                case TelephonyManager.SIM_STATE_PIN_REQUIRED: //SimState = "PIN Required to access SIM!";
                    break;
                case TelephonyManager.SIM_STATE_PUK_REQUIRED: //SimState = "PUK Required to access SIM!"; // Personal Unblocking Code
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN: //SimState = "Unknown SIM State!";
                    break;
            }
            return false;
        }
    }
}
