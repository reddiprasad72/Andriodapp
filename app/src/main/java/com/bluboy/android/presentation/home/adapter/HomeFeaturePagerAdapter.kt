package com.bluboy.android.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bluboy.android.R
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.presentation.utility.invisible
import com.bluboy.android.presentation.utility.loadCornerImageBanner
import com.bluboy.android.presentation.utility.loadCornerImageGame
import com.bluboy.android.presentation.utility.visible

open class HomeFeaturePagerAdapter(
    val mContext: Context,
    val list: ArrayList<GamesData>,
    val callback : (temp : GamesData, position: Int) -> Unit
    ) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        var view : View? = null
            view = LayoutInflater.from(mContext).inflate(R.layout.item_feature_game, container, false)
        val imgBanner: AppCompatImageView? = view?.findViewById(R.id.imgBanner)
        val imgBlurBanner: AppCompatImageView? = view?.findViewById(R.id.imageViewBlur)
        val textViewGameName : AppCompatTextView? = view?.findViewById(R.id.textViewGameName)
        val textViewComingSoon : AppCompatTextView? = view?.findViewById(R.id.tvComingSoon)
        val btnPlayNow : AppCompatButton? = view?.findViewById(R.id.buttonPlayNow)

        imgBanner?.let { loadCornerImageGame(it,list.get(position).gameImage) }
        imgBlurBanner?.let { loadCornerImageBanner(it,list.get(position).gameBanner) }
        if (textViewGameName != null) {
            textViewGameName.text = list[position].gameName
        }

        if (list[position]?.gameCommingSoon == "Y") {
            textViewComingSoon?.visible()
            btnPlayNow?.invisible()
//            rowBinding.textViewOnlineUser.invisible()
        } else {
            textViewComingSoon?.invisible()
            btnPlayNow?.visible()
//            rowBinding.textViewOnlineUser.visible()
        }

        if (list[position]?.gameCommingSoon == "Y") {
        }else {
            btnPlayNow?.setOnClickListener {
                callback.invoke(list[position], position)
            }
        }

        if (list[position]?.gameCommingSoon == "Y") {
        }else {
            view.setOnClickListener {
                callback.invoke(list[position], position)
            }
        }

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as ConstraintLayout
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}