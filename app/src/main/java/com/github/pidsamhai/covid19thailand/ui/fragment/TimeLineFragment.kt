package com.github.pidsamhai.covid19thailand.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.github.pidsamhai.covid19thailand.databinding.FragmentTimeLineBinding
import com.github.pidsamhai.covid19thailand.network.response.ddc.CoVidDataSets
import com.github.pidsamhai.covid19thailand.network.response.ddc.toLineDataSet
import com.github.pidsamhai.covid19thailand.ui.viewmodel.TimeLineViewModel
import com.github.pidsamhai.covid19thailand.utilities.StringForMetter
import com.github.pidsamhai.covid19thailand.utilities.lastUpdate
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


@Suppress("UNCHECKED_CAST")
class TimeLineFragment : Fragment() {

    private lateinit var materialToolbar: MaterialToolbar
    private val viewModel: TimeLineViewModel by viewModel()
    private lateinit var _binding: FragmentTimeLineBinding
    private val binding: FragmentTimeLineBinding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeLineBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.title = resources.getString(R.string.covid_timeline)

        viewModel.timeline.observe(viewLifecycleOwner, {
            it?.let {
                materialToolbar.subtitle = lastUpdate(it.updateDate)
            }
        })

        viewModel.datas.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty())
                    return@Observer
                val a = it.reversed().toLineDataSet()
                setLineChart(a)
            }
        })
    }


    private fun setLineChart(sets: CoVidDataSets) {
        val lineChart = binding.lineChart
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}