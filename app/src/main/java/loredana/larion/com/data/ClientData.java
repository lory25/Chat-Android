package loredana.larion.com.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import loredana.larion.com.R;
import loredana.larion.com.activity.MainActivity;

public class ClientData {
    private String username;
    private String usernameID;
    private String password;
    private static String tokenFirebase = "";

    //wee need singleton of client data in our app
    private static ClientData instance = null;

    // constructor only to defeat instantiation.
    protected ClientData() { }

    public static ClientData getInstance() {
        final Context context = new MainActivity().getMainAppContext();
        final SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (instance == null) {
            if(!sharedPreferences.getString(context.getString(R.string.saved_data_key), "").equals("")) {
                instance = (ClientData) new Gson().fromJson(sharedPreferences.getString(context.getString(R.string.saved_data_key), ""), ClientData.class);
            } else {
                instance = new ClientData();
            }
        }
        return instance;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setUsernameID(String usernameID) {
        this.usernameID = usernameID;
    }

    public String getUsernameID() {
        return usernameID;
    }

    public void setTokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
    }
    public String getTokenFirebase() {
        return tokenFirebase;
    }
}
