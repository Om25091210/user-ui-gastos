package com.cu.gastosmerchant1.registration.new_flow;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.DB.TinyDB;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Discount;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.upiparse.UpiView;

import java.util.ArrayList;


public class Set_Discount extends Fragment {

    View view;
    SeekBar seekBar;
    ImageView back;
    TinyDB tinyDB;
    String from;
    EditText min_bill;
    int limit=5;
    int amt_limit=-1;
    ArrayList<Payment_Information> upiArrayList = new ArrayList<>();
    ArrayList<UpiView> upiViewList = new ArrayList<>();
    ArrayList<String> list=new ArrayList<>();
    TextView percent_text,add_discount,start_value;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_set__discount, container, false);
        seekBar=view.findViewById(R.id.seekBar4);
        min_bill=view.findViewById(R.id.min_bill);
        back=view.findViewById(R.id.imageView);
        percent_text=view.findViewById(R.id.percent_text);
        start_value=view.findViewById(R.id.start_value);
        if(getArguments()!=null){
            from=getArguments().getString("fromHome");
        }
        tinyDB=new TinyDB(getContext());
        if(tinyDB.getListString("discount_list").size()>=2) {
           limit=Integer.parseInt(tinyDB.getListString("discount_list").get(0));
            seekBar.setProgress(limit+1);
            percent_text.setText(limit+1+"");
            start_value.setText(limit+1+"");
            if(tinyDB.getListString("discount_list").get(1).trim().equals(""))
                amt_limit=0;
            else
                amt_limit=Integer.parseInt(tinyDB.getListString("discount_list").get(1).trim());
        }
        if(tinyDB.getListString("discount_list").size()>=4){
            limit=Integer.parseInt(tinyDB.getListString("discount_list").get(2));
            seekBar.setProgress(limit+1);
            percent_text.setText(limit+1+"");
            start_value.setText(limit+1+"");
            if(tinyDB.getListString("discount_list").get(3).trim().equals(""))
                amt_limit=0;
            else
                amt_limit=Integer.parseInt(tinyDB.getListString("discount_list").get(3).trim());
        }
        if(tinyDB.getListString("discount_list").size()==6){
            limit=Integer.parseInt(tinyDB.getListString("discount_list").get(4));
            seekBar.setProgress(limit+1);
            percent_text.setText(limit+1+"");
            start_value.setText(limit+1+"");
            if(tinyDB.getListString("discount_list").get(5).trim().equals(""))
                amt_limit=0;
            else
                amt_limit=Integer.parseInt(tinyDB.getListString("discount_list").get(5).trim());
        }
        seekBar.setMax(40);
        seekBar.setProgress(5);
        if(getArguments()!=null){
            upiArrayList= (ArrayList<Payment_Information>) getArguments().getSerializable("upiArrayList");
            upiViewList= (ArrayList<UpiView>) getArguments().getSerializable("upiViewList");
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<limit){
                    seekBar.setProgress(limit);
                    start_value.setText(limit+"");
                }
                else {
                    percent_text.setText(i+"");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        add_discount=view.findViewById(R.id.add_discount);
        add_discount.setOnClickListener(v->{
            int amount;
            if(min_bill.getText().toString().trim().equals(""))
                amount=0;
            else
                amount=Integer.parseInt(min_bill.getText().toString().trim());
            if(amount>amt_limit) {
                if (tinyDB.getListString("discount_list").size() >= 2) {
                    list.addAll(tinyDB.getListString("discount_list"));
                }
                list.add(percent_text.getText().toString().trim());
                list.add(String.valueOf(amount));
                tinyDB.putListString("discount_list", list);
                if(from==null) {
                    Bundle args = new Bundle();
                    args.putSerializable("upiArrayList", upiArrayList);
                    args.putSerializable("upiViewList", upiViewList);
                    args.putString("discount", percent_text.getText().toString().trim());
                    args.putString("minamount",amount+"");
                    user_agreement user_agreement = new user_agreement();
                    user_agreement.setArguments(args);
                    ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            .add(R.id.cps, user_agreement, "tag2")
                            .commit();
                }
                else{
                    Bundle args = new Bundle();
                    args.putSerializable("upiArrayList", upiArrayList);
                    args.putSerializable("upiViewList", upiViewList);
                    args.putString("fromHome","NotnullHome");
                    args.putString("discount", percent_text.getText().toString().trim());
                    args.putString("minamount",amount+"");
                    user_agreement user_agreement = new user_agreement();
                    user_agreement.setArguments(args);
                    ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            .add(R.id.lay, user_agreement, "tag2")
                            .commit();
                }
            }
            else{
                Toast.makeText(getContext(), "Please choose the amount greater than the previous discount.", Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(v->{
            back();
        });
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               back();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
        return view;
    }
    private void back(){
        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,R.anim.enter_from_right, R.anim.exit_to_left)
                .remove(((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("tag1")).commit();
    }
}