package com.cu.gastosmerchant1.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.cu.gastosmerchant1.Activity.OTP.Login
import com.cu.gastosmerchant1.Adapters.PageAdapter
import com.cu.gastosmerchant1.Adapters.PagerModel
import com.cu.gastosmerchant1.R

import com.google.firebase.auth.FirebaseAuth

class OnBoardingActivity : AppCompatActivity() {

    lateinit var binding: com.cu.gastosmerchant1.databinding.ActivityOnBoardingBinding
    lateinit var mAuth: FirebaseAuth
    private  val list = listOf(PagerModel(
        R.drawable.imgone),
        PagerModel(R.drawable.imgtwo), PagerModel(
            R.drawable.imgthree)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_on_boarding)
        mAuth = FirebaseAuth.getInstance()
        val adapter = PageAdapter(applicationContext,list)
        binding.apply {
            viewPager.adapter = adapter
            tabIndicator.setViewPager(viewPager)
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    if (position==0){
                        heading1.text = getString(R.string.onBoarding_1)
                        heading2.text = getString(R.string.content_m1)
                    }
                    if (position==1){
                        heading1.text = getString(R.string.onBoarding_2)
                        heading2.text = getString(R.string.content_m2)
                    }
                    if (position==2){
                        heading1.text = getString(R.string.onBoarding_3)
                        heading2.text = getString(R.string.content_m3)
                    }
                }

                override fun onPageSelected(position: Int) {

                }
                override fun onPageScrollStateChanged(state: Int) {

                }
            })

            // preChecks()
        }


        binding.getStarted.setOnClickListener {
            val intent= Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}