package loredana.larion.com.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import loredana.larion.com.R;
import loredana.larion.com.data.ClientData;
import loredana.larion.com.service.RequestTask;
import loredana.larion.com.util.Utils;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private final int REQUEST_ACCESS_NETWORK = 1;
    private final int REQUEST_ACCESS_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
    }


    private void askPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_NETWORK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(LoginActivity.this, "Permission granted for NETWORK", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Permission denied for NETWORK", Toast.LENGTH_SHORT).show();
                }
            case REQUEST_ACCESS_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(LoginActivity.this, "Permission granted for SMS", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Permission denied for SMS", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void getLogged(View view) {
        final String usernameValue = username.getText().toString();
        final String passwordValue = password.getText().toString();

        if(usernameValue.length() > 0 && passwordValue.length() > 0) {
            final Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("username", usernameValue);
            mapParams.put("password", passwordValue);
            final ClientData clientData = ClientData.getInstance();
            clientData.setUsername(usernameValue);
            clientData.setPassword(passwordValue);
            askPermission(Manifest.permission.ACCESS_NETWORK_STATE, REQUEST_ACCESS_NETWORK);
           if (! new Utils().hasInternetConnection(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            } else {
                new RequestTask(getString(R.string.semafor_login)).execute(new Utils().requestCommand(getApplicationContext(), null, mapParams));
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_inputs),Toast.LENGTH_LONG).show();
        }
    }

    public void getRegistered(View view) {
        final String usernameValue = username.getText().toString();
        final String passwordValue = password.getText().toString();
        final ClientData clientData = ClientData.getInstance();
        clientData.setUsername(usernameValue);
        clientData.setPassword(passwordValue);

        if (passwordValue.length() == 0 || passwordValue.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_inputs), Toast.LENGTH_SHORT).show();
        } else {
            final Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("username", usernameValue);
            mapParams.put("password", passwordValue);
            askPermission(Manifest.permission.ACCESS_NETWORK_STATE, REQUEST_ACCESS_NETWORK);
            askPermission(Manifest.permission.SEND_SMS, REQUEST_ACCESS_SMS);
            if (!new Utils().hasInternetConnection(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            } else {
                new RequestTask(getResources().getString(R.string.semafor_register)).execute(new Utils().requestCommand(getApplicationContext(), getString(R.string.semafor_register), mapParams));
            }
        }
    }

    @Override
    public void onBackPressed() { }
}

