package com.bluboy.android.presentation.utility

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.lang.ref.WeakReference

class EditTextSearchCommon constructor(@NonNull editText: EditText) {

    private val editTextWeakReference: WeakReference<EditText>?
    private val editTextHandler: Handler = Handler(Looper.getMainLooper())
    private var editTextWorker: Runnable? = null
    private var delayMillis: Int = 0
    private var minLength: Int = 0
    private val textWatcher: TextWatcher
    private var callback: ((String) -> Unit)? = null

    init {
        this.editTextWorker = DebounceRunnable("", callback, minLength)
        this.delayMillis = 500
        this.textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //unused
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //unused
            }

            override fun afterTextChanged(s: Editable) {
                editTextHandler.removeCallbacks(editTextWorker!!)
                editTextWorker = DebounceRunnable(s.toString(), callback, minLength)
                editTextHandler.postDelayed(editTextWorker!!, delayMillis.toLong())
            }
        }
        this.editTextWeakReference = WeakReference(editText)
        val editTextInternal = this.editTextWeakReference.get()
        editTextInternal?.addTextChangedListener(textWatcher)
    }

    fun create(editText: EditText): EditTextSearchCommon {
        return EditTextSearchCommon(editText)
    }

    fun create(editText: EditText, delayMillis: Int): EditTextSearchCommon {
        val editTextDebounce = EditTextSearchCommon(editText)
        editTextDebounce.setDelayMillis(delayMillis)
        return editTextDebounce
    }

    fun watch(@Nullable callback: ((String) -> Unit)) {
        this.callback = callback
    }

    fun watch(@Nullable callback: ((String) -> Unit), delayMillis: Int) {
        this.callback = callback
        this.delayMillis = delayMillis
    }

    private fun setDelayMillis(delayMillis: Int) {
        this.delayMillis = delayMillis
    }

    private class DebounceRunnable internal constructor(
        private val result: String,
        private val debounceCallback: ((String) -> Unit)?,
        private val minLength: Int
    ) : Runnable {

        override fun run() {
            if (result.length >= minLength)
                debounceCallback?.invoke(result)
        }
    }
}