package com.myapplicationdev.android.p06_taskmanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

public class TaskReminderReceiver extends BroadcastReceiver {

    int notifReqCode = 123;

	@Override
	public void onReceive(Context context, Intent i) {

		int id = i.getIntExtra("id", -1);
		String name = i.getStringExtra("name");
		String desc = i.getStringExtra("desc");

		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		android.support.v7.app.NotificationCompat.Action action = new
				android.support.v7.app.NotificationCompat.Action.Builder(
				R.mipmap.ic_launcher,
				context.getString(Integer.parseInt("Launch task manager")),
				pendingIntent).build();

		Intent intentreply = new Intent(context, MainActivity.class); //pass the reply choice

		PendingIntent pendingIntentReply = PendingIntent.getActivity
				(context, 0, intentreply,
						PendingIntent.FLAG_UPDATE_CURRENT);

		RemoteInput ri = new RemoteInput.Builder("status")
				.setLabel("Status report")
				.setChoices(new String [] {"Completed", "Not yet"})
				.build();

		android.support.v7.app.NotificationCompat.Action action2 = new
				android.support.v7.app.NotificationCompat.Action.Builder(
				R.mipmap.ic_launcher,
				"Reply",
				pendingIntentReply)
				.addRemoteInput(ri)
				.build();


		android.support.v7.app.NotificationCompat.WearableExtender extender = new
				android.support.v7.app.NotificationCompat.WearableExtender();
		extender.addAction(action);
		extender.addAction(action2);

		Notification notification = new
				android.support.v7.app.NotificationCompat.Builder(context)
				.setContentText(desc)
				.setContentTitle("Task: " + name)
				.setSmallIcon(R.mipmap.ic_launcher)
				// When Wear notification is clicked, it performs
				// the action we defined in line below
				.extend(extender)
				.build();

		NotificationManagerCompat notificationManagerCompat =
				NotificationManagerCompat.from(context);
		notificationManagerCompat.notify(notifReqCode, notification);;
	}

}
