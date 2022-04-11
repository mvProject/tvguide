package com.mvproject.tvprogramguide.utils

private const val DEFAULT_TOP_MARGIN = 24
private const val LOAD_DEFAULT_ZERO = 0

/**
 * Create dialog with information design
 *
 * @param activity parent activity for builder
 * @param title string title for dialog display
 * @param confirmClick callback for button close
 */
/*
fun createAddDialog(
    activity: Activity,
    title: String,
    confirmClick: (result: String) -> Unit
): AlertDialog {
    val builder = MaterialAlertDialogBuilder(activity, R.style.MaterialAlertDialogRounded)
    val binding = DialogAddBinding.inflate(LayoutInflater.from(activity))
    builder.setView(binding.root).apply {
        with(binding) {
            dialogTitle.text = title
            fieldName.onTextChanged {
                btnAddClose.isEnabled = it.length > COUNT_ONE
            }
            btnAddClose.setOnClickListener { confirmClick(fieldName.text.toString()) }
        }
    }

    return builder.create().apply {
        setCancelable(true)
    }
}
*/

/*
fun createSelectDialog(
    activity: Activity,
    initialValue : String,
    options: List<String>,
    confirmClick: (result: String) -> Unit
): AlertDialog {
    val builder = MaterialAlertDialogBuilder(activity, R.style.MaterialAlertDialogRounded)
    val binding = DialogSelectListBinding.inflate(LayoutInflater.from(activity))
    var selected = initialValue

    builder.setView(binding.root).apply {
        with(binding) {
            val params = RadioGroup.LayoutParams(binding.root.context, null).apply {
                setMargins(
                    LOAD_DEFAULT_ZERO,
                    DEFAULT_TOP_MARGIN,
                    LOAD_DEFAULT_ZERO,
                    LOAD_DEFAULT_ZERO
                )
            }

            for (item in options.indices) {
                MaterialRadioButton(binding.root.context, null, R.attr.radioButtonStyle).apply {
                    id = View.generateViewId()
                    text = options[item]
                    layoutParams = params
                    setTextColor(
                        ContextCompat.getColorStateList(
                            binding.root.context,
                            R.color.selector_dialog_text_color
                        )
                    )
                    isChecked = text == selected
                }.also { rb ->
                    radioGroupReceipts.addView(rb)
                }
            }
            radioGroupReceipts.setOnCheckedChangeListener { radioGroup, clickedButton ->
                selected = radioGroup.findViewById<RadioButton>(clickedButton).text.toString()
            }

            btnApplySelection.setOnClickListener {
                confirmClick(selected)
            }
        }
    }

    return builder.create().apply {
        setCancelable(true)
    }
}


 */
