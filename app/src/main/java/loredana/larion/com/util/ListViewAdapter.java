package loredana.larion.com.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import loredana.larion.com.R;
import loredana.larion.com.activity.ChatActivity;
import loredana.larion.com.data.ClientData;

public class ListViewAdapter  extends ArrayAdapter<String> {
        private int layout;


        public ListViewAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            layout = resource;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = new ViewHolder();
            String item=getItem(position);
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.text =  convertView.findViewById(R.id.textChat);
                viewHolder.text.setText(item);
                convertView.setTag(viewHolder);
                //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(new ChatActivity(), android.R.layout.list_view, listMessages);
            } else {
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.text.setText(getItem(position));
            }
            return convertView;
        }

    class ViewHolder {
        TextView text;
        Button button;

        String getString(){
            return (String) text.getText();
        }
    }
}

