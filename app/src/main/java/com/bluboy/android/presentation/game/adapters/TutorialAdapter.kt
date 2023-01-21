package com.bluboy.android.presentation.game.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bluboy.android.databinding.FragmentTutorialBinding

class TutorialAdapter constructor(
    private val context: Context,
    private val images: List<Int>
) : PagerAdapter() {

    lateinit var rowBinding: FragmentTutorialBinding

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        rowBinding =
            FragmentTutorialBinding.inflate(
                LayoutInflater.from(container.context),
                container, false
            )

        rowBinding.imageViewTutorial.setImageResource(images[position])

        val vp = container as ViewPager
        vp.addView(rowBinding.root, 0)
        return rowBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}