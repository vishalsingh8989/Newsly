package com.kepler.news.newsly.updateUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.
            //throw new UnsupportedOperationException("Not yet implemented");


            Calendar cal = null;
            cal = Calendar.getInstance();

            Intent intent1= new Intent(context, UpdateDBservice.class);
            PendingIntent pintent = PendingIntent.getService(context, 0, intent1, 0);
            AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);


            //5 seconds
            alarm.cancel(pintent);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 3*60*60*1000, pintent);



            Log.v("UpdateDB", "AlarmReceiver : onReceive ");
        }

}
