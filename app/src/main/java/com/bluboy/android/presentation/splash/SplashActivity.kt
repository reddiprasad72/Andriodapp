package com.bluboy.android.presentation.splash

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.*
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import com.pixplicity.easyprefs.library.Prefs
import com.bluboy.android.BuildConfig
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivitySplashBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.utility.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import com.scottyab.rootbeer.RootBeer
import com.bluboy.android.data.models.IpResponse
import com.bluboy.android.domain.network.ServiceApiCall.WebApiCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.zhengsr.zdwon_lib.bean.ZBean
import com.zhengsr.zdwon_lib.utils.ZCommontUitls
import com.zhengsr.zdwon_lib.ZDown
import com.zhengsr.zdwon_lib.callback.TaskListener
import com.zhengsr.zdwon_lib.callback.CheckListener
import java.io.File

import android.content.pm.PackageManager
import android.provider.Settings
import android.provider.Settings.Global.ADB_ENABLED
import android.util.Base64
import android.util.Log
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity : BaseActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun getBaseViewModel() = splashViewModel

    private lateinit var binding: ActivitySplashBinding

    private val APP_PACKAGE_DOT_COUNT = 2 // number of dots present in package name
    private val DUAL_APP_ID_999 = "999"
    private val DOT = '.'
    private var latestVersionCode = ""
    private var latestURL = ""
    private var ip = ""
    private var isRoot = "N"

    private var dialog: CusDialog? = null
    private var updateBtn: AppCompatButton? = null

    private var progressBarDownload: ProgressBar? = null
    private var textViewDownload: AppCompatTextView? = null
    private var mhashKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor =
                ContextCompat.getColor(context, R.color.colorTransparent) //Color.TRANSPARENT
        }

        checkAppCloning()

        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()

        checkAppCloning()*/
        binding.tvAppVersion.text = getAppVersion()
        attachObserver()
        // update live
        // 1) base url check
        // 2) version code check
        // 3) USB Debugging on check
    }

    private fun checkAppCloning() {
        val path = filesDir.path
        Log.i("Log", path)
        Log.i("Log", getDeviceUniqueId())

        if (BuildConfig.DEBUG) {
            try {
                val info: PackageInfo =
                    packageManager.getPackageInfo(
                        BuildConfig.APPLICATION_ID,
                        PackageManager.GET_SIGNATURES
                    )
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    var hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                    Log.e("MY KEY HASH >>:", hashKey)
                    mhashKey = hashKey
                    splashViewModel.checkAppHashKey(
                        hashKey.trim(),
                        BuildConfig.APPLICATION_ID
                    )
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                Log.e("MY KEY Package manager:", e.printStackTrace().toString())
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                Log.e("MY KEY HASHnot fopund:", e.printStackTrace().toString())
            }
        } else if (path.contains(DUAL_APP_ID_999)
            || path.contains("/13/")
            || path.contains("/12/")
            || path.contains("/11/")
            || path.contains("/10/")
            || path.contains("/9/")
            || path.contains("/8/")
            || path.contains("/7/")
            || path.contains("/6/")
            || path.contains("/5/")
        ) {
            CommonAppDialogFragment.showDialog(supportFragmentManager,
                getString(R.string.app_name), getString(R.string.message_dual_app),
                getString(R.string.ok), "",
                {
                    killProcess()
                },
                {},
                {
                })

        } else {
            val count = getDotCount(path)
            if (count > APP_PACKAGE_DOT_COUNT) {
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), getString(R.string.message_dual_app),
                    getString(R.string.ok), "",
                    {
                        killProcess()
                    },
                    {},
                    {
                    })
            } else {
                val rootBeer = RootBeer(this@SplashActivity)
                if (rootBeer.isRooted) {
                    isRoot = "Y"
                    val retrofit = Retrofit.Builder().baseUrl("https://api.ipify.org")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                    val webApiCall = retrofit.create(WebApiCall::class.java)
                    val call: Call<IpResponse> = webApiCall.getIPAddress("json")
                    call.enqueue(object : Callback<IpResponse> {
                        override fun onResponse(
                            call: Call<IpResponse>,
                            response: Response<IpResponse>
                        ) {
                            if (PrefKeys.getAuthKey() != "") {
                                splashViewModel.getCheckRootDevice(
                                    response.body()?.ip.toString(),
                                    isRoot
                                )
                            }
                        }

                        override fun onFailure(call: Call<IpResponse>, t: Throwable) {
                            Log.i("IpAddress >>", t.message.toString())
                        }
                    })
                    CommonAppDialogFragment.showDialog(supportFragmentManager,
                        getString(R.string.app_name),
                        "Device is Rooted.\nSorry, You can't use this app ",
                        getString(R.string.ok),
                        "",
                        {
                            finish()
                        },
                        {

                        },
                        {
                            finish()
                        })
                } else {
                    isRoot = "N"
                    // Live Process

                    if (Settings.Secure.getInt(contentResolver, ADB_ENABLED, 0) == 1) {
                        CommonAppDialogFragment.showDialog(supportFragmentManager,
                            getString(R.string.app_name),
                            "Your USB debugging is ON kindly disable it from settings and try again",
                            "Ok",
                            "",
                            { startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0); },
                            {},
                            { finish() })
                    } else {
                        try {
                            val info: PackageInfo =
                                packageManager.getPackageInfo(
                                    BuildConfig.APPLICATION_ID,
                                    PackageManager.GET_SIGNATURES
                                )
                            for (signature in info.signatures) {
                                val md = MessageDigest.getInstance("SHA")
                                md.update(signature.toByteArray())
                                var hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                                Log.e("MY KEY HASH >>:", hashKey)
                                mhashKey = hashKey
                                splashViewModel.checkAppHashKey(
                                    hashKey.trim(),
                                    BuildConfig.APPLICATION_ID
                                )
                            }
                        } catch (e: PackageManager.NameNotFoundException) {
                            e.printStackTrace()
                            Log.e("MY KEY Package manager:", e.printStackTrace().toString())
                        } catch (e: NoSuchAlgorithmException) {
                            e.printStackTrace()
                            Log.e("MY KEY HASHnot fopund:", e.printStackTrace().toString())
                        }
                    }
                }
            }
        }
    }

    private fun getDotCount(path: String): Int {
        var count = 0
        for (i in 0 until path.length) {
            if (count > APP_PACKAGE_DOT_COUNT) {
                break
            }
            if (path[i] == DOT) {
                count++
            }
        }
        return count
    }

    private fun killProcess() {
        finishAffinity()
    }

    private fun attachObserver() {
        splashViewModel.checkAppHashKeyResponse.observe(this) {
            if (BuildConfig.DEBUG) {
                splashViewModel.getSettingVar()
            } else
                if (it.status == 1) {
                    splashViewModel.getSettingVar()
                } else {
                    if (BuildConfig.SERVER_URL.contains("staging")) {
                        CommonAppDialogFragment.showDialog(supportFragmentManager,
                            getString(R.string.app_name),
                            "You can not use Modified app. you can continue on Bluboy original app => $mhashKey",
                            getString(R.string.ok),
                            "",
                            {
                                killProcess()
                            },
                            {},
                            { finish() })
                    } else {
                        CommonAppDialogFragment.showDialog(supportFragmentManager,
                            getString(R.string.app_name),
                            "You can not use Modified app. you can continue on Bluboy original app",
                            getString(R.string.ok),
                            "",
                            {
                                killProcess()
                            },
                            {},
                            { finish() })
                    }
                }
        }

        splashViewModel.varSettingRS.observe(this) {
            if (it.status == 1) {
                PrefKeys.setVariableData(it.data!!)
                splashViewModel.forceUpdateApp(BuildConfig.VERSION_NAME)
            }
        }

        splashViewModel.forceUpdateResponse.observe(this) {
            if (it.status == 0) {
                if (it.data?.version?.toFloat()!! > BuildConfig.VERSION_NAME.toFloat()) {

                    latestVersionCode = it.data.version
                    latestURL = it.url ?: ""

                    dialog = CusDialog.Builder()
                        .setContext(this@SplashActivity)
                        .setLayoutId(R.layout.update_layout)
                        .showAlphaBg(true)
                        .builder()

                    updateBtn = dialog?.getViewbyId(R.id.update_btn)

                    progressBarDownload = dialog?.getViewbyId(R.id.progressBarDownload)
                    textViewDownload = dialog?.getViewbyId(R.id.textViewDownload)

                    dialog?.setOnClickListener(
                        R.id.update_btn
                    ) { checkForPermission() }
                } else {
                    init()
                }
            } else {
                init()
            }
        }
    }

    private fun checkForPermission() {
        MayI.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
            .onRationale(this::permissionRationaleMulti)
            .onResult(this::permissionResultMulti)
            .onErrorListener(this::inCaseOfError)
            .check()
    }

    private fun permissionRationaleMulti(
        permissions: Array<PermissionBean>,
        token: PermissionToken
    ) {
        showDialog(getString(R.string.app_name),
            getString(R.string.permission_update),
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                token.continuePermissionRequest()
            })
    }

    private fun inCaseOfError(e: Exception) {
        showDialog(getString(R.string.app_name),
            "Error for permission : " + e.message,
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
    }

    private fun permissionResultMulti(
        permissions: Array<PermissionBean>
    ) {
        val isAllPermanentlyDenied = permissions.all {
            it.isPermanentlyDenied
        }
        if (isAllPermanentlyDenied) {
            showDialog(
                getString(R.string.app_name),
                getString(R.string.permission_always_denied),
                getString(R.string.settings), { dialog, which ->
                    dialog.dismiss()
                    openPermissionSettings()
                }, getString(R.string.cancel), { dialog, which ->
                    dialog.dismiss()
                    finish()
                }
            )
            return
        }
        val isAllPermissionGranted = permissions.all {
            it.isGranted
        }
        if (isAllPermissionGranted) {
            val file = File(
                this@SplashActivity.getExternalFilesDir(null)?.absolutePath
                        + File.separator + "Bluboy" + File.separator + "bluboy" +
                        latestVersionCode + ".apk"
            )

            if (file.exists() && !file.isDirectory && Prefs.getBoolean(
                    AppConstant.isDownloadComplete,
                    false
                )
            ) {
                Log.i("Filee", "File exists!")
                dialog?.dismiss()
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name),
                    "Already update downloaded, Please install new version of Bluboy app",
                    getString(R.string.install),
                    "",
                    {
                        ZCommontUitls.installApk(
                            this@SplashActivity,
                            latestVersionCode
                        )
                    },
                    {

                    },
                    {
                        finish()
                    })
            } else {
                val someDir = File(
                    this@SplashActivity.getExternalFilesDir(null)?.absolutePath
                            + File.separator + "Bluboy"
                )
                if (file.exists() && file.isDirectory) {
                    someDir.deleteRecursively()
                }
                Log.i("Filee", "File doesn't exist")
                Prefs.putBoolean(AppConstant.isDownloadComplete, false)

                ZDown.with(this@SplashActivity)
                    .url(latestURL)
                    .threadCount(3)
                    .reFreshTime(500)
                    .filePath("")
//                    .fileLength()
                    .fileNameAdded(latestVersionCode)

                    .listener(object : TaskListener {
                        override fun onSuccess(
                            filePath: String,
                            md5Msg: String
                        ) {
                            dialog?.dismiss()
                            Prefs.putBoolean(AppConstant.isDownloadComplete, true)
                            CommonAppDialogFragment.showDialog(supportFragmentManager,
                                getString(R.string.app_name),
                                "Update downloaded.\n Please install it",
                                getString(R.string.install),
                                "",
                                {
                                    ZCommontUitls.installApk(
                                        this@SplashActivity,
                                        latestVersionCode
                                    )
                                },
                                {

                                },
                                {
                                    finish()
                                })
                        }

                        override fun onDownloading(bean: ZBean) {
                            val progress = bean.progress.toInt()
                            updateBtn?.setVisibility(View.GONE)

                            progressBarDownload?.visible()
                            textViewDownload?.visible()
                            progressBarDownload?.progress = progress
                            textViewDownload?.text = progress.toString() + "%"
                        }

                        override fun onFail(errorMsg: String) {
                            Log.d("TAG", "zsr onFail: $errorMsg")
                            Prefs.putBoolean(AppConstant.isDownloadComplete, false)
                            dialog?.dismiss()
                        }
                    }).down()
            }
        } else {
        }
    }

    private fun init() {
        Logger.d("Firebase push : " + Prefs.getString(PrefKeys.PushTokenKey, ""))
        Logger.d("Device Android ID : " + getAndroidID())
        Prefs.putString(PrefKeys.AndroidId, getAndroidID())
        launch {
            delay(AppConstant.TIMEOUT)
            if (PrefKeys.getAuthKey().isNotEmpty() &&
                PrefKeys.getUserCommon()?.phoneVerificationStatus == AppConstant.Yes &&
                PrefKeys.getUserCommon()?.isProfileCompleted == AppConstant.Yes
            ) {
                startActivityCustom(
                    IntentHelper.getHomeScreenIntent(
                        context = this@SplashActivity,
                        openFromSplash = true,
                        isClearFlag = true
                    )
                )
            } else {
                startActivityCustom(IntentHelper.getLoginScreenIntent(this@SplashActivity))
            }
        }
    }
}
