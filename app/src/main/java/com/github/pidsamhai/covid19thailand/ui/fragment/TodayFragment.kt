package com.github.pidsamhai.covid19thailand.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.databinding.FragmentTodayBinding
import com.github.pidsamhai.covid19thailand.databinding.LayoutStaticBinding
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.utilities.lastUpdate
import com.github.pidsamhai.covid19thailand.utilities.newS
import com.github.pidsamhai.covid19thailand.utilities.toCurrency
import com.github.pidsamhai.covid19thailand.utilities.toToday
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


@Suppress("CAST_NEVER_SUCCEEDS")
class TodayFragment : BaseFragment() {

    private val viewModel: ToDayViewModel by sharedViewModel()
    private var cache: String? = null
    private lateinit var materialToolbar: MaterialToolbar
    private lateinit var _binding: FragmentTodayBinding
    private lateinit var _contentBinding: LayoutStaticBinding
    private val binding: FragmentTodayBinding
        get() = _binding
    private val contentBinding: LayoutStaticBinding
        get() = _contentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        _contentBinding = LayoutStaticBinding.bind(_binding.root)
        return _binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.title = resources.getString(R.string.covid_today)

        viewModel.today.observe(viewLifecycleOwner, {
            it?.let {
                binding.refreshLayout.isRefreshing = false
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
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun updateUI(today: Today) {
        Timber.e("YEAH!!!!!!!!!")
        contentBinding.conFirmed.text = today.confirmed.toCurrency()
        contentBinding.newConFirmed.text = newS(today.newConfirmed)

        contentBinding.hospitalized.text = today.hospitalized.toCurrency()
        contentBinding.newHospitalized.text =
            if (today.newHospitalized!! > 0) "( เพิ่มขึ้น ${today.newHospitalized.toCurrency()} )" else ""

        contentBinding.recovered.text = today.recovered.toCurrency()
        contentBinding.newRecovered.text = newS(today.newRecovered)

        contentBinding.deaths.text = today.deaths.toCurrency()
        contentBinding.newDeaths.text = newS(today.newDeaths)
        materialToolbar.subtitle = lastUpdate(today.updateDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cache = cache
    }

}