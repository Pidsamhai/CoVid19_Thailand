package com.github.pidsamhai.covid19thailand.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.pidsamhai.covid19thailand.R
import com.google.android.material.appbar.MaterialToolbar

class TimeLineFragment : Fragment() {

    private lateinit var materialToolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        materialToolbar =  (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.subtitle = ""
    }
}