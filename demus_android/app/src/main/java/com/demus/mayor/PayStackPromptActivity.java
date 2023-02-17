package com.demus.mayor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demus.mayor.home.HomeActivity;
import com.demus.mayor.models.PaystackTransaction;
import com.demus.mayor.models.ServerResponse;
import com.demus.mayor.utils.Utility;
import com.demus.mayor.handlers.TransactionAPI_Handler;
import com.demus.mayor.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.UUID;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ChargeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class PayStackPromptActivity extends AppCompatActivity {

    EditText txtExpiryDate, txtAmount;
    String tranxRef;
    static PaystackTransaction subscription;

    Button cmdPay;

    static AppCompatActivity current;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// setup header
		setTitle(getString(R.string.paystack));
		setContentView(R.layout.prompt_pay_stack);

		current = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView toolTitle = toolbar.findViewById(R.id.toolbar_title);
        toolTitle.setText(R.string.paystack);
        setSupportActionBar(toolbar);

        // setup header
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		InitializeUI();
	}
	
	public void InitializeUI() {
		cmdPay = (Button) findViewById(R.id.cmdPay);
		cmdPay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    v.setEnabled(false);

				EditText txtCardNumber = (EditText) findViewById(R.id.txtCardNumber);
				EditText txtCVV = (EditText) findViewById(R.id.txtCVV);
                EditText txtAmount = (EditText) findViewById(R.id.txtAmount);

				if(txtCardNumber.getText().toString().equals("") ||
                   txtCVV.getText().toString().equals("") ||
                   txtExpiryDate.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), R.string.error_incomplete, Toast.LENGTH_LONG).show();
                    cmdPay.setEnabled(true);
					return;
				}
				else {
				    String[] splittedDates = txtExpiryDate.getText().toString().split("/");
				    String expiryMonth = splittedDates[0],
                           expiryYear = splittedDates[1];

				    final int amount = Integer.parseInt(txtAmount.getText().toString().replaceAll(",", "")) * 100;

                    Card card = new Card(txtCardNumber.getText().toString(), Integer.parseInt(expiryMonth), Integer.parseInt(expiryYear), txtCVV.getText().toString());

                    if (!card.validNumber()) {
                        txtCardNumber.setError(getString(R.string.invalid_card_number));
                        cmdPay.setEnabled(true);
                        return;
                    }
                    if (!card.validCVC()) {
                        txtCVV.setError(getString(R.string.invalid_cvc));
                        cmdPay.setEnabled(true);
                        return;
                    }
                    if (!card.validExpiryDate()) {
                        txtExpiryDate.setError(getString(R.string.invalid_expiry));
                        cmdPay.setEnabled(true);
                        return;
                    }
                    if (!card.isValid()) {
                        Toast.makeText(getApplicationContext(), R.string.invalid_card, Toast.LENGTH_LONG).show();
                        cmdPay.setEnabled(true);
                        return;
                    }
				    else {
                        RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
                        layProgress.setVisibility(View.VISIBLE);

                        tranxRef = UUID.randomUUID().toString();

                        Charge charge = new Charge();
                        charge.setCard(card);
                        charge.setCurrency("NGN");
                        charge.setAmount(amount);
                        charge.setEmail(Utility.getUserAccount(current).getEmail());
                        charge.setReference(tranxRef);

                        PaystackSdk.chargeCard(PayStackPromptActivity.this, charge, new Paystack.TransactionCallback() {
                            @Override
                            public void onSuccess(Transaction transaction) {
                                tranxRef = transaction.getReference();

                                //Store transaction locally
                                subscription = new PaystackTransaction(tranxRef, amount, Utility.getUserAccount(current).getUserID());
                                subscription.insert(current);

                                //call fanssify api to verify and give value
                                confirmPaystackTransaction();
                            }

                            @Override
                            public void beforeValidate(Transaction transaction) {
                                tranxRef = transaction.getReference();
                            }

                            @Override
                            public void onError(Throwable error, Transaction transaction) {
                                RelativeLayout layProgress = (RelativeLayout) findViewById(R.id.layProgress);
                                layProgress.setVisibility(View.GONE);

                                String friendlyMessage = "";

                                if(error instanceof ChargeException)
                                    friendlyMessage = error.getMessage();
                                else
                                    friendlyMessage = getString(R.string.transaction_error_general);

                                AlertDialog alertDialog = new AlertDialog.Builder(PayStackPromptActivity.this).create();
                                alertDialog.setTitle(getString(R.string.transaction_error));
                                alertDialog.setMessage(friendlyMessage);
                                alertDialog.setCancelable(false);
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();

                                cmdPay.setEnabled(true);
                            }
                        });
                    }
                }
			}
		});

		txtExpiryDate = (EditText) findViewById(R.id.txtExpiryDate);
        txtExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    final char c = editable.charAt(editable.length() - 1);

                    if ('/' == c) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }
                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    char c = editable.charAt(editable.length() - 1);

                    if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf("/")).length <= 2) {
                        editable.insert(editable.length() - 1, String.valueOf("/"));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }
        });

        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtAmount.addTextChangedListener(new TextWatcher() {

            private DecimalFormat df = new DecimalFormat("#,###.##");
            private DecimalFormat dfnd = new DecimalFormat("#,###");
            private boolean hasFractionalPart = false;

            @Override
            public void afterTextChanged(Editable s) {
                txtAmount.removeTextChangedListener(this);

                try {
                    int inilen, endlen;
                    inilen = txtAmount.getText().length();

                    String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                    Number n = df.parse(v);
                    int cp = txtAmount.getSelectionStart();
                    if (hasFractionalPart) {
                        txtAmount.setText(df.format(n));
                    } else {
                        txtAmount.setText(dfnd.format(n));
                    }
                    endlen = txtAmount.getText().length();
                    int sel = (cp + (endlen - inilen));
                    if (sel > 0 && sel <= txtAmount.getText().length()) {
                        txtAmount.setSelection(sel);
                    } else {
                        // place cursor at the end?
                        txtAmount.setSelection(txtAmount.getText().length() - 1);
                    }
                } catch (NumberFormatException nfe) {
                    // do nothing?
                } catch (ParseException e) {
                    // do nothing?
                }

                txtAmount.addTextChangedListener(this);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
                    hasFractionalPart = true;
                else
                    hasFractionalPart = false;
            }
        });
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            Intent returnIntent = new Intent();
            returnIntent.putExtra("nav_index", 3);

		    setResult(RESULT_CANCELED, returnIntent);
			finish();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

    static void confirmPaystackTransaction() {
        new Thread() {
            public void run() {
                ServerResponse response = new TransactionAPI_Handler().confirmPaystack(subscription, current, 1);

                Bundle bundle = new Bundle();
                bundle.putParcelable("response", response);

                Message message = new Message();
                message.setData(bundle);
                subscriptionHandler.sendMessage(message);
            }
        }.start();
    }

    static Handler subscriptionHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
            layProgress.setVisibility(View.GONE);

            ServerResponse response = msg.getData().getParcelable("response");

            if (response.getResponseCode().equals(Utility.STATUS_OK)) {
                Toast.makeText(current, R.string.verification_success, Toast.LENGTH_LONG).show();

                subscription.delete(current);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("nav_index", 3);

                current.setResult(RESULT_OK, returnIntent);
                current.finish();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(current).create();
                alertDialog.setTitle(current.getString(R.string.verification_error));
                alertDialog.setMessage(current.getString(R.string.verification_error_msg));
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, current.getString(R.string.retry), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(subscription != null) {
                            RelativeLayout layProgress = (RelativeLayout) current.findViewById(R.id.layProgress);
                            layProgress.setVisibility(View.VISIBLE);

                            confirmPaystackTransaction();
                        }

                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, current.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("nav_index", 3);

                        current.setResult(RESULT_CANCELED, returnIntent);
                        current.finish();
                    }
                });
                alertDialog.show();
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("nav_index", 3);

        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}
