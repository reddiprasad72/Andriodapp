package com.bluboy.android.presentation.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bluboy.android.R
import com.bluboy.android.data.models.IpResponse
import com.bluboy.android.databinding.ActivityContactUsBinding
import com.bluboy.android.domain.network.ServiceApiCall.WebApiCall
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContactUsActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private lateinit var binding: ActivityContactUsBinding
    var ip = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        init()
    }

    private fun init() {
        attachObserver()
        setToolBar()
        if (PrefKeys.getUserCommon() != null && PrefKeys.getUserCommon()?.email != null)
            binding.editTextEmail.setText(PrefKeys.getUserCommon()?.email.toString())
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.ipify.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        val webApiCall = retrofit.create(WebApiCall::class.java)
        val call: Call<IpResponse> = webApiCall.getIPAddress("json")
        call.enqueue(object : Callback<IpResponse> {
            override fun onResponse(call: Call<IpResponse>, response: Response<IpResponse>) {
                ip = response.body()?.ip.toString()
            }

            override fun onFailure(call: Call<IpResponse>, t: Throwable) {
                Log.i("IpAddress >>", t.message.toString())
                val wifiMan = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                val wifiInf: WifiInfo = wifiMan.connectionInfo
                val ipAddress: Int = wifiInf.ipAddress
                ip = String.format(
                    "%d.%d.%d.%d",
                    ipAddress and 0xff,
                    ipAddress shr 8 and 0xff,
                    ipAddress shr 16 and 0xff,
                    ipAddress shr 24 and 0xff
                )

            }
        })

        binding.buttonSubmit.setSafeOnClickListener {
            if (isValid()) {
                hideKeyboardFrom(it)
                showProgress()
                homeViewModel.getContactUs(
                    binding.editTextEmail.text.toString().trim(),
                    binding.editTextDescription.text.toString().trim(),
                    ip
                )
            }
        }

        binding.groupWhatsapp.setAllOnClickListener() {
            val url =
                "https://api.whatsapp.com/send?phone=${PrefKeys.getVariableData()?.whatsAppContact}"
            when {
                isAppInstalled("com.whatsapp.w4b") -> {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }
                isAppInstalled("com.whatsapp") -> {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }
                else -> {
                    toastError("Whatsapp app not installed in your phone")
                }
            }
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        val pm1 = this.packageManager
        var appInstalled = try {
            pm1.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return appInstalled
    }

    private fun isValid(): Boolean {
        if (StringUtils.isEmpty(binding.editTextEmail.text.toString())) {
            toastError(getString(R.string.enter_email))
            return false
        } else if (!StringUtils.isValidEmail(binding.editTextEmail.text.toString())) {
            toastError(getString(R.string.validation_email_valid))
            return false
        } else if (StringUtils.isEmpty(binding.editTextDescription.text.toString())) {
            toastError(getString(R.string.validation_description))
            return false
        }
        return true
    }

    fun setToolBar() {
        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }
        binding.tool.txtHeader.text = getString(R.string.label_customer_support)
    }

    private fun attachObserver() {
        homeViewModel.contactUsRSLiveData.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {
                toastSuccess(it.message.toString())
                finish()
            } else {
                toastError(it.message.toString())
            }
        })
    }
}
