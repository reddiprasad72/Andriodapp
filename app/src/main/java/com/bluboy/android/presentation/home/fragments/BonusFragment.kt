package com.bluboy.android.presentation.home.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.R
import com.bluboy.android.data.models.TransactionHistory
import com.bluboy.android.databinding.FragmentBonusBinding
import com.bluboy.android.databinding.FragmentWalletBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.home.HomeActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.transaction.BonusAdapter
import com.bluboy.android.presentation.transaction.TransactionAdapter
import com.bluboy.android.presentation.utility.PrefKeys
import com.bluboy.android.presentation.utility.gone
import com.bluboy.android.presentation.utility.visible
import com.bumptech.glide.Glide
import com.cashfree.pg.ui.CFNonWebBaseActivity.onBackPressed
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class BonusFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by sharedViewModel()
    private lateinit var mContext: Context

    private lateinit var binding: FragmentBonusBinding
    private var page = 1
    private var arrayTransaction = arrayListOf<TransactionHistory>()
    private var mAdapter: BonusAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBonusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context

        homeViewModel.getProfile()
        homeViewModel.walletBonusTransaction(page)

        setupToolbar()
        mAdapter = BonusAdapter(
            mContext, arrayTransaction
        ) {
        }

        var mLayoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTransaction.layoutManager = mLayoutManager
        binding.recyclerViewTransaction.adapter = mAdapter

        attachObserver()
    }

    private fun setupToolbar() {
        binding.toolbar.txtHeader.text = "Bonus"
        /*binding.toolbar.imageViewBack.gone()*/
        binding.toolbar.imageViewBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        if (HomeActivity.bonusAmt != "") {
            binding.textViewBonusBalance.text =
                getString(R.string.x_price_rupees, HomeActivity.bonusAmt)

            binding.shimmer1.gone()
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun attachObserver() {

        homeViewModel.profileObserver.observe(this, Observer {
            if (it.status == 1) {
                it.user?.let { it1 -> PrefKeys.setUser(it1) }
                binding.textViewBonusBalance.text =
                    getString(R.string.x_price_rupees, it.user?.userBonusBalance.toString())

                binding.shimmer1.gone()
            }
        })

        homeViewModel.walletHistoryBonusObserver.observe(this, Observer {

            arrayTransaction.clear()
            mAdapter?.notifyDataSetChanged()

            if (it.status == 1) {
                binding.recyclerViewTransaction.visible()
                it.data?.transactionHistory?.let { it1 -> arrayTransaction.addAll(it1) }
                mAdapter?.notifyDataSetChanged()
            } else {
                binding.recyclerViewTransaction.gone()
            }
        })
    }
}