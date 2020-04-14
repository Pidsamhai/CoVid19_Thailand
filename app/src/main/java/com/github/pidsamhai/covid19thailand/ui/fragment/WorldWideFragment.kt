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
import com.github.pidsamhai.covid19thailand.network.response.ddc.CoVidDataSets
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.toLineDataSet
import com.github.pidsamhai.covid19thailand.ui.adapter.CustomSpinnerAdapter
import com.github.pidsamhai.covid19thailand.ui.viewmodel.WorldWideModel
import com.github.pidsamhai.covid19thailand.utilities.StringForMetter
import com.github.pidsamhai.covid19thailand.utilities.addEmptyFirst
import com.github.pidsamhai.covid19thailand.utilities.lastUpdate
import com.github.pidsamhai.covid19thailand.utilities.toDatas
import com.github.pidsamhai.gitrelease.GitRelease
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_world_wide.*
import kotlinx.android.synthetic.main.layout_static.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.text.NumberFormat


class WorldWideFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val viewModel: WorldWideModel by sharedViewModel()
    private lateinit var materialToolbar: MaterialToolbar
    private var _cacheCountries: List<String>? = null
    private var _cachePosition: Int = 0
    private var _cacheDatas: Datas? = null
    private lateinit var adapter: CustomSpinnerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_world_wide, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.title = resources.getString(R.string.covid_world_wide)
        materialToolbar.subtitle = ""
        adapter = CustomSpinnerAdapter(activity as AppCompatActivity, mutableListOf())
        country.adapter = adapter

        initObserve()

        if (viewModel.cacheCountries != null && viewModel.cacheDatas != null && viewModel.cachePosition != null) {
            Timber.e("CACHE : DETECTED")
            updateUI(viewModel.cacheDatas!!.toDatas())
            _cacheCountries = viewModel.cacheCountries
            _cacheDatas = viewModel.cacheDatas?.toDatas()
            _cachePosition = viewModel.cachePosition!!
            updateSpinner()
            country.setSelection(_cachePosition)
        } else {
            viewModel.refresh()
        }

        refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        country.onItemSelectedListener = this

    }

    private fun initObserve() {
        Timber.e("INIT OBSERVE")

        viewModel.countries.observe(viewLifecycleOwner, Observer {
            it?.let { countries ->
                refreshLayout.isRefreshing = false
                _cacheCountries = countries.addEmptyFirst()
                updateSpinner()
            }
        })
    }

    private fun updateUI(today: Datas) {
        val nbf = NumberFormat.getInstance()
        Timber.e("YEAH!!!!!!!!!")
        conFirmed.text = nbf.format(today.cases?.total)
        newConFirmed.text = today.cases?.new

        hospitalized.text = nbf.format(today.cases?.active)
        newHospitalized.text = ""

        recovered.text = nbf.format(today.cases?.recovered)
        newRecovered.text = ""

        deaths.text = nbf.format(today.deaths?.total)
        newDeaths.text = today.deaths?.new
        materialToolbar.subtitle = today.day?.let { lastUpdate(it) }
    }

    private fun updateSpinner() {
        adapter.clear()
        adapter.addAll(_cacheCountries!!)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != 0 && _cacheCountries != null) {
            graphLoading.visibility = View.VISIBLE
            _cachePosition = position
            _cacheCountries!![position].let { it ->
                Timber.e(it)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.getStatic(it).observe(viewLifecycleOwner, Observer { data ->
                        data?.let {
                            _cacheDatas = data
                            updateUI(data)
                            Timber.e(data.toString())
                        }
                    })
                    val his = viewModel.getHistory(it)
                    his.observe(viewLifecycleOwner, object : Observer<List<History>> {
                        override fun onChanged(t: List<History>?) {
                            if (t != null && t.size > 1){
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
        lineChart.fitScreen()
        lineChart.data?.clearValues()
        lineChart.xAxis.valueFormatter = null
        lineChart.notifyDataSetChanged()
        lineChart.clear()
        lineChart.invalidate()
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
            lineChart.data = LineData(dataSets)
            lineChart.xAxis.setLabelCount(sets.date.size, true)
            lineChart.xAxis.setDrawLabels(true)
            lineChart.xAxis.valueFormatter = StringForMetter(sets.date as ArrayList<String>)
            lineChart.xAxis.labelRotationAngle = -70f
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

            lineChart.setDrawGridBackground(false)
            lineChart.axisLeft.isEnabled = true
            lineChart.axisRight.isEnabled = false
            lineChart.description.isEnabled = false
            lineChart.invalidate()
            graphLoading.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
