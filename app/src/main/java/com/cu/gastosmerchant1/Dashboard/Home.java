package com.cu.gastosmerchant1.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.Dashboard.Settings.settings;
import com.cu.gastosmerchant1.Dashboard.membership.membership_status;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.databinding.ActivityHomeBinding;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.cu.gastosmerchant1.recvd_paymt;
import com.cu.gastosmerchant1.Settings;
import com.cu.gastosmerchant1.registration.BasicDetails;
import com.cu.gastosmerchant1.registration.new_flow.Add_Discount;
import com.cu.gastosmerchant1.registration.new_flow.Add_QR;
import com.cu.gastosmerchant1.registration.new_flow.ProfileDetails;
import com.cu.gastosmerchant1.registration.new_flow.Set_Discount;
import com.cu.gastosmerchant1.selectPackage.selectPackage;
import com.cu.gastosmerchant1.settings.ManageDesign;
import com.cu.gastosmerchant1.settings.PostAds;
import com.cu.gastosmerchant1.settings.ProviderWallet;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Home extends AppCompatActivity {

    private SimpleDraweeView shopImage,profile_photo;
    private TextView shopName,address;
    private String shopImageUri;
    private ImageView manage_qr,pos_qr,discount,settings,my_designs,active,promotions,payments;
    private String ads_String;
    FirebaseAuth auth;
    FirebaseUser user;

    CardView addQrButton,generateQr,settingsButton;
    private LinearLayout load_payments;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String MyPrefs = "MY_PREFERENCE";
    private final String NAME = "MY_NAME";
    //
    private ArrayList<Payment_Information> upiArrayList;
    private Shop_Information shop_information;
    private Account_Information account_information;
    //
    private DatabaseReference mDatabase,reference;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    CardView marketMembership;
    ImageView providerWallet;
    LinearLayout membership;
    ActivityHomeBinding binding;
    DatabaseReference ref_merchant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        boolean check_completion=getSharedPreferences("Checking_completion",MODE_PRIVATE)
                .getBoolean("getting_completion",false);
        if(check_completion) {
            setContentView(R.layout.updated_home);
            ref_merchant=FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid());
            Fresco.initialize(
                    Home.this,
                    ImagePipelineConfig.newBuilder(Home.this)
                            .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                            .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                            .experiment().setNativeCodeDisabled(true)
                            .build());
            shopImage = findViewById(R.id.shop_image);
            address = findViewById(R.id.address);
            profile_photo = findViewById(R.id.profile_photo);
            marketMembership = findViewById(R.id.marketMembership);
            shopName = findViewById(R.id.shopName);
            membership = findViewById(R.id.double_dhamaka);
            providerWallet = findViewById(R.id.providerWallet);
            manage_qr = findViewById(R.id.manage_qr);
            pos_qr = findViewById(R.id.pos_qr);
            discount = findViewById(R.id.discount);
            settings = findViewById(R.id.settings);
            active = findViewById(R.id.active);
            promotions = findViewById(R.id.promotions);
            my_designs = findViewById(R.id.my_designs);
            payments = findViewById(R.id.payments);
            fetch_data();
            settings.setOnClickListener(v->{
                Home.this.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.lay, new settings())
                        .addToBackStack(null)
                        .commit();
            });
            promotions.setOnClickListener(v->{
                Intent i=new Intent(getApplicationContext(), PostAds.class);
                startActivity(i);
            });
            marketMembership.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(getApplicationContext(), membership_status.class);
                    startActivity(i);
                }
            });
            membership.setOnClickListener(v->{
                Intent i=new Intent(getApplicationContext(), selectPackage.class);
                startActivity(i);
            });
            my_designs.setOnClickListener(v->{
                Intent i=new Intent(getApplicationContext(), ManageDesign.class);
                startActivity(i);
            });
            payments.setOnClickListener(v->{
                Home.this.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.lay, new received_payments())
                        .addToBackStack(null)
                        .commit();
            });
            providerWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ProviderWallet.class));
                }
            });
            manage_qr.setOnClickListener(v->{
                Bundle args=new Bundle();
                Add_QR add_qr=new Add_QR();
                args.putString("fromHome","NotnullHome");
                add_qr.setArguments(args);
                Home.this.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.lay, add_qr)
                        .addToBackStack(null)
                        .commit();
            });
            pos_qr.setOnClickListener(v->{
                Home.this.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.lay, new Pos_qr())
                        .addToBackStack(null)
                        .commit();
            });
            discount.setOnClickListener(v->{
                Bundle args=new Bundle();
                Add_Discount add_discount = new Add_Discount();
                args.putString("fromHome","NotnullHome");
                add_discount.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.lay, add_discount,"dis")
                        .commit();
            });
        }
        else {
            binding=ActivityHomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            get_views();
        }
        /*shopName = findViewById(R.id.shopName);
        shopImage = findViewById(R.id.shopImage);
        marketMembership = findViewById(R.id.marketMembership);
        providerWallet = findViewById(R.id.providerWallet);
        userName = findViewById(R.id.userName);
        membership = findViewById(R.id.double_dhamaka);

        adsImage = findViewById(R.id.ads_image_home);
        load_payments=findViewById(R.id.load_payments);

        mfirebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
        //
        shop_information = (Shop_Information) getIntent().getSerializableExtra("shop_information");
        account_information = (Account_Information) getIntent().getSerializableExtra("account_information");
        upiArrayList = (ArrayList<Payment_Information>) getIntent().getSerializableExtra("upiArrayList");
        //





        firebaseDatabase = FirebaseDatabase.getInstance();


        firebaseDatabase.getReference().

                child("Merchant_data").

                child(Objects.requireNonNull(mfirebaseAuth.getUid())).

                child("Account_Information").

                child("ownerName").

                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("vall", "onDataChange: "+snapshot.getValue());
                        userName.setText("By "+snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        firebaseDatabase.getReference().child("Merchant_data").child(Objects.requireNonNull(mfirebaseAuth.getUid())).child("Shop_Information").child("shopName").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue(String.class).equals(sharedPreferences.getString(NAME, "cafe Bistro"))) {
                    editor = sharedPreferences.edit();
                    editor.putString(NAME, snapshot.getValue(String.class));
                    editor.apply();
                }
                Log.d("getValue", "onDataChange: "+snapshot.getValue());
                shopName.setText(sharedPreferences.getString(NAME, "Cafe Bistro"));
                Log.d("sName", "onDataChange: "+shopName.getText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        firebaseDatabase.getReference().

                child("Merchant_ads").

                child("ads").

                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            ads_String = snapshot.getValue(String.class);
                            Picasso.get().load(Uri.parse(ads_String)).into(adsImage);
                        } catch (Exception e) {

                            Toast.makeText(Home.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        firebaseDatabase.getReference().

                child("Merchant_data").

                child(Objects.requireNonNull(mfirebaseAuth.getUid())).

                child("Shop_Information").

                child("shopImageUri").

                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String imageUri = snapshot.getValue(String.class);
                        try {
                            Picasso.get().load(Uri.parse(imageUri)).into(shopImage);
                        } catch (Exception e) {
                            Toast.makeText(Home.this, "Something went wrong to load image", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        addQrButton = findViewById(R.id.manageQr);
        generateQr = findViewById(R.id.posQr);
        settingsButton = findViewById(R.id.settings);

        addQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, pymt_info.class));
            }
        });


        generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,GenerateQrActivity.class));
            }
        });

        load_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, recvd_paymt.class));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Settings.class);
                startActivity(i);
            }
        });
        marketMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               Intent i=new Intent(getApplicationContext(), membership_status.class);
               startActivity(i);
            }
        });
        membership.setOnClickListener(v->{
            Intent i=new Intent(getApplicationContext(), selectPackage.class);
            startActivity(i);
        });
        providerWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProviderWallet.class));
            }
        });*/
    }

    private void fetch_data() {
        ref_merchant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String coverPhoto=snapshot.child("Shop_Information").child("CoverPhotoUri").getValue(String.class);
                    String ProfilePhoto=snapshot.child("Shop_Information").child("ProfilePhotoUri").getValue(String.class);
                    String shopname=snapshot.child("Shop_Information").child("shopName").getValue(String.class);
                    String shopaddress=snapshot.child("Shop_Information").child("ShopAddress").getValue(String.class);
                    String expiry=snapshot.child("membership").child("expiry").getValue(String.class);

                    if(coverPhoto!=null) {
                        Uri uri = Uri.parse(coverPhoto);
                        shopImage.setImageURI(uri);
                    }
                    if(ProfilePhoto!=null) {
                        Uri uriPhoto = Uri.parse(ProfilePhoto);
                        profile_photo.setImageURI(uriPhoto);
                    }
                    if(shopname!=null)
                        shopName.setText(shopname);
                    if(shopaddress!=null)
                        address.setText(shopaddress);
                        
                    try {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        Date date1 = sdf.parse(expiry+"");
                        Date date2 = sdf.parse(sdf.format(date));
                        if(date1.compareTo(date2) > 0)
                        {
                            active.setImageResource(R.drawable.ic_active);
                        }
                        else{
                            active.setImageResource(R.drawable.ic_inactive);;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void get_views() {
        binding.completeProfile.setOnClickListener(v->{
            Intent intent=new Intent(Home.this, ProfileDetails.class);
            startActivity(intent);
        });
    }


}
