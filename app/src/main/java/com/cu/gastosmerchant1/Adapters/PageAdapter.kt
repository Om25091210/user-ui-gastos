package com.cu.gastosmerchant1.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.cu.gastosmerchant1.R


open class PageAdapter(context: Context, val list :List<PagerModel>) : PagerAdapter() {

    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View = LayoutInflater.from(container.context).inflate(R.layout.single_row_image, container, false)

        val imageView = view.findViewById<ImageView>(R.id.image)
        imageView.setImageResource(list.get(position).icon)

        container.addView(view)

        return view
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
