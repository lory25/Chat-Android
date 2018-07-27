package loredana.larion.com.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import loredana.larion.com.R;
import loredana.larion.com.activity.MainActivity;
import loredana.larion.com.data.ClientData;

public class FirebaseMessagingInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        //Displaying token registration on logcat
        final String tokenUser = FirebaseInstanceId.getInstance().getToken();

        Log.d("CLOUD MESSAGING TOKEN: ", tokenUser);
        //ClientData.getInstance().setTokenFirebase(tokenUser);
    }
}
