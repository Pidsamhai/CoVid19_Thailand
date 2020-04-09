package com.github.pidsamhai.covid19thailand.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.github.pidsamhai.covid19thailand.R
import kotlinx.android.synthetic.main.spinner_items.view.*

class CustomSpinnerAdapter(context: Context, string: List<String>) :
    ArrayAdapter<String>(context, 0, string) {

    private val initText:String = "Please Select area ..."

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {

        return this.createView(position, recycledView, parent)

    }


    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {

        return this.createView(position, recycledView, parent)

    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {

        val view = recycledView ?: LayoutInflater.from(context).inflate(

            R.layout.spinner_items,

            parent,

            false

        )

        if(position == 0){
            view.text1.text = initText
        }else{
            view.text1.text = getItem(position )
        }
        return view
    }

}