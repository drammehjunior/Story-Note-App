package com.example.submissionintermediate.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submissionintermediate.R
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import java.util.*

class MyEditTextPassword: AppCompatEditText {

    private lateinit var passwordButton: Drawable
    private var timer = Timer()


    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init(){
        passwordButton = ContextCompat.getDrawable(context, R.drawable.ic_lock_24) as Drawable
        setCompoundDrawablesWithIntrinsicBounds(passwordButton, null, null, null)



        addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(s.toString().length < 6){
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                    error = "Password must be more than 6 characters"

                }else{
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }
            override fun afterTextChanged(str: Editable) {
            }

        })
    }

}