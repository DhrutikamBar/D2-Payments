package com.d2payments.d2payments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    /**
     * Object Declaration
     * */
    Button startPaymentButton ;
    TextView paymentSuccessTextView,paymentFailedTextView;
    EditText amountEditText ;
    String moneyAmount ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Object Initialization
         * */
        startPaymentButton = findViewById(R.id.startPaymentButton);
        paymentSuccessTextView = findViewById(R.id.paymentSuccessTextView);
        paymentFailedTextView = findViewById(R.id.paymentFailedTextView);
        amountEditText = findViewById(R.id.amountEditText);
        Checkout.preload(getApplicationContext());


        /**
         * Onclick Listener For Starting a Payment
         * */
        startPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moneyAmount = amountEditText.getText().toString()+"00";
                if(Integer.parseInt(moneyAmount)>0 ){
                    startPayment();
                }else{
                    Toast.makeText(MainActivity.this, "Enter valid amount!", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }


    /**
     * For starting the payment
     * */
    public void startPayment () {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_LX9WYE2yABujrH");
//        checkout.setImage(R.drawable.logo);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Swasti Groceries");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", moneyAmount);//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","7064292542");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    /**
     * If any payment succeed
     * */
    @Override
    public void onPaymentSuccess(String s) {

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long l) {
                paymentSuccessTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                paymentSuccessTextView.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    /**
     * If any payment error
     * */
    @Override
    public void onPaymentError(int i, String s) {

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long l) {
                paymentFailedTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                paymentFailedTextView.setVisibility(View.INVISIBLE);
            }
        }.start();
    }
}