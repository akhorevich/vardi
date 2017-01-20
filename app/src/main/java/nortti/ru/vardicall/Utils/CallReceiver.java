package nortti.ru.vardicall.Utils;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import nortti.ru.vardicall.MainActivity;
import nortti.ru.vardicall.MyToast;
import nortti.ru.vardicall.Number;
import nortti.ru.vardicall.R;

public class CallReceiver extends BroadcastReceiver {
    // This String will hold the incoming phone number
    private String number;
    CustomDialog dialog;
    TelephonyManager telephonyManager;
    PhoneStateListener listener;
    Context context;
    Number mNumber;
    String nameString;
    long phoneString;
    public static final String TAG = CallReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {


        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("phones");

        // If, the received action is not a type of "Phone_State", ignore it
        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
            return;

            // Else, try to do some action
        else {
            this.context = context;
            if(dialog == null){
                dialog = new CustomDialog(context);
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();
            }
            // Fetch the number of incoming call

            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            listener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    String stateString = "N/A";
                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE:
                            stateString = "Idle";
                            dialog.dismiss();
                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            stateString = "Off Hook";
                            dialog.dismiss();
                            break;
                        case TelephonyManager.CALL_STATE_RINGING:
                            stateString = "Ringing";
                            final String incoming = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        mNumber = snapshot.getValue(Number.class);
                                        if (incoming.contains(String.valueOf(String.valueOf(mNumber.getPhone())))){
                                            nameString = mNumber.getName();
                                            phoneString = mNumber.getPhone();
                                            dialog.setNameStr(nameString);
                                            dialog.setPhoneStr(phoneString);
                                            dialog.show();
                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            break;
                    }
                    Toast.makeText(context, stateString,Toast.LENGTH_LONG).show();
                }
            };

            // Register the listener with the telephony manager
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

            // Check, whether this is a member of "Black listed" phone numbers
            // stored in the database
			/*if (MainActivity.blockList.contains(new Blacklist(number))) {
				// If yes, invoke the method
				disconnectPhoneItelephony(context);
				return;
			}*/
        }




    }



    class CustomDialog extends Dialog{

        String nameStr;
        long phoneStr;

        public long getPhoneStr() {
            return phoneStr;
        }

        public void setPhoneStr(long phoneStr) {
            this.phoneStr = phoneStr;
        }

        public String getNameStr() {
            return nameStr;
        }

        public void setNameStr(String nameStr) {
            this.nameStr = nameStr;
        }

        public CustomDialog(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            setContentView(R.layout.custom_view);

            TextView name = (TextView) findViewById(R.id.text_info);
            if (nameString != null){
                name.setText(getNameStr());

            }

            TextView phone = (TextView) findViewById(R.id.text_phone);
            if (phoneString != 0){
                phone.setText(String.valueOf(getPhoneStr()));

            }


        }

    }
}
