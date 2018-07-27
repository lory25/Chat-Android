package loredana.larion.com.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import loredana.larion.com.R;
import loredana.larion.com.data.ClientData;
import loredana.larion.com.util.Utils;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    public static Context getMainAppContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Firebase.setAndroidContext(this);

//        final SharedPreferences.Editor editorSharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
//        editorSharedPreferences.putString(context.getResources().getString(R.string.token), "AAAA_UxLfmM:APA91bE6DkbIe_-B57y7dMKMRiSCvWv6esWS93XdRwTxO_X0zjdgDGkeuhxI_mD1QNlSunjqG9y1JtqAK8Uee-NKTdjFXOLdg3Q_rBvUadTwdYjwjvo9Jkq9Tv80ws6qNi5swT6rIGnbwJEHlKCxTiDQh9LKA7B1jw");
//        editorSharedPreferences.commit();

        if (! new Utils().hasInternetConnection(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.no_internet))
                    .setMessage(getResources().getString(R.string.no_internet_text_alert))
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).create().show();
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("all");
            final SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

            Intent intent = null;
            if (!sharedPreferences.getString(context.getString(R.string.saved_data_key), "").equals("")) {
                intent = new Intent(this, ChatActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
        }
    }
}
