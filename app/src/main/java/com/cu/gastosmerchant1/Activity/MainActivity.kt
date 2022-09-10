package com.cu.gastosmerchant1.Activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.cu.gastosmerchant1.Adapters.MyRegex
import com.cu.gastosmerchant1.R
import com.cu.gastosmerchant1.databinding.ActivityMain2Binding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var mAuth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var verificationId:String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)
        mAuth= FirebaseAuth.getInstance()



        binding.edtPhoneNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                Log.d("hi", "onTextChanged: ${binding.edtPhoneNo.text.toString().length}")
                if(binding.edtPhoneNo.text.toString().length==10)
                {
                    binding.nextContinue.setBackgroundResource(R.drawable.bg_get_started)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        binding.nextContinue.setOnClickListener(View.OnClickListener {
            if(binding.edtPhoneNo.text.isEmpty())
            {


                val snackBar = Snackbar.make(
                    it, "Please Enter ContactNo",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackBar.setActionTextColor(Color.RED)
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(Color.RED)
                val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                snackBar.show()
            }
            else{
                val b:Boolean= MyRegex.contact(binding.edtPhoneNo.text.toString())
                if(!b)
                {
                    val snackBar = Snackbar.make(
                        it, "Please Enter Valid ContactNo",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null)
                    snackBar.setActionTextColor(Color.RED)
                    val snackBarView = snackBar.view
                    snackBarView.setBackgroundColor(Color.RED)
                    val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.WHITE)
                    snackBar.show()
                }
                else{

                    val userRef = FirebaseDatabase.getInstance().getReference("UserAppData")
                    userRef.orderByChild("contact").equalTo(binding.edtPhoneNo.text.toString()).addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            binding.nextContinue.visibility= View.GONE
                            binding.loader.visibility= View.VISIBLE
                            if(dataSnapshot.getValue()!=null)
                            {
                                Log.d("register", "Already Registered")
                                otpSend()
                            }
                            else
                            {

                                otpSend()
                                // Log.d("register", "not Registered")
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    })
                }
            }
        })
    }

    /*fun buttonClickEvent(v: View) {
        var phoneNo: String = binding.edtPhoneNo.getText().toString()
        try {
            when (v.id) {
                R.id.btnZero -> {
                    phoneNo += "0"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btnOne -> {
                    phoneNo += "1"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btnTwo -> {
                    phoneNo += "2"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btnThree -> {
                    phoneNo += "3"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btnFour -> {
                    phoneNo += "4"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btnFive -> {
                    phoneNo += "5"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btnSix -> {
                    phoneNo += "6"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btnSeven -> {
                    phoneNo += "7"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btnEight -> {
                    phoneNo += "8"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btnNine -> {
                    phoneNo += "9"
                    binding.edtPhoneNo.setText(phoneNo)
                }
                R.id.btndel -> {
                    if (phoneNo != null && phoneNo.isNotEmpty())
                    {
                        phoneNo = phoneNo.substring(0, phoneNo.length - 1)
                        binding.nextContinue.setBackgroundResource(R.drawable.bg_grey_continue)
                    }
                    binding.edtPhoneNo.setText(phoneNo)
                }
            }
        } catch (ex: Exception) {
        }
    }*/



    private fun otpSend()
    {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential)
            {
                Log.d("registered", "onVerificationCompleted: Already Registered")

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("failed", "onVerificationFailed: $e")
                val snackBar = Snackbar.make(
                    binding.root, "Oops Verification Failed",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackBar.setActionTextColor(Color.RED)
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(Color.RED)
                val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                snackBar.show()
                binding.nextContinue.visibility= View.VISIBLE
                binding.loader.visibility= View.GONE
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                /*  binding.nextContinue.visibility=View.VISIBLE
                  binding.loader.visibility=View.GONE*/



                if(!binding.edtPhoneNo.text.isNullOrEmpty())
                {
                    val i = Intent(applicationContext, OTPActivity::class.java)
                    i.putExtra("contactNumber",binding.edtPhoneNo.text.toString())
                    i.putExtra("verificationId",verificationId)
                    startActivity(i)
                    binding.edtPhoneNo.setText("")
                }
                finish()
            }
        }
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber("+91"+binding.edtPhoneNo.text.toString())
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}
