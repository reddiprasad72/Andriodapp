package com.bluboy.android.presentation.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.data.models.TransactionHistory
import com.bluboy.android.data.models.WalletHistoryRs
import com.bluboy.android.databinding.FragmentTransactionAllBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.gone
import com.bluboy.android.presentation.utility.visible
import org.koin.android.viewmodel.ext.android.sharedViewModel

class TransactionAllFragment : BaseFragment() {

    private var mAdapter: TransactionAdapter? = null
    private var arrayTransaction = ArrayList<TransactionHistory>()
    private var walletType = "A"
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private var walletHistoryRs: WalletHistoryRs? = null
    private var isInProgress = false
    private var page = 1
    private lateinit var binding: FragmentTransactionAllBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionAllBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        arrayTransaction.clear()
        mAdapter?.notifyDataSetChanged()
        page = 1
//        showProgress()
//        Handler(Looper.getMainLooper()).postDelayed({
        homeViewModel.walletAllTransaction(page)
//        },100)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun observer() {
        homeViewModel.walletHistoryAllObserver.observe(this, Observer {
            isInProgress = false
//                hideProgress()
            binding.progressBottom.gone()
            binding.mShimmerTransaction.gone()
//            if (page == 1){
//                arrayTransaction.clear()
//            }

            if (it.status == 1) {
                walletHistoryRs = it!!
                binding.nestedScrollViewTransaction.visible()
                binding.llEmpty.gone()
                it.data?.transactionHistory?.let { it1 -> arrayTransaction.addAll(it1) }
                mAdapter?.notifyDataSetChanged()
            } else {
                binding.nestedScrollViewTransaction.gone()
                binding.llEmpty.visible()
            }
        })
    }

    private fun setAdapter() {
        mAdapter = TransactionAdapter(requireContext(), arrayTransaction, {

        })

        var mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTransaction.layoutManager = mLayoutManager
        binding.recyclerViewTransaction.adapter = mAdapter
        binding.nestedScrollViewTransaction.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // end of the scroll view
                if (walletHistoryRs?.data?.isHaveMoreRecords.equals("Yes") && !isInProgress) {
                    page++
                    binding.progressBottom.visible()
                    getWalletsAll(walletType, true)
//                    homeViewModel.walletTransaction(walletType, page)
                }
            }
        })
    }

    fun getWalletsAll(wallype: String, isScroll: Boolean? = false) {
        if (isInProgress) {
            return
        }
        isInProgress = true
        this.walletType = "A"
        if (isScroll == false) {
            page = 1
            arrayTransaction.clear()
            mAdapter?.notifyDataSetChanged()
        }

        if (page == 1) {
            binding.mShimmerTransaction.visible()
            binding.nestedScrollViewTransaction.gone()
            binding.llEmpty.gone()
        }
//            showProgress()
//            Handler(Looper.getMainLooper()).postDelayed({
        homeViewModel.walletAllTransaction(page)
//            },200)

    }
}