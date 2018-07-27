package loredana.larion.com.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loredana.larion.com.R;
import loredana.larion.com.data.ClientData;
import loredana.larion.com.util.ListViewAdapter;
import loredana.larion.com.util.Utils;

public class ChatActivity extends AppCompatActivity {
    private EditText chatMessage;
    private ListView listView;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {
        // super.onBackPressed(); // Comment this super call to avoid calling finish() from device button
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.exit_title_dialog))
                .setMessage(getResources().getString(R.string.exit_messsage_dialog))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ChatActivity.super.onBackPressed();
                        final SharedPreferences.Editor sharedPreferencesEditor = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
                        sharedPreferencesEditor.remove("SAVE_DATA");
                        sharedPreferencesEditor.apply();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                }).create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        chatMessage = (EditText) findViewById(R.id.chatMessage);
        listView = (ListView) findViewById(R.id.listView);
        new ReqTask(getResources().getString(R.string.semafor_update)).execute(new Utils().requestCommand(getApplicationContext(), getResources().getString(R.string.semafor_mesaje), null));
        final String token = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("TOKEN_USER", "");
        if (!token.equals("") ) {
            new Utils().getOnlinePresence(token);
        }
    }

    public void setListView(String[] t){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, t) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.colorWhite));
               // textView.setBackground(getResources().getDrawable(R.drawable.bubble_chat));
                textView.setSingleLine(false);
                return textView;
            }
        };
        listView.setAdapter(adapter) ;

        if (t != null) {
            listView.setClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getApplicationContext(),listView.getItemAtPosition(i).toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    //onClick send  message write it
    public void sendChatMessage (View view) {
        final String chatMessageText = chatMessage.getText().toString();
        final Map<String, String> mapParams = new HashMap<String, String>();
        final Utils util = new Utils();
        if (chatMessageText.length() > 0) {
            mapParams.put("text", chatMessageText);
            mapParams.put("id", ClientData.getInstance().getUsernameID());
            new ReqTask(getResources().getString(R.string.semafor_send)).execute(util.requestCommand(getApplicationContext(), getResources().getString(R.string.semafor_send), mapParams));

            // after message is sent, clear the input
            chatMessage.setText("");
            new ReqTask(getResources().getString(R.string.semafor_update)).execute(util.requestCommand(getApplicationContext(), getResources().getString(R.string.semafor_mesaje), null));
            mapParams.clear();
            mapParams.put("mesaj", ClientData.getInstance().getUsername());
            new ReqTask(getString(R.string.semafor_notificare)).execute(util.requestCommand(getApplicationContext(), getString(R.string.semafor_notificare), mapParams));
        }
        else{
            util.afis(getResources().getString(R.string.empty_message_chat), getApplicationContext());
            new ReqTask(getResources().getString(R.string.semafor_update)).execute(util.requestCommand(getApplicationContext(), getResources().getString(R.string.semafor_mesaje), null));
        }
    }

    public void logout(View view) {
        onBackPressed();
    }

    public class ReqTask extends AsyncTask<String, String, String> {
        String semafor;

        public ReqTask(String semafor){
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
            if(semafor.contains("update")){
                final String[] li= result.split("<br>");
                setListView(li);
            }
        }
    }
}

