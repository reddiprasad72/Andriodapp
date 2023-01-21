package com.bluboy.android.presentation.transaction

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.TransactionHistory
import com.bluboy.android.databinding.BonusTransactionBinding
import com.bluboy.android.databinding.ItemTransactionBinding
import com.bluboy.android.presentation.utility.DateTimeHelper
import com.bluboy.android.presentation.utility.gone
import com.bluboy.android.presentation.utility.loadImage
import com.bluboy.android.presentation.utility.visible

class BonusAdapter(
    var context: Context,
    var arrayTransaction: ArrayList<TransactionHistory>,
    val callback: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: BonusTransactionBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            BonusTransactionBinding.inflate(
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

        var dateTimeFirst =
            DateTimeHelper.convertLongToTime(arrayTransaction[position].createdAt.toLong())
        var first = DateTimeHelper.convertDate(
            dateTimeFirst,
            input = "yyyy-MM-dd'T'hh:mm:ss",
            output = "dd-MM-yyyy"
        )

        rowBinding.textViewDt.text = first.uppercase()

        rowBinding.textViewAmount.text =
            if (arrayTransaction[position].historyType == "C")
                "+ ₹ ${arrayTransaction[position].amount}"
            else
                "- ₹ ${arrayTransaction[position].amount}"
    }

    inner class ViewHolder(binding: BonusTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}