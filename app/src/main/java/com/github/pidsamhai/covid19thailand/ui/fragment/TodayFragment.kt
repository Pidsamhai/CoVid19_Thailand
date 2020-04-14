package com.github.pidsamhai.covid19thailand.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.utilities.lastUpdate
import com.github.pidsamhai.covid19thailand.utilities.newS
import com.github.pidsamhai.covid19thailand.utilities.toToday
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.layout_static.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


@Suppress("CAST_NEVER_SUCCEEDS")
class TodayFragment : BaseFragment() {

    private val viewModel: ToDayViewModel by sharedViewModel()
    private var cache: String? = null

    private lateinit var materialToolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.title = resources.getString(R.string.covid_today)

        viewModel.today.observe(viewLifecycleOwner, Observer {
            it?.let {
                refreshLayout.isRefreshing = false
                Timber.e("Fetch Data")
                cache = it.toGsonString()
                updateUI(it)
            }
        })

        if (viewModel.cache != null) {
            Timber.e("CACHE DETECTED")
            val today = viewModel.cache!!.toToday()
            cache = today.toGsonString()
            updateUI(today)
        } else {
            viewModel.refresh()
        }
        refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun updateUI(today: Today) {
        Timber.e("YEAH!!!!!!!!!")
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cache = cache
    }

}