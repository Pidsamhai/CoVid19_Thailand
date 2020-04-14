package com.github.pidsamhai.covid19thailand.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {
    lateinit var savedState: Bundle
    val SAVE_BUNDLE_TAG = this::class.java.simpleName
    var createdStateInDestroyView = false

}