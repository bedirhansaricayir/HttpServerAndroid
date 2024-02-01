package com.bilmsoft.httpserver.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes val layoutRes: Int): Fragment(layoutRes)