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
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.CoVidDataSets
import com.github.pidsamhai.covid19thailand.network.response.toLineDataSet
import com.github.pidsamhai.covid19thailand.ui.viewmodel.TimeLineViewModel
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_time_line.*
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST")
class TimeLineFragment : Fragment() {

    private lateinit var materialToolbar: MaterialToolbar
    private val viewModel:TimeLineViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        materialToolbar =  (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.subtitle = ""

        viewModel.timeline.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.e("onActivityCreated: ", "Trigger Timeline")
                val a = it.data!!.toLineDataSet()
                Log.e("onActivityCreated: ", it.data.toString())
                setBarChart(a)
                setLineChart(a)
            }
        })

    }

    private fun setAnyChart() {
//        val cartesian:Cartesian = AnyChart.column()
//
//        val data:ArrayList<DataEntry>  = ArrayList()
//        data.add(ValueDataEntry("Rouge", 80540))
//        data.add(ValueDataEntry("Foundation", 94190))
//        data.add(ValueDataEntry("Mascara", 102610))
//        data.add(ValueDataEntry("Lip gloss", 110430))
//        data.add(ValueDataEntry("Lipstick", 128000))
//        data.add(ValueDataEntry("Nail polish", 143760))
//        data.add(ValueDataEntry("Eyebrow pencil", 170670))
//        data.add(ValueDataEntry("Eyeliner", 213210))
//
//        val column = cartesian.column(data)

//        column.tooltip()
//            .titleFormat("{%X}")
//            .position(Position.CENTER_BOTTOM)
//            .anchor(Anchor.CENTER_BOTTOM)
//            .offsetX(0.0)
//            .offsetY(5.0)
//            .format("\${%Value}{groupsSeparator: }")
//
//        cartesian.animation(true)
//        cartesian.title("Top 10 Cosmetic Products by Revenue")
//
//        cartesian.yScale().minimum(0.0)
//
//        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")
//
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
//        cartesian.interactivity().hoverMode(HoverMode.BY_X)
//
//        cartesian.xAxis(0).title("Product")
//        cartesian.yAxis(0).title("Revenue")
//        cartesian.credits().enabled(false)
//
//        anyChart.setChart(cartesian)

    }

    private fun setBarChart(sets:CoVidDataSets) {
        try {
            Log.e("setBarChart: ", "Update Data")
            val dataSets: ArrayList<IBarDataSet> = ArrayList()

            val colors = listOf(Color.YELLOW,Color.RED, Color.GREEN)
            val status = listOf("Confirmed","Death","Recovered")

            colors.forEachIndexed { index, el ->
                dataSets.add(BarDataSet((sets.toList() as List<ArrayList<BarEntry>>)[index], status[index]).apply {
//                    lineWidth = 0.5f
//                    setDrawCircleHole(false)
                    color = el
//                    circleRadius = 3f
                    valueTextSize = 5f
//                    setCircleColor(el)
                })
            }

            barChart.data = BarData(dataSets)
            barChart.xAxis.setLabelCount(sets.date.size,true)
//            barChart.xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(sets.date as ArrayList<String>)
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

            lineChart.setDrawGridBackground(true)
            lineChart.axisLeft.isEnabled = true
            lineChart.axisRight.isEnabled = true
            lineChart.description.isEnabled = true
//            barChart.xAxis.valueFormatter = object : ValueFormatter(){
//                override fun getFormattedValue(value: Float): String {
//                    return sets.date[value.toInt()]
//                }
//            }
//            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(sets.date)
            lineChart.invalidate()
        }catch (e:Exception){
            e.printStackTrace()
        }

//        val xAxis: XAxis = barChart.xAxis
//        xAxis.granularity = 1f
//        xAxis.isGranularityEnabled = true
//        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
////        xAxis.valueFormatter = vformatter
//
//        xAxis.setLabelCount(6,true)

    }

    private fun setLineChart(sets:CoVidDataSets) {
        try {
            Log.e("setBarChart: ", "Update Data")
            val dataSets: ArrayList<ILineDataSet> = ArrayList()

            val colors = listOf(Color.YELLOW,Color.RED, Color.GREEN)
            val status = listOf("Confirmed","Death","Recovered")

            colors.forEachIndexed { index, el ->
                dataSets.add(LineDataSet((sets.toList() as List<ArrayList<Entry>>)[index], status[index]).apply {
                    lineWidth = 0.5f
                    setDrawCircleHole(false)
                    color = el
                    circleRadius = 3f
                    valueTextSize = 5f
                    setCircleColor(el)
                })
            }
            lineChart.data = LineData(dataSets)
            lineChart.xAxis.setLabelCount(sets.date.size,true)
//            barChart.xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(sets.date as ArrayList<String>)
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

            lineChart.setDrawGridBackground(false)
            lineChart.axisLeft.isEnabled = false
            lineChart.axisRight.isEnabled = false
            lineChart.description.isEnabled = false
//            barChart.xAxis.valueFormatter = object : ValueFormatter(){
//                override fun getFormattedValue(value: Float): String {
//                    return sets.date[value.toInt()]
//                }
//            }
//            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(sets.date)
            lineChart.invalidate()
        }catch (e:Exception){
            e.printStackTrace()
        }

//        val xAxis: XAxis = barChart.xAxis
//        xAxis.granularity = 1f
//        xAxis.isGranularityEnabled = true
//        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
////        xAxis.valueFormatter = vformatter
//
//        xAxis.setLabelCount(6,true)

    }

}