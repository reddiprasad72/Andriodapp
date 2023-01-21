package com.bluboy.android.presentation.settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.FAQDetail
import com.bluboy.android.databinding.ItemFaqBinding

class FaqAdapter(
    val mContext: Context,
    val arrayList: ArrayList<FAQDetail>,
    val callback: (FAQDetail, Int, Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemFaqBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        rowBinding =
            ItemFaqBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        rowBinding.textViewTitleQue.text = arrayList[position].faqQuestion
        rowBinding.textViewTitleAns.text = arrayList[position].faqAnswer.parseAsHtml()

        if (arrayList[position].isExpanded) {

            rowBinding.textViewTitleQue.setTextColor(mContext.resources.getColor(R.color.colorSecondaryText))

            rowBinding.rowBackTop.isVisible = true
            rowBinding.textViewTitleQue.isVisible = true
            rowBinding.textViewTitleAns.isVisible = true
            rowBinding.divider.isVisible = true
            rowBinding.imageViewOpen.isInvisible = true
            rowBinding.imageViewClose.isVisible = true

        } else {
            rowBinding.textViewTitleQue.setTextColor(mContext.resources.getColor(R.color.colorText))

            rowBinding.rowBackTop.isVisible = false
            rowBinding.textViewTitleQue.isVisible = true
            rowBinding.textViewTitleAns.isVisible = false
            rowBinding.divider.isInvisible = true
            rowBinding.imageViewOpen.isVisible = true
            rowBinding.imageViewClose.isVisible = false
        }

        rowBinding.imageViewOpen.setOnClickListener {
            callback.invoke(arrayList[position], position, true)
        }
        rowBinding.imageViewClose.setOnClickListener {
            callback.invoke(arrayList[position], position, false)
        }
    }

    inner class ViewHolder(binding: ItemFaqBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
