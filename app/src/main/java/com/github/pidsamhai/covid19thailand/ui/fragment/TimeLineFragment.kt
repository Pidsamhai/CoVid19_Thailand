package com.github.pidsamhai.covid19thailand.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.CoVidDataSets
import com.github.pidsamhai.covid19thailand.network.response.toLineDataSet
import com.github.pidsamhai.covid19thailand.ui.viewmodel.TimeLineViewModel
import com.github.pidsamhai.covid19thailand.utilities.StringForMetter
import com.github.pidsamhai.covid19thailand.utilities.lastUpdate
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_time_line.*
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST")
class TimeLineFragment : Fragment() {

    private lateinit var materialToolbar: MaterialToolbar
    private val viewModel: TimeLineViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.title = "Covid 19 TimeLine"

        viewModel.timeline.observe(viewLifecycleOwner, Observer {
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
        try {
//            Log.e("setBarChart: ", "Update Data")
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
                        valueFormatter = object : ValueFormatter(){
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                        setCircleColor(el)
                    })
            }
            lineChart.data = LineData(dataSets)
            lineChart.xAxis.setLabelCount(sets.date.size,true)
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