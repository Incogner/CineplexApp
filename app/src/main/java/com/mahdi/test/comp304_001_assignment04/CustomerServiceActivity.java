package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Objects;

public class CustomerServiceActivity extends AppCompatActivity {

    //views
    EditText txtMessage;
    Button btnSend;
    ListView textList;
    Activity activity = this;
    BroadcastReceiver smsReceiver;

    //constants
    private static final String TAG = CustomerServiceActivity.class.getSimpleName();
    // MY_PERMISSIONS_REQUEST_SEND_SMS is an
    // app-defined int constant. The callback method gets the
    // result of the request.
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 2;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 3;
    private static final String address = "6505551212";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        //set toolbar name and back button
        Toolbar toolbar = findViewById(R.id.toolbarCustomerService);
        toolbar.setTitle("Customer Service");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //optimize layout with soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //define layout views
        btnSend = findViewById(R.id.btnSendSms);
        txtMessage = findViewById(R.id.txtMessage);
        txtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0 || count > 0) {
                    btnSend.setEnabled(true);
                } else {
                    btnSend.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textList = findViewById(R.id.textList);
        //send click listener
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send sms
                sendSms(address, txtMessage.getText().toString());
                txtMessage.setText("");
            }
        });

        // Check for all permissions for SMS and enable and disable send Button
        checkForSmsPermission();
        btnSend.setEnabled(false);

        //this method loads list of sms from content://sms/
        loadSMS();

        //this method will register the SMS Receiver BroadcastReceiver
        receiveSmsBroadCast();

    }//end of onCreate method

    //method to retrieve sms and set data to list view to display
    public void loadSMS() {
        Statics.MESSAGES.clear();
        readAllSMS(address);
        Collections.reverse(Statics.MESSAGES); // Reverse the order of SMS list
        //Statics.MESSAGES.notifyDataSetChanged;
        TextListAdapter adapter = new TextListAdapter(this, Statics.MESSAGES);
        textList.setAdapter(adapter);
        //textList.invalidateViews();
    }

    //method to check and request permissions
    private void checkForSmsPermission() {
        boolean sendSmsNotGranted = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED;
        boolean receiveSmsNotGranted = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED;
        boolean readSmsNotGranted = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED;
        if (sendSmsNotGranted) {
            Log.d(TAG, getString(R.string.send_permission_not_granted));
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        if (receiveSmsNotGranted) {
            Log.d(TAG, getString(R.string.receive_permission_not_granted));
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_RECEIVE_SMS);

        }
        if (readSmsNotGranted) {
            Log.d(TAG, getString(R.string.read_permission_not_granted));
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);
        }
        if (!sendSmsNotGranted && !readSmsNotGranted && !receiveSmsNotGranted) {
            // Permission already granted. Enable the send button.
            enableSend();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Enable sms button.
                    enableSend();
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();
                    // Disable the sms button.
                    disableSend();
                }
            }
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Enable sms button.
                    enableSend();
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();
                    // Disable the sms button.
                    disableSend();
                }
            }
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.READ_SMS)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Enable sms button.
                    enableSend();
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();
                    // Disable the sms button.
                    disableSend();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        //unregister the broadcast receiver
        unregisterReceiver(smsReceiver);
        super.onDestroy();
    }

    public void enableSend() {
        btnSend.setEnabled(true);
        txtMessage.setEnabled(true);
    }

    public void disableSend() {
        btnSend.setEnabled(false);
        txtMessage.setEnabled(false);
    }

    //method to register SMS Receiver
    public void receiveSmsBroadCast() {

        //---when the SMS Received it will be added to the list---
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get the SMS message.
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs;
                assert bundle != null;
                String format = bundle.getString("format");
                // Retrieve the SMS message received.
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    // Check the Android version.
                    boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                    // Fill the msgs array.
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        // Check Android version and use appropriate createFromPdu.
                        if (isVersionM) {
                            // If Android version M or newer:
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                        } else {
                            // If Android version L or older:
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        }//end of if
                        //using SMS object
                        long millis = new java.util.Date().getTime();
                        SMSObject newSMS = new SMSObject();
                        newSMS.smsBody = msgs[i].getMessageBody();
                        newSMS.address = msgs[i].getOriginatingAddress();
                        newSMS.date = millis;
                        newSMS.type = 1;
                        Statics.MESSAGES.add(newSMS);//add to the list
                    }//end of for
                }//end of if
                //load the list again
                TextListAdapter adapter = new TextListAdapter(activity, Statics.MESSAGES);
                textList.setAdapter(adapter);
            }//end of onReceive
        };//end broadcast object

        //---Registering the receiver---
        registerReceiver(smsReceiver,
                new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        //Alternative to register the SMS Receiver while its a separate class
        //SmsReceiver otp = new SmsReceiver();
        //IntentFilter filter = new IntentFilter();
        //filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //registerReceiver(otp, filter);

    }//end of receiveSmsBroadcast method

    //method to send sms and receive broadcasts for it
    public void sendSms(String phoneNumber, String message) {

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                loadSMS();
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    //method to retrieve sms from android device content
    public void readAllSMS(String address) {
        final String SMS_URI_OUTBOX = "content://sms/sent";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = getContentResolver().query(uri, projection, "address='" + address + "'", null, "date desc");
            assert cur != null;
            if (cur.moveToFirst()) {
                int index_id = cur.getColumnIndex("_id");
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    SMSObject newSMS = new SMSObject();
                    newSMS.id = cur.getLong(index_id);
                    newSMS.address = cur.getString(index_Address);
                    newSMS.person = cur.getInt(index_Person);
                    newSMS.smsBody = cur.getString(index_Body);
                    newSMS.date = cur.getLong(index_Date);
                    newSMS.type = cur.getInt(index_Type);
                    Statics.MESSAGES.add(newSMS);
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                Toast.makeText(this, "No SMS Found!", Toast.LENGTH_SHORT).show();
            } // end if

        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }//end of readAllSMS

}//end of Activity Class
