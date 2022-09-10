package com.cu.gastosmerchant1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class about_us extends AppCompatActivity {

    private ImageView prev;

    private TextView aboutUsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        prev=findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        aboutUsDetails = findViewById(R.id.aboutUsText);

        final String about = "Welcome to GASTOS, your number one solution for benefits on your daily payments. We're dedicated to providing you with the best of the services, with a focus on dependability. Customer service, and your benefits.\n" +
                "\n" +
                "GASTOS is FinTech Startup Based in Chandigarh aimed to uplift the local and small businesses. Gastos is an Application based startup which helps user to save their money while doing payments at their local shops and businesses also helping them to manage all of their expenses at one place.\n" +
                "Gastos has a potential of becoming a Billion Dollar Industry in coming years and has a potential user base of more than 200 Million to target. Gastos is the only startup in India which is bringing UPI Digital cards for the users which helps them to save their money.\n" +
                "\n" +
                "During this Covid-19 situation time and between the hard races of online delivery, the local and small scaled businesses are mostly affected. Gastos is the startup which is promoting these small businesses to grow and become a brand before the user. Gastos is solving multiple problems for users like fake discounts, lack of saving,\n" +
                "unmanaged expenses.\n" +
                "\n" +
                "We're working to turn our passion for the services into a booming Market Network. We hope you enjoy our services as much as we enjoy offering them to you.\n" +
                "\n" +
                "\n" +
                "Sincerely,\n" +
                "Sahgal Kumar\n" +
                "(Founder)\n" +
                "\n" +
                "here are some following ways through which you will be connected with us.\n" +
                "\n" +
                "•\tInstagram : gastos.in\n" +
                "•\tFacebook : GASTOS DEALZ\n" +
                "•\tTwitter  : gastos_in\n" +
                "For any queries you can contact us as help@gastos.in  \n";

        aboutUsDetails.setText(about);
    }
}
