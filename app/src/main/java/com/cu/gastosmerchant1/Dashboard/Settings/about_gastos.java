package com.cu.gastosmerchant1.Dashboard.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cu.gastosmerchant1.R;

public class about_gastos extends Fragment {

   View view;
   LinearLayout about ,grievance, privacy, terms;
   ImageView back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.about_gastos, container, false);

        about = view.findViewById(R.id.about);
        grievance = view.findViewById(R.id.grievance);
        privacy  = view.findViewById(R.id.privacy);
        terms = view.findViewById(R.id.terms);
        back = view.findViewById(R.id.imageView);


        back.setOnClickListener(v->{
            back();
        });


        about.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.about_main,new about_us())
                    .addToBackStack(null)
                    .commit();
        });

        grievance.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.about_main,new greivance())
                    .addToBackStack(null)
                    .commit();
        });

        privacy.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.about_main,new privacy_policy())
                    .addToBackStack(null)
                    .commit();
        });

        terms.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.about_main,new terms_of_service())
                    .addToBackStack(null)
                    .commit();
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