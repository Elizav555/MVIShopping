package com.elizav.mvishopping.utils

import android.content.DialogInterface

data class DialogParams(
    val title:String,
    val message:String,
    val submitBtnText:String,
    val submitOnClickListener: DialogInterface.OnClickListener,
    val cancelOnClickListener: DialogInterface.OnClickListener
)
