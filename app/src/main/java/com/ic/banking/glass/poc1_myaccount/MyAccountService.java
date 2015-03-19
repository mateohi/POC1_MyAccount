package com.ic.banking.glass.poc1_myaccount;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Pair;
import android.widget.RemoteViews;

public class MyAccountService extends Service {

    private static final String LIVE_CARD_TAG = MyAccountService.class.getSimpleName();

    private LiveCard liveCard;

    private final AccountBinder binder = new AccountBinder();

    public class AccountBinder extends Binder {

        public void updateView() {
            liveCard.setViews(randomizeView());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (liveCard == null) {
            liveCard = new LiveCard(this, LIVE_CARD_TAG);
            liveCard.setViews(randomizeView());

            // Display the options menu when the live card is tapped.
            Intent menuIntent = new Intent(this, LiveCardMenuActivity.class);
            liveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
            liveCard.publish(PublishMode.REVEAL);
        } else {
            liveCard.navigate();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (liveCard != null && liveCard.isPublished()) {
            liveCard.unpublish();
            liveCard = null;
        }
        super.onDestroy();
    }

    private RemoteViews randomizeView() {
        Pair<Integer, Integer> pair = AccountUtils.randomBalanceAndAccountNumbers();

        String balance = "Account balance \n" + "U$S " + pair.first;
        String accountNumber = AccountUtils.maskAllButLastFour(pair.second);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.my_account);
        remoteViews.setTextViewText(R.id.account_balance, balance);
        remoteViews.setTextViewText(R.id.footer, accountNumber);
        remoteViews.setTextViewText(R.id.timestamp, "just now");

        return remoteViews;
    }
}
