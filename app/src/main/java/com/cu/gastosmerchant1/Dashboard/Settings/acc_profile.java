package com.cu.gastosmerchant1.Dashboard.Settings;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.UpiPaymentUtils;
import com.cu.gastosmerchant1.model.Account_Information;
import com.cu.gastosmerchant1.registration.PhoneAuthentication;
import com.cu.gastosmerchant1.registration.RegistrationPaymentActivity;
import com.cu.gastosmerchant1.settings.acc_settings;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class acc_profile extends Fragment {

    View view;
    DatabaseReference ref_merchant,reference;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText editText;
    LinearLayout logout;
    Account_Information account_information;
    TextView ownername,number,email,validate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.acc_profile_details, container, false);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        ownername=view.findViewById(R.id.textView10);
        number=view.findViewById(R.id.textView13);
        email=view.findViewById(R.id.textView15);
        logout=view.findViewById(R.id.logout);
        editText=view.findViewById(R.id.editText);
        validate=view.findViewById(R.id.validate);
        logout.setOnClickListener(v->{
            logOutDialogFunc();
        });
        number.setText(user.getPhoneNumber()+"");
        ref_merchant= FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid());
        reference= FirebaseDatabase.getInstance().getReference();
        get_details();

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editText.getText().toString().toUpperCase().trim();
                reference.child("Merchant_data").child("BDSales").child(code).child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        try {
                            if (!dataSnapshot.getValue().toString().isEmpty()) {
                                Toast.makeText(getContext(), "promo code applied Successfully", Toast.LENGTH_SHORT).show();
                                Format f = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                                String str_time = f.format(new Date());

                                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                                String str_date = formatter.format(new Date());
                                reference.child("Merchant_data").child("BDSales").child(code).child("Providers").child(user.getPhoneNumber().substring(3)).child("date_time").setValue(str_date+" - "+str_time);
                                reference.child("Merchant_data").child("BDSales").child(code).child("Providers").child(user.getPhoneNumber().substring(3)).child("uid").setValue(user.getUid());
                                account_information = new Account_Information(
                                        email.getText().toString().trim(),
                                        ownername.getText().toString().trim(),
                                        user.getPhoneNumber().substring(3),
                                        code);
                                reference.child("Merchant_data").child("BDSales").child(code).child("Providers").child(user.getPhoneNumber().substring(3)).child("Account_Information").setValue(account_information);

                                reference.child("Merchant_data").child(user.getUid()).child("Account_Information").child("salesCode").setValue(code);

                                validate.setVisibility(View.GONE);
                                editText.setText(dataSnapshot.getValue().toString());
                                editText.setEnabled(false);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Some problem Occurred...", Toast.LENGTH_SHORT).show();
                            Log.e("Bd_Intern", e.toString());
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.imageView).setOnClickListener(v->{
            back();
        });
        return view;
    }

    private void get_details() {
        ref_merchant.child("Account_Information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String emailstr=snapshot.child("emailAddress").getValue(String.class);
                    String OwnerName=snapshot.child("ownerName").getValue(String.class);
                    ownername.setText(OwnerName);
                    email.setText(emailstr);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void logOutDialogFunc() {

        class LogoutDialog extends Dialog {
            Button cancelButton;
            TextView logout_text;

            public LogoutDialog(@NonNull Context context) {
                super(context);
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.logout_alert_dialog);
                cancelButton = findViewById(R.id.cancel_logout);
                logout_text = findViewById(R.id.logout_text);
            }
        }
        LogoutDialog logoutDialog = new LogoutDialog(getContext());
        logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutDialog.show();
        logoutDialog.logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                logoutDialog.dismiss();
                getContext().getSharedPreferences("Checking_completion",MODE_PRIVATE)
                        .getBoolean("getting_completion",false);
                Intent intent = new Intent(getContext(), UpiPaymentUtils.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Home home=new Home();
                home.finish();
            }
        });
        logoutDialog.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
            }
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