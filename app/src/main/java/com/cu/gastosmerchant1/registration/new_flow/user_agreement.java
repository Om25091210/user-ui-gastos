package com.cu.gastosmerchant1.registration.new_flow;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.upiparse.UpiView;

import java.util.ArrayList;

public class user_agreement extends Fragment {

    View view;
    ArrayList<Payment_Information> upiArrayList = new ArrayList<>();
    ArrayList<UpiView> upiViewList = new ArrayList<>();
    String discount,minamount;
    TextView terms,i_agree;
    CheckBox checkbox;
    String from;
    String half_terms="Any user signing up on this will be considered as GASTOS PROVIDER and he/she agrees to offer a commission Percentage, given text field in shop profile, according to his/her own comfort of business so that we can use it as a discount on every billed amount of the GASTOS customer. We hereby declare that the payment of the billed amount after discounting will be directly reflected in the PROVIDER’s account as of which we don’t keep any money in-between the transaction. The received amount will be only ";
    String another_half=" as of the bill created by the PROVIDER. The PROVIDER’s UPI address and payment receivable information will be asked and will not disclosed or shared to anyone or anywhere. The information which will be visible in our main application and before the users are mentioned below along with their purpose: • Images – All images posted by the provider will be shown in his profile page in the user application (their cover photo of the shop and other images related to their businesses) • Owner Name – At Shop profile page so that user can know about the Provider • Shop Address – At shop profile to help user to get clear idea about the shop address and can reach there • Shop Co-ordinates – At shop profile so that user can directly navigate to the shop location saved by the Provider • Commission Percentage – At shop profile in user app as well as in other pages inside user application so that user can see how much discount the shop is giving, as we are not keeping money in between the transaction so the entered Commission value will be depending on their business and how much they want to offer and grow. • Contact Number – At shop profile page in GASTOS user application so that a communication can be done between the both for services related to their business or order delivery (if they provide) or order pickup (if they provide) GASTOS is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it.";
    ImageView back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.user_agreement, container, false);
        if(getArguments()!=null){
            upiArrayList= (ArrayList<Payment_Information>) getArguments().getSerializable("upiArrayList");
            upiViewList= (ArrayList<UpiView>) getArguments().getSerializable("upiViewList");
            discount=  getArguments().getString("discount");
            minamount=  getArguments().getString("minamount");
        }
        if(getArguments()!=null){
            from=getArguments().getString("fromHome");
        }
        terms=view.findViewById(R.id.terms);
        back=view.findViewById(R.id.imageView);
        checkbox=view.findViewById(R.id.checkbox);
        i_agree=view.findViewById(R.id.add_discount);
        Log.e("min",minamount+"");
        float dis=Float.parseFloat(discount)/100;
        Log.e("dis",dis+"");
        float dis_amt=Integer.parseInt(minamount)*dis;
        Log.e("dis_amt",dis_amt+"");
        terms.setText(half_terms+discount+"% less ("+dis_amt+" = value of percentage commission given by the provider)"+another_half);
        i_agree.setOnClickListener(v->{
            if(checkbox.isChecked()){
                try {
                    ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                            .remove(((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("tag1")).commit();
                    ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                            .remove(((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("tag2")).commit();
                    ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                            .remove(((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dis")).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(from==null) {
                    Bundle args = new Bundle();
                    args.putSerializable("upiArrayList", upiArrayList);
                    args.putSerializable("upiViewList", upiViewList);
                    Add_Discount add_discount = new Add_Discount();
                    add_discount.setArguments(args);
                    ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            .add(R.id.cps, add_discount, "dis")
                            .commit();
                }
                else{
                    Bundle args = new Bundle();
                    args.putSerializable("upiArrayList", upiArrayList);
                    args.putSerializable("upiViewList", upiViewList);
                    args.putString("fromHome","NotnullHome");
                    Add_Discount add_discount = new Add_Discount();
                    add_discount.setArguments(args);
                    ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            .add(R.id.lay, add_discount, "dis")
                            .commit();
                }
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
                .remove(((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("tag2")).commit();
    }
}