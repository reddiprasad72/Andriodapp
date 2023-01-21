package com.bluboy.android.presentation.wallet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.BeneficiaryData
import com.bluboy.android.databinding.ItemBeneficiaryWithdrawBinding
import com.bluboy.android.presentation.utility.*

class WithdrawDetailAdapter(
    val arrayList: ArrayList<BeneficiaryData>,
    val callback: (Int, View, BeneficiaryData) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemBeneficiaryWithdrawBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        rowBinding =
            ItemBeneficiaryWithdrawBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        rowBinding.constraintBank.setSafeOnClickListener {
            callback.invoke(position, rowBinding.constraintBank, arrayList[position])
        }
        if (position == arrayList.size - 1) {
            rowBinding.viewLine.invisible()
        } else {
            rowBinding.viewLine.visible()
        }

        when (arrayList[position].transferMode) {
            "upi" -> {
                loadImage(rowBinding.imageViewBank, R.drawable.bheem_upi)
                rowBinding.textViewBankAcc.text =
                    "UPI"//arrayList[position].beneficiaryNickName
                rowBinding.textViewAccountNumber.text = arrayList[position].beneficiaryVpa
                rowBinding.textViewAccountName.text = arrayList[position].beneficiaryName
            }
            "banktransfer" -> {
                loadImage(rowBinding.imageViewBank, R.drawable.bank_icon)
                rowBinding.textViewBankAcc.text =
                    "Bank Acc."//arrayList[position].beneficiaryNickName
                rowBinding.textViewAccountNumber.text =
                    arrayList[position].beneficiaryAccountNumber
                rowBinding.textViewAccountName.text = arrayList[position].beneficiaryName
            }
            "paytm" -> {
                loadImage(rowBinding.imageViewBank, R.drawable.paytm)
                rowBinding.textViewBankAcc.text =
                    "Paytm"//arrayList[position].beneficiaryNickName
                rowBinding.textViewAccountNumber.text =
                    arrayList[position].beneficiaryPhone//arrayList[position].beneficiaryPhone
                rowBinding.textViewAccountName.text = arrayList[position].beneficiaryName
            }
        }
    }

    inner class ViewHolder(binding: ItemBeneficiaryWithdrawBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}