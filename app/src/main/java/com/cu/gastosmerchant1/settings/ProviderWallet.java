package com.cu.gastosmerchant1.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.selectPackage.selectPackage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProviderWallet extends AppCompatActivity {

    TextView mTotalAmount, mPromotionAmount, mBrandingAmount;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView mBack;
    TextView add_coins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_wallet);

        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        mTotalAmount = findViewById(R.id.total);
        add_coins = findViewById(R.id.add_coins);
        mBack = findViewById(R.id.imageButton);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid());
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String walletd=snapshot.child("Account_Information").child("wallet").getValue(String.class);
                    if(Objects.equals(walletd, ""))
                        mTotalAmount.setText("0.0");
                    else
                        mTotalAmount.setText(walletd);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        add_coins.setOnClickListener(v->{
            Intent i=new Intent(getApplicationContext(), selectPackage.class);
            startActivity(i);
        });
    }
}
