package nortti.ru.vardicall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import nortti.ru.vardicall.Utils.CallReceiver;

public class MainActivity extends AppCompatActivity {
    TextView info;
    private static MainActivity ins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.info);
        ins = this;
    }

    public static MainActivity  getInstace(){
        return ins;
    }

    public void update(final String msg){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                info.setText(msg);
            }
        });
    }


}
