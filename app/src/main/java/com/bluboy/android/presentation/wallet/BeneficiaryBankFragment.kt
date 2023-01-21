package com.bluboy.android.presentation.wallet

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import com.bluboy.android.R
import com.bluboy.android.databinding.FragmentBankBeneficiaryBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class BeneficiaryBankFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by sharedViewModel()
    private lateinit var binding: FragmentBankBeneficiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBankBeneficiaryBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.etIFSC.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(et: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                var s = et.toString()
                if (s != s.uppercase()) {
                    s = s.uppercase()
                    binding.etIFSC.setText(s)
                }
                binding.etIFSC.setSelection(binding.etIFSC.text?.length!!)
            }

            override fun beforeTextChanged(
                arg0: CharSequence, arg1: Int, arg2: Int,
                arg3: Int
            ) {
            }

            override fun afterTextChanged(et: Editable) {

            }
        })


        binding.btnAdd.setSafeOnClickListener {
            if (isValid()) {
                showProgress()
                homeViewModel.getBeneficiaryAdd(
                    binding.etAccountantName.text.toString(),
                    binding.etAccountantNickName.text.toString(),
                    PrefKeys.getUserCommon()?.email.toString(),
                    PrefKeys.getUserCommon()?.phone.toString(),
                    binding.etBankName.text.toString(),
                    binding.etAccontNumber.text.toString().replace(" ", ""),
                    binding.etIFSC.text.toString(),
                    binding.etAddress.text.toString(),
                    binding.etCity.text.toString(),
                    binding.etState.text.toString(),
                    binding.etPincode.text.toString(),
                    "",
                    "banktransfer"//transferMode
                )
            }
        }

        binding.etAccontNumber.addTextChangedListener(object : TextWatcher {
            private val TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
            private val TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
            private val DIVIDER_MODULO =
                5 // means divider position is every 5th symbol beginning with 1
            private val DIVIDER_POSITION =
                DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
            private val DIVIDER = ' '

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // noop
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // noop
            }

            override fun afterTextChanged(s: Editable) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(
                        0,
                        s.length,
                        buildCorrectString(
                            getDigitArray(s, TOTAL_DIGITS),
                            DIVIDER_POSITION,
                            DIVIDER
                        )
                    )
                }
            }

            private fun isInputCorrect(
                s: Editable,
                totalSymbols: Int,
                dividerModulo: Int,
                divider: Char
            ): Boolean {
                var isCorrect = s.length <= totalSymbols // check size of entered string
                for (i in s.indices) { // check that every element is right
                    isCorrect = if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect and (divider == s[i])
                    } else {
                        isCorrect and Character.isDigit(s[i])
                    }
                }
                return isCorrect
            }

            private fun buildCorrectString(
                digits: CharArray,
                dividerPosition: Int,
                divider: Char
            ): String {
                val formatted = StringBuilder()
                for (i in digits.indices) {
                    if (digits[i].toInt() != 0) {
                        formatted.append(digits[i])
                        if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                            formatted.append(divider)
                        }
                    }
                }
                return formatted.toString()
            }

            private fun getDigitArray(s: Editable, size: Int): CharArray {
                val digits = CharArray(size)
                var index = 0
                var i = 0
                while (i < s.length && index < size) {
                    val current = s[i]
                    if (Character.isDigit(current)) {
                        digits[index] = current
                        index++
                    }
                    i++
                }
                return digits
            }
        })

        binding.etConfirmAccontNumber.addTextChangedListener(object : TextWatcher {
            private val TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
            private val TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
            private val DIVIDER_MODULO =
                5 // means divider position is every 5th symbol beginning with 1
            private val DIVIDER_POSITION =
                DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
            private val DIVIDER = ' '

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // noop
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // noop
            }

            override fun afterTextChanged(s: Editable) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(
                        0,
                        s.length,
                        buildCorrectString(
                            getDigitArray(s, TOTAL_DIGITS),
                            DIVIDER_POSITION,
                            DIVIDER
                        )
                    )
                }
            }

            private fun isInputCorrect(
                s: Editable,
                totalSymbols: Int,
                dividerModulo: Int,
                divider: Char
            ): Boolean {
                var isCorrect = s.length <= totalSymbols // check size of entered string
                for (i in s.indices) { // check that every element is right
                    isCorrect = if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect and (divider == s[i])
                    } else {
                        isCorrect and Character.isDigit(s[i])
                    }
                }
                return isCorrect
            }

            private fun buildCorrectString(
                digits: CharArray,
                dividerPosition: Int,
                divider: Char
            ): String {
                val formatted = StringBuilder()
                for (i in digits.indices) {
                    if (digits[i].toInt() != 0) {
                        formatted.append(digits[i])
                        if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                            formatted.append(divider)
                        }
                    }
                }
                return formatted.toString()
            }

            private fun getDigitArray(s: Editable, size: Int): CharArray {
                val digits = CharArray(size)
                var index = 0
                var i = 0
                while (i < s.length && index < size) {
                    val current = s[i]
                    if (Character.isDigit(current)) {
                        digits[index] = current
                        index++
                    }
                    i++
                }
                return digits
            }
        })


    }

    fun EditText.onlyUppercase() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS

        filters = arrayOf(InputFilter.AllCaps())
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


    private fun isValid(): Boolean {
        if (!StringUtils.isValid(binding.etAccountantNickName.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_nick_name))
            return false
        } else if (!StringUtils.isValid(binding.etAccountantName.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_accountant_name))
            return false
        } else if (!StringUtils.isValid(binding.etAccountantNickName.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_nick_name))
            return false
        } else if (!StringUtils.isValid(binding.etBankName.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_bank_name))
            return false
        } else if (!StringUtils.isValid(binding.etAccontNumber.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_account_number))
            return false
        } else if (!StringUtils.isValid(binding.etConfirmAccontNumber.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_confirm_account_number))
            return false
        } else if (!StringUtils.equals(
                binding.etAccontNumber.text.toString(),
                binding.etConfirmAccontNumber.text.toString()
            )
        ) {
            context?.toastError(getString(R.string.validation_mismatch_account))
            return false
        } else if (!StringUtils.isValid(binding.etIFSC.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_ifsc))
            return false
        } else if (!StringUtils.isValidIFSCode(binding.etIFSC.text.toString())) {
            context?.toastError(getString(R.string.validation_enter_valid_ifsc))
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