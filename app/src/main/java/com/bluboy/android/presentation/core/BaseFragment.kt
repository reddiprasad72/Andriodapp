package com.bluboy.android.presentation.core



import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import android.view.View

open class BaseFragment : Fragment() {

    var rootView: View? = null
    private var progress: CustomProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progress = CustomProgressDialog(requireContext())
    }

    fun showProgress() {
        if (!progress?.isShowing!!) {
        progress?.show()
        }
    }

    fun hideProgress() {
//        if (!this.isFinishing && progress?.isShowing == true) {
        progress?.dismiss()
//        }
    }

    protected fun addFragment(@IdRes containerViewId: Int, fragment: Fragment, fragmentTag: String) {
        childFragmentManager
            .beginTransaction()
            .add(containerViewId, fragment, fragmentTag)
            .disallowAddToBackStack()
            .commit()
    }

    protected fun replaceFragment(
        @IdRes containerViewId: Int, fragment: Fragment, fragmentTag: String, addToBackStack: Boolean? = false
    ) {
        childFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragment, fragmentTag)
            .addToBackStack(if (addToBackStack!!) fragment::class.java.simpleName else null)
            .commit()
    }

}