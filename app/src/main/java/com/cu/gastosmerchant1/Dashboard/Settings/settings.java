package com.cu.gastosmerchant1.Dashboard.Settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.Settings;
import com.cu.gastosmerchant1.about_us;
import com.cu.gastosmerchant1.help_centre;
import com.cu.gastosmerchant1.registration.new_flow.ProfileDetails;
import com.cu.gastosmerchant1.registration.new_flow.SelectServices;
import com.cu.gastosmerchant1.settings.ProviderWallet;
import com.cu.gastosmerchant1.settings.ReferAndEarn;

import java.util.Objects;


public class settings extends Fragment {

    View view;
    LinearLayout profile_details,shop_details,providerwallet, refer , rate_us ,about_gastos , help_support;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.settings, container, false);
        view.findViewById(R.id.imageView).setOnClickListener(v->{
            back();
        });

        refer = view.findViewById(R.id.refer);
        rate_us = view.findViewById(R.id.rate_us); // done
        help_support = view.findViewById(R.id.help);
        about_gastos = view.findViewById(R.id.about_gas);

        rate_us.setOnClickListener(v->{
            final String appPackageName = requireActivity().getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });

        about_gastos.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.lay,new about_gastos())
                    .addToBackStack(null)
                    .commit();
        });

        help_support.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.lay,new help_support())
                    .addToBackStack(null)
                    .commit();
        });

        refer.setOnClickListener(v->{
            /*((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.lay,new refer())
                    .addToBackStack(null)
                    .commit();*/
            final String appPackageName = requireActivity().getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });


        profile_details=view.findViewById(R.id.profile_details);
        profile_details.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.lay,new acc_profile())
                    .addToBackStack(null)
                    .commit();
        });
        shop_details=view.findViewById(R.id.shop_details);
        shop_details.setOnClickListener(v->{
            Intent intent=new Intent(getContext(), ProfileDetails.class);
            intent.putExtra("profile","from_settings");
            startActivity(intent);
        });
        providerwallet=view.findViewById(R.id.wallet);
        providerwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProviderWallet.class));
            }
        });
        return view;
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