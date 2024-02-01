package com.bilmsoft.httpserver.util.extension

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/** Activity binding delegate, may be used since onCreate up to onDestroy (inclusive) */
inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline factory: (LayoutInflater) -> T,
) = lazy(LazyThreadSafetyMode.NONE) { factory(layoutInflater) }