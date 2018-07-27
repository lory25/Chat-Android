package loredana.larion.com.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.Map;

import loredana.larion.com.R;


public class Utils {
    boolean isOnline = false;

    public void afis(String s, Context context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public String requestCommand (Context context, String typeAction, Map<String, String> mapParams) {
        String command = context.getResources().getString(R.string.protocol) + context.getResources().getString(R.string.host) + context.getResources().getString(R.string.app_name_php);
        if (typeAction != null) {
            command = command + typeAction + ".php";
        }
        if (mapParams != null) {
            command = command + "?";
            for (Map.Entry<String, String> entry : mapParams.entrySet()) {
                command = command + entry.getKey() + "=" + entry.getValue();
                if (mapParams.size() > 1) {
                    command = command + "&";
                }
            }
        }
        return command;
    }

    public boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo wifiInfo, mobileInfo;
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(wifiInfo.isConnected() || mobileInfo.isConnected()) {
                return true;
            }
        } catch (Exception ex) {
            afis("Network error. Try reconnecting...", context);
            System.out.println("CheckConnectivity Exception: " + ex.getMessage());
        }
        return false;
    }

    //this needs realtime database from Firebase, for see who is online/active/offline
    public boolean getOnlinePresence(String userId) {
        final Firebase userRef = new Firebase("https://loredana.larion.com.firebaseio.com/presence/" + userId);
        final Firebase connectedRef = new Firebase("https://loredana.larion.com.firebaseio.com/.info/connected");
        //final Firebase lastOnlineRef = new Firebase("https://loredana.larion.com.firebaseio.com/users/lory/lastOnline");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    userRef.onDisconnect().removeValue();
                    userRef.setValue(true);
                    isOnline = true;
                    System.out.println("connected " + userRef.getApp());
                } else {
                    isOnline = false;
                }
            }
            @Override
            public void onCancelled(FirebaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        });
        return isOnline;
    }

}
