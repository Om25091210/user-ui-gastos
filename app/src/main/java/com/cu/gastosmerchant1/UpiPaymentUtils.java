package com.cu.gastosmerchant1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cu.gastosmerchant1.Activity.OnBoardingActivity;
import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.registration.PhoneAuthentication;
import com.cu.gastosmerchant1.registration.RegistrationPayActivity;
import com.cu.gastosmerchant1.registration.RegistrationPaymentActivity;
import com.cu.gastosmerchant1.registration.setpin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UpiPaymentUtils extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private final String MyPrefs = "MY_PREFERENCE";
    private final String REG_PAYMENT = "REG_PAYMENT";
    boolean isPaid=false;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=getSharedPreferences(MyPrefs,MODE_PRIVATE);
        isPaid=sharedPreferences.getBoolean(REG_PAYMENT,false);

        mAuth = FirebaseAuth.getInstance();
        //This method is used so that your splash activity
        //can cover the entire screen.
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            Intent intent=new Intent(UpiPaymentUtils.this,Home.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent=new Intent(UpiPaymentUtils.this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

