package com.bluboy.android.presentation.transaction

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.TransactionHistory
import com.bluboy.android.databinding.ItemTransactionBinding
import com.bluboy.android.presentation.utility.DateTimeHelper
import com.bluboy.android.presentation.utility.gone
import com.bluboy.android.presentation.utility.loadImage
import com.bluboy.android.presentation.utility.visible

class TransactionAdapter(
    var context: Context,
    var arrayTransaction: ArrayList<TransactionHistory>,
    val callback: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemTransactionBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemTransactionBinding.inflate(
                LayoutInflater.from(container.context),
                container, false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayTransaction.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        rowBinding.title.text = arrayTransaction[position].histroyMessage
        rowBinding.orderId.text = "Order Id: ${arrayTransaction[position].orderId}"

        if (arrayTransaction[position].orderId.isNotBlank()) {
            rowBinding.orderId.visible()
        }
//        rowBinding.textViewStatus.text = arrayTransaction[position].transactionType

        var dateTimeFirst =
            DateTimeHelper.convertLongToTime(arrayTransaction[position].createdAt.toLong())
        var first = DateTimeHelper.convertDate(
            dateTimeFirst,
            input = "yyyy-MM-dd'T'hh:mm:ss",
            output = "dd-MM-yyyy hh:mm a"
        )

        rowBinding.textViewDateTime.text = first.uppercase()
        rowBinding.textViewAmount.text =
            if (arrayTransaction[position].historyType == "C") "+ ₹ ${arrayTransaction[position].amount}"
            else
                "- ₹ ${arrayTransaction[position].amount}"

//        rowBinding.textViewAmount2.text = rowBinding.textViewAmount.text

        /*if (arrayTransaction[position].historyType == "C") {
            rowBinding.textViewAmount.visible()
            rowBinding.textViewAmount2.gone()
        } else {
            rowBinding.textViewAmount2.visible()
            rowBinding.textViewAmount.gone()
        }*/

        if (arrayTransaction[position].historyType == "C") {

//            rowBinding.textViewAmount.setTextAppearance(context, R.style.btnStyleGreen)
//             rowBinding.textViewStatus.background = ContextCompat.getDrawable(context, R.drawable.ic_bg_green)
            loadImage(rowBinding.textViewStatus, R.drawable.gift_icon)
        } else {
//            rowBinding.textViewAmount.setTextAppearance(context, R.style.btnStyleYellow)
//              holder.itemView.textViewStatus.background = ContextCompat.getDrawable(context, R.drawable.ic_bg_red)
            loadImage(rowBinding.textViewStatus, R.drawable.notification_cup)
        }

        if (arrayTransaction[position].transactionType == "Withdraw Refund") {   //Wallet Withdraw
            if (arrayTransaction[position].withdrawReason.toString() != "") {
                rowBinding.textViewWithdrawFailedReason.visible()
                rowBinding.textViewWithdrawFailedReason.text =
                    arrayTransaction[position].withdrawReason.toString()
            }
        } else {
            rowBinding.textViewWithdrawFailedReason.gone()
        }
    }

    inner class ViewHolder(binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}