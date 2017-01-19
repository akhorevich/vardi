package nortti.ru.vardicall;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import nortti.ru.vardicall.Utils.CallReceiver;

public class MainActivity extends AppCompatActivity {
    TextView info, phone;
    View view;
    private static MainActivity ins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ins = this;
        //LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

    }

    public static MainActivity  getInstace(){
        return ins;
    }

    public void update(final String infoMsg, final String phoneMsg){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view = LayoutInflater.from(ins).inflate(R.layout.custom_view, null);
                info = (TextView) view.findViewById(R.id.text_info);
                phone = (TextView) view.findViewById(R.id.text_phone);
                info.setText(infoMsg);
                phone.setText(phoneMsg);
                Crouton crouton = Crouton.make(ins, view);
                crouton.show();

            }
        });
    }


}
