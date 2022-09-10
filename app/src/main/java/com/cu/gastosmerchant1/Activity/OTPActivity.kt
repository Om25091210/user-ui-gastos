package com.cu.gastosmerchant1.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.cu.gastosmerchant1.Dashboard.Home
import com.cu.gastosmerchant1.R
import com.cu.gastosmerchant1.databinding.ActivityOtpactivityBinding
import com.cu.gastosmerchant1.details.Model
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
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    //  lateinit var contact: String
    lateinit var verificationId: String
    lateinit var otp: String
    companion object{
        var array = ArrayList<Model>()
    }
    var contact: String = ""
    var email: String = ""
    var name: String = ""
    var dob: String = ""
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var editText = arrayOfNulls<EditText>(6)
    private lateinit var binding: ActivityOtpactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpactivity)
        countTimer()
        val snackBar = Snackbar.make(
            binding.root, "OTP Sent Successfully",
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackBar.setActionTextColor(Color.BLACK)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(Color.GREEN)
        val textView =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snackBar.show()


        binding.resend.setOnClickListener {
            binding.resend.visibility = View.INVISIBLE
            binding.optLoader.visibility = View.VISIBLE
            otpSend()
        }


        val intent = intent
        if (intent != null) {
            contact = intent.getStringExtra("contactNumber").toString()
            verificationId = intent.getStringExtra("verificationId").toString()
        }
        binding.textMob.text =
            "Enter the OTP sent to your mobile number\n" + "(" + contact + ")" + "to proceed."

        Log.d("vId", "onCreate: $verificationId")



        binding.nextContinue.setOnClickListener(View.OnClickListener {

            if (binding.edt1.text.isEmpty()
                || binding.edt2.text.isEmpty()
                || binding.edt3.text.isEmpty()
                || binding.edt4.text.isEmpty()
                || binding.edt5.text.isEmpty()
                || binding.edt6.text.isEmpty()
            ) {
                val snackBar = Snackbar.make(
                    it, "Please Enter ValidOTP",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackBar.setActionTextColor(Color.RED)
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(Color.RED)
                val textView =
                    snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                snackBar.show()
            } else {

                if (verificationId != null) {
                    binding.loader.visibility = View.VISIBLE
                    binding.nextContinue.visibility = View.GONE
                    var code = binding.edt1.text.toString() +
                            binding.edt2.text.toString() +
                            binding.edt3.text.toString() +
                            binding.edt4.text.toString() +
                            binding.edt5.text.toString() +
                            binding.edt6.text.toString()
                    val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
                    Log.d("checkCodes", "onCreate: ${verificationId}${code}")
                    val firebaseAuth = FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                checkRegisteredOrNot(it.result.user?.phoneNumber!!)
                                binding.loader.visibility = View.GONE
                                binding.nextContinue.visibility = View.VISIBLE
                                Log.d("GetUsers", "onCreate: ${it.result.user?.phoneNumber}")


                            } else {
                                binding.loader.visibility = View.GONE
                                binding.nextContinue.visibility = View.VISIBLE
                                val snackBar = Snackbar.make(
                                    binding.root, "Please Enter ValidOTP",
                                    Snackbar.LENGTH_LONG
                                ).setAction("Action", null)
                                snackBar.setActionTextColor(Color.RED)
                                val snackBarView = snackBar.view
                                snackBarView.setBackgroundColor(Color.RED)
                                val textView =
                                    snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                                textView.setTextColor(Color.WHITE)
                                snackBar.show()
                            }
                        }

                }
            }
        })
        editText[0] = findViewById<View>(R.id.edt1) as EditText
        editText[1] = findViewById<View>(R.id.edt2) as EditText
        editText[2] = findViewById<View>(R.id.edt3) as EditText
        editText[3] = findViewById<View>(R.id.edt4) as EditText
        editText[4] = findViewById<View>(R.id.edt5) as EditText
        editText[5] = findViewById<View>(R.id.edt6) as EditText

        editText[0]!!.requestFocus()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            editText[0]!!.addOnUnhandledKeyEventListener { v: View?, event: KeyEvent ->
                Log.i("UnhandledKeyEvent", "" + event)
                if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                    if (editText[0]!!.isFocused) {
                        Log.i("et[1]", "Focused")
                    }
                    if (editText[1]!!.isFocused) {
                        editText[1]!!.clearFocus()
                        editText[0]!!.requestFocus()
                        editText[0]!!.setSelection(editText[0]!!.text.length)
                    }
                    if (editText[2]!!.isFocused) {
                        editText[2]!!.clearFocus()
                        editText[1]!!.requestFocus()
                        editText[1]!!.setSelection(editText[1]!!.text.length)
                    }
                    if (editText[3]!!.isFocused) {
                        editText[3]!!.clearFocus()
                        editText[2]!!.requestFocus()
                        editText[2]!!.setSelection(editText[2]!!.text.length)
                    }
                    if (editText[4]!!.isFocused) {
                        editText[4]!!.clearFocus()
                        editText[3]!!.requestFocus()
                        editText[3]!!.setSelection(editText[3]!!.text.length)
                    }
                    if (editText[5]!!.isFocused) {
                        editText[5]!!.clearFocus()
                        editText[4]!!.requestFocus()
                        editText[4]!!.setSelection(editText[4]!!.text.length)
                    }
                }
                false
            }
        } else {
            editText[0]!!.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected $event")
                    Log.i("Hardware", "Key detected $keyCode")
                }
                false
            }
            editText[1]!!.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected $event")
                    Log.i("Hardware", "Key detected $keyCode")
                    editText[1]!!.clearFocus()
                    editText[0]!!.requestFocus()
                    editText[0]!!.setSelection(editText[0]!!.text.length)
                }
                false
            }
            editText[2]!!.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected $event")
                    Log.i("Hardware", "Key detected $keyCode")
                    editText[2]!!.clearFocus()
                    editText[1]!!.requestFocus()
                    editText[1]!!.setSelection(editText[1]!!.text.length)
                }
                false
            }
            editText[3]!!.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected $event")
                    Log.i("Hardware", "Key detected $keyCode")
                    editText[3]!!.clearFocus()
                    editText[2]!!.requestFocus()
                    editText[2]!!.setSelection(editText[2]!!.text.length)
                }
                false
            }
            editText[4]!!.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected $event")
                    Log.i("Hardware", "Key detected $keyCode")
                    editText[4]!!.clearFocus()
                    editText[3]!!.requestFocus()
                    editText[3]!!.setSelection(editText[3]!!.text.length)
                }
                false
            }
            editText[5]!!.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected $event")
                    Log.i("Hardware", "Key detected $keyCode")
                    editText[5]!!.clearFocus()
                    editText[4]!!.requestFocus()
                    editText[4]!!.setSelection(editText[4]!!.text.length)
                }
                false
            }
        }


        editText[0]!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Log.i("Char", "editText[0] $s")
                Log.i("start", "editText[0] $start")
                Log.i("count", "editText[0] $count")
                Log.i("after", "editText[0] $after")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    editText[0]!!.clearFocus()
                    editText[1]!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {
                Log.i("Char", "editText[0] After $s")
            }
        })
        editText[1]!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Log.i("Char", "editText[1] $s")
                Log.i("start", "editText[1] $start")
                Log.i("count", "editText[1] $count")
                Log.i("after", "editText[1] $after")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    editText[1]!!.clearFocus()
                    editText[2]!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        editText[2]!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Log.i("Char length", "editText[2] " + s.length)
                Log.i("Char", "editText[2] $s")
                Log.i("start", "editText[2] $start")
                Log.i("count", "editText[2] $count")
                Log.i("after", "editText[2] $after")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.i("Char length", "editText[2] " + s.length)
                if (count > 0) {
                    editText[2]!!.clearFocus()
                    editText[3]!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {
                Log.i("Char length", "editText[2] " + s.length)
            }
        })
        editText[3]!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Log.i("Char", "editText[3] $s")
                Log.i("start", "editText[3] $start")
                Log.i("count", "editText[3] $count")
                Log.i("after", "editText[3] $after")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    editText[3]!!.clearFocus()
                    editText[4]!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        editText[4]!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Log.i("Char", "editText[4] $s")
                Log.i("start", "editText[4] $start")
                Log.i("count", "editText[4] $count")
                Log.i("after", "editText[4] $after")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    editText[4]!!.clearFocus()
                    editText[5]!!.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        editText[5]!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Log.i("Char", "editText[5] $s")
                Log.i("start", "editText[5] $start")
                Log.i("count", "editText[5] $count")
                Log.i("after", "editText[5] $after")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    // editText[5].clearFocus();
                    //Intent to Signup Fragment here
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun checkRegisteredOrNot(any: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("Merchant_data")
        userRef.orderByChild("phoneNumber").equalTo(any.replace("+91", ""))
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    binding.nextContinue.visibility = View.GONE
                    binding.loader.visibility = View.VISIBLE
                    if (dataSnapshot.value != null) {

                        dataSnapshot.children.forEach {
                            dob = it.child("dob").getValue(String::class.java).toString()
                            email = it.child("email").getValue(String::class.java).toString()
                            contact = it.child("contact").getValue(String::class.java).toString()
                            name = it.child("name").getValue(String::class.java).toString()
                            Log.d("dataValue", "onDataChange: ${dob},${email},${name},${contact}")
                        }
                        array.add(Model(name, email, dob, contact,""))
                        val i = Intent(applicationContext, Home::class.java)
                        i.putExtra("contact", contact)
                        startActivity(i)


                        finish()
                        Log.d("dataValue", "onDataChange: ${dataSnapshot.value.toString()}")
                        /*   val dob=dataSnapshot
                               .getValue(String::class.java)*/


                        //   Log.d("Vallues", "onDataChange: ${dob}")
                        Log.d("register", "Already Registered")
                        binding.nextContinue.visibility = View.GONE
                        binding.loader.visibility = View.VISIBLE

                    } else {
                        val i =
                            Intent(applicationContext, Home::class.java)
                        i.putExtra("contact", contact)
                        startActivity(i)
                        Log.d("register", "not Registered")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("checkiserror", "$databaseError")
                }
            })


    }

    private fun countTimer() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding.timer.text =
                    ("The OTP is valid for the remaining time of\n" + f.format(min) + ":" + f.format(
                        sec
                    ))
                binding.resend.visibility = View.GONE

            }

            // When the task is over it will print 00:00:00 there
            override fun onFinish() {
                // resendOTPTV.setVisibility(View.VISIBLE)
                // timer.setVisibility(View.GONE)
                // btnVerify.setEnabled(true);
                binding.resend.visibility = View.VISIBLE
                binding.timer.visibility = View.GONE


            }
        }.start()
    }

    private fun otpSend() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {


            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("failed", "onVerificationFailed: $e")
                val snackBar = Snackbar.make(
                    binding.root, "Oops Verification Failed",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackBar.setActionTextColor(Color.parseColor("#FF5C5C"))
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(Color.parseColor("#FF5C5C"))
                val textView =
                    snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                snackBar.show()
                binding.nextContinue.visibility = View.VISIBLE
                binding.loader.visibility = View.GONE
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                binding.resend.visibility = View.VISIBLE
                binding.optLoader.visibility = View.GONE
                val snackBar = Snackbar.make(
                    binding.root, "OTP Sent Successfully",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackBar.setActionTextColor(Color.BLACK)
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(Color.parseColor("#8AFF8A"))
                val textView =
                    snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                snackBar.show()
            }
        }
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber("+91$contact")
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}