package com.cu.gastosmerchant1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cu.gastosmerchant1.details.SettingCard;
import com.cu.gastosmerchant1.details.SettingItem;
import com.cu.gastosmerchant1.settings.ManageAds;
import com.cu.gastosmerchant1.settings.ManageDesign;
import com.cu.gastosmerchant1.settings.PostAds;
import com.cu.gastosmerchant1.settings.PrivacyPolicyWeb;
import com.cu.gastosmerchant1.settings.ProviderWallet;
import com.cu.gastosmerchant1.settings.RecyclerAdapterSetting;
import com.cu.gastosmerchant1.settings.RecyclerAdapterSettingMenu;
import com.cu.gastosmerchant1.settings.ReferAndEarn;
import com.cu.gastosmerchant1.settings.RequestDesign;
import com.cu.gastosmerchant1.settings.acc_settings;
import com.cu.gastosmerchant1.settings.TermsOfService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements RecyclerAdapterSettingMenu.clickItem {


    private RecyclerView recycler_setting;


    private ImageView prev;
    FirebaseAuth mAuth;

    private CardView logOut;
    private ArrayList<SettingCard> settingCardArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        RecyclerAdapterSetting adapterSetting = new RecyclerAdapterSetting(settingCardArrayList,Settings.this);

        ArrayList<SettingItem> settingItems = new ArrayList<>();
        //Account
        settingItems.add(new SettingItem("Manage Account",R.drawable.ic_account_stroke_icon));
        settingItems.add(new SettingItem("Manage Shop",R.drawable.ic_group_5470_buy_shop));
       // settingItems.add(new SettingItem("Provider Wallet",R.drawable.ic_setting_wallet));
        settingCardArrayList.add(new SettingCard("Account",settingItems));


        ArrayList<SettingItem> settingItems1 = new ArrayList<>();
        //Promotion
        settingItems1.add(new SettingItem("Post Ads",R.drawable.ic_plus_setting));
        settingItems1.add(new SettingItem("Manage Ads",R.drawable.ic_chart));
        settingCardArrayList.add(new SettingCard("Promotion",settingItems1));

        ArrayList<SettingItem> settingItems2 = new ArrayList<>();
        //
        settingItems2.add(new SettingItem("Request Design",R.drawable.ic_downlaod_setting));
        settingItems2.add(new SettingItem("Manage Designs",R.drawable.ic_folder_setting));
        settingCardArrayList.add(new SettingCard("Branding",settingItems2));

        ArrayList<SettingItem> settingItems3 = new ArrayList<>();
        //Refer And Earn
        settingItems3.add(new SettingItem("Invite Your Friend",R.drawable.ic_add_user));
        settingCardArrayList.add(new SettingCard("Refer & Earn",settingItems3));

        ArrayList<SettingItem> settingItems4 = new ArrayList<>();
        //Support
        settingItems4.add(new SettingItem("Report a Problem",R.drawable.ic_pen_stroke_icon));
        settingItems4.add(new SettingItem("Help Center",R.drawable.ic_group_5437_help));
        settingCardArrayList.add(new SettingCard("Support",settingItems4));

        ArrayList<SettingItem> settingItems5 = new ArrayList<>();
        //About
        settingItems5.add(new SettingItem("About Us",R.drawable.ic_info_square_setting));
        settingItems5.add(new SettingItem("Terms of Service",R.drawable.ic_document_setting));
        settingItems5.add(new SettingItem("Privacy Policy",R.drawable.ic_paper_setting));
        settingCardArrayList.add(new SettingCard("About",settingItems5));

        recycler_setting.setAdapter(adapterSetting);
        recycler_setting.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mAuth = FirebaseAuth.getInstance();

        prev = findViewById(R.id.prev);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*


        rateUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo rate the app here.
                Toast.makeText(Settings.this, "This must open the playstore directly to rate the app", Toast.LENGTH_SHORT).show();
            }
        });

 */
    }

    @Override
    public void onSettingOptionClicked(String optionName) {
        switch (optionName){
            case "Manage Account":
                ManageAccount();
                break;
            case "Manage Shop":
                ManageShop();
                break;
//            case "Provider Wallet":
//                ProviderWaller();
//                break;
            case "Post Ads":
                PostAds1();
                break;
            case "Manage Ads":
                ManageAds();
                break;
            case "Request Design":
                RequestDesign();
                break;
            case "Manage Designs":
                ManageDesigns();
                break;
            case "Invite Your Friend":
                InviteYouFriend();
                break;
            case "Report a Problem":
                ReportAProblem();
                break;
            case "Help Center":
                HelpCenter();
                break;
            case "About Us":
                AboutUs();
                break;
            case "Terms of Service":
                TermsOfService();
                break;
            case "Privacy Policy":
                PrivacyPolicy();
                break;
        }
    }

    private void PrivacyPolicy() {
        startActivity(new Intent(Settings.this, PrivacyPolicyWeb.class));
    }

    private void TermsOfService() {
        startActivity(new Intent(Settings.this, TermsOfService.class));
    }

    private void AboutUs() {
        startActivity(new Intent(Settings.this, about_us.class));
    }

    private void HelpCenter() {
        startActivity(new Intent(Settings.this, help_centre.class));
    }

    private void ReportAProblem() {
        startActivity(new Intent(Settings.this, report_problem.class));
    }

    private void InviteYouFriend() {
        startActivity(new Intent(Settings.this, ReferAndEarn.class));
    }

    private void ManageDesigns() {
        startActivity(new Intent(Settings.this, ManageDesign.class));
    }

    private void RequestDesign() {
        startActivity(new Intent(Settings.this, RequestDesign.class));
    }

    private void ManageAds() {
        startActivity(new Intent(Settings.this, ManageAds.class));
    }

    private void PostAds1() {
        startActivity(new Intent(Settings.this, PostAds.class));
    }

    private void ProviderWaller() {
        startActivity(new Intent(Settings.this, ProviderWallet.class));
    }

    private void ManageShop() {
        startActivity(new Intent(Settings.this, shop_setting.class));
    }

    private void ManageAccount() {
        startActivity(new Intent(Settings.this, acc_settings.class));
    }
}
