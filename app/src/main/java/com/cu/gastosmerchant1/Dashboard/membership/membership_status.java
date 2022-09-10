package com.cu.gastosmerchant1.Dashboard.membership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.databinding.ActivityMembershipStatusBinding;
import com.cu.gastosmerchant1.registration.CashFree_Activity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;
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
import java.util.Date;
import java.util.Locale;

public class membership_status extends AppCompatActivity {

    ActivityMembershipStatusBinding binding;
    String phone,shop_name,expiry,shop_image;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    SimpleDraweeView roundedImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMembershipStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        roundedImageView=findViewById(R.id.roundedImageView);
        reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid());
        Fresco.initialize(
                membership_status.this,
                ImagePipelineConfig.newBuilder(membership_status.this)
                        .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                        .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                        .experiment().setNativeCodeDisabled(true)
                        .build());
        get_details();
        binding.imageView12.setOnClickListener(v->{
            finish();
        });
        binding.renew.setOnClickListener(view -> {
            Intent intent=new Intent(membership_status.this,membership.class);
            startActivity(intent);
        });


    }
    void get_details(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phone=snapshot.child("Account_Information").child("phoneNumber").getValue(String.class);
                shop_name=snapshot.child("Shop_Information").child("shopName").getValue(String.class);
                shop_image=snapshot.child("Shop_Information").child("ProfilePhotoUri").getValue(String.class);
                String coverPhoto=snapshot.child("Shop_Information").child("CoverPhotoUri").getValue(String.class);
                if(coverPhoto!=null) {
                    Uri uri = Uri.parse(coverPhoto);
                    roundedImageView.setImageURI(uri);
                }
                expiry=snapshot.child("membership").child("expiry").getValue(String.class);
                try {
                    if(shop_image!=null)
                        Picasso.get().load(Uri.parse(shop_image)).into(binding.polygonImageView);
                } catch (Exception e) {
                    Toast.makeText(membership_status.this, "Something went wrong to load image", Toast.LENGTH_SHORT).show();
                }
                binding.textView22.setText(shop_name);
                binding.textView23.setText(phone);
                if(expiry!=null) {
                    binding.renew.setText(R.string.renew_membership);
                    binding.date.setText(expiry);
                }
                else {
                    binding.renew.setText(R.string.activate_membership);
                    binding.date.setText("--");
                }
                binding.textView24.setText("InActive");
                binding.textView24.setTextColor(Color.parseColor("#F44336"));
                try {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    Date date1 = sdf.parse(expiry+"");
                    Date date2 = sdf.parse(sdf.format(date));
                    if(date1.compareTo(date2) > 0)
                    {
                        binding.textView24.setText("Active");
                        binding.renew.setText(R.string.renew_membership);
                        binding.textView24.setTextColor(Color.parseColor("#16A34A"));
                    }
                    else{
                        binding.renew.setText(R.string.activate_membership);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}