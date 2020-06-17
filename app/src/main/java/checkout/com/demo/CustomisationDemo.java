package checkout.com.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.android.volley.VolleyError;
import com.checkout.android_sdk.Models.BillingModel;
import com.checkout.android_sdk.Models.PhoneModel;
import com.checkout.android_sdk.PaymentForm;
import com.checkout.android_sdk.PaymentForm.PaymentFormCallback;
import com.checkout.android_sdk.Response.CardTokenisationFail;
import com.checkout.android_sdk.Response.CardTokenisationResponse;
import com.checkout.android_sdk.Utils.CardUtils.Cards;
import com.checkout.android_sdk.Utils.Environment;

import java.util.Locale;

public class CustomisationDemo extends Activity {

    private PaymentForm mPaymentForm;
    private ProgressDialog mProgressDialog;

    // Callback used for the Payment Form interaction
    private final PaymentFormCallback mFormListener = new PaymentFormCallback() {
        @Override
        public void onFormSubmit() {
            mProgressDialog.show(); // show loader
        }

        @Override
        public void onTokenGenerated(CardTokenisationResponse response) {
            mProgressDialog.dismiss(); // dismiss the loader
            mPaymentForm.clearForm(); // clear the form
            displayMessage("Token", response.getId());
        }

        @Override
        public void onError(CardTokenisationFail response) {
            displayMessage("Token Error", response.getErrorCode());
        }

        @Override
        public void onNetworkError(VolleyError error) {
            displayMessage("Network Error", String.valueOf(error));
        }

        @Override
        public void onBackPressed() {
            displayMessage("Back", "The user decided to leave the payment page.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customisation_activity);

        // initialise the loader
        mProgressDialog = new ProgressDialog(CustomisationDemo.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading...");

        // initialise the payment from
        mPaymentForm = findViewById(R.id.checkout_card_form);
        mPaymentForm
                .setFormListener(mFormListener)
                .setEnvironment(Environment.SANDBOX)
                .setKey("pk_test_7d8d24fc-ffdb-4efc-b945-a19847ce319a")
                .setAcceptedCard(new Cards[]{Cards.VISA, Cards.MASTERCARD})
                .setAcceptedCardsLabel("We accept:")
                .setCardHolderLabel("Name on Card")
                .setCardLabel("Card Number")
                .setDateLabel("Expiration Date")
                .setCvvLabel("Security Code")
                .setAddress1Label("Address 1")
                .setAddress2Label("Address 2")
                .setTownLabel("City")
                .setStateLabel("State")
                .setPostcodeLabel("Zip Code")
                .setPhoneLabel("Phone No.")
                .setPayButtonText("Pay Now")
                .setDoneButtonText("Save")
                .setClearButtonText("Clear")
                .setDefaultBillingCountry(Locale.UK)
                .injectCardHolderName("John Smith")
                .injectBilling(
                        new BillingModel(
                                "Address 1",
                                "Address 2",
                                "Post code",
                                "GB",
                                "City",
                                "State",
                                new PhoneModel(
                                        "+44",
                                        "7123456789"
                                )
                        )
                );
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}