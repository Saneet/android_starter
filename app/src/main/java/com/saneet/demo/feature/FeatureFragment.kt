package com.saneet.demo.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saneet.demo.DemoApplication
import com.saneet.demo.R
import com.saneet.demo.ViewModelFactory
import javax.inject.Inject

class FeatureFragment : Fragment() {

    companion object {
        fun newInstance() = FeatureFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<FeatureViewModel>

    private val viewModel: FeatureViewModel by lazy {
        viewModelFactory.get<FeatureViewModel>(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_feature, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as DemoApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }
}