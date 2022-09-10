package com.cu.gastosmerchant1.Dashboard;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;


public class Pos_qr extends Fragment {

    View view;
    ImageView back;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser user;

    private String baseUpiString = "&am=";
    private String baseUpiZero = "&am=0.00";
    private String baseString;
    private ArrayList<Payment_Information> upiStringList = new ArrayList<>();
    Payment_Information primary;
    private ImageView qrImageView;
    private EditText qrEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_pos_qr, container, false);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid()).child("QR_Information");

        back=view.findViewById(R.id.imageView);
        qrImageView = view.findViewById(R.id.qr_image);
        qrEditText = view.findViewById(R.id.qr_edit);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Payment_Information upi = dataSnapshot.getValue(Payment_Information.class);
                    Log.v(GenerateQrActivity.class.getSimpleName(), "upi " + upi.getUpiId());
                    if (upi.isPrimary()) {
                        primary = upi;
                        baseString = upi.getRawString();
                        Log.e("striiing",baseString);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        qrEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 5) {
                    String newUpiString = baseString + baseUpiString + "99999.00";
                    initializeQrFormation(newUpiString);
                    Toast.makeText(getContext(), "max amount Rs.99,999 only", Toast.LENGTH_SHORT).show();
                    qrEditText.setText("99999");
                    return;
                }
                String newUpiString = baseString + baseUpiString + charSequence + ".00";
                initializeQrFormation(newUpiString);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //check for zero value
                if (qrEditText.getText().toString().equals("")) {
                    initializeQrFormation(baseString + baseUpiZero);

                }
            }
        });

        back.setOnClickListener(v->{
            back();
        });

        return view;
    }
    private void initializeQrFormation(String uriString) {

        qrImageView.setScaleX(1);
        qrImageView.setScaleY(1);

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(uriString, BarcodeFormat.QR_CODE, 1800, 1800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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