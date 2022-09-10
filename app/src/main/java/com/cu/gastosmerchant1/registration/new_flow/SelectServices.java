package com.cu.gastosmerchant1.registration.new_flow;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.DB.TinyDB;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.databinding.FragmentSelectServicesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectServices extends Fragment {

    boolean delivery=false;
    boolean cash_in_hand=false;
    boolean dining=false;
    boolean take_away=false;
    boolean air_condition=false;
    boolean wifi=false;
    View view;
    DatabaseReference ref_merchant;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView save;
    ArrayList<Boolean> listservices=new ArrayList<>();
    ImageView back,delivery_img,cash_img,dinein_img,take_img,air_img
            ,wifi_img,right0,right1,right2,right3,right4,right5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_select_services, container, false);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        ref_merchant= FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid()).child("Shop_Information").child("services");
        get_data();
        back=view.findViewById(R.id.imageView);
        delivery_img=view.findViewById(R.id.imageView7);
        cash_img=view.findViewById(R.id.imageView6);
        dinein_img=view.findViewById(R.id.imageView10);
        take_img=view.findViewById(R.id.imageView9);
        air_img=view.findViewById(R.id.imageView8);
        wifi_img=view.findViewById(R.id.wifi);
        save=view.findViewById(R.id.save);

        right0=view.findViewById(R.id.right);
        right1=view.findViewById(R.id.right1);
        right2=view.findViewById(R.id.right2);
        right3=view.findViewById(R.id.right3);
        right4=view.findViewById(R.id.right4);
        right5=view.findViewById(R.id.right5);
        TinyDB tinyDB=new TinyDB(getContext());
        if(tinyDB.getListBoolean("DB_services").size()==6){
            air_condition=tinyDB.getListBoolean("DB_services").get(4);
            if(air_condition)
                right4.setVisibility(View.VISIBLE);
            cash_in_hand=tinyDB.getListBoolean("DB_services").get(1);
            if(cash_in_hand)
                right1.setVisibility(View.VISIBLE);
            delivery=tinyDB.getListBoolean("DB_services").get(0);
            if(delivery)
                right0.setVisibility(View.VISIBLE);
            dining=tinyDB.getListBoolean("DB_services").get(2);
            if(dining)
                right2.setVisibility(View.VISIBLE);
            take_away=tinyDB.getListBoolean("DB_services").get(3);
            if(take_away)
                right3.setVisibility(View.VISIBLE);
            wifi=tinyDB.getListBoolean("DB_services").get(5);
            if(wifi)
                right5.setVisibility(View.VISIBLE);
        }
        save.setOnClickListener(v->{
            listservices.add(delivery);
            listservices.add(cash_in_hand);
            listservices.add(dining);
            listservices.add(take_away);
            listservices.add(air_condition);
            listservices.add(wifi);
            tinyDB.putListBoolean("DB_services",listservices);
            back();
        });
        back.setOnClickListener(v->{
            back();
        });

        delivery_img.setOnClickListener(v->{
            if(delivery){
                delivery=false;
                right0.setVisibility(View.GONE);
            }
            else{
                delivery=true;
                right0.setVisibility(View.VISIBLE);
            }
        });
        cash_img.setOnClickListener(v->{
            if(cash_in_hand){
                cash_in_hand=false;
                right1.setVisibility(View.GONE);
            }
            else{
                cash_in_hand=true;
                right1.setVisibility(View.VISIBLE);
            }
        });
        dinein_img.setOnClickListener(v->{
            if(dining){
                dining=false;
                right2.setVisibility(View.GONE);
            }
            else{
                dining=true;
                right2.setVisibility(View.VISIBLE);
            }
        });

        take_img.setOnClickListener(v->{
            if(take_away){
                take_away=false;
                right3.setVisibility(View.GONE);
            }
            else{
                take_away=true;
                right3.setVisibility(View.VISIBLE);
            }
        });

       air_img.setOnClickListener(v->{
            if(air_condition){
                air_condition=false;
                right4.setVisibility(View.GONE);
            }
            else{
                air_condition=true;
                right4.setVisibility(View.VISIBLE);
            }
        });

        wifi_img.setOnClickListener(v->{
            if(wifi){
                wifi=false;
                right5.setVisibility(View.GONE);
            }
            else{
                wifi=true;
                right5.setVisibility(View.VISIBLE);
            }
        });
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                listservices.add(delivery);
                listservices.add(cash_in_hand);
                listservices.add(dining);
                listservices.add(take_away);
                listservices.add(air_condition);
                listservices.add(wifi);
                tinyDB.putListBoolean("DB_services",listservices);
                back();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
        return view;
    }

    private void get_data() {
        ref_merchant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    boolean airConditioner=snapshot.child("airConditioner").getValue(Boolean.class);
                    boolean cashInHand =snapshot.child("cashInHand").getValue(Boolean.class);
                    boolean deliveryd =snapshot.child("delivery").getValue(Boolean.class);
                    boolean dineIn =snapshot.child("dineIn").getValue(Boolean.class);
                    boolean takeAway =snapshot.child("takeAway").getValue(Boolean.class);
                    boolean wifid =snapshot.child("wifi").getValue(Boolean.class);
                    Log.e("air",airConditioner+"");
                    air_condition=airConditioner;
                    if(air_condition)
                        right4.setVisibility(View.VISIBLE);
                    cash_in_hand=cashInHand;
                    if(cash_in_hand)
                        right1.setVisibility(View.VISIBLE);
                    delivery=deliveryd;
                    if(delivery)
                        right0.setVisibility(View.VISIBLE);
                    dining=dineIn;
                    if(dining)
                        right2.setVisibility(View.VISIBLE);
                    take_away=takeAway;
                    if(take_away)
                        right3.setVisibility(View.VISIBLE);
                    wifi=wifid;
                    if(wifi)
                        right5.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void back(){
        FragmentManager fm=((FragmentActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        ft.commit();
    }
}