package com.cu.gastosmerchant1.registration.new_flow.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cu.gastosmerchant1.DB.TinyDB;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.registration.new_flow.Add_QR;
import com.cu.gastosmerchant1.upiparse.UpiView;

import java.util.ArrayList;

public class QRAdapter extends RecyclerView.Adapter<QRAdapter.ViewHolder> {

    private Context context;
    private ArrayList<UpiView> upiViewArrayList;
    private ArrayList<Payment_Information> upiArrayList;
    View view;
    public QRAdapter(Context context, ArrayList<UpiView> upiViewArrayList, ArrayList<Payment_Information> upiArrayList,View view) {
        this.context = context;
        this.upiViewArrayList = upiViewArrayList;
        this.upiArrayList = upiArrayList;
        this.view=view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.qr_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image_upi.setImageResource(upiViewArrayList.get(position).getResourceId());
        holder.upiid.setText(upiViewArrayList.get(position).getUpiId());
        holder.pay.setText(upiViewArrayList.get(position).getUpiName());
        String upiName = upiViewArrayList.get(position).getUpiName();
        if (upiName.equals("BharatPe")) {
            holder.image_upi.setImageResource(R.drawable.ic_bharatpay);
            holder.pay.setBackgroundResource(R.drawable.bg_bharatpay);
            holder.pay.setTextColor(Color.parseColor("#00AECA"));
        } else if (upiName.equals("GooglePay")) {
            holder.image_upi.setImageResource(R.drawable.google_img);
            holder.pay.setBackgroundResource(R.drawable.bg_google);
            holder.pay.setTextColor(Color.parseColor("#4285F4"));
        } else if (upiName.equals("PayTm")) {
            holder.image_upi.setImageResource(R.drawable.paytm_img);
            holder.pay.setBackgroundResource(R.drawable.bg_paytm);
            holder.pay.setTextColor(Color.parseColor("#FFFFFF"));
        } else if (upiName.equals("BHIM")) {
            holder.image_upi.setImageResource(R.drawable.bhim_upi_img);
            holder.pay.setBackgroundResource(R.drawable.bg_bhim);
            holder.pay.setTextColor(Color.parseColor("#27803B"));
        } else if (upiName.equals("PhonePe")) {
            holder.image_upi.setImageResource(R.drawable.phone_pe_img);
            holder.pay.setBackgroundResource(R.drawable.bg_phone_pay);
            holder.pay.setTextColor(Color.parseColor("#5F259F"));
        } else if (upiName.equals("Amazon")) {
            holder.image_upi.setImageResource(R.drawable.amazon_pay_img);
            holder.pay.setBackgroundResource(R.drawable.bg_amazon);
            holder.pay.setTextColor(Color.parseColor("#FF9800"));
        } else if (upiName.equals("Airtel")) {
            holder.image_upi.setImageResource(R.drawable.airtel_img);
            holder.pay.setBackgroundResource(R.drawable.bg_airtel);
            holder.pay.setTextColor(Color.parseColor("#E20010"));
        }else if (upiName.equals("ICICI")) {
            holder.image_upi.setImageResource(R.drawable.icici_img);
            holder.pay.setBackgroundResource(R.drawable.bg_airtel);
            holder.pay.setTextColor(Color.parseColor("#E20010"));
        }
        else if (upiName.equals("SBI")) {
            holder.image_upi.setImageResource(R.drawable.sbi_img);
            holder.pay.setBackgroundResource(R.drawable.bg_bharatpay);
            holder.pay.setTextColor(Color.parseColor("#0189f5"));
        }
        else if (upiName.equals("HDFC")) {
            holder.image_upi.setImageResource(R.drawable.hdfc_img);
            holder.pay.setBackgroundResource(R.drawable.bg_airtel);
            holder.pay.setTextColor(Color.parseColor("#E20010"));
        }
        else {
            holder.image_upi.setImageResource(R.drawable.other_pay);
        }
        holder.delete.setOnClickListener(v->{
            int actualPosition = holder.getAdapterPosition();
            upiArrayList.remove(actualPosition);
            upiViewArrayList.remove(actualPosition);
            notifyItemRemoved(actualPosition);
            notifyItemRangeChanged(actualPosition, upiViewArrayList.size());
            Add_QR add_qr=new Add_QR();
            add_qr.get_updated_list_items(view,upiArrayList,upiViewArrayList);
            if(upiViewArrayList.size()==1){
                for (UpiView upiView : upiViewArrayList) {
                    if (upiView.isPrimary()) {
                        upiView.setPrimary(false);
                    }
                }
                for (Payment_Information payment_information : upiArrayList) {
                    if (payment_information.isPrimary()) {
                        payment_information.setPrimary(false);
                    }
                }
                upiViewArrayList.get(0).setPrimary(true);
                upiArrayList.get(0).setPrimary(true);
                Add_QR add_qr1=new Add_QR();
                add_qr1.get_updated_list_items(view,upiArrayList,upiViewArrayList);
                notifyDataSetChanged();
            }
        });
        holder.layout.setOnClickListener(v -> {
            for (UpiView upiView : upiViewArrayList) {
                if (upiView.isPrimary()) {
                    upiView.setPrimary(false);
                }
            }
            for (Payment_Information payment_information : upiArrayList) {
                if (payment_information.isPrimary()) {
                    payment_information.setPrimary(false);
                }
            }
            upiViewArrayList.get(position).setPrimary(true);
            upiArrayList.get(position).setPrimary(true);
            Add_QR add_qr=new Add_QR();
            add_qr.get_updated_list_items(view,upiArrayList,upiViewArrayList);
            notifyDataSetChanged();
        });
        if (upiArrayList.get(position).isPrimary()) {
            holder.layout.setBackgroundResource(R.drawable.bg_primary_stroke);
            holder.primary.setVisibility(View.VISIBLE);
        } else {
            holder.layout.setBackgroundResource(R.drawable.bg_layout);
            holder.primary.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return upiViewArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image_upi,delete;
        TextView upiid,pay,primary;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_upi=itemView.findViewById(R.id.image_upi);
            upiid=itemView.findViewById(R.id.upiid);
            pay=itemView.findViewById(R.id.pay);
            primary=itemView.findViewById(R.id.primary);
            layout=itemView.findViewById(R.id.layout);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
