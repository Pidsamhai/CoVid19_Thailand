package com.github.pidsamhai.covid19thailand.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.db.CovidDatabase
import com.github.pidsamhai.covid19thailand.db.TodayDao
import com.github.pidsamhai.covid19thailand.network.api.Covid19ApiServices
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_today.*


@Suppress("CAST_NEVER_SUCCEEDS")
class TodayFragment : Fragment() {

    private lateinit var viewModel: ToDayViewModel
    private lateinit var coVidRepository: CoVidRepository
    private lateinit var todayDao: TodayDao
    private lateinit var apiServices: Covid19ApiServices
    private lateinit var materialToolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        materialToolbar =  (activity as AppCompatActivity).findViewById(R.id.materialToolbar)

        apiServices = Covid19ApiServices.invoke()
        context?.let {
            todayDao = CovidDatabase.getInstance(it).todayDao()
            coVidRepository = CoVidRepository.getInstance(todayDao, apiServices)
            viewModel = ViewModelProvider(
                this,
                ToDayViewModelFactory(coVidRepository)
            ).get(ToDayViewModel::class.java)
            viewModel.today.observe(viewLifecycleOwner, Observer {
                it.let {

                    materialToolbar.subtitle = lastUpdate(it.updateDate)
                    conFirmed.text = it.confirmed.toString()
                    newConFirmed.text = newS(it.newConfirmed)

                    hospitalized.text = it.hospitalized.toString()
                    newHospitalized.text =  if (it.newHospitalized!! > 0) "( เพิ่มขึ้น ${it.newHospitalized} )" else ""

                    recovered.text = it.recovered.toString()
                    newRecovered.text = newS(it.newRecovered)
                }
            })
        }
    }

    private fun newS(data: Int?): String {
        return "( เพิ่มขึ้น $data )"
    }

    private fun lastUpdate(data: String) : String{
        return "( อัพเดทล่าสุด $data)"
    }
}