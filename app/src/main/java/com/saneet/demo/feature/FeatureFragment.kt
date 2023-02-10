package com.saneet.demo.feature

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.saneet.demo.DemoApplication
import com.saneet.demo.PickSinglePhotoContract
import com.saneet.demo.R
import com.saneet.demo.ViewModelFactory
import kotlinx.coroutines.launch
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

    private val singlePhotoPickerLauncher =
        registerForActivityResult(PickSinglePhotoContract()) { imageUri: Uri? ->
            imageUri?.let { viewModel.setImageUri(requireContext(), it) }
        }


    var asked: Boolean = false

    private fun pickPhoto() = singlePhotoPickerLauncher.launch(null)

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

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            viewModel.viewState.collect {
                setImage(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!asked) {
            pickPhoto()
            asked = true
        }

    }

    private fun setImage(viewState: FeatureViewModel.ViewState) {
        viewState.imageBitmap?.let {
            requireView().findViewById<ImageView>(R.id.image)
                .setImageBitmap(it)
        }
    }
}