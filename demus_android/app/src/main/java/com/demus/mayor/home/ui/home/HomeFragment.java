package com.demus.mayor.home.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.demus.mayor.PinResetActivity;
import com.demus.mayor.R;
import com.demus.mayor.adapters.CustomSpinnerAdapter;
import com.demus.mayor.adapters.PlainAdapter;
import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.models.NetObject;
import com.demus.mayor.models.Product;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.models.User;
import com.demus.mayor.utils.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    public static final String PREFS_NAME = "FrequentMSISDN";

    static private String chosenNetwork;
    //static private String selectedAmount;
    private Button rechargeBtn;
    static private Spinner networkSpinner;
    static private EditText phoneNumberEt;
    static private EditText txtAmount;
    static private Spinner amountSpinner;
    static private EditText pinEditText;
    static private CardView amountCardView;
    private SharedPreferences settings;
    static int chosenType = Product.AIRTIME_PRODUCT_TYPE;
    static FragmentActivity current;
    private static RechargeContract listener;

    String[] amountObjects;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initUi(root);
        return root;
    }

    private void initUi(View root) {
        if (getActivity() == null) return;
        current = getActivity();

        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        networkSpinner = root.findViewById(R.id.home_network_spinner);
        phoneNumberEt = root.findViewById(R.id.home_phone_input);
        amountSpinner = root.findViewById(R.id.home_amount_spinner);
        pinEditText = root.findViewById(R.id.home_secret_pin_input);
        txtAmount = root.findViewById(R.id.txtAmount);
        amountCardView = root.findViewById(R.id.home_amount);

        TextView rechargeTopBtn = root.findViewById(R.id.home_recharge_btn_top);
        rechargeTopBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_home));

        TextView referBtn = root.findViewById(R.id.home_refer_btn);
        referBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_refer));

        TextView walletBtn = root.findViewById(R.id.home_wallet_btn);
        walletBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_wallet));

        rechargeBtn = root.findViewById(R.id.home_send_btn_down);
        rechargeBtn.setOnClickListener(v -> actionRecharge());

        setUpNetworkSpinner();
        setUpAmountSpinner();

        root.findViewById(R.id.airtime_radio).setOnClickListener(this::onRadioButtonClicked);
        root.findViewById(R.id.datum_radio).setOnClickListener(this::onRadioButtonClicked);
    }

    public static void setListener(RechargeContract viewListener) {
        listener = viewListener;
    }

    private void actionRecharge() {
        listener.onRequestStarted();

        if (phoneNumberEt.getText().toString().equals("")||
                txtAmount.getText().toString().equals("") ||
                pinEditText.getText().toString().equals("") ||
                chosenNetwork.equals("") ) {
            listener.onRequestComplete();

            // create alert dialog
            Toast.makeText(getActivity(),R.string.error_incomplete, Toast.LENGTH_LONG).show();

            return;
        }
        if ((chosenNetwork.equals("GLO") && Integer.parseInt(txtAmount.getText().toString()) < 45) &&
                chosenType == Product.AIRTIME_PRODUCT_TYPE) {
            listener.onRequestComplete();

            // create alert dialog
            Toast.makeText(getActivity(), R.string.minimum_45, Toast.LENGTH_LONG).show();

            return;
        }

        if ((chosenNetwork.equals("MTN") ||
                chosenNetwork.equals("AIRTEL") ||
                chosenNetwork.equals("9 MOBILE")) &&
                (Integer.parseInt(txtAmount.getText().toString()) < 50) &&
                chosenType == Product.AIRTIME_PRODUCT_TYPE) {
            listener.onRequestComplete();

            // create alert dialog
            Toast.makeText(getActivity(), R.string.minimum_50, Toast.LENGTH_LONG).show();
        }
        else {
            if(chosenNetwork.equals("9 MOBILE"))
                chosenNetwork = "ETISALAT";

            Product product = new Product();
            product.setMobileNetworkOperator(chosenNetwork);
            product.setReceiverNumber(phoneNumberEt.getText().toString());
            product.setAmount(new BigDecimal(txtAmount.getText().toString()));
            product.setPin(pinEditText.getText().toString());
            product.setType(chosenType);

            recharge(product);
        }
    }

    private void setUpNetworkSpinner() {
        ArrayList<NetObject> netObjects = new ArrayList<>();
        NetObject netObject = new NetObject();
        netObject.setId(1);
        netObject.setText("MTN");
        netObject.setImgResource(R.drawable.mtn);
        netObjects.add(netObject);

        netObject = new NetObject();
        netObject.setId(2);
        netObject.setText("AIRTEL");
        netObject.setImgResource(R.drawable.airtel);
        netObjects.add(netObject);

        netObject = new NetObject();
        netObject.setId(3);
        netObject.setText("9 MOBILE");
        netObject.setImgResource(R.drawable.ninemobile);
        netObjects.add(netObject);

        netObject = new NetObject();
        netObject.setId(4);
        netObject.setText("GLO");
        netObject.setImgResource(R.drawable.glo);
        netObjects.add(netObject);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), R.layout.custom_spinner_normal_state, netObjects);
        networkSpinner.setAdapter(adapter);
        networkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
                chosenNetwork = ((NetObject) parent.getItemAtPosition(pos)).getText();

                if(chosenType == Product.DATA_PRODUCT_TYPE)
                    setDataAmounts();
                Log.d(TAG, "onItemSelected: "+chosenNetwork);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        String freqMSISDN = settings.getString("freqMSISDN", "");
        if(freqMSISDN.equals(""))
            freqMSISDN = new User().getAppOwner(getActivity()).getPhoneNumber();

        phoneNumberEt.setText(freqMSISDN);
    }

    private void setUpAmountSpinner() {
        amountObjects = new String[]{"Amount", "200", "500", "750", "1000", "1500", "2000", "Enter Value"};

        if(getActivity() == null) return;

        ArrayAdapter<String> amountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, amountObjects);
        amountSpinner.setPrompt("Amount");
        amountSpinner.setAdapter(amountAdapter);
        amountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < amountObjects.length - 1) {
                    amountCardView.setVisibility(View.GONE);

                    if(position == 0)
                        txtAmount.setText("");
                    else
                        txtAmount.setText(parent.getItemAtPosition(position).toString());
                }
                else {
                    amountCardView.setVisibility(View.VISIBLE);
                    txtAmount.setText("");
                }
                Log.d(TAG, "onItemSelected: "+txtAmount.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        int freqAmountPos = settings.getInt("freqAmountPos", 0);
        amountSpinner.setSelection(freqAmountPos, true);
    }

    private void setDataAmounts() {
        ArrayList<NetObject> netObjects = new ArrayList<NetObject>();

        if(chosenNetwork.equals("MTN")) {
            NetObject netObject = new NetObject();
            netObject.setId(16);
            netObject.setText("100 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(17);
            netObject.setText("500 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(97);
            netObject.setText("1 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(101);
            netObject.setText("5 GB");
            netObjects.add(netObject);
        }
        else if(chosenNetwork.equals("AIRTEL")) {
            NetObject netObject = new NetObject();
            netObject.setId(4);
            netObject.setText("30 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(5);
            netObject.setText("500 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(6);
            netObject.setText("1.5 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(7);
            netObject.setText("3.5 GB");
            netObjects.add(netObject);
        }
        else if(chosenNetwork.equals("GLO")) {
            NetObject netObject = new NetObject();
            netObject.setId(32);
            netObject.setText("12.5 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(18);
            netObject.setText("27.5 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(21);
            netObject.setText("92 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(28);
            netObject.setText("242 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(27);
            netObject.setText("920 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(2);
            netObject.setText("1.2 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(25);
            netObject.setText("4.5 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(19);
            netObject.setText("7.2 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(23);
            netObject.setText("8.75 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(122);
            netObject.setText("12.5 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(55);
            netObject.setText("15.6 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(44);
            netObject.setText("25 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(100);
            netObject.setText("32.5 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(11);
            netObject.setText("52.5 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(20);
            netObject.setText("62.5 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(33);
            netObject.setText("78.7 GB");
            netObjects.add(netObject);
        }
        else {
            NetObject netObject = new NetObject();
            netObject.setId(67);
            netObject.setText("200 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(68);
            netObject.setText("500 MB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(378);
            netObject.setText("1.5 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(105);
            netObject.setText("3 GB");
            netObjects.add(netObject);

            netObject = new NetObject();
            netObject.setId(379);
            netObject.setText("4 GB");
            netObjects.add(netObject);
        }

        PlainAdapter adapter2 = new PlainAdapter(current, R.layout.custom_spinner_normal_state, netObjects);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amountSpinner.setAdapter(adapter2);
        amountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
                amountCardView.setVisibility(View.GONE);
                txtAmount.setText(String.valueOf(((NetObject)parent.getItemAtPosition(pos)).getId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        int freqDataAmountPos = settings.getInt("freqDataAmountPos", 0);
        if(freqDataAmountPos < netObjects.size()) {
            amountSpinner.setSelection(freqDataAmountPos, true);

            String freqDataAmount = settings.getString("freqDataAmount", "200");
            txtAmount.setText(freqDataAmount);
        }
    }

    private void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.airtime_radio:
                if (checked) {
                    chosenType = Product.AIRTIME_PRODUCT_TYPE;

                    ArrayAdapter<String> amountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, amountObjects);
                    amountSpinner.setPrompt("Amount");
                    amountSpinner.setAdapter(amountAdapter);
                    amountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position < amountObjects.length - 1) {
                                amountCardView.setVisibility(View.GONE);

                                if(position == 0)
                                    txtAmount.setText("");
                                else
                                    txtAmount.setText(parent.getItemAtPosition(position).toString());
                            }
                            else {
                                amountCardView.setVisibility(View.VISIBLE);
                                txtAmount.setText("");
                            }
                            Log.d(TAG, "onItemSelected: "+txtAmount.getText().toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                }
                break;
            case R.id.datum_radio:
                if (checked) {
                    setDataAmounts();
                    chosenType = Product.DATA_PRODUCT_TYPE;
                }
                break;
        }
    }

    private static void recharge(final Product product) {
        new Thread() {
            @Override
            public void run() {
                ServerResponse response = new TransactionAPI_Handler().recharge(product, current, 1);

                Bundle bundle = new Bundle();
                bundle.putParcelable("response", response);

                Message message = new Message();
                message.setData(bundle);
                rechargeHandler.sendMessage(message);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    static Handler rechargeHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            final ServerResponse response = msg.getData().getParcelable("response");
            listener.onRequestComplete();

            pinEditText.setText("");

            LayoutInflater li = LayoutInflater.from(current);
            View newView = li.inflate(R.layout.prompt_important_message, null);
            AlertDialog alertDialog = new AlertDialog.Builder(current).create();
            alertDialog.setView(newView);
            alertDialog.setCancelable(true);
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, current.getString(R.string.ok), (dialog, which) -> {
                dialog.dismiss();

                if(response.getResponseMessage().contains("expired")) {
                    Intent i = new Intent(current, PinResetActivity.class);
                    current.startActivity(i);
                }
                else {
                    SharedPreferences settings = current.getSharedPreferences(PREFS_NAME, 0);
                    String freqMSISDN = settings.getString("freqMSISDN", "");

                    if(!freqMSISDN.equals(phoneNumberEt.getText().toString())) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("freqTelco", networkSpinner.getSelectedItemPosition());
                        editor.putString("freqMSISDN", phoneNumberEt.getText().toString());

                        if(chosenType == Product.AIRTIME_PRODUCT_TYPE) {
                            editor.putString("freqAmount", txtAmount.getText().toString());
                            editor.putInt("freqAmountPos", amountSpinner.getSelectedItemPosition());
                        }

                        // Apply the edits!
                        editor.apply();
                    }

                    if(response.getResponseCode().equals(Utility.STATUS_OK)) {
                        // Navigate to wallet.
                        listener.onRecharged();
                    }
                }
            });

            TextView lblMessage = newView.findViewById(R.id.lblMessage);
            lblMessage.setText(response.getResponseMessage());

            alertDialog.show();
        }
    };

    public interface RechargeContract {
        void onRecharged();
        void onRequestComplete();
        void onRequestStarted();
    }
}

