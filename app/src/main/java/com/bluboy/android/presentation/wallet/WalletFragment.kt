package com.bluboy.android.presentation.wallet

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bluboy.android.R
import com.bluboy.android.data.models.CheckWithdrawalTransactionData
import com.bluboy.android.data.models.User
import com.bluboy.android.databinding.FragmentWalletBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.home.HomeActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class WalletFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by viewModel()

    private var userData: User? = null
    private var checkWithdrawTransaction: CheckWithdrawalTransactionData? = null
    private lateinit var binding: FragmentWalletBinding
    private lateinit var mContext: Context
    var kycStatus = false
    var userDepositBalance = "0"
    var userWinningBalance = "0"
    var userTotalBalance = "0"
    var userCashBonus = "0"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        setToolBar()
        init()
    }


    fun setToolBar() {
        binding.toolbar.txtHeader.text = mContext.getString(R.string.label_my_wallet)
        /*binding.toolbar.imageViewBack.gone()*/
        binding.toolbar.imageViewBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.textViewTransactionHistory.setOnClickListener {
            startActivityCustom(
                IntentHelper.getTransactionScreenIntent
                    (mContext)
            )
        }
    }

    private fun init() {
        attachObserver()
        /*BounceView.addAnimTo(binding.buttonAddCash)
        BounceView.addAnimTo(binding.buttonWithdraw)*/

        binding.info1.setOnClickListener {
            binding.groupTool2.gone()
            binding.groupTool3.gone()
            binding.groupTool1.visible()

            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                binding.groupTool1.gone()
            }
        }

        binding.info2.setOnClickListener {
            binding.groupTool1.gone()
            binding.groupTool3.gone()
            binding.groupTool2.visible()

            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                binding.groupTool2.gone()
            }
        }

        binding.info3.setOnClickListener {
            binding.groupTool1.gone()
            binding.groupTool2.gone()
            binding.groupTool3.visible()

            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                binding.groupTool3.gone()
            }
        }

        binding.buttonAddCash.setOnClickListener {
            (activity as HomeActivity).launchBottomNavFragmentByIndex(2)
        }

        binding.buttonWithdraw.setSafeOnClickListener {
            if (!kycStatus) {
                var message = ""

                if (userData?.kycStatus == "NOT_UPLOADED") {
                    message = "Please Complete KYC Process"
                } else if (userData?.kycStatus == "IN_REVIEW") {
                    message = "Your KYC is in review"
                } else if (userData?.kycStatus == "REJECTED") {
                    message = "Your KYC is Rejected. Please apply again"
                }

                CommonAppDialogFragment.showDialog(childFragmentManager,
                    getString(R.string.app_name), message,
                    getString(R.string.ok), "",
                    {
                        if ((userData?.kycStatus == "NOT_UPLOADED") || (userData?.kycStatus == "REJECTED")) {
                            startActivityCustom(
                                IntentHelper.getKYCIntent(
                                    requireContext()
                                )
                            )
                        }
                    }, {}, {})

                return@setSafeOnClickListener
            }

//            if(kycStatus.text == "Approved") {  // check kyc
            if (userWinningBalance.toFloat() >= PrefKeys.getVariableData()?.mINWITHDRAWAMOUNT?.toFloat()!!) {
                if (checkWithdrawTransaction != null
                    && checkWithdrawTransaction?.remainTotalWithdrawAmount != null
                    && checkWithdrawTransaction?.remainTotalWithdrawAmount!! > 0
                ) {
                    if (checkWithdrawTransaction != null
                        && checkWithdrawTransaction?.remainTotalWithdrawTransactions != null
                        && checkWithdrawTransaction?.remainTotalWithdrawTransactions!! > 0
                    ) {
                        startActivityCustom(
                            IntentHelper.getWithdrawScreenIntent(
                                mContext, checkWithdrawTransaction!!
                            )
                        )
                    } else {
                        mContext.toastError("Your daily transaction limit is ${checkWithdrawTransaction?.allowedTotalWithdrawTransactions}")
                    }
                } else {
                    mContext.toastError("Your daily withdrawal limit is ₹${checkWithdrawTransaction?.allowedTotalWithdrawAmounts}")
                }
            } else {
                mContext.toastError("Amount must be ${PrefKeys.getVariableData()?.mINWITHDRAWAMOUNT} or more than ${PrefKeys.getVariableData()?.mINWITHDRAWAMOUNT}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getProfile()
        homeViewModel.getSettingVar()
        //showProgress()
        homeViewModel.getCheckWithdrawalTransaction()
    }

    private fun attachObserver() {
        homeViewModel.profileObserver.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it.status == 1) {
                it.user?.let { it1 -> PrefKeys.setUser(it1) }
                userData = it.user
                setUserData()
            } else {

            }
        })

        homeViewModel.varSettingRS.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it.status == 1) {
                PrefKeys.setVariableData(it.data!!)
            }
        })

        homeViewModel.checkWithdrawalTransactionObserver.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                hideProgress()
                if (it.status == 1) {
                    checkWithdrawTransaction = it.data
                }
            })
    }

    private fun setUserData() {

        userData?.userDepositBalance?.let {
            userDepositBalance = it
        }

        userData?.userWinningAmountBalance?.let {
            userWinningBalance = it
        }

        userData?.totalWalletAmount?.let {
            userTotalBalance = it
        }

        if (userData?.kycStatus == "APPROVED") {
            kycStatus = true
        }

        userData?.userBonusBalance?.let {
            userCashBonus = it
        }
        binding.textViewTotalBalance.text =
            mContext.getString(R.string.x_price_rupees, userTotalBalance)
        binding.textViewDepositeAmount.text =
            mContext.getString(R.string.x_price_rupees, userDepositBalance)
        binding.textViewWinningAmount.text =
            mContext.getString(R.string.x_price_rupees, userWinningBalance)
        binding.textViewBonusAmount.text =
            mContext.getString(R.string.x_price_rupees, userCashBonus)

        binding.shimmer1.gone()
        binding.shimmer2.gone()
        binding.shimmer3.gone()
        binding.shimmer4.gone()

//        kycStatus.visible()
//        if (userData?.kycAadharStatus == "A" || userData?.kycPanStatus == "A"){
//            kycStatus.text = "Approved"
//            buttonKYC.gone()
//        }else if (userData?.kycAadharStatus == "R" || userData?.kycPanStatus == "R"){
//            kycStatus.text = "Rejected"
//            buttonKYC.visible()
//        }else{
//            kycStatus.text = "Pending"
//            buttonKYC.visible()
//        }
    }

    private fun showToolTipPopup(v: View, str: String) {
        var view = v
        val popupView = LayoutInflater.from(view.rootView.context)
            .inflate(R.layout.custom_goal_tooltip_popup_view, null)
        var message = popupView.findViewById<TextView>(R.id.textviewMessage)
        message.text = str
        var popupWindow = PopupWindow(
            popupView, WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, true
        )

        popupWindow.animationStyle = R.style.ToolTipPopupAnimation
        val values = IntArray(2)
        view.getLocationInWindow(values)
        val positionOfIcon = values[1]
        //Get the height of 2/3rd of the height of the screen
        val displayMetrics = resources.displayMetrics
//                val height = displayMetrics.heightPixels * 2 / 3
        val height = displayMetrics.heightPixels * 0.5

        popupWindow.showAsDropDown(view, -dpToPx(128), -dpToPx(128))
        //popupWindow.showAsDropDown(view)
    }

    /*private fun showToolTipPopup(view: View, str: String) {
        val popupView = LayoutInflater.from(view.rootView.context)
            .inflate(R.layout.custom_goal_tooltip_popup_view, null)
        var linearLayout = popupView.findViewById<ConstraintLayout>(R.id.linearLayout)
        var imageviewBottom = popupView.findViewById<ImageView>(R.id.imageviewBottom)
        var message = popupView.findViewById<TextView>(R.id.textviewMessage)
        message.text = str
        var popupWindow = PopupWindow(
            popupView, WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, true
        )

        popupWindow.animationStyle = R.style.ToolTipPopupAnimation
        val values = IntArray(2)
        view.getLocationInWindow(values)
        val positionOfIcon = values[1]
        //Get the height of 2/3rd of the height of the screen
        val displayMetrics = resources.displayMetrics
//                val height = displayMetrics.heightPixels * 2 / 3
        val height = displayMetrics.heightPixels * 0.5

        popupWindow.showAsDropDown(view, -dpToPx(108), -dpToPx(108))
        imageviewBottom.visibility = View.VISIBLE
    }*/

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

}