package nortti.ru.vardicall;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {
    public static void show(Context context, String name, long message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_view, null);

        TextView textName = (TextView) layout.findViewById(R.id.text_info);
        textName.setText(name);

        TextView textPhone = (TextView) layout.findViewById(R.id.text_phone);
        textPhone.setText(String.valueOf(message));

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
