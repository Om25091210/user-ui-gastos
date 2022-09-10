package com.cu.gastosmerchant1.registration.new_flow;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cu.gastosmerchant1.DB.TinyDB;
import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.registration.new_flow.model.Discount;
import com.cu.gastosmerchant1.registration.new_flow.model.Services;
import com.cu.gastosmerchant1.registration.new_flow.model.Shop_Information;
import com.cu.gastosmerchant1.upiparse.UpiView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class onboarded extends Fragment {

    View view;
    ArrayList<Payment_Information> upiArrayList = new ArrayList<>();
    ArrayList<UpiView> upiViewList = new ArrayList<>();
    Shop_Information shop_information;
    Services services;
    TinyDB tinyDB;
    DatabaseReference ref_merchant;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    ArrayList<Discount> list_discount=new ArrayList<>();
    static private Timestamp timestamp;
    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_onboarded, container, false);
        reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(getArguments()!=null){
            upiArrayList= (ArrayList<Payment_Information>) getArguments().getSerializable("upiArrayList");
            upiViewList= (ArrayList<UpiView>) getArguments().getSerializable("upiViewList");
        }
        tinyDB=new TinyDB(getContext());
        timestamp = new Timestamp(System.currentTimeMillis());//timestamp.getTime()
        ref_merchant= FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid());
        if(tinyDB.getListString("discount_list").size()>=2) {
            int limit;
            int amt_limit;
            limit=Integer.parseInt(tinyDB.getListString("discount_list").get(0));
            if(tinyDB.getListString("discount_list").get(1).trim().equals(""))
                amt_limit=0;
            else
                amt_limit=Integer.parseInt(tinyDB.getListString("discount_list").get(1).trim());
            Discount discount=new Discount(amt_limit,limit);
            list_discount.add(discount);
        }
        if(tinyDB.getListString("discount_list").size()>=4){
            int limit;
            int amt_limit;
            limit=Integer.parseInt(tinyDB.getListString("discount_list").get(2));
            if(tinyDB.getListString("discount_list").get(3).trim().equals(""))
                amt_limit=0;
            else
                amt_limit=Integer.parseInt(tinyDB.getListString("discount_list").get(3).trim());
            Discount discount=new Discount(amt_limit,limit);
            list_discount.add(discount);
        }
        if(tinyDB.getListString("discount_list").size()==6){
            int limit;
            int amt_limit;
            limit=Integer.parseInt(tinyDB.getListString("discount_list").get(4));
            if(tinyDB.getListString("discount_list").get(5).trim().equals(""))
                amt_limit=0;
            else
                amt_limit=Integer.parseInt(tinyDB.getListString("discount_list").get(5).trim());
            Discount discount=new Discount(amt_limit,limit);
            list_discount.add(discount);
        }

        shop_information=new Shop_Information();
        if(!tinyDB.getListString("Saved_data_step_1").get(0).equals("ok")) {
            //for image storing
            String imagepath = user.getUid() + "/cover_" + tinyDB.getListString("Saved_data_step_1").get(10) + ".png";

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagepath);
            try {
                InputStream stream = new FileInputStream(new File(tinyDB.getListString("Saved_data_step_1").get(0)));

                storageReference.putStream(stream)
                        .addOnSuccessListener(taskSnapshot ->
                                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                        task -> {
                                            String shop_cover_image_link = Objects.requireNonNull(task.getResult()).toString();
                                            //for image storing
                                            String imagepath_profile = user.getUid() + "/profile_" + tinyDB.getListString("Saved_data_step_1").get(10) + ".png";

                                            StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child(imagepath_profile);
                                            try {
                                                InputStream stream1 = new FileInputStream(new File(tinyDB.getListString("Saved_data_step_1").get(1)));

                                                storageReference1.putStream(stream1)
                                                        .addOnSuccessListener(taskSnapshot1 ->
                                                                taskSnapshot1.getStorage().getDownloadUrl().addOnCompleteListener(
                                                                        task1 -> {
                                                                            String profile_image_link = Objects.requireNonNull(task1.getResult()).toString();

                                                                            ref_merchant.child("Shop_Information").child("creationTimeStamp").setValue(timestamp.getTime()+"");
                                                                            ref_merchant.child("Shop_Information").child("CoverPhotoUri").setValue(shop_cover_image_link);
                                                                            ref_merchant.child("Shop_Information").child("ProfilePhotoUri").setValue(profile_image_link);
                                                                            ref_merchant.child("Shop_Information").child("ShopAddress").setValue(tinyDB.getListString("Saved_data_step_1").get(6));
                                                                            ref_merchant.child("Shop_Information").child("shopState").setValue(tinyDB.getListString("Saved_data_step_1").get(4));
                                                                            ref_merchant.child("Shop_Information").child("shopDistrict").setValue(tinyDB.getListString("Saved_data_step_1").get(5));
                                                                            ref_merchant.child("Shop_Information").child("shopArea").setValue(tinyDB.getListString("Saved_data_step_1").get(9));
                                                                            ref_merchant.child("Shop_Information").child("shopName").setValue(tinyDB.getListString("Saved_data_step_1").get(12));
                                                                            ref_merchant.child("Shop_Information").child("category").setValue(tinyDB.getListString("Saved_data_step_1").get(2));
                                                                            ref_merchant.child("Shop_Information").child("subCategory").setValue(tinyDB.getListString("Saved_data_step_1").get(3));
                                                                            ref_merchant.child("Shop_Information").child("shopAddressLongitude").setValue(tinyDB.getListString("Saved_data_step_1").get(7));
                                                                            ref_merchant.child("Shop_Information").child("shopAddressLatitude").setValue(tinyDB.getListString("Saved_data_step_1").get(8));
                                                                            services=new Services(tinyDB.getListBoolean("DB_services").get(0),tinyDB.getListBoolean("DB_services").get(1)
                                                                                    ,tinyDB.getListBoolean("DB_services").get(2),tinyDB.getListBoolean("DB_services").get(3)
                                                                                    ,tinyDB.getListBoolean("DB_services").get(4),tinyDB.getListBoolean("DB_services").get(5));
                                                                            ref_merchant.child("Shop_Information").child("services").setValue(services);
                                                                            ref_merchant.child("Shop_Information").child("discounts").setValue(list_discount);

                                                                            reference.child(user.getUid()).child("QR_Information").setValue(upiArrayList);
                                                                            getContext().getSharedPreferences("Checking_completion", MODE_PRIVATE).edit()
                                                                                    .putBoolean("getting_completion", true).apply();
                                                                            Intent intent = new Intent(getContext(), Home.class);
                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                            startActivity(intent);
                                                                        }));
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            ref_merchant.child("Shop_Information").child("creationTimeStamp").setValue(timestamp.getTime()+"");
            ref_merchant.child("Shop_Information").child("ShopAddress").setValue(tinyDB.getListString("Saved_data_step_1").get(6));
            ref_merchant.child("Shop_Information").child("shopState").setValue(tinyDB.getListString("Saved_data_step_1").get(4));
            ref_merchant.child("Shop_Information").child("shopDistrict").setValue(tinyDB.getListString("Saved_data_step_1").get(5));
            ref_merchant.child("Shop_Information").child("shopArea").setValue(tinyDB.getListString("Saved_data_step_1").get(9));
            ref_merchant.child("Shop_Information").child("shopName").setValue(tinyDB.getListString("Saved_data_step_1").get(12));
            ref_merchant.child("Shop_Information").child("category").setValue(tinyDB.getListString("Saved_data_step_1").get(2));
            ref_merchant.child("Shop_Information").child("subCategory").setValue(tinyDB.getListString("Saved_data_step_1").get(3));
            ref_merchant.child("Shop_Information").child("shopAddressLongitude").setValue(tinyDB.getListString("Saved_data_step_1").get(7));
            ref_merchant.child("Shop_Information").child("shopAddressLatitude").setValue(tinyDB.getListString("Saved_data_step_1").get(8));
            services=new Services(tinyDB.getListBoolean("DB_services").get(0),tinyDB.getListBoolean("DB_services").get(1)
                    ,tinyDB.getListBoolean("DB_services").get(2),tinyDB.getListBoolean("DB_services").get(3)
                    ,tinyDB.getListBoolean("DB_services").get(4),tinyDB.getListBoolean("DB_services").get(5));
            ref_merchant.child("Shop_Information").child("services").setValue(services);
            ref_merchant.child("Shop_Information").child("discounts").setValue(list_discount);

            reference.child(user.getUid()).child("QR_Information").setValue(upiArrayList);
            getContext().getSharedPreferences("Checking_completion", MODE_PRIVATE).edit()
                    .putBoolean("getting_completion", true).apply();
            Intent intent = new Intent(getContext(), Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return view;
    }
}