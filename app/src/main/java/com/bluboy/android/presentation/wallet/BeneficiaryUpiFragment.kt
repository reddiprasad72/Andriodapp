package com.bluboy.android.presentation.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bluboy.android.R
import com.bluboy.android.databinding.FragmentUpiBeneficiaryBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class BeneficiaryUpiFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by sharedViewModel()
    private lateinit var binding: FragmentUpiBeneficiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpiBeneficiaryBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnAdd.setSafeOnClickListener {
            if (isValidUpi()) {
                showProgress()
                homeViewModel.getBeneficiaryAdd(
                    binding.etAccountantName.text.toString(),
                    binding.etAccountantNickName.text.toString(),
                    PrefKeys.getUserCommon()?.email.toString(),
                    PrefKeys.getUserCommon()?.phone.toString(),
                    "",
                    "",
                    "",
                    binding.etAddress.text.toString(),
                    binding.etCity.text.toString(),
                    binding.etState.text.toString(),
                    binding.etPincode.text.toString(),
                    binding.etUpiID.text.toString(),
                    "upi"
                )
            }
        }
    }

    private fun observer() {
        homeViewModel.beneficiaryAddObserver.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {
                activity?.toastSuccess(it?.message.toString())
                activity?.finish()
            } else {
                activity?.toastError(it?.message.toString())
            }
        })
    }

    private fun isValidUpi(): Boolean {
        if (!StringUtils.isValid(binding.etAccountantNickName.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_nick_name))
            return false
        } else if (!StringUtils.isValid(binding.etAccountantName.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_accountant_name))
            return false
        } else if (!StringUtils.isValid(binding.etUpiID.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_upi))
            return false
        } else if (!StringUtils.isValid(binding.etAddress.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_address))
            return false
        } else if (!StringUtils.isValid(binding.etCity.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_city))
            return false
        } else if (!StringUtils.isValid(binding.etState.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_state))
            return false
        } else if (!StringUtils.isValid(binding.etPincode.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_pincode))
            return false
        } else if (binding.etPincode.text.toString().length != 6) {
            context?.toastError(getString(R.string.validation_enter_valid_pincode))
            return false
        }
        return true
    }
}