package com.bluboy.android.presentation.wallet.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.BeneficiaryData
import com.bluboy.android.databinding.ItemBeneficiaryBinding
import com.bluboy.android.presentation.utility.loadImage

class MyBeneficiaryAdapter(
    val arrayList: ArrayList<BeneficiaryData>,
    val callback: (Int, View, BeneficiaryData) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemBeneficiaryBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        rowBinding =
            ItemBeneficiaryBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        rowBinding.constraintBank.setOnClickListener {
            callback.invoke(position, rowBinding.constraintBank, arrayList[position])
        }

        rowBinding.imageViewDelete.setOnClickListener {
            callback.invoke(position, rowBinding.imageViewDelete, arrayList[position])
        }

        when (arrayList[position].transferMode) {
            "upi" -> {
                loadImage(rowBinding.imageViewBank, R.drawable.bheem_upi)
                //                rowBinding.textViewNickName.text = "UPI Transfer"
                rowBinding.textViewBankAcc.text =
                    "UPI"//arrayList[position].beneficiaryNickName
                rowBinding.textViewAccountNumber.text = arrayList[position].beneficiaryVpa
                rowBinding.textViewAccountName.text = arrayList[position].beneficiaryName
                //                rowBinding.tvData.text = "johnyd@upi"
            }

            "banktransfer" -> {
                loadImage(rowBinding.imageViewBank, R.drawable.bank_icon)
                //                rowBinding.textViewNickName.text = "Bank Transfer"
                //                rowBinding.tvData.text = "SBI"
                rowBinding.textViewBankAcc.text =
                    "Bank Acc."//arrayList[position].beneficiaryNickName
                rowBinding.textViewAccountNumber.text =
                    arrayList[position].beneficiaryAccountNumber
                rowBinding.textViewAccountName.text = arrayList[position].beneficiaryName
            }

            "paytm" -> {
                loadImage(rowBinding.imageViewBank, R.drawable.paytm)
                //                rowBinding.textViewNickName.text = "Paytm Wallet"
                //                rowBinding.tvData.text = "987654321@paytm"
                rowBinding.textViewBankAcc.text =
                    "Paytm"//arrayList[position].beneficiaryNickName
                rowBinding.textViewAccountNumber.text = arrayList[position].beneficiaryPhone
                rowBinding.textViewAccountName.text = arrayList[position].beneficiaryName
            }
        }
    }

    inner class ViewHolder(binding: ItemBeneficiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}