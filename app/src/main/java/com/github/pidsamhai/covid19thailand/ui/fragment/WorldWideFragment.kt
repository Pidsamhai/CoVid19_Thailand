package com.github.pidsamhai.covid19thailand.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Response
import com.github.pidsamhai.covid19thailand.ui.CustomSpinnerAdapter
import com.github.pidsamhai.covid19thailand.ui.viewmodel.WorldWideModel
import com.github.pidsamhai.covid19thailand.utilities.addEmptyFirst
import com.github.pidsamhai.covid19thailand.utilities.lastUpdate
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_world_wide.*
import kotlinx.android.synthetic.main.layout_static.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat

class WorldWideFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val viewModel: WorldWideModel by viewModel()
    private lateinit var _cacheCountries: List<String>
    private lateinit var materialToolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_world_wide, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
        materialToolbar.title = "Covid 19 Today"

        viewModel.countries.observe(viewLifecycleOwner, Observer {
            it?.let { countries ->
                _cacheCountries = countries.addEmptyFirst()
                country.adapter = activity?.applicationContext?.let { context ->
                    CustomSpinnerAdapter(context, _cacheCountries)
                }
            }
        })

        refresh.setOnClickListener {
            viewModel.initData()
        }

        country.onItemSelectedListener = this

    }

    private fun updateUI(today: Response) {
        val nbf = NumberFormat.getInstance()
        Log.e("updateUI: ", "YEAH!!!!!!!!!")
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


    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != 0)
            R.layout.support_simple_spinner_dropdown_item
            _cacheCountries[position].let {
                Log.e("onItemSelected: ", it)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.getStatic(it).observe(viewLifecycleOwner, Observer { static ->
                        static?.response?.let { updateUI(static.response[0]) }
                    })
                }
            }
    }

}
