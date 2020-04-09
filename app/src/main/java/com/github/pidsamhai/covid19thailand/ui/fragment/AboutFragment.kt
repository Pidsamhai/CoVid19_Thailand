package com.github.pidsamhai.covid19thailand.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
class AboutFragment : Fragment() {

    private lateinit var materialToolbar:MaterialToolbar
    private val viewModel:ToDayViewModel by sharedViewModel()
    private var cache:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.apply {
            title = "About"
            subtitle = "   "
        }

    }
}