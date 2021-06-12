package com.github.pidsamhai.covid19thailand.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.github.pidsamhai.covid19thailand.databinding.SpinnerItemsBinding

class CustomSpinnerAdapter(context: Context, string: List<String>) :
    ArrayAdapter<String>(context, 0, string) {

    private val initText:String = "Please Select area ..."

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {

        return this.createView(position, parent)

    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {

        return this.createView(position, parent)

    }

    private fun createView(position: Int, parent: ViewGroup): View {

        val view = SpinnerItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        if(position == 0){
            view.text1.text = initText
        }else{
            view.text1.text = getItem(position )
        }
        return view.root
    }

}