package com.cu.gastosmerchant1.registration.new_flow;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.DB.TinyDB;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.registration.new_flow.model.Discount;
import com.cu.gastosmerchant1.upiparse.UpiView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Add_Discount extends Fragment {

    View view;
    ArrayList<Payment_Information> upiArrayList = new ArrayList<>();
    ArrayList<UpiView> upiViewList = new ArrayList<>();
    ArrayList<String> empty=new ArrayList<>();
    TextView add_dis,next;
    ImageView back,del2,del3;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String from;
    ArrayList<Discount> list_discount=new ArrayList<>();
    LinearLayout linearLayout,layout,layout1,layout2;
    TextView per1,msg_1,per2,msg_2,per3,msg_3;
    TinyDB tinyDB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.discount, container, false);
        if(getArguments()!=null){
            from=getArguments().getString("fromHome");
        }
        tinyDB=new TinyDB(getContext());
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        add_dis=view.findViewById(R.id.add_dis);
        per1=view.findViewById(R.id.percent3);
        per2=view.findViewById(R.id.percent35);
        per3=view.findViewById(R.id.percent352);
        back=view.findViewById(R.id.imageView);
        msg_1=view.findViewById(R.id.text5);
        msg_2=view.findViewById(R.id.text55);
        msg_3=view.findViewById(R.id.text552);
        next = view.findViewById(R.id.next);
        linearLayout = view.findViewById(R.id.linearLayout);
        layout = view.findViewById(R.id.layout);
        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);
        del2 = view.findViewById(R.id.del2);
        del3 = view.findViewById(R.id.del3);
        //tinyDB.putListString("discount_list",empty); don't uncomment it
        if(getArguments()!=null){
            upiArrayList= (ArrayList<Payment_Information>) getArguments().getSerializable("upiArrayList");
            upiViewList= (ArrayList<UpiView>) getArguments().getSerializable("upiViewList");
        }
        if(tinyDB.getListString("discount_list").size()>=6){
            add_dis.setVisibility(View.GONE);
        }
        if(from==null) {
            del2.setVisibility(View.GONE);
            del3.setVisibility(View.GONE);
        }
        else{
            next.setText("Save");
            del2.setVisibility(View.VISIBLE);
            del3.setVisibility(View.VISIBLE);
        }
        del2.setOnClickListener(v->{
            ArrayList<String> new_list=tinyDB.getListString("discount_list");
            new_list.remove(2);
            new_list.remove(2);
            list_discount.remove(1);
            Log.e("lisst",new_list+"");
            tinyDB.putListString("discount_list",new_list);
            try {
               ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                        .remove(((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dis")).commit();
                Bundle args = new Bundle();
                args.putSerializable("upiArrayList", upiArrayList);
                args.putSerializable("upiViewList", upiViewList);
                Add_Discount add_discount = new Add_Discount();
                add_discount.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.lay, add_discount,"dis")
                        .commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        del3.setOnClickListener(v->{
            ArrayList<String> new_list=tinyDB.getListString("discount_list");
            new_list.remove(4);
            new_list.remove(4);
            list_discount.remove(2);
            Log.e("lisst",new_list+"");
            tinyDB.putListString("discount_list",new_list);
            ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                    .remove(((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dis")).commit();
            Bundle args = new Bundle();
            args.putSerializable("upiArrayList", upiArrayList);
            args.putSerializable("upiViewList", upiViewList);
            Add_Discount add_discount = new Add_Discount();
            add_discount.setArguments(args);
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.lay, add_discount,"dis")
                    .commit();
        });
        add_dis.setOnClickListener(v->{
            if(from==null) {
                Bundle args = new Bundle();
                args.putSerializable("upiArrayList", upiArrayList);
                args.putSerializable("upiViewList", upiViewList);
                Set_Discount set_discount = new Set_Discount();
                set_discount.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.cps, set_discount, "tag1")
                        .commit();
            }
            else{
                Bundle args = new Bundle();
                args.putSerializable("upiArrayList", upiArrayList);
                args.putSerializable("upiViewList", upiViewList);
                args.putString("fromHome","NotnullHome");
                Set_Discount set_discount = new Set_Discount();
                set_discount.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.lay, set_discount, "tag1")
                        .commit();
            }
        });

        if(tinyDB.getListString("discount_list").size()>=2) {
            layout.setVisibility(View.VISIBLE);
            per1.setText(tinyDB.getListString("discount_list").get(0));
            int amount;
            if(tinyDB.getListString("discount_list").get(1).trim().equals(""))
                amount=0;
            else
                amount=Integer.parseInt(tinyDB.getListString("discount_list").get(1).trim());
            if(amount==0) {
                msg_1.setText(R.string.on_any_billed_amount);
            }
            else{
                msg_1.setText("at min amount of Rs "+tinyDB.getListString("discount_list").get(1));
            }
        }
        if(tinyDB.getListString("discount_list").size()>=4){
            layout1.setVisibility(View.VISIBLE);
            per2.setText(tinyDB.getListString("discount_list").get(2));
            int amount;
            if(tinyDB.getListString("discount_list").get(3).trim().equals(""))
                amount=0;
            else
                amount=Integer.parseInt(tinyDB.getListString("discount_list").get(3).trim());
            if(amount==0) {
                msg_2.setText(R.string.on_any_billed_amount);
            }
            else{
                msg_2.setText("at min amount of Rs "+tinyDB.getListString("discount_list").get(3));
            }
        }
        if(tinyDB.getListString("discount_list").size()==6){
            layout2.setVisibility(View.VISIBLE);
            per3.setText(tinyDB.getListString("discount_list").get(4));
            int amount;
            if(tinyDB.getListString("discount_list").get(5).trim().equals(""))
                amount=0;
            else
                amount=Integer.parseInt(tinyDB.getListString("discount_list").get(5).trim());
            if(amount==0) {
                msg_3.setText(R.string.on_any_billed_amount);
            }
            else{
                msg_3.setText("at min amount of Rs "+tinyDB.getListString("discount_list").get(5));
            }
        }
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
        back.setOnClickListener(v->{
            back();
        });
        if(tinyDB.getListString("discount_list").size()>=2) {
            next.setBackgroundResource(R.drawable.bg_next);
            linearLayout.setVisibility(View.GONE);
        }
        else{
            next.setBackgroundResource(R.drawable.bg_next_inactive);
            linearLayout.setVisibility(View.VISIBLE);
        }
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                back();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
        next.setOnClickListener(v->{
            if(tinyDB.getListString("discount_list").size()>=2 && from==null) {
                Bundle args = new Bundle();
                args.putSerializable("upiArrayList", upiArrayList);
                args.putSerializable("upiViewList", upiViewList);
                onboarded onboarded = new onboarded();
                onboarded.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.cps, onboarded)
                        .commit();
            }
            else{
                reference.child(user.getUid()).child("Shop_Information").child("discounts").setValue(list_discount);
                Toast.makeText(getContext(), "Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        reference.child(user.getUid()).child("Shop_Information").child("discounts").setValue(list_discount);
    }

    private void back(){
        reference.child(user.getUid()).child("Shop_Information").child("discounts").setValue(list_discount);
        ((FragmentActivity) getContext()).getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,R.anim.enter_from_right, R.anim.exit_to_left)
                .remove(((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dis")).commit();
    }
}