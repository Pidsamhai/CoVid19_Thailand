package com.github.pidsamhai.covid19thailand.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.databinding.FragmentWorldWideBinding
import com.github.pidsamhai.covid19thailand.databinding.LayoutStaticBinding
import com.github.pidsamhai.covid19thailand.network.response.ddc.CoVidDataSets
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.toLineDataSet
import com.github.pidsamhai.covid19thailand.ui.adapter.CustomSpinnerAdapter
import com.github.pidsamhai.covid19thailand.ui.viewmodel.WorldWideModel
import com.github.pidsamhai.covid19thailand.utilities.*
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class WorldWideFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val viewModel: WorldWideModel by sharedViewModel()
    private lateinit var materialToolbar: MaterialToolbar
    private var _cacheCountries: List<String>? = null
    private var _cachePosition: Int = 0
    private var _cacheDatas: Datas? = null
    private lateinit var adapter: CustomSpinnerAdapter
    private lateinit var _binding: FragmentWorldWideBinding
    private lateinit var _contentBinding: LayoutStaticBinding
    private val binding: FragmentWorldWideBinding
        get() = _binding
    private val contentBinding: LayoutStaticBinding
        get() = _contentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorldWideBinding.inflate(inflater, container, false)
        _contentBinding = LayoutStaticBinding.bind(_binding.root)
        return _binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.title = resources.getString(R.string.covid_world_wide)
        materialToolbar.subtitle = ""
        adapter = CustomSpinnerAdapter(activity as AppCompatActivity, mutableListOf())
        binding.country.adapter = adapter

        initObserve()

        if (viewModel.cacheCountries != null && viewModel.cacheDatas != null && viewModel.cachePosition != null) {
            Timber.e("CACHE : DETECTED")
            updateUI(viewModel.cacheDatas!!.toDatas())
            _cacheCountries = viewModel.cacheCountries
            _cacheDatas = viewModel.cacheDatas?.toDatas()
            _cachePosition = viewModel.cachePosition!!
            updateSpinner()
            binding.country.setSelection(_cachePosition)
        } else {
            viewModel.refresh()
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.country.onItemSelectedListener = this
    }

    private fun initObserve() {
        Timber.e("INIT OBSERVE")

        viewModel.countries.observe(viewLifecycleOwner, {
            it?.let { countries ->
                binding.refreshLayout.isRefreshing = false
                _cacheCountries = countries.addEmptyFirst()
                updateSpinner()
            }
        })
    }

    private fun updateUI(today: Datas) {
        Timber.e("YEAH!!!!!!!!!")
        contentBinding.conFirmed.text = today.cases?.total.toCurrency()
        contentBinding.newConFirmed.text = today.cases?.new

        contentBinding.hospitalized.text = today.cases?.active.toCurrency()
        contentBinding.newHospitalized.text = ""

        contentBinding.recovered.text = today.cases?.recovered.toCurrency()
        contentBinding.newRecovered.text = ""

        contentBinding.deaths.text = today.deaths?.total.toCurrency()
        contentBinding.newDeaths.text = today.deaths?.new
        materialToolbar.subtitle = today.day?.let { lastUpdate(it) }
    }

    private fun updateSpinner() {
        adapter.clear()
        adapter.addAll(_cacheCountries!!)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != 0 && _cacheCountries != null) {
            binding.graphLoading.visibility = View.VISIBLE
            _cachePosition = position
            _cacheCountries!![position].let { it ->
                Timber.e(it)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.getStatic(it).observe(viewLifecycleOwner, { data ->
                        data?.let {
                            _cacheDatas = data
                            updateUI(data)
                            Timber.e(data.toString())
                        }
                    })
                    val his = viewModel.getHistory(it)
                    his.observe(viewLifecycleOwner, object : Observer<List<History>> {
                        override fun onChanged(t: List<History>?) {
                            if (!t.isNullOrEmpty()) {
                                resetChart()
                                setLineChart(t.toLineDataSet())
                                his.removeObserver(this)
                            }
                            Timber.e("ObServe Histry %s", t.toString())
                        }
                    })
                }
            }
        }
    }

    private fun resetChart() {
        binding.lineChart.fitScreen()
        if (binding.lineChart.data != null)
            binding.lineChart.data.clearValues()
        binding.lineChart.xAxis.valueFormatter = null
        binding.lineChart.notifyDataSetChanged()
        binding.lineChart.clear()
        binding.lineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.apply {
            cachePosition = _cachePosition
            cacheDatas = _cacheDatas?.toGsonString()
            cacheCountries = _cacheCountries
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setLineChart(sets: CoVidDataSets) {
        try {
            Timber.e("Update Data")
            val dataSets: ArrayList<ILineDataSet> = ArrayList()

            val colors = listOf(Color.YELLOW, Color.RED, Color.GREEN)
            val status = listOf("Confirmed", "Death", "Recovered")

            colors.forEachIndexed { index, el ->
                dataSets.add(
                    LineDataSet(
                        (sets.toList() as List<ArrayList<Entry>>)[index],
                        status[index]
                    ).apply {
                        lineWidth = 2f
                        color = el
                        setDrawCircles(false)
                        valueTextSize = 9f
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                        setCircleColor(el)
                    })
            }
            binding.lineChart.data = LineData(dataSets)
            binding.lineChart.xAxis.setLabelCount(sets.date.size, true)
            binding.lineChart.xAxis.setDrawLabels(true)
            binding.lineChart.xAxis.valueFormatter = StringForMetter(sets.date as ArrayList<String>)
            binding.lineChart.xAxis.labelRotationAngle = -70f
            binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

            binding.lineChart.setDrawGridBackground(false)
            binding.lineChart.axisLeft.isEnabled = true
            binding.lineChart.axisRight.isEnabled = false
            binding.lineChart.description.isEnabled = false
            binding.lineChart.invalidate()
            binding.graphLoading.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
