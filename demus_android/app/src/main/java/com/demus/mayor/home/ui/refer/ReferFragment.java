package com.demus.mayor.home.ui.refer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.demus.mayor.DemusMayorApplication;
import com.demus.mayor.R;
import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.models.Product;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.utils.Utility;

import java.math.BigDecimal;


public class ReferFragment extends Fragment {
    private static final String TAG = "ReferFragment";

    private EditText txtPhoneNumber, txtAmount, txtPin;

    private AlertDialog alertDialog;

    private Product referent;

    private static FragmentActivity current;

    private static ReferContract listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_refer, container, false);

        current = this.getActivity();

        setUpUi(root);
        return root;
    }

    public static void setListener(ReferContract viewListener) {
        listener = viewListener;
    }

    private void setUpUi(View root) {
        TextView rechargeBtn = root.findViewById(R.id.refer_recharge_btn_top);
        rechargeBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_home));

        TextView referBtn = root.findViewById(R.id.refer_refer_btn);
        referBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_refer));

        TextView walletBtn = root.findViewById(R.id.refer_wallet_btn);
        walletBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_wallet));

        txtPhoneNumber = (EditText) root.findViewById(R.id.txtPhoneNumber);

        txtAmount = (EditText) root.findViewById(R.id.txtAmount);
        InputFilter filter = new InputFilter() {
            final int maxDigitsBeforeDecimalPoint = 6;
            final int maxDigitsAfterDecimalPoint = 2;

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source
                        .subSequence(start, end).toString());
                if (!builder.toString().matches(
                        "(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint-1)
                                + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?"
                )) {
                    if(source.length()==0)
                        return dest.subSequence(dstart, dend);

                    return "";
                }

                return null;

            }
        };
        txtAmount.setFilters(new InputFilter[] { filter });

        txtPin = (EditText) root.findViewById(R.id.txtPin);

        Button cmdSend = (Button) root.findViewById(R.id.cmdSend);
        cmdSend.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                listener.onRequestStarted();

                String amount = "0";
                if(!txtAmount.getText().toString().equals(""))
                    amount = txtAmount.getText().toString();

                if (txtPhoneNumber.getText().toString().equals("") ||
                        txtPin.getText().toString().equals("")) {
                    listener.onRequestComplete();

                    // create alert dialog
                    Toast.makeText(ReferFragment.this.getContext(), R.string.error_incomplete, Toast.LENGTH_LONG).show();

                    return;
                }
                if (txtPhoneNumber.getText().toString().equals(Utility.getUserAccount(ReferFragment.this.getContext()).getPhoneNumber())) {
                    listener.onRequestComplete();

                    // create alert dialog
                    Toast.makeText(ReferFragment.this.getContext(), R.string.refer_error, Toast.LENGTH_LONG).show();

                    return;
                }
                if(Integer.parseInt(amount) > 20000) {
                    listener.onRequestComplete();

                    // create alert dialog
                    Toast.makeText(ReferFragment.this.getContext(), R.string.max_20k, Toast.LENGTH_LONG).show();
                }
                else {
                    referent = new Product();
                    referent.setReceiverNumber(txtPhoneNumber.getText().toString());
                    referent.setAmount(new BigDecimal(amount));
                    referent.setPin(txtPin.getText().toString());

                    LayoutInflater li = LayoutInflater.from(ReferFragment.this.getContext());
                    View newView = li.inflate(R.layout.prompt_confirm_referral, null);

                    ((TextView) newView.findViewById(R.id.lblName)).setText("");
                    ((TextView) newView.findViewById(R.id.lblPhoneNumber)).setText(getString(R.string.ref_number) + referent.getReceiverNumber());
                    ((TextView) newView.findViewById(R.id.lblAmount)).setText(getString(R.string.ref_amt) + getString(R.string.naira_sign) + referent.getAmount().toString());

                    alertDialog = new AlertDialog.Builder(ReferFragment.this.getContext()).create();
                    alertDialog.setView(newView);
                    alertDialog.setCancelable(false);

                    ((ImageView) newView.findViewById(R.id.imgX)).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            listener.onRequestComplete();

                            alertDialog.dismiss();
                        }
                    });

                    ((Button) newView.findViewById(R.id.cmdSend)).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //spin thread
                            doReferral(referent);

                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }
            }
        });
    }

    private void doReferral(final Product referent) {
        listener.onRequestStarted();
        new Thread() {
            @Override
            public void run() {
                ServerResponse response = new TransactionAPI_Handler().refer(referent, getContext(), 1);

                Bundle bundle = new Bundle();
                bundle.putParcelable("response", response);

                Message message = new Message();
                message.setData(bundle);
                referralHandler.sendMessage(message);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private static Handler referralHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ServerResponse response = msg.getData().getParcelable("response");

            if(current != null) {
                Toast.makeText(current, response.getResponseMessage(), Toast.LENGTH_LONG).show();

                listener.onRequestComplete();

                if(response.getResponseCode().equals(Utility.STATUS_OK))
                    listener.onReferred();
            }
            else
                Toast.makeText(DemusMayorApplication.getAppContext(), response.getResponseMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public interface ReferContract {
        void onReferred();
        void onRequestComplete();
        void onRequestStarted();
    }
}