package com.cu.gastosmerchant1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.details.CashFreeData;
import com.cu.gastosmerchant1.registration.CashFree_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class PaymentStatus extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String MyPrefs = "MY_PREFERENCE";
    private final String REG_PAYMENT = "REG_PAYMENT";


    private UpiPaymentUtils upiPaymentUtils;
    private final int UPI_PAYMENT = 999;
    final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private Timestamp timestamp;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private AppCompatButton jumpToHome;
    private RelativeLayout relativeLayout;
    private ImageButton info_btn;
    private TextView payment_status, date, time, service_amount, savings, retry, activateTv, savingTittle, service_chargeTv;
    private ImageView payment_status_img;
    private TextView gst, gstTax, amountTv;
    private String internName = "";

    private static final int PAYMENT_SUCCESS = 1;
    private static final int PAYMENT_FAILED = -1;
    private boolean isDiscountApplied = false;
    private String phoneNumber;
    private String email;
    private String orderId;
    private String promoCode;
    private String amount;
    private int paymentStatus;
    // TODO -> initialize these values when received from bundle intent
    private String str_date, str_time;
    private String int_bill_amount, int_savings;
    private CashFreeData cashFreeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);


        //getIntent
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        email = getIntent().getStringExtra("email");
        amount = getIntent().getStringExtra("amount");
        if (amount.equals("706.82")){
            amount = "599.0";
        }
        else if (amount.equals("352.82")){
            amount = "299.0";
        }
        isDiscountApplied = getIntent().getBooleanExtra("isPromoApplied", false);
        promoCode = getIntent().getStringExtra("promoCode");
        orderId = getIntent().getStringExtra("orderId");
        paymentStatus = getIntent().getIntExtra("transaction_status", 1);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        int_savings = "\u20B9 35";
        jumpToHome = findViewById(R.id.jumpToHome);

        gst = findViewById(R.id.gst);
        gstTax = findViewById(R.id.gstTax);
        amountTv = findViewById(R.id.amounttv);
        activateTv = findViewById(R.id.activationTv);
        relativeLayout = findViewById(R.id.relativeLayout);
        info_btn = findViewById(R.id.info);
        payment_status = findViewById(R.id.payment_msg);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        service_amount = findViewById(R.id.service_charge);
        savings = findViewById(R.id.savings);
        retry = findViewById(R.id.retry);
        payment_status_img = findViewById(R.id.payment_status_img);
        savingTittle = findViewById(R.id.tittle_saving);
        service_chargeTv = findViewById(R.id.service_chargeTv);

        timestamp = new Timestamp(System.currentTimeMillis());

        //make the node here
        if(paymentStatus==0)
        {
            cashFreeData = new CashFreeData(orderId, amount, "TXN_SUCCESS", sdf1.format(timestamp));
          
        } else {
            cashFreeData = new CashFreeData(orderId, amount, "TXN_FAILURE", sdf1.format(timestamp));
        }


        jumpToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentStatus.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        Log.v("amount is ",amount);

        if (cashFreeData.getTransaction_status().equals("TXN_SUCCESS")) {
            //success
            setLayoutElements(PAYMENT_SUCCESS);
        } else {
            //failure
            setLayoutElements(PAYMENT_FAILED);
        }

        // TODO -> initialize values from bundle intent and call setLayoutElements function

    }

    void applyDiscount(boolean discount) {
        if (discount) {
            gst.setText("\u20B9 53.82");
            amountTv.setText("\u20B9 352.82");
            savings.setVisibility(View.VISIBLE);
            savings.setText("-\u20B9 300.00");
            service_amount.setText("\u20B9 599.00");
            savingTittle.setVisibility(View.VISIBLE);

            if(promoCode.equals("G0000")){

                gst.setText("\u20B9 0.00");
                amountTv.setText("\u20B9 1.00");
                savings.setVisibility(View.VISIBLE);
                savings.setText("\u20B9 0.00");
                service_amount.setText("\u20B9 1.00");
            }

        } else {
            gst.setText("\u20B9 107.82");
            amountTv.setText("\u20B9 706.82");
            service_amount.setText("\u20B9 599.00");
//            service_chargeTv.setVisibility(View.GONE);
//            service_amount.setVisibility(View.GONE);
        }
    }

    // TODO -> pass this function PAYMENT_SUCCESS, PAYMENT_FAILED OR PAYMENT_SUBMITTED AS PER THE STATUS
    private void setLayoutElements(int payment_Status) {
        Format f = new SimpleDateFormat("hh:mm:ss a");
        str_time = f.format(new Date());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        str_date = formatter.format(new Date());

        service_amount.setText(String.valueOf("\u20B9 " + int_bill_amount));

        date.setText(str_date);

        time.setText(str_time);

        applyDiscount(isDiscountApplied);

        databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Registration_Payment_Info").setValue(cashFreeData);

        if (payment_Status == PAYMENT_FAILED) {
            // Payment Failed
            relativeLayout.setBackgroundResource(R.drawable.red_gradient);
            info_btn.setVisibility(View.GONE);
            payment_status.setText(R.string.bill_payment_failed);
            payment_status_img.setImageResource(R.drawable.pymnt_failed);
            jumpToHome.setVisibility(View.GONE);
            activateTv.setText("Failed");

            sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean(REG_PAYMENT, false);


            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(PaymentStatus.this, CashFree_Activity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("isPromoApplied", isDiscountApplied);
                    intent.putExtra("email", email);
                    intent.putExtra("promoCode", promoCode);
                    intent.putExtra("amount", amount);
                    startActivity(intent);
                    finish();
                }
            });

        } else if (payment_Status == PAYMENT_SUCCESS) {
            // Payment Success
            relativeLayout.setBackgroundResource(R.drawable.green_gradient);
            info_btn.setVisibility(View.GONE);
            payment_status.setText(R.string.bill_paid_succss);
            payment_status_img.setImageResource(R.drawable.pymnt_succs);

            sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean(REG_PAYMENT, true);

            if (isDiscountApplied) {
                databaseReference.child("Merchant_data").child("BDSales").child(promoCode).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        internName = snapshot.getValue(String.class);
                        databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("salesCode").setValue(internName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                databaseReference.child("Merchant_data").child("BDSales").child(promoCode).child("Providers").child(phoneNumber).child("account_information").child("registrationPaymentDone").setValue(true);
                final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                databaseReference.child("Merchant_data").child("BDSales").child(promoCode).child("Providers").child(phoneNumber).child("payment_time").setValue(sdf1.format(timestamp));


                databaseReference.child("Merchant_data").child("BDSales").child(promoCode).child("Providers").child(phoneNumber).child("payment_amount").setValue(amount);

            }
            databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("registrationPaymentDone").setValue(true);


            databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("registrationPayment").setValue(amount);


            if (amount.equals("299.0")) {
                databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("walletPromotion").setValue("200");
                databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("walletBranding").setValue("1000");
            } else if (amount.equals("599.0")) {
                databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("walletPromotion").setValue("600");
                databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("walletBranding").setValue("2400");
            } else {
                databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("walletPromotion").setValue("0");
                databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("walletBranding").setValue("0");
            }

            databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("registrationTime").setValue(sdf1.format(timestamp));
            retry.setVisibility(View.GONE);
        }
    }
}