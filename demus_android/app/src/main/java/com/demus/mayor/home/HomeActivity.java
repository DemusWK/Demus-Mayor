package com.demus.mayor.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demus.mayor.ContactActivity;
import com.demus.mayor.LoginActivity;
import com.demus.mayor.ManageAccountActivity;
import com.demus.mayor.NotificationStreamActivity;
import com.demus.mayor.PayStackPromptActivity;
import com.demus.mayor.PinResetActivity;
import com.demus.mayor.R;
import com.demus.mayor.RecentTransactionsActivity;
import com.demus.mayor.handlers.UserAccountAPI_Handler;
import com.demus.mayor.home.ui.home.HomeFragment;
import com.demus.mayor.home.ui.refer.ReferFragment;
import com.demus.mayor.home.ui.wallet.WalletFragment;
import com.demus.mayor.models.BankAccount;
import com.demus.mayor.models.ListObject;
import com.demus.mayor.models.PaystackTransaction;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.models.User;
import com.demus.mayor.utils.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hbb20.GThumb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.RechargeContract, NavController.OnDestinationChangedListener, WalletFragment.WalletContract, ReferFragment.ReferContract {
    private DrawerLayout drawerLayout;
    private static User user;
    private NavigationView drawerView;
    private static AppCompatActivity current;
    ProgressDialog progressDialog;
    NavController navController;

    final int PAY_INIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        current = this;
        user = Utility.getUserAccount(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        //drawer nav view
        drawerView = findViewById(R.id.drawer_nav_view);
        drawerView.setNavigationItemSelectedListener(this);
        initSideDrawerView();

        // toolbar
        Toolbar homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        //drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener(this);

        // Set action bar user info
        if(user != null && getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.wallet_id) + user.getPhoneNumber());

        if (user != null && user.getPhoneVerified() == 0) {
            progressDialog.show();

            showPhoneVerificationAlert();
        }
        else
            checkTags();

        // Set Home Fragment Listener
        HomeFragment.setListener(this);
        // Set Refer Fragment Listener
        ReferFragment.setListener(this);
        // Set Wallet Fragment Listener
        WalletFragment.setListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(navController != null) {
            //check nav destination
            int nav = getIntent().getIntExtra("nav_index", 0);
            switch (nav) {
                case 1:
                    navController.navigate(R.id.navigation_home);
                    break;
                case 2:
                    navController.navigate(R.id.navigation_refer);
                    break;
                case 3:
                    navController.navigate(R.id.navigation_wallet);
                    break;
                default:
                    navController.navigate(R.id.navigation_home);
            }

            getIntent().putExtra("nav_index", 0);
        }
    }

    private void initSideDrawerView() {
        View drawerViewHeader = drawerView.getHeaderView(0);
        TextView drawerUserName = drawerViewHeader.findViewById(R.id.drawer_user_name);
        // Set user's full name to the navigation drawer
        String userFullName = user.getFirstName() + " " + user.getLastName();
        drawerUserName.setText(userFullName);
        // Set user's wallet ID to the navigation drawer
        TextView drawerWalletId = drawerViewHeader.findViewById(R.id.drawer_wallet_id);
        String walletIdInfo = getString(R.string.wallet_id) + user.getPhoneNumber();
        drawerWalletId.setText(walletIdInfo);

        // Load and set user's image
        GThumb thumbView = drawerViewHeader.findViewById(R.id.drawer_user_image);
        thumbView.loadThumbForName(user.getProfilePictureURL(), user.getFirstName(), user.getLastName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                Utility.closeSoftKey(this);
                return true;
            case R.id.action_open_messages:
                showNotificationsActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNotificationsActivity() {
        Intent i = new Intent(this, NotificationStreamActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(menuItem.getItemId()) {
            case R.id.drawer_option_load_retail_wallet:
                showLoadWalletActivity();
                return true;
            case R.id.drawer_option_refer_someone:
                navController.navigate(R.id.navigation_refer);
                return true;
            case R.id.drawer_option_reset_pin:
                showResetPinActivity();
                return true;
            case R.id.drawer_option_contact_us:
                showContactUsActivity();
                return true;
            case R.id.drawer_option_sign_out:
                logOut();
                return true;
            case R.id.drawer_option_manage_account:
                showManageAccountActivity();
                return true;
            case R.id.drawer_option_recent_transactions:
                showRecentTransactionsActivity();
                return true;

        }
        return false;
    }

    private void showLoadWalletActivity() {
        Intent intent = new Intent(HomeActivity.this, PayStackPromptActivity.class);
        startActivityForResult(intent, PAY_INIT);
    }

    private void showResetPinActivity() {
        Intent i = new Intent(this, PinResetActivity.class);
        startActivity(i);
    }

    private void showRecentTransactionsActivity() {
        Intent i = new Intent(this, RecentTransactionsActivity.class);
        startActivity(i);
    }

    private void showManageAccountActivity() {
        Intent i = new Intent(this, ManageAccountActivity.class);
        startActivity(i);
    }

    private void showContactUsActivity() {
        drawerLayout.closeDrawer(GravityCompat.START);
        Intent i = new Intent(this, ContactActivity.class);
        startActivity(i);
    }

    private void logOut() {
        //clear data
        new User().deleteAll(HomeActivity.this);
        new BankAccount().deleteAll(HomeActivity.this);
        new ListObject().deleteAllTransactions(HomeActivity.this);
        new PaystackTransaction().deleteAll(HomeActivity.this);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showPhoneVerificationAlert() {
        LayoutInflater li = LayoutInflater.from(current);
        final View promptView = li.inflate(R.layout.prompt_access_code, null);

        AlertDialog alertDialog = new AlertDialog.Builder(current).create();
        alertDialog.setTitle(current.getString(R.string.conf_number));
        alertDialog.setView(promptView);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, current.getString(R.string.confirm_caps), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final String accessCode = ((EditText) promptView.findViewById(R.id.editTextDialogUserInput)).getText().toString();

                if (!accessCode.equals("")) {
                    progressDialog.show();

                    new Thread() {
                        @Override
                        public void run() {
                            ServerResponse response = new UserAccountAPI_Handler().confirmPhoneNumber(accessCode, current, 1);

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("response", response);

                            Message message = new Message();
                            message.setData(bundle);
                            checkAccessCodeHandler.sendMessage(message);
                        }
                    }.start();
                }
                else
                    Toast.makeText(current, current.getString(R.string.access_invalid), Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();

        TextView lblResend = promptView.findViewById(R.id.lblResend);
        lblResend.setOnClickListener(v -> {
            alertDialog.dismiss();
            progressDialog.show();

            new Thread() {
                @Override
                public void run() {
                    ServerResponse response = new UserAccountAPI_Handler().resendAccessCode(Utility.getUserAccount(current).getPhoneNumber(), current, 1);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("response", response);

                    Message message = new Message();
                    message.setData(bundle);
                    resendAccessCodeHandler.sendMessage(message);
                }
            }.start();
        });


    }

    @SuppressLint("HandlerLeak")
    Handler checkAccessCodeHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ServerResponse response = msg.getData().getParcelable("response");
            progressDialog.hide();

            Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();

            if(response.getResponseCode().equals(Utility.STATUS_OK)) {
                user = Utility.getUserAccount(current);

                showEmailVerificationAlert();
            }
            else
                showPhoneVerificationAlert();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler resendAccessCodeHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ServerResponse response = msg.getData().getParcelable("response");

            progressDialog.hide();
            Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();

            LayoutInflater li = LayoutInflater.from(current);
            final View promptView = li.inflate(R.layout.prompt_access_code, null);

            AlertDialog alertDialog = new AlertDialog.Builder(current).create();
            alertDialog.setTitle(current.getString(R.string.conf_number));
            alertDialog.setView(promptView);
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, current.getString(R.string.confirm_caps), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final String accessCode = ((EditText) promptView.findViewById(R.id.editTextDialogUserInput)).getText().toString();

                    if (!accessCode.equals("")) {
                        progressDialog.show();

                        new Thread() {
                            @Override
                            public void run() {
                                ServerResponse response = new UserAccountAPI_Handler().confirmPhoneNumber(accessCode, current, 1);

                                Bundle bundle = new Bundle();
                                bundle.putParcelable("response", response);

                                Message message = new Message();
                                message.setData(bundle);
                                checkAccessCodeHandler.sendMessage(message);
                            }
                        }.start();
                    }
                    else
                        Toast.makeText(current, current.getString(R.string.access_invalid), Toast.LENGTH_LONG).show();
                }
            });
            alertDialog.show();
        }
    };

    private static void showEmailVerificationAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(current).create();
        alertDialog.setTitle(current.getString(R.string.email_ver_title));
        alertDialog.setMessage(current.getString(R.string.email_verification));
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, current.getString(R.string.ok), (dialog, which) -> {
            if(user != null)
                if(current == null || current.getSupportActionBar() == null) return;
                current.getSupportActionBar().setTitle(current.getString(R.string.wallet_id) + Utility.getUserAccount(current).getPhoneNumber());
        });
        alertDialog.show();
    }

    private void checkTags(){
        new Thread() {
            @Override
            public void run() {
                User user = Utility.getUserAccount(current);

                ServerResponse response = new UserAccountAPI_Handler().getTags(user.getPhoneNumber(), user.getPassword(), current.getIntent().getStringExtra("key"), current, 1);

                Bundle bundle = new Bundle();
                bundle.putParcelable("response", response);

                Message message = new Message();
                message.setData(bundle);
                getTagsHandler.sendMessage(message);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    Handler getTagsHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ServerResponse response = msg.getData().getParcelable("response");
            progressDialog.hide();

            if(response.getResponseCode().equals(Utility.STATUS_OK)) {
                if(response.getTags() != null) {
                    if(response.getTags().contains(Utility.TAG_EMAIL_EXPIRED))
                        showEmailVerificationAlert();
                    else {
                        //set email verified
                        user.setEmailVerified(1);
                        user.update(current, user.getUserID());

                        Utility.setUserAccount(user);

                        if(response.getTags().contains(Utility.TAG_PIN_EXPIRED)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(current);
                            builder.setTitle(current.getString(R.string.message));
                            builder.setMessage(current.getString(R.string.pin_expired_msg));

                            builder.setPositiveButton(current.getString(R.string.ok), (dialog, which) -> {
                                Intent i = new Intent(current, PinResetActivity.class);
                                current.startActivity(i);
                            });

                            builder.show();
                        }
                        else {
                            Toast.makeText(current, current.getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    //set email verified
                    user.setEmailVerified(1);
                    user.update(current, user.getUserID());

                    Utility.setUserAccount(user);

                    checkForUpdate();
                }
            } else {
                Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();
            }

        }
    };

    private static void checkForUpdate() {
        new Thread() {
            @Override
            public void run() {
                String response = Utility.checkForUpdate(current);

                Bundle bundle = new Bundle();
                bundle.putString("response", response);

                Message message = new Message();
                message.setData(bundle);
                updateCheckHandler.sendMessage(message);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    static Handler updateCheckHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String response = msg.getData().getString("response");

            if(response != null) {
                if(response.equals(Utility.STATUS_EXPIRED)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(current);
                    builder.setTitle(current.getString(R.string.update));
                    builder.setMessage(current.getString(R.string.new_update_avail));

                    builder.setPositiveButton(current.getString(R.string.yes), (dialog, which) -> {
                        String appPackageName = current.getPackageName();

                        try {
                            current.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        }
                        catch (android.content.ActivityNotFoundException anfe) {
                            current.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    });

                    builder.setNegativeButton(current.getString(R.string.no), (dialog, which) -> dialog.dismiss());

                    builder.show();
                }
            }
        }
    };

    @Override
    public void onRequestComplete() {
        progressDialog.hide();
    }

    @Override
    public void onRecharged() {
        showWallet();
    }

    @Override
    public void onReferred() {
        showWallet();
    }

    @Override
    public void onRequestStarted() {
        progressDialog.show();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        Utility.closeSoftKey(HomeActivity.this);

        shouldSetActionBarTitle(destination);
    }

    private void shouldSetActionBarTitle(NavDestination destination) {
        if (getSupportActionBar() == null) return;

        if(destination.getId() == R.id.navigation_profile) {
            getSupportActionBar().setTitle("");
        } else {
            getSupportActionBar().setTitle(getString(R.string.wallet_id) + user.getPhoneNumber());
        }
    }

    private void showWallet() {
        navController.navigate(R.id.navigation_wallet);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        if (requestCode == PAY_INIT) {
            getIntent().putExtra("nav_index", returnedIntent.getIntExtra("nav_index", 0));
            if (resultCode == RESULT_CANCELED)
                Toast.makeText(current, R.string.transaction_canceled, Toast.LENGTH_LONG).show();
        }
    }
}
