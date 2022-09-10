package com.cu.gastosmerchant1.Activity.OTP;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simform.customcomponent.SSCustomEdittextOutlinedBorder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    LinearLayout linearLayout;
    TextView send_otp,terms_and_condition,didnt,resend;
    ImageView p_back;
    String DeviceToken;
    int downspeed;
    int upspeed;
    private static final int CREDENTIAL_PICKER_REQUEST = 1;  // Set to an unused request code
    private static final int RESOLVE_HINT = 1101;  // Set to an unused request code
    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    PinView pinView;
    SSCustomEdittextOutlinedBorder edtEmail;
    int count=0;
    CountDownTimer countDownTimer;
    private PhoneAuthProvider.ForceResendingToken resendOTPtoken;
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    String station_name;
    // string for storing our verification ID
    private String verificationId;
    DatabaseReference user_reference;
    ArrayList<String> emptylist=new ArrayList<>();
    FirebaseUser user;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter, Context.RECEIVER_VISIBLE_TO_INSTANT_APPS);
        user_reference= FirebaseDatabase.getInstance().getReference().child("users");
        linearLayout=findViewById(R.id.sign_in);
        edtEmail=findViewById(R.id.edtEmail);
        send_otp=findViewById(R.id.textView23);
        p_back=findViewById(R.id.p_back);
        terms_and_condition=findViewById(R.id.textView12);
        didnt=findViewById(R.id.textView13);
        resend=findViewById(R.id.textView14);
        pinView = findViewById(R.id.pin_view);
        //upAnimate(logo_layout);

        linearLayout.setOnClickListener(v->{
            if(!send_otp.getText().toString().trim().equals("Verify")) {
                if (edtEmail.getGetTextValue().trim().length() == 10) {
                    offanimate(edtEmail);
                    terms_and_condition.setVisibility(View.GONE);
                    didnt.setVisibility(View.VISIBLE);
                    resend.setVisibility(View.VISIBLE);
                    p_back.setVisibility(View.VISIBLE);
                    send_otp.setText("Verify");
                    onAnimate(pinView);
                    pinView.setVisibility(View.VISIBLE);
                    String phone = "+91" + edtEmail.getGetTextValue();
                    sendVerificationCode(phone);
                    countTimer();
                } else {
                    Toast.makeText(Login.this, "Enter 10 digit mobile number.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(pinView.getText().toString().trim().length()==6){
                    String otp_text= Objects.requireNonNull(pinView.getText()).toString().trim();
                    Log.e("pinView","==========");
                    verifyCode(otp_text);
                }
                else{
                    Toast.makeText(this, "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resend.setOnClickListener(v->{
            if(resend.getText().toString().equals("RESEND NEW CODE")) {
                String phone = "+91" + edtEmail.getGetTextValue();
                resendVerificationCode(phone, resendOTPtoken);
                countTimer();
            }
        });

        terms_and_condition.setText(Html.fromHtml(getString(R.string.sampleText)));
        terms_and_condition.setMovementMethod(LinkMovementMethod.getInstance());

        p_back.setOnClickListener(v->{
            terms_and_condition.setVisibility(View.VISIBLE);
            didnt.setVisibility(View.GONE);
            resend.setVisibility(View.GONE);
            onAnimate(edtEmail);
            pinView.setText("");
            p_back.setVisibility(View.GONE);
            send_otp.setText("Send OTP");
            offanimate(pinView);
            countDownTimer.cancel();
        });

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ch=s+"";
                if(ch.length()==6){
                    String otp_text= Objects.requireNonNull(pinView.getText()).toString().trim();
                    Log.e("pinView","==========");
                    verifyCode(otp_text);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
            // Start listening for SMS User Consent broadcasts from senderPhoneNumber
            // The Task<Void> will be successful if SmsRetriever was able to start
            // SMS User Consent, and will error if there was an error starting.
            Task<Void> task = SmsRetriever.getClient(Login.this).startSmsUserConsent(null);
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(Login.this, ""+code, Toast.LENGTH_SHORT).show();
            Log.e("thats the code",code+"");
            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                pinView.setText(code);
                Log.e("inside code block","==========");
                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Log.e("error",e+"");
        }
    };
    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            // after getting credential we are
            // calling sign in method.
            signInWithCredential(credential);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Log.e("task successfull","Success");
                            update_ui();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Log.e("task result",task.getException().getMessage());
                            pinView.setError("Wrong Pin");
                        }
                    }
                });
    }
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };
    // Construct a request for phone numbers and show the picker
    private void requestHint() throws IntentSender.SendIntentException {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                RESOLVE_HINT, null, 0, 0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREDENTIAL_PICKER_REQUEST:
                // Obtain the phone number from the result
                if (resultCode == RESULT_OK) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  <-- will need to process phone number string
                }
                break;
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.e("message",message+"");
                    // Extract one-time code from the message and complete verification
                    // `sms` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                    String oneTimeCode = message.substring(0,6); // define this function
                    pinView.setText(oneTimeCode);
                    verifyCode(oneTimeCode);

                    // send one time code to the server
                } else {
                    // Consent canceled, handle the error ...
                }
                break;
        }
    }

    private void update_ui() {
        Intent intent=new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void countTimer()
    {
        countDownTimer=new CountDownTimer(25000, 1000)
        {
            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                resend.setText("Retry after - "+f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                resend.setText("RESEND NEW CODE");
                resend.setVisibility(View.VISIBLE);
                // btnVerify.setEnabled(true);
            }
        };
        countDownTimer.start();
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(0L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(0L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    void offanimate(View view){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationX",-800f);
        move.setDuration(1000);
        ObjectAnimator alpha2= ObjectAnimator.ofFloat(view, "alpha",0);
        alpha2.setDuration(500);
        AnimatorSet animset=new AnimatorSet();
        animset.play(alpha2).with(move);
        animset.start();
    }
    void onAnimate(View view){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationX",0f);
        move.setDuration(1000);
        ObjectAnimator alpha2= ObjectAnimator.ofFloat(view, "alpha",100);
        alpha2.setDuration(500);
        AnimatorSet animset=new AnimatorSet();
        animset.play(alpha2).with(move);
        animset.start();
    }

    void upAnimate(View view){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationY",-180f);
        move.setDuration(500);
        ObjectAnimator alpha2= ObjectAnimator.ofFloat(view, "alpha",100);
        alpha2.setDuration(500);
        AnimatorSet animset=new AnimatorSet();
        animset.play(alpha2).with(move);
        animset.start();
    }
}