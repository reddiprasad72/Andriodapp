package com.bluboy.android.presentation.wallet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.data.models.Coupon
import com.bluboy.android.databinding.ItemPromocodeBinding
import com.bluboy.android.presentation.utility.gone
import com.bluboy.android.presentation.utility.setSafeOnClickListener

class PromocodeAdapter(
    var arrayListCoupon: ArrayList<Coupon>,
    val callback: (Coupon) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemPromocodeBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemPromocodeBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayListCoupon.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        rowBinding.textViewCouponName.text = arrayListCoupon[position].couponName
        rowBinding.textViewCouponDescription.text = arrayListCoupon[position].couponDescription
        rowBinding.textViewCouponCode.text = arrayListCoupon[position].couponCode

        arrayListCoupon[position].couponDescription.ifBlank {
            rowBinding.textViewCouponDescription.gone()
        }

        holder.itemView.setSafeOnClickListener {
            callback.invoke(arrayListCoupon[position])
        }
        
        rowBinding.textViewRedeemNow.setSafeOnClickListener {
            callback.invoke(arrayListCoupon[position])
        }
    }

    inner class ViewHolder(binding: ItemPromocodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}