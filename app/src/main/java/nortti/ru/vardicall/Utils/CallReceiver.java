package nortti.ru.vardicall.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nortti.ru.vardicall.MainActivity;
import nortti.ru.vardicall.MyToast;
import nortti.ru.vardicall.Number;

public class CallReceiver extends BroadcastReceiver {
    public CallReceiver() {
    }
    public static final String TAG = CallReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("phones");

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
            final String incoming = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Number number = snapshot.getValue(Number.class);
                        long numberStr = number.getPhone();
                        if (incoming.contains(String.valueOf(numberStr))){
                            Log.d(TAG,number.getName() + " is calling");
                            String msg = number.getName();
                            MyToast.show(context, msg, number.getPhone());
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE) || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            Toast.makeText(context, "Call detected", Toast.LENGTH_SHORT).show();
        }
    }
}
