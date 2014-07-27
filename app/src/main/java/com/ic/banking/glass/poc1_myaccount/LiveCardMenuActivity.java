package com.ic.banking.glass.poc1_myaccount;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

public class LiveCardMenuActivity extends Activity {

    private static final String TAG = LiveCardMenuActivity.class.getSimpleName();

    private MyAccountService.AccountBinder accountBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof MyAccountService.AccountBinder) {
                accountBinder = (MyAccountService.AccountBinder) service;
                openOptionsMenu();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Do nothing.
        }
    };

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Open the options menu right away.
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_account, menu);
        bindService(new Intent(this, MyAccountService.class), this.serviceConnection, 0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stop:
                stopService(new Intent(this, MyAccountService.class));
                return true;
            case R.id.action_refresh:
                this.accountBinder.updateView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        unbindService(this.serviceConnection);
        finish();
    }
}
