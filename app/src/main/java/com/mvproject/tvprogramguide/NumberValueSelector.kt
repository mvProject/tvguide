package com.mvproject.tvprogramguide

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class NumberValueSelector @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attributeSet, defStyleAttr) {

    interface OnDefaultValueChange {
        fun onValueChanged(value: Int)
    }

    private var view: View =
        LayoutInflater.from(context).inflate(R.layout.layout_value_selector, this, true)

    private var numberValue: TextView = view.findViewById(R.id.value)
    private var valueDecrease: ImageView = view.findViewById(R.id.valueDecrease)
    private var valueIncrease: ImageView = view.findViewById(R.id.valueIncrease)

    private var defaultValue: Int = 0
    private var onDefaultValueChangeListener: OnDefaultValueChange? = null

    private var minusDrawable: Drawable?
    private var plusDrawable: Drawable?
    private var minusColor: Int
    private var plusColor: Int
    private var valueColor: Int
    init {
        val typedArray =
            context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.NumberValueSelector,
                0,
                defStyleAttr
            )

        try {
            minusDrawable =
                typedArray.getDrawable(R.styleable.NumberValueSelector_nvs_minusImage)
                    ?: ContextCompat.getDrawable(context,R.drawable.ic_minus)
            plusDrawable =
                typedArray.getDrawable(R.styleable.NumberValueSelector_nvs_plusImage)
                    ?: ContextCompat.getDrawable(context,R.drawable.ic_plus)

            minusColor = typedArray.getColor(
                R.styleable.NumberValueSelector_nvs_minusTint,
                Color.BLACK
            )
            plusColor = typedArray.getColor(
                R.styleable.NumberValueSelector_nvs_plusTint,
                Color.BLACK
            )
            valueColor = typedArray.getColor(
                R.styleable.NumberValueSelector_nvs_numberColor,
                Color.BLACK
            )

            defaultValue = typedArray.getInt(R.styleable.NumberValueSelector_nvs_defaultNumber, 0)
        } finally {
            typedArray.recycle()
        }

        defaultInit()

        valueDecrease.setOnClickListener {
            decrease()
        }
        valueIncrease.setOnClickListener {
            increase()
        }
    }

    private fun defaultInit(){
        minusDrawable?.let { image ->
            valueDecrease.setImageDrawable(image)
            valueDecrease.setColorFilter(minusColor)
        }
        plusDrawable?.let { image ->
            valueIncrease.setImageDrawable(image)
            valueIncrease.setColorFilter(plusColor)
        }

        numberValue.setTextColor(valueColor)
    }

    fun setValue(value: Int) {
        defaultValue = value
        updateTextView()
    }

    private fun increase() {
        defaultValue += 1
        updateTextView()
        onDefaultValueChangeListener?.onValueChanged(defaultValue)
    }

    private fun decrease() {
        defaultValue -= 1
        updateTextView()
        onDefaultValueChangeListener?.onValueChanged(defaultValue)
    }

    private fun updateTextView() {
        numberValue.text = defaultValue.toString()
    }

    val currentValue get() = defaultValue

    fun onValueChange(action: (value: Int) -> Unit) {
        onDefaultValueChangeListener = object : OnDefaultValueChange {
            override fun onValueChanged(value: Int) {
                action(value)
            }
        }
    }
}