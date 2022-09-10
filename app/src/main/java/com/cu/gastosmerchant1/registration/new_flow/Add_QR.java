package com.cu.gastosmerchant1.registration.new_flow;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.DB.TinyDB;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.cu.gastosmerchant1.registration.AddQR;
import com.cu.gastosmerchant1.registration.new_flow.Adapter.QRAdapter;
import com.cu.gastosmerchant1.upiparse.QRCodeAdapter;
import com.cu.gastosmerchant1.upiparse.QRScannerActivity;
import com.cu.gastosmerchant1.upiparse.UpiView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Add_QR extends Fragment {

    ArrayList<Payment_Information> upiArrayList = new ArrayList<>();
    ArrayList<Payment_Information> upiArrayList_pre = new ArrayList<>();
    ArrayList<UpiView> upiViewList = new ArrayList<>();
    private QRAdapter qrCodeAdapter;
    DatabaseReference reference,ref_merchant;
    ImageView back;
    TextView addUpi,no_qr,next;
    RecyclerView qrRecyclerView;
    FirebaseAuth auth;
    FirebaseUser user;
    boolean isPrimary;
    View view;
    int c=0;
    String from;
    TinyDB tinyDB;
    ConstraintLayout constraint;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.qr_codes, container, false);
        constraint=view.findViewById(R.id.constraint);
        if(getArguments()!=null){
            from=getArguments().getString("fromHome");
        }
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        ref_merchant=FirebaseDatabase.getInstance().getReference().child("All_QRS");
        reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data").child(user.getUid()).child("QR_Information");
        qrRecyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager mManager = new LinearLayoutManager(getContext());
        qrRecyclerView.setItemViewCacheSize(500);
        qrRecyclerView.setDrawingCacheEnabled(true);
        qrRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        qrRecyclerView.setLayoutManager(mManager);

        no_qr = view.findViewById(R.id.no_qr);
        next = view.findViewById(R.id.next);
        back = view.findViewById(R.id.imageView);
        addUpi = view.findViewById(R.id.add_qr);
        addUpi.setOnClickListener(view -> {
            //gallery open and pick a qr photo.
            Intent i = new Intent(getContext(), QRScannerActivity.class);
            startActivityForResult(i, 0000);
        });
        get_qrs();
        tinyDB=new TinyDB(getContext());
        qrCodeAdapter = new QRAdapter(getContext(), upiViewList, upiArrayList,view);
        qrRecyclerView.setAdapter(qrCodeAdapter);
        back.setOnClickListener(v->{
            back();
        });
        next.setOnClickListener(v->{
            if(upiArrayList.size()!=0 && from==null) {
                Bundle args = new Bundle();
                args.putSerializable("upiArrayList", upiArrayList);
                args.putSerializable("upiViewList", upiViewList);
                Add_Discount add_discount = new Add_Discount();
                add_discount.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.cps, add_discount,"dis")
                        .commit();
            }
            else{
                Toast.makeText(getContext(), "Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                reference.setValue(upiArrayList);
                back();
            }
        });
        return view;
    }

    private void get_qrs() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    no_qr.setVisibility(View.GONE);
                    for(int i=0;i<snapshot.getChildrenCount();i++){
                        String merchantId=snapshot.child(i+"").child("merchantId").getValue(String.class);
                        Boolean primary=snapshot.child(i+"").child("primary").getValue(Boolean.class);
                        String rawString=snapshot.child(i+"").child("rawString").getValue(String.class);
                        String upiId=snapshot.child(i+"").child("upiId").getValue(String.class);
                        String upiName=snapshot.child(i+"").child("upiName").getValue(String.class);
                        Payment_Information payment_information=new Payment_Information(upiId,rawString,upiName, Boolean.TRUE.equals(primary),merchantId);
                        upiArrayList_pre.add(payment_information);
                        fetch_id(i);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetch_id(int i) {
        next.setBackgroundResource(R.drawable.bg_next);
        if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("BHARATPE")) {//todo check
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "BharatPe", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.ic_bharatpay, upiArrayList_pre.get(i).getUpiId(), "BharatPe", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@ok")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "GooglePay", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.google_img, upiArrayList_pre.get(i).getUpiId(), "GooglePay", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@paytm")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "PayTm", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.paytm_img, upiArrayList_pre.get(i).getUpiId(), "PayTm", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@upi")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "BHIM", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.bhim_upi_img, upiArrayList_pre.get(i).getUpiId(), "BHIM", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@ibl")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "PhonePe", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.phone_pe_img, upiArrayList_pre.get(i).getUpiId(), "PhonePe", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@ybl")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "PhonePe", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.phone_pe_img, upiArrayList_pre.get(i).getUpiId(), "PhonePe", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@axl")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "PhonePe", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.phone_pe_img, upiArrayList_pre.get(i).getUpiId(), "PhonePe", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@ybl")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "PhonePe", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.phone_pe_img, upiArrayList_pre.get(i).getUpiId(), "PhonePe", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@apl")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "Amazon", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.amazon_pay_img, upiArrayList_pre.get(i).getUpiId(), "Amazon", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@airtel")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "Airtel", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.airtel_img, upiArrayList_pre.get(i).getUpiId(), "Airtel", upiArrayList_pre.get(i).isPrimary()));
        } else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@icici")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "ICICI", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.airtel_img, upiArrayList_pre.get(i).getUpiId(), "ICICI", upiArrayList_pre.get(i).isPrimary()));
        }
        else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@sbi")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "SBI", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.airtel_img, upiArrayList_pre.get(i).getUpiId(), "SBI", upiArrayList_pre.get(i).isPrimary()));
        }
        else if (upiArrayList_pre.get(i).getUpiId().substring(upiArrayList_pre.get(i).getUpiId().indexOf("@")).contains("@hdfcbank")) {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "HDFC", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.airtel_img, upiArrayList_pre.get(i).getUpiId(), "HDFC", upiArrayList_pre.get(i).isPrimary()));
        }
        else {
            upiArrayList.add(new Payment_Information(upiArrayList_pre.get(i).getUpiId(), upiArrayList_pre.get(i).getRawString(), "Other", upiArrayList_pre.get(i).isPrimary(), upiArrayList_pre.get(i).getMerchantId()));
            upiViewList.add(new UpiView(R.drawable.other_pay, upiArrayList_pre.get(i).getUpiId(), "Other", upiArrayList_pre.get(i).isPrimary()));//todo needed
        }
        qrCodeAdapter.notifyDataSetChanged();
    }

    public void get_updated_list_items(View view,ArrayList<Payment_Information> upiArrayList, ArrayList<UpiView> upiViewList){
        this.upiArrayList=upiArrayList;
        this.upiViewList=upiViewList;
        if(this.upiViewList.size()==0) {
            view.findViewById(R.id.next).setBackgroundResource(R.drawable.bg_next_inactive);
            view.findViewById(R.id.no_qr).setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0000) {
            if (resultCode == RESULT_OK) {
                String rawString = data.getStringExtra("rawString");
                Log.e("rawString",rawString);
                String upiId = data.getStringExtra("UPI");
                Log.e("upiId",upiId);
                String merchantId = data.getStringExtra("merchantId");
                Log.e("merchantId",merchantId);
                isPrimary = false;
                exist(rawString,upiId,merchantId);
                if(c==1){
                    return;
                }
            }
        }
    }

    private void exist(String rawString, String upiId, String merchantId) {
        c=0;
        ref_merchant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(snapshot.child(ds.getKey()).child("rawString").getValue(String.class).trim().equals(rawString.trim())){
                        c=1;
                        Toast.makeText(getContext(), "QR already exist in our database, Scan a new one.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(c!=1){
                    if (rawString == null) {
                        Toast.makeText(getContext(), "Invalid Qr", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int flag = 0;
                    for (Payment_Information payment_information : upiArrayList) {
                        if (upiId.equals(payment_information.getUpiId())) {
                            flag = 1;
                            Toast.makeText(getContext(), "This UPI Already Exist!! ", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    if (upiArrayList.isEmpty()) {
                        isPrimary = true;
                    }

                    if (flag == 0) {
                        next.setBackgroundResource(R.drawable.bg_next);
                        no_qr.setVisibility(View.GONE);
                        if (upiId.contains("BHARATPE")) {//todo check
                            upiArrayList.add(new Payment_Information(upiId, rawString, "BharatPe", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.ic_bharatpay, upiId, "BharatPe", isPrimary));
                        } else if (upiId.contains("@ok")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "GooglePay", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.google_img, upiId, "GooglePay", isPrimary));
                        } else if (upiId.contains("@paytm")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "PayTm", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.paytm_img, upiId, "PayTm", isPrimary));
                        } else if (upiId.contains("@upi")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "BHIM", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.bhim_upi_img, upiId, "BHIM", isPrimary));
                        } else if (upiId.contains("@ibl")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "PhonePe", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.phone_pe_img, upiId, "PhonePe", isPrimary));
                        } else if (upiId.contains("@ybl")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "PhonePe", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.phone_pe_img, upiId, "PhonePe", isPrimary));
                        } else if (upiId.contains("@axl")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "PhonePe", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.phone_pe_img, upiId, "PhonePe", isPrimary));
                        } else if (upiId.contains("@ybl")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "PhonePe", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.phone_pe_img, upiId, "PhonePe", isPrimary));
                        } else if (upiId.contains("@apl")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "Amazon", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.amazon_pay_img, upiId, "Amazon", isPrimary));
                        } else if (upiId.contains("@airtel")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "Airtel", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.airtel_img, upiId, "Airtel", isPrimary));
                        } else if (upiId.contains("@icici")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "ICICI", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.airtel_img, upiId, "ICICI", isPrimary));
                        }
                        else if (upiId.contains("@sbi")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "SBI", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.airtel_img, upiId, "SBI", isPrimary));
                        }
                        else if (upiId.contains("@hdfcbank")) {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "HDFC", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.airtel_img, upiId, "HDFC", isPrimary));
                        }
                        else {
                            upiArrayList.add(new Payment_Information(upiId, rawString, "Other", isPrimary, merchantId));
                            upiViewList.add(new UpiView(R.drawable.other_pay, upiId, "Other", isPrimary));//todo needed
                        }
                        qrCodeAdapter.notifyDataSetChanged();
                        isPrimary = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        qrCodeAdapter = new QRAdapter(getContext(), upiViewList, upiArrayList,view);
        LinearLayoutManager mManager = new LinearLayoutManager(getContext());
        qrRecyclerView.setItemViewCacheSize(500);
        qrRecyclerView.setDrawingCacheEnabled(true);
        qrRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        qrRecyclerView.setLayoutManager(mManager);
        qrRecyclerView.setAdapter(qrCodeAdapter);
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