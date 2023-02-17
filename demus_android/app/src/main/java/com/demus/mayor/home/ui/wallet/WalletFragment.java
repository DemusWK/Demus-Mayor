package com.demus.mayor.home.ui.wallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.demus.mayor.PayStackPromptActivity;
import com.demus.mayor.PinResetActivity;
import com.demus.mayor.R;
import com.demus.mayor.RecentTransactionsActivity;
import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.utils.Utility;

import java.text.DecimalFormat;

@SuppressLint("StaticFieldLeak")
public class WalletFragment extends Fragment {
    private static FragmentActivity current;

    static private TextView lblAmount, lblBonusAmount;
    private static WalletContract _listener;

    private final int PAY_INIT = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wallet, container, false);

        setUpView(root);
        return root;
    }

    public static void setListener(WalletContract walletContract) {
       _listener = walletContract;
    }

    private void setUpView(View root) {
        TextView rechargeBtn = root.findViewById(R.id.wallet_recharge_btn_top);
        rechargeBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_home));

        TextView referBtn = root.findViewById(R.id.wallet_refer_btn);
        referBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_refer));

        TextView walletBtn = root.findViewById(R.id.wallet_wallet_btn);
        walletBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_wallet));

        initializeUI(root);
        checkBalance();
    }

    /**
     * This method initializes the User Interface controls.
     */
    private void initializeUI(View root) {
        if(getActivity() == null) return;
        current = getActivity();
        lblAmount = root.findViewById(R.id.lblAmount);
        lblBonusAmount = root.findViewById(R.id.lblBonusAmount);

        //recent transaction
        root.findViewById(R.id.wallet_recent_transactions).setOnClickListener((View view) -> {
            if (getActivity() == null) return;
            Intent intent = new Intent(getActivity(), RecentTransactionsActivity.class);
            startActivity(intent);
        });

        root.findViewById(R.id.wallet_load_retail_wallet).setOnClickListener((View view) -> {
            if (getActivity() == null) return;
            Intent intent = new Intent(getActivity(), PayStackPromptActivity.class);
            getActivity().startActivityForResult(intent, PAY_INIT);
        });
    }

    private static void checkBalance() {
        _listener.onRequestStarted();
        new Thread() {
            @Override
            public void run() {
                ServerResponse response = new TransactionAPI_Handler().checkBalance(current, 1);

                Bundle bundle = new Bundle();
                bundle.putParcelable("response", response);

                Message message = new Message();
                message.setData(bundle);
                genericHandler.sendMessage(message);
            }
        }.start();
    }

    @SuppressLint({"HandlerLeak", "InflateParams"})
    private static Handler genericHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            ServerResponse response = msg.getData().getParcelable("response");
            _listener.onRequestComplete();


            if(response != null && response.getResponseCode().equals(Utility.STATUS_OK)) {
                //show balance
                double balance = response.getExtraDoubleValue();
                DecimalFormat formatter = new DecimalFormat("#,###.00");
                String naira_symbol = "₦";

                if(balance > 0.0) {
                    String mainBalance = naira_symbol + formatter.format(balance);
                    lblAmount.setText(mainBalance);
                }else{
                    String zeroBalance = naira_symbol + current.getString(R.string.zero_balance_value);
                    lblAmount.setText(zeroBalance);
                }

                //show bonus amount
                double bonusBalance = response.getExtraDoubleValue2();

                if(bonusBalance > 0.0){
                    String bonus = naira_symbol + formatter.format(bonusBalance);
                    lblBonusAmount.setText(bonus);
                } else{
                    String zeroBonus = naira_symbol + current.getString(R.string.zero_balance_value);
                    lblBonusAmount.setText(zeroBonus);
                }

            } else {
                if(response == null) return;
                Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();

                if(response.getResponseMessage().contains("expired")) {
                    Intent i = new Intent(current, PinResetActivity.class);
                    current.startActivity(i);
                    return;
                }

                LayoutInflater li = LayoutInflater.from(current);
                View newView = li.inflate(R.layout.prompt_important_message, null);
                AlertDialog alertDialog = new AlertDialog.Builder(current).create();
                alertDialog.setView(newView);
                alertDialog.setCancelable(true);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, current.getString(R.string.ok), (dialog, which) -> dialog.dismiss());

                TextView lblMessage = newView.findViewById(R.id.lblMessage);
                lblMessage.setText(R.string.account_balance_retry);

                alertDialog.show();
            }
        }
    };

    public interface WalletContract {
        void onRequestComplete();
        void onRequestStarted();
    }
}