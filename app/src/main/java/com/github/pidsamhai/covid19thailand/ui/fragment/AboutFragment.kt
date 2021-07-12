package com.github.pidsamhai.covid19thailand.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.databinding.FragmentAboutBinding
import com.github.pidsamhai.covid19thailand.db.CoVid19Database
import com.github.pidsamhai.covid19thailand.ui.fragment.scope.ScopeFragment
import com.github.pidsamhai.gitrelease.GitRelease
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AboutFragment : ScopeFragment(){

    private lateinit var materialToolbar: MaterialToolbar
    private val gitRelease: GitRelease by inject { parametersOf(requireActivity()) }
    private val coVid19Database:CoVid19Database by inject()
    private lateinit var _binding: FragmentAboutBinding
    private val binding: FragmentAboutBinding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.btnCheckUpdate.setOnClickListener {
            gitRelease.checkNewVersion()
        }
        binding.btnCLeanDatabase.setOnClickListener {
            launch(Dispatchers.IO) {
                coVid19Database.clearAllTables()
                showToast()
            }
        }
//        materialToolbar = (activity as AppCompatActivity).findViewById(R.id.materialToolbar)
//        materialToolbar.apply {
//            title = "About"
//            subtitle = "   "
//        }
    }

    private fun showToast() = launch {
        Toast.makeText(requireContext(),"Cleared",Toast.LENGTH_SHORT).show()
    }
}