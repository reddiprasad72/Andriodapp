package com.bluboy.android.presentation.wallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bluboy.android.R
import com.bluboy.android.data.models.Coupon
import com.bluboy.android.data.models.User
import com.bluboy.android.databinding.FragmentAddMoneyBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddMoneyFragment : BaseFragment() {
    private lateinit var binding: FragmentAddMoneyBinding
    private val homeViewModel: HomeViewModel by viewModel()
    private var userData: User? = null
    private lateinit var mContext: Context
    private var promocode: String = ""
    private var coupon: Coupon? = null
    private var applyPromoOne = false
    lateinit var params2: ConstraintLayout.LayoutParams

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        setToolBar()
        init()
    }

    fun setToolBar() {
        binding.toolbar.txtHeader.text = getString(R.string.label_add_cash)
        /*binding.toolbar.imageViewBack.gone()*/
        binding.toolbar.imageViewBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }


    private fun init() {
        attachObserver()


        params2 = binding.buttonProceed.layoutParams as ConstraintLayout.LayoutParams

        binding.buttonPromoCode.setOnClickListener {
            if (binding.editTextAmount.length() != 0) {
                var i = Intent(mContext, PromocodeActivity::class.java)
                startActivityForResult(i, AppConstant.GET_MY_OFFER_DATA)
            } else {
                requireActivity().toastError("Please enter amount")
            }
        }

        binding.imageViewClose.setOnClickListener {
            binding.groupPromocode.gone()
            binding.promoCode.visible()
            binding.buttonPromoCode.visible()
            promocode = ""

            params2.topToBottom = ConstraintLayout.LayoutParams.UNSET
            params2.topToBottom = binding.buttonPromoCode.id
        }


        binding.textViewApply.setOnClickListener {
            homeViewModel.getApplyPromocode(
                binding.editTextAmount.text.toString(),
                coupon?.couponCode.toString()
            )
        }

        binding.editTextAmount.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(PrefKeys.getVariableData()?.MAX_ADD_AMOUNT_DIGIT?.toInt()!!))

        binding.editTextAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.groupPromocode.gone()
                binding.promoCode.visible()
                binding.buttonPromoCode.visible()
                promocode = ""

                params2.topToBottom = ConstraintLayout.LayoutParams.UNSET
                params2.topToBottom = binding.buttonPromoCode.id
            }
        })

        binding.buttonProceed.setSafeOnClickListener {
            if (binding.editTextAmount.text?.length == 0) {
                requireActivity().toastError("Please enter amount")
            } else if (binding.editTextAmount.text.toString()
                    .toFloat() < PrefKeys.getVariableData()?.mINADDAMOUNT?.toFloat()!!
            ) {
                requireActivity().toastError("Minimum Deposit Amount should be greater than or equal to ${PrefKeys.getVariableData()?.mINADDAMOUNT}")
            } else if (binding.editTextAmount.text?.length!! > PrefKeys.getVariableData()?.MAX_ADD_AMOUNT_DIGIT?.toInt()!!) {
                requireActivity().toastError("Maximum Deposit Amount should be ${PrefKeys.getVariableData()?.MAX_ADD_AMOUNT_DIGIT} digit")
            } else {
                startActivityCustom(
                    IntentHelper.getPAymetOptionScreenIntent(
                        mContext,
                        binding.editTextAmount.text.toString(),
                        promocode
                    )
                )
            }
        }

        binding.quick1.setSafeOnClickListener {
            binding.editTextAmount.setText("100")
            binding.editTextAmount.setSelection(binding.editTextAmount.text.toString().length)
            requireActivity().hideKeyboardFrom(it)
        }
        binding.quick2.setSafeOnClickListener {
            binding.editTextAmount.setText("200")
            binding.editTextAmount.setSelection(binding.editTextAmount.text.toString().length)
            requireActivity().hideKeyboardFrom(it)
        }
        binding.quick3.setSafeOnClickListener {
            binding.editTextAmount.setText("1000")
            binding.editTextAmount.setSelection(binding.editTextAmount.text.toString().length)
            requireActivity().hideKeyboardFrom(it)
        }
        binding.quick4.setSafeOnClickListener {
            binding.editTextAmount.setText("2000")
            binding.editTextAmount.setSelection(binding.editTextAmount.text.toString().length)
            requireActivity().hideKeyboardFrom(it)
        }
    }

    private fun attachObserver() {
        // Apply Promocode
        homeViewModel.applyPromocodeObserver.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                if (applyPromoOne) {
                    applyPromoOne = false
                    hideProgress()
                    if (it.status == 1) {
                        requireActivity().toastSuccess(it.message!!)
                        if (coupon != null) {
                            binding.groupPromocode.visible()
                            binding.textViewApply.visible()
                            binding.promoCode.gone()
                            binding.buttonPromoCode.gone()
                            binding.textViewAddPromocode.text = coupon?.couponCode
                            promocode = coupon?.couponCode.toString()
                            binding.textViewAddPromocode.text = promocode

                            params2.topToBottom = ConstraintLayout.LayoutParams.UNSET
                            params2.topToBottom = binding.tag.id

                        }
                    } else {
                        requireActivity().toastError(it.message!!)
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.GET_MY_OFFER_DATA) {
            if (resultCode == Activity.RESULT_OK) {
                coupon = data?.getParcelableExtra<Coupon>("result")!!
                if (coupon != null && coupon?.couponCode != "" && binding.editTextAmount != null
                    && binding.editTextAmount.text?.isNotEmpty()!!
                ) {
                    applyPromoOne = true
                    homeViewModel.getApplyPromocode(
                        binding.editTextAmount.text.toString(),
                        coupon?.couponCode.toString()
                    )
                }
            }
        }
    }
}