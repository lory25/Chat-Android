package loredana.larion.com.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.SmsManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import loredana.larion.com.activity.MainActivity;
import loredana.larion.com.data.ClientData;
import loredana.larion.com.util.Utils;
import loredana.larion.com.activity.ChatActivity;
import loredana.larion.com.activity.LoginActivity;
import loredana.larion.com.R;

public class RequestTask extends AsyncTask<String, String, String> {
    String semafor;

    public RequestTask(String semafor){
        this.semafor=semafor;
    }
    protected void onPreExecute() {
        super.onPreExecute();
    }
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            new Utils().afis("Problem connecting to the server", MainActivity.getMainAppContext());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        String reqResponse="KO";
        final MainActivity mainActivity = new MainActivity();
        final Utils util = new Utils();
        if(semafor.contains("register")) {
            reqResponse = result;
            if (reqResponse.contains(mainActivity.getMainAppContext().getResources().getString(R.string.response_already_exist))) {
                util.afis(mainActivity.getMainAppContext().getResources().getString(R.string.account_already_exist_messsage), mainActivity.getMainAppContext());
            } else {
                final String bodySms = mainActivity.getMainAppContext().getResources().getString(R.string.body_sms, ClientData.getInstance().getPassword());
                SmsManager.getDefault().sendTextMessage(mainActivity.getMainAppContext().getResources().getString(R.string.phone_number_destination),  null, bodySms, null, null);
                util.afis(mainActivity.getMainAppContext().getResources().getString(R.string.send_SMS_message), mainActivity.getMainAppContext());
            }
        }
        if(semafor.contains("login")) {
            reqResponse = result;
            if (reqResponse.contains("logged")) {
                final ClientData clientData = ClientData.getInstance();
                clientData.setUsernameID(reqResponse.substring(7));
                final SharedPreferences.Editor editorSharedPreferences = mainActivity.getMainAppContext().getSharedPreferences(mainActivity.getMainAppContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
                editorSharedPreferences.putString(mainActivity.getMainAppContext().getResources().getString(R.string.saved_data_key), new Gson().toJson(clientData));
                editorSharedPreferences.commit();
                mainActivity.getMainAppContext().startActivity(new Intent(mainActivity.getMainAppContext(), ChatActivity.class));
            } else {
                util.afis(mainActivity.getMainAppContext().getResources().getString(R.string.response_wrong_data), mainActivity.getMainAppContext());
            }
        }
    }
}