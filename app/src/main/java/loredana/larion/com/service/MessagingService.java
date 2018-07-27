package loredana.larion.com.service;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import loredana.larion.com.R;
import loredana.larion.com.activity.MainActivity;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        notificationCall(message.getNotification().getBody());
    }

    private void notificationCall(String sender) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icongreen)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icongreen))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(getApplicationContext().getResources().getString(R.string.notification_title))
                .setContentText(getApplicationContext().getResources().getString(R.string.notification_message, sender))
                .setContentInfo("Info")
                .setSound(soundUri)
                .setOngoing(true)
                .setAutoCancel(true);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001, notificationBuilder.build());
    }
}
