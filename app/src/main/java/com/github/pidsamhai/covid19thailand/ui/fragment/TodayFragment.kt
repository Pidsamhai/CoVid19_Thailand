package com.github.pidsamhai.covid19thailand.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.Today
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_today.*
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("CAST_NEVER_SUCCEEDS")
class TodayFragment : Fragment() {

    private val viewModel: ToDayViewModel by viewModel()
    private lateinit var materialToolbar: MaterialToolbar
    private lateinit var _cacheToday: Today

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.today.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.e("onActivityCreated: ", "Trigger")
                _cacheToday = it
                updateUI(it)
            }
            refreshLayout.isRefreshing = false
        })


        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)

        refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun updateUI(today: Today) {
        conFirmed.text = today.confirmed.toString()
        newConFirmed.text = newS(today.newConfirmed)

        hospitalized.text = today.hospitalized.toString()
        newHospitalized.text =
            if (today.newHospitalized!! > 0) "( เพิ่มขึ้น ${today.newHospitalized} )" else ""

        recovered.text = today.recovered.toString()
        newRecovered.text = newS(today.newRecovered)

        deaths.text = today.deaths.toString()
        newDeaths.text = newS(today.newDeaths)
        materialToolbar.subtitle = lastUpdate(today.updateDate)
    }

    private fun newS(data: Int?): String {
        return "( เพิ่มขึ้น $data )"
    }

    private fun lastUpdate(data: String): String {
        return "( อัพเดทล่าสุด $data)"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e("onSaveInstanceState: ", "On Save")
    }

}