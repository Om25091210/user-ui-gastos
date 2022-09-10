package com.cu.gastosmerchant1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.databinding.ActivityEnterFullNameBinding;
import com.cu.gastosmerchant1.model.Account_Information;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class EnterFullNameActivity extends AppCompatActivity {

    String contact;
    Account_Information account_information;
    ActivityEnterFullNameBinding binding;
    DatabaseReference reference,ref_mer;
    FirebaseAuth auth;
    FirebaseUser user;
    String wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityEnterFullNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        contact=getIntent().getStringExtra("contact");
        reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        ref_mer=FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        get_wallet();
        binding.nextContinue.setOnClickListener(v->{
            if(binding.nameEdt.getVisibility()==View.VISIBLE && !binding.nameEdt.getText().toString().trim().equals("")){
                offanimate(binding.nameEdt);
                onAnimate(binding.emailEdt);
                binding.emailEdt.setVisibility(View.VISIBLE);
                binding.nextContinue.setBackgroundResource(R.drawable.bg_grey_continue);
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.nameEdt.setVisibility(View.GONE);
                    }
                },500);
            }
            else if(binding.emailEdt.getVisibility()==View.VISIBLE && !binding.emailEdt.getText().toString().trim().equals("")){
                offanimate(binding.emailEdt);
                onAnimate(binding.shopName);
                binding.shopName.setVisibility(View.VISIBLE);
                binding.nextContinue.setBackgroundResource(R.drawable.bg_grey_continue);
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.emailEdt.setVisibility(View.GONE);
                    }
                },500);
            }
            else if(binding.shopName.getVisibility()==View.VISIBLE && !binding.shopName.getText().toString().trim().equals("")){
                //TODO:Database things
                if(!binding.nameEdt.getText().toString().trim().equals("") &&
                   !binding.emailEdt.getText().toString().trim().equals("") &&
                   !binding.shopName.getText().toString().trim().equals("")) {
                    if(wallet!=null) {
                        account_information = new Account_Information(
                                binding.emailEdt.getText().toString().trim(),
                                binding.nameEdt.getText().toString().trim(),
                                contact,
                                "",
                                "",
                                wallet,
                                binding.shopName.getText().toString().trim());
                        reference.child(user.getUid()).child("Account_Information").setValue(account_information);
                        Intent intent = new Intent(this, SimpleOnboard.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Toast.makeText(this, "All Fields are necessary.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.imageView.setOnClickListener(v->{
            if(binding.nameEdt.getVisibility()==View.VISIBLE){
                binding.imageView.setVisibility(View.GONE);
            }
            else if(binding.emailEdt.getVisibility()==View.GONE){
                offanimate(binding.shopName);
                onAnimate(binding.emailEdt);
                binding.emailEdt.setVisibility(View.VISIBLE);
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.shopName.setVisibility(View.GONE);
                    }
                },500);
            }
            else if(binding.nameEdt.getVisibility()==View.GONE){
                offanimate(binding.emailEdt);
                onAnimate(binding.nameEdt);
                binding.nameEdt.setVisibility(View.VISIBLE);
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.emailEdt.setVisibility(View.GONE);
                    }
                },500);
            }
        });

        binding.shopName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable){
                if(!editable.toString().equals(""))
                    binding.nextContinue.setBackgroundResource(R.drawable.bg_get_started);
                else
                    binding.nextContinue.setBackgroundResource(R.drawable.bg_grey_continue);
            }
        });

        binding.emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable){
                if(!editable.toString().equals(""))
                    binding.nextContinue.setBackgroundResource(R.drawable.bg_get_started);
                else
                    binding.nextContinue.setBackgroundResource(R.drawable.bg_grey_continue);
            }
        });

        binding.nameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable){
                if(!editable.toString().equals(""))
                    binding.nextContinue.setBackgroundResource(R.drawable.bg_get_started);
                else
                    binding.nextContinue.setBackgroundResource(R.drawable.bg_grey_continue);
            }
        });

    }

    private void get_wallet() {
        ref_mer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("here",wallet+"");
                if(snapshot.child(user.getUid()).child("Account_Information").exists()){
                    wallet=snapshot.child(user.getUid()).child("Account_Information").child("wallet").getValue(String.class);
                    Log.e("here1",wallet+"");
                }
                else{
                    wallet="0";
                    Log.e("here2",wallet+"");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    void offanimate(View view){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationX",-800f);
        move.setDuration(1000);
        ObjectAnimator alpha2= ObjectAnimator.ofFloat(view, "alpha",0);
        alpha2.setDuration(500);
        AnimatorSet animset=new AnimatorSet();
        animset.play(alpha2).with(move);
        animset.start();
    }
    void onAnimate(View view){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationX",0f);
        move.setDuration(1000);
        ObjectAnimator alpha2= ObjectAnimator.ofFloat(view, "alpha",100);
        alpha2.setDuration(500);
        AnimatorSet animset=new AnimatorSet();
        animset.play(alpha2).with(move);
        animset.start();
    }

}