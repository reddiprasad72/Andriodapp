package com.bluboy.android.presentation.wallet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.payment.cashfreeunityplugin.CashFreeUtility
import com.payment.cashfreeunityplugin.PaymentInitListener
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.paytm.pgsdk.TransactionManager
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivityPaymentOptionsBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel

class PaymentOptionsActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private var hyptoOrderId = ""
    private var cfOrderId = ""
    private var paytmOrderId = ""
    private var mAmount = ""
    private var paymentTypeUpi = false
    private var jsonPaymentResponse = "";
    private var txnTokenString: String? = ""
    private var midString = ""
    private var promocode: String = ""
    private lateinit var binding: ActivityPaymentOptionsBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getSettingVar()
    }

    fun setToolBar() {
        binding.toolbar.txtHeader.text = getString(R.string.label_payment_options)
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

    private fun init() {

        attachObserver()
        midString = PrefKeys.getVariableData()?.paytmMID.toString()
        mAmount = intent.getStringExtra(AppConstant.TRANSFER_AMOUNT).toString()
        promocode = intent.getStringExtra(AppConstant.TRANSFER_PROMOCODE).toString()
        binding.textViewAmount.text = getString(R.string.x_price_rupees, mAmount)

        binding.groupPaytm.setAllOnClickListener {
            paytmOrderId =
                "paytm_" + System.currentTimeMillis() + "-" + PrefKeys.getUserCommon()?.userId

            if (PrefKeys.getVariableData()?.paytmMerchantKey.isNullOrBlank()) {
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), getString(R.string.coming_soon),
                    getString(R.string.ok), "",
                    {},
                    {},
                    {})
            } else {
                getPaytmToken()
            }
        }

        binding.groupUPI.setAllOnClickListener {
            paymentTypeUpi = true
            /*cfOrderId =
                "cf_" + System.currentTimeMillis() + "-" + PrefKeys.getUserCommon()?.userId*/

            if (PrefKeys.getVariableData()?.cASHFREESECRETKEY.isNullOrBlank()) {
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), getString(R.string.coming_soon),
                    getString(R.string.ok), "",
                    {},
                    {},
                    {})
            } else {
                callGenerateToken()
            }
        }

        binding.groupHypto.setAllOnClickListener {
            hyptoOrderId =
                "hypto_" + System.currentTimeMillis() + "-" + PrefKeys.getUserCommon()?.userId

            if (PrefKeys.getVariableData()?.cASHFREESECRETKEY.isNullOrBlank()) {
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), getString(R.string.coming_soon),
                    getString(R.string.ok), "",
                    {},
                    {},
                    {})
            } else {
                callHyptoGenerateToken()
            }
        }

        binding.groupCard.setAllOnClickListener {
            paymentTypeUpi = false
            /*cfOrderId =
                "cf_" + System.currentTimeMillis() + "-" + PrefKeys.getUserCommon()?.userId*/

            if (PrefKeys.getVariableData()?.cASHFREESECRETKEY.isNullOrBlank()) {
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), getString(R.string.coming_soon),
                    getString(R.string.ok), "",
                    {},
                    {},
                    {})
            } else {
                callGenerateToken()
            }
        }
    }

    private fun getPaytmToken() {
        showProgress()
        homeViewModel.generatePaytmToken(paytmOrderId, mAmount, promocode)
    }

    private fun callGenerateToken() {
        showProgress()
        homeViewModel.generateToken(
            "", mAmount,
            "INR", promocode
        )
    }

    private fun callHyptoGenerateToken() {
        showProgress()
        homeViewModel.generateHyptoToken(hyptoOrderId, mAmount, "INR", promocode)
    }

    private fun addMoneyToWallet() {
        homeViewModel.addMoneyToWallet(mAmount, cfOrderId, jsonPaymentResponse, "")
    }

    fun startPaytmPayment(token: String, callBackUrl: String) {
        txnTokenString = token

        // for test mode use it
//         val host = "https://securegw-stage.paytm.in/"
        // for production mode use it
//        val host = "https://securegw.paytm.in/"
        val orderDetails =
            ("MID: " + midString + ", paytmOrderId: " + paytmOrderId + ", TxnToken: " + txnTokenString
                    + ", Amount: " + mAmount)
        Log.e("TAG", "order details " + orderDetails)
//        val callBackUrl = host + "theia/paytmCallback?ORDER_ID=" + paytmOrderId
        Log.e("TAG", " callback URL $callBackUrl")
        val paytmOrder = PaytmOrder(paytmOrderId, midString, txnTokenString, mAmount, callBackUrl)

        val transactionManager =
            TransactionManager(paytmOrder, object : PaytmPaymentTransactionCallback {
                override fun onTransactionResponse(bundle: Bundle?) {
                    Log.e("TAG", "Response (onTransactionResponse) : " + bundle.toString())
                    var finalResponse = ""

                    if (bundle != null && bundle.toString() != null)
                        finalResponse =
                            bundle.toString().subSequence(7, bundle.toString().length - 1)
                                .toString()

                    Log.e("TAG", "Response (String) : " + finalResponse)


                    if (bundle != null && bundle.toString().contains("RESPCODE=01")) {
//                paymentOne = true
                        homeViewModel.addMoneyToWallet(
                            mAmount,
                            paytmOrderId,
                            finalResponse, ""
                        )
                    } else if (bundle != null && !bundle.toString().contains("RESPCODE=01")) {
                        val map: MutableMap<String, String> = HashMap()
                        if (bundle != null) {
                            for (key in bundle.keySet()) {
                                map[key] =
                                    if (bundle[key] != null) bundle[key].toString() else "NULL"

                                Log.e(
                                    "TAG",
                                    key + " : " + if (bundle[key] != null) bundle[key] else "NULL"
                                )
                            }
                            CommonAppDialogFragment.showDialog(supportFragmentManager,
                                getString(R.string.app_name), map.get("RESPMSG").toString(),
                                getString(R.string.ok), "",
                                {
//                                    startActivityCustom(IntentHelper.getHomeScreenIntent(this@PaymentOptionsActivity))
                                },
                                {},
                                {})
                        }
                    }
                }

                override fun networkNotAvailable() {
                    Log.e("TAG", "network not available ")
                }

                override fun onErrorProceed(s: String) {
                    Log.e("TAG", " onErrorProcess $s")
                }

                override fun clientAuthenticationFailed(s: String) {
                    Log.e("TAG", "Clientauth $s")
                }

                override fun someUIErrorOccurred(s: String) {
                    Log.e("TAG", " UI error $s")
                }

                override fun onErrorLoadingWebPage(i: Int, s: String, s1: String) {
                    Log.e("TAG", " error loading web $s--$s1")
                }

                override fun onBackPressedCancelTransaction() {
                    Log.e("TAG", "backPress ")
                }

                override fun onTransactionCancel(s: String, bundle: Bundle) {
                    Log.e("TAG", " transaction cancel $s")
                }
            })
        if (PrefKeys.getVariableData()?.PAYTM_TEST_MODE == "1") {
            transactionManager.setShowPaymentUrl("https://securegw-stage.paytm.in/theia/api/v1/showPaymentPage") //staging
        } else {
            transactionManager.setShowPaymentUrl("https://securegw.paytm.in/theia/api/v1/showPaymentPage")
        }
//        transactionManager.setShowPaymentUrl("https://securegw-stage.paytm.in/theia/api/v1/showPaymentPage")
//        transactionManager.setAppInvokeEnabled(false)
        transactionManager.startTransaction(this, AppConstant.GET_PAYTM)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.GET_PAYTM && data != null && resultCode == Activity.RESULT_OK) {
            val bundle = data.extras
            if (data.getStringExtra("response")!!.contains("\"RESPCODE\":\"01\"")) {
//                paymentOne = true
                homeViewModel.addMoneyToWallet(
                    mAmount,
                    paytmOrderId,
                    data.getStringExtra("response").toString(), ""
                )
            } else if (!data.getStringExtra("response")!!.contains("\"RESPCODE\":\"01\"")) {
                val map: HashMap<*, *> =
                    Gson().fromJson(data.getStringExtra("response"), HashMap::class.java)
                Log.e("TAG", "MAP = " + map)
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), map.get("RESPMSG").toString(),
                    getString(R.string.ok), "",
                    {
                    },
                    {},
                    {
//                                finish()
                    })

            }
        }


        Log.e("TAG", " data " + data?.getStringExtra("nativeSdkForMerchantMessage"))
        Log.e("TAG", " data response - " + data?.getStringExtra("response"))
        /*
data response - {"BANKNAME":"WALLET","BANKTXNID":"1395841115",
"CHECKSUMHASH":"7jRCFIk6eRmrep+IhnmQrlrL43KSCSXrmMP5pH0hekXaaxjt3MEgd1N9mLtWyu4VwpWexHOILCTAhybOo5EVDmAEV33rg2VAS/p0PXdk\u003d",
"CURRENCY":"INR","GATEWAYNAME":"WALLET","MID":"EAcR4116","ORDERID":"100620202152",
"PAYMENTMODE":"PPI","RESPCODE":"01","RESPMSG":"Txn Success","STATUS":"TXN_SUCCESS",
"TXNAMOUNT":"2.00","TXNDATE":"2020-06-10 16:57:45.0","TXNID":"202006101112128001101683631290118"}
*/
    }

    private fun initPayment(isUpiIntent: Boolean, generatedToken: String) {
        CashFreeUtility.getInstance().initCashFreePayment(
            this@PaymentOptionsActivity,
            object : PaymentInitListener {

                override fun onPaymentInit(s: String?) {
                    Logger.i(s.toString())
                    val reader = JSONObject(s.toString())
                    var status = reader.getString("txStatus")
                    var txMsg = ""
                    if (status != "CANCELLED" && reader.getString("txMsg") != null
                        && reader.getString(
                            "txMsg"
                        ) != ""
                    ) {
                        txMsg = reader.getString("txMsg")
                    }

                    if (status == "SUCCESS") {
                        try {
                            jsonPaymentResponse = reader.toString()

                            CommonAppDialogFragment.showDialog(supportFragmentManager,
                                getString(R.string.app_name),
                                txMsg.split("::").last().ifBlank { status },
                                getString(R.string.ok),
                                "",
                                {
                                    startActivityCustom(
                                        IntentHelper.getHomeScreenIntent(
                                            context = this@PaymentOptionsActivity,
                                            wallet = true,
                                            isClearFlag = true
                                        )
                                    )

                                }, {}, {})//addMoneyToWallet()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        CommonAppDialogFragment.showDialog(supportFragmentManager,
                            getString(R.string.app_name),
                            txMsg.split("::").last().ifBlank { status }, getString(R.string.ok),
                            "", {}, {}, {})
                    }
                }
            },

            if (PrefKeys.getVariableData()?.CASHFREE_TEST_MODE == "1") "TEST" else "PROD",
            PrefKeys.getVariableData()?.cASHFREEAPPID.toString(),
            if (paymentTypeUpi) "true" else "false",
            "false",
            "false",
            "false",
            generatedToken,
            PrefKeys.getUserCommon()?.userName.toString(),
            PrefKeys.getUserCommon()?.phone.toString(),
            PrefKeys.getUserCommon()?.email.toString(),
            cfOrderId,
            "INR",
            mAmount,
            if (paymentTypeUpi) "" else "cc,dc",  //,nb,wallet
            PrefKeys.getVariableData()?.CASHFREE_NOTIFY_URL,
            "$cfOrderId"
        )
    }

    private fun attachObserver() {

        homeViewModel.varSettingRS.observe(this) {
            if (it.status == 1) {
                PrefKeys.setVariableData(it.data!!)
                it.data.apply {

                    if (this.paytm) binding.constraintPaytm.visible() else binding.constraintPaytm.gone()

                    if (this.hypto) {
                        binding.groupHypto.visible()
                    } else {
                        binding.groupHypto.gone()
                    }

                    if (this.cashFree) {
                        binding.constraintCard.visible()
                        binding.groupUPI.visible()
                    } else {
                        binding.constraintCard.gone()
                        binding.groupUPI.gone()
                    }
                    if (!this.hypto && !this.cashFree) {
                        binding.constraintHypto.gone()
                    }
                }
            }
        }

        homeViewModel.hyptoTokenObserver.observe(this) {
            hideProgress()
            if (it.status == 1) {
                hideProgress()
                val uri = Uri.Builder()
                    .scheme("upi")
                    .authority("pay")
                    .appendQueryParameter(
                        "pa",
                        "starwaresports@yesbank"
                    ) // virtual ID  kkkinfotech@yesbank /  starwaresports@yesbank
                    .appendQueryParameter("pn", "Star War Gaming") // name
                    .appendQueryParameter("mc", "") // optional your-merchant-code
                    .appendQueryParameter("tr", hyptoOrderId) // optional your-transaction-ref-id
                    .appendQueryParameter(
                        "tn",
                        "Pay to Bluboy \n $hyptoOrderId"
                    ) // any note about payment
                    .appendQueryParameter("am", mAmount) // amount
                    .appendQueryParameter("cu", "INR") // currency
                    .appendQueryParameter("url", "") // optional your-transaction-url
                    .build()
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.data =
                    uri  //Uri.parse("upi://pay?pa=starwaresports@yesbank&pn=Star War Gaming&am=$mAmount&tn=Pay to SikandarJi&tr=$orderId")
                val chooser = Intent.createChooser(intent, "Pay with...")
                if (null != chooser.resolveActivity(getPackageManager())) {
                    startActivityForResult(chooser, 123, null)
                } else {
                    toastError("No UPI app found, please install one to continue")
                }
            } else {
                if (StringUtils.isValid(it.message!!)) {
                    toastError(it.message!!)
                }
            }
        }

        homeViewModel.paymentTokenObserver.observe(this) {
            hideProgress()
            if (it.status == 1) {
                cfOrderId = it.data.orderId
                initPayment(false, it.data.token)
            } else {
                if (StringUtils.isValid(it.message!!)) {
                    toastError(it.message!!)
                }
            }
        }

        homeViewModel.paymentPaytmTokenObserver.observe(this) {
            hideProgress()
            if (it.status == 1) {
                startPaytmPayment(it.data.token, it.data.callBackUrl)
            } else {
                if (StringUtils.isValid(it.message!!)) {
                    toastError(it.message!!)
                }
            }
        }

        homeViewModel.addMoneyToWalletObserver.observe(
            this
        ) {
            if (it.status == 1) {
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), it.message.toString(),
                    getString(R.string.ok), "",
                    {
                        startActivityCustom(
                            IntentHelper.getHomeScreenIntent(
                                context = this@PaymentOptionsActivity,
                                wallet = true,
                                isClearFlag = true
                            )
                        )
                    },
                    {},
                    {
//                                finish()
                    })
                getUserProfile()

            } else {
                if (StringUtils.isValid(it.message!!)) {
//                            activity?.toastError(it.message!!)
                    CommonAppDialogFragment.showDialog(supportFragmentManager,
                        getString(R.string.app_name), it.message.toString(),
                        getString(R.string.ok), "",
                        {

                        },
                        {},
                        {
//                                finish()
                        })
                }
            }
        }
    }
}
