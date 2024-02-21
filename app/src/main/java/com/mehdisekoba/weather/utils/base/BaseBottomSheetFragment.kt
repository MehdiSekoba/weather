package com.mehdisekoba.weather.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.utils.network.NetworkChecker
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("ktlint:standard:property-naming")
abstract class BaseBottomSheetFragment<T : ViewBinding> : BottomSheetDialogFragment() {
    // Binding
    protected abstract val bindingInflater: (inflater: LayoutInflater) -> T

    private var _binding: T? = null
    protected val binding: T get() = requireNotNull(_binding)

    @Inject
    lateinit var networkChecker: NetworkChecker

    // Theme
    override fun getTheme() = R.style.RemoveDialogBackground

    // Other
    var isNetworkAvailable = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // Check network
        lifecycleScope.launch {
            networkChecker.checkNetwork().collect {
                isNetworkAvailable = it
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setBackgroundDrawableResource(R.color.backShadow)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
