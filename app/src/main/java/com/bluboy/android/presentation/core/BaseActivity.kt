package com.bluboy.android.presentation.core

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.danimahardhika.cafebar.CafeBar
import com.danimahardhika.cafebar.CafeBarGravity
import com.danimahardhika.cafebar.CafeBarTheme
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.bluboy.android.BuildConfig
import com.bluboy.android.R
import com.bluboy.android.data.models.User
import com.bluboy.android.domain.exceptions.MyException
import com.bluboy.android.presentation.dialog.BlockedUserDialogFragment
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.dialog.InternetOffDialogFragment
import com.bluboy.android.presentation.loginsignup.LoginActivity
import com.bluboy.android.presentation.settings.ContactUsActivity
import com.bluboy.android.presentation.splash.CusDialog
import com.bluboy.android.presentation.splash.MyBean
import com.bluboy.android.presentation.utility.*
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import com.zhengsr.zdwon_lib.ZDown
import com.zhengsr.zdwon_lib.bean.ZBean
import com.zhengsr.zdwon_lib.callback.CheckListener
import com.zhengsr.zdwon_lib.callback.TaskListener
import com.zhengsr.zdwon_lib.utils.ZCommontUitls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.File
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        var dialogShowing = false
    }

    private var maintenanceDialog = false
    private var inActiveUserDialog = false
    private var internetDialog = false

    lateinit var toolbar1: Toolbar
    private var needToShowBackButton: Boolean? = false
    private var t: Thread? = null
    lateinit var job: Job
    private var progress: CustomProgressDialog? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private var dialog: CusDialog? = null
    private var updateBtn: AppCompatButton? = null
    private var textContent: AppCompatTextView? = null

    private var progressBarDownload: ProgressBar? = null
    private var textViewDownload: AppCompatTextView? = null
    private var beanData: MyBean? = null


    abstract fun getBaseViewModel(): BaseViewModel?

    var user: User? = null
    private val APP_UPDATE_REQUEST_CODE = 1991

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

        progress = CustomProgressDialog(this)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        attachBaseObserver()

        getUserProfile()
    }

    fun getUserProfile() {
        val userProfile = Prefs.getString(PrefKeys.UserProfile, "")
        if (userProfile.isNotEmpty()) {
            user = Gson().fromJson(userProfile, User::class.java)
        }
    }

    /* override fun onResume() {
         super.onResume()
         t?.let {
             if (it.isAlive) {
                 it.interrupt();
                 it.join();
             }
             t = null;
         }
     }

     override fun onPause() {
         super.onPause()
         t = object : Thread() {
             override fun run() {
                 try {
                     sleep((100 * 1000).toLong()) // 60SEC
                     // Wipe your valuable data here

                     val runningAppProcessInfo = ActivityManager.RunningAppProcessInfo()
                     ActivityManager.getMyMemoryState(runningAppProcessInfo)
                     val appRunningBackground =
                         runningAppProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                     if (appRunningBackground) {
                         finishAffinity()
                     }

                 } catch (e: InterruptedException) {
                     return
                 }
             }
         }

         t?.let {
             it.start()
         }
     }
 */
    private fun attachBaseObserver() {
        getBaseViewModel()?.exceptionLiveData?.observe(this, Observer {
            hideProgress()
            it?.apply {
                when (this) {
                    is MyException.InternetConnectionException -> {
                        InternetOffDialogFragment.showDialog(supportFragmentManager, {
                            if (isOnline(baseContext)) {
                                if (PrefKeys.getAuthKey() != null && PrefKeys.getAuthKey() != ""
                                    && PrefKeys.getUserCommon()?.isProfileCompleted != AppConstant.No
                                    && PrefKeys.getUserCommon()?.userName != ""
                                ) {
                                    startActivityCustom(
                                        IntentHelper.getHomeScreenIntent(
                                            baseContext
                                        )
                                    )
                                } else {
                                    startActivityCustom(
                                        IntentHelper.getLoginScreenIntent(
                                            baseContext
                                        )
                                    )
                                }
                            } else {
                                finishAffinity()
                            }
                        }, {
                            //cancel
                        })
                    }

                    is MyException.JsonException -> {
                        if (PrefKeys.getAuthKey().isNotEmpty())
                            showErrorDialog(getString(R.string.exception_error_unparceble))
                        /*else
                            showErrorDialog(getString(R.string.user_logout))*/
                    }

                    is MyException.ServerNotAvailableException -> showErrorDialog(getString(R.string.exception_error_server))
                    is MyException.AppUpgradeVersionException -> goAppUpdateFlow(getString(R.string.exception_attempt))
                    is MyException.TooManyAttemptException -> showErrorDialog(getString(R.string.exception_attempt))
                    is MyException.DatabaseException -> showErrorDialog(getString(R.string.exception_error_database))
                    is MyException.NetworkCallCancelException -> {
                    }
                    else -> {
                        var message = it.message
                        message = if (!message.isNullOrBlank()) {
                            it.localizedMessage
                        } else {
                            getString(R.string.exception_try_again)
                        }
                        showErrorDialog(message.toString())
                    }
                }
            }
        })

        getBaseViewModel()?.statusLiveData?.observe(this, Observer {
            hideProgress()
            it?.apply {
                when (this) {

                    5 -> {
                        if (!inActiveUserDialog) {
                            inActiveUserDialog = true
                            BlockedUserDialogFragment.showDialog(supportFragmentManager,
                                getString(R.string.app_name),
                                "You have been blocked by the admin.",
                                "", "Contact Us", { },
                                {
                                    Prefs.putString(PrefKeys.AuthKey, "")
                                    Prefs.putString(PrefKeys.UserProfile, "")
                                    val i = Intent(this@BaseActivity, ContactUsActivity::class.java)
                                    i.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    this@BaseActivity.startActivity(i)
                                },
                                {
                                    Prefs.putString(PrefKeys.AuthKey, "")
                                    Prefs.putString(PrefKeys.UserProfile, "")
                                    val i = Intent(this@BaseActivity, LoginActivity::class.java)
                                    i.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    this@BaseActivity.startActivity(i)
                                })
                        }
                    }

                    4 -> {
                        if (maintenanceDialog == false) {
                            maintenanceDialog = true
                            CommonAppDialogFragment.showDialog(supportFragmentManager,
                                getString(R.string.app_name),
                                "We are under maintenance. We will be back soon.",
                                getString(R.string.ok),
                                "",
                                {
                                    finishAffinity()
                                },
                                {

                                },
                                {
                                    finish()
                                })
                        }
                    }

                    else -> {
                        if (Settings.Secure.getInt(
                                contentResolver, Settings.Global.ADB_ENABLED, 0
                            ) == 1 && !BuildConfig.DEBUG
                        ) {
                            CommonAppDialogFragment.showDialog(supportFragmentManager,
                                getString(R.string.app_name),
                                "Your USB debugging is ON kindly disable it from settings and try again",
                                getString(
                                    R.string.settings
                                ),
                                "",
                                {
                                    startActivityForResult(
                                        Intent(android.provider.Settings.ACTION_SETTINGS),
                                        0
                                    );
                                },
                                {

                                },
                                {
                                    finish()
                                })
                        } else {
//                            splashViewModel.getSettingVar()
                        }
                    }
                }
            }
        })
    }

    private fun checkForPermission() {
        MayI.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                this@BaseActivity.getExternalFilesDir(null)?.absolutePath
                        + File.separator + "Bluboy" + File.separator + "bluboy" + beanData?.latestVersionCode + ".apk"
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
                    "Already update downloaded, Please install new version of BluBoy app",
                    getString(R.string.install),
                    "",
                    {
                        ZCommontUitls.installApk(
                            this@BaseActivity,
                            beanData?.latestVersionCode
                        )
                    },
                    {

                    },
                    {
                        finish()
                    })
            } else {
                val someDir = File(
                    this@BaseActivity.getExternalFilesDir(null)?.absolutePath
                            + File.separator + "Bluboy"
                )
                if (file.exists() && file.isDirectory) {
                    someDir.deleteRecursively()
                }
                Log.i("Filee", "File doesn't exist")
                Prefs.putBoolean(AppConstant.isDownloadComplete, false)
                ZDown.with(this@BaseActivity)
                    .url(PrefKeys.getVariableData()?.dOWNLOADURL)
                    .threadCount(3)
                    .reFreshTime(500)
                    .filePath("")
//                    .fileLength()
                    .fileNameAdded(beanData?.latestVersionCode)

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
                                        this@BaseActivity,
                                        beanData?.latestVersionCode
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
//                        progressBar?.setVisibility(View.VISIBLE)
//                        progressBar?.setProgress(progress)

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

    open fun isOnline(context: Context?): Boolean {
        var result = false
        if (context != null) {
            val cm: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm != null) {
                val networkInfo: NetworkInfo? = cm.getActiveNetworkInfo()
                if (networkInfo != null) {
                    result = networkInfo.isConnected()
                }
            }
        }
        return result
    }


    private fun goAppUpdateFlow(message: String) {
        showDialog(
            getString(R.string.app_name),
            "App Update Required",
            getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }
        )
    }

    private fun showErrorDialog(message: String) {
        showDialog(
            getString(R.string.app_name),
            message,
            getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    protected fun addFragment(
        @IdRes containerViewId: Int, fragment: Fragment,
        fragmentTag: String
    ) {
        supportFragmentManager
            .beginTransaction()
            .add(containerViewId, fragment, fragmentTag)
            .disallowAddToBackStack()
            .commit()
    }

    protected fun replaceFragment(
        @IdRes containerViewId: Int, fragment: Fragment,
        fragmentTag: String,
        addToBackStack: Boolean? = false
    ) {
        supportFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragment, fragmentTag)
            .addToBackStack(if (addToBackStack!!) fragment::class.java.simpleName else null)
            .commit()
    }

    protected fun replaceFragmentWithPop(
        @IdRes containerViewId: Int, fragment: Fragment,
        fragmentTag: String,
        addToBackStack: Boolean? = false
    ) {
        supportFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragment, fragmentTag)
            .disallowAddToBackStack()
            .commit()
    }


    fun showProgress() {
        if (!this.isFinishing) {
            progress?.show()
        }
    }

    fun hideProgress() {
        if (!this.isFinishing && progress?.isShowing == true) {
            progress?.dismiss()
        }
    }

    /**
     * Method for perform multiple action by single line
     *
     * @param toolbar - pass your toolbar added in your layout
     * @param title - screen title
     * @param needToShowBackButton - want to show back button or not in activity
     * @param titleTextColor - color for toolbar title
     * @param toolbarColor - color for toolbar background
     * @param backButtonColor - color for back button
     */
    fun setupToolbar(
        toolbar: Toolbar,
        title: String,
        needToShowBackButton: Boolean? = false,
        titleTextColor: Int? = R.color.colorBlack,
        toolbarColor: Int? = null,
        backButtonColor: Int? = null
    ) {
        this.toolbar1 = toolbar
        this.needToShowBackButton = needToShowBackButton
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        if (needToShowBackButton!!) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setHomeButtonEnabled(true)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black)
        }
        toolbar.findViewById<AppCompatTextView>(R.id.txt_header).text = title
        toolbar.findViewById<AppCompatTextView>(R.id.txt_header).setTextColor(titleTextColor!!)

        if (toolbarColor != null) {
            toolbar.background = ColorDrawable(toolbarColor)
        } else {
            toolbar.background = ColorDrawable(Color.TRANSPARENT)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.invisible, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    fun showMessage(
        message: String? = null,
        autoDismiss: Boolean = true,
        shadow: Boolean = true,
        isLong: Boolean = false,
        maxLines: Int = 5,
        showButton: Boolean = false,
        buttonName: String = "",
        @ColorRes buttonColor: Int = R.color.colorWhite,
        @ColorRes backgroundColor: Int = R.color.colorBlack,
        buttonCallback: ((CafeBar) -> Unit) = {}
    ) {
        if (!message.isNullOrEmpty()) {
            val builder = CafeBar.builder(this)
                .content(message)
                .theme(CafeBarTheme.Custom(ContextCompat.getColor(this, backgroundColor)))
                .floating(true)
                .gravity(CafeBarGravity.CENTER)
                .typeface(
                    ResourcesCompat.getFont(this, R.font.montserrat),
                    ResourcesCompat.getFont(this, R.font.montserrat_bold)
                )
                .swipeToDismiss(true)
                .showShadow(shadow)
                .autoDismiss(autoDismiss)
                .duration(if (isLong) CafeBar.Duration.LONG else CafeBar.Duration.SHORT)
                .maxLines(maxLines)

            if (showButton) {
                builder.neutralText(buttonName)
                builder.neutralColor(ContextCompat.getColor(this, buttonColor))
                builder.onNeutral(buttonCallback)
            }

            builder.show()
        }
    }


    //endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(
                    this,
                    "App Update failed, please try again on the next app launch.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}