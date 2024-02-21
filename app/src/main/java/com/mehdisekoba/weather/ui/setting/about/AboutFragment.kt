package com.mehdisekoba.weather.ui.setting.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import coil.load
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.databinding.FragmentAboutBinding
import com.mehdisekoba.weather.utils.ABOUT_URL_IMAGE
import com.mehdisekoba.weather.utils.base.BaseBottomSheetFragment
import com.mehdisekoba.weather.utils.extensions.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : BaseBottomSheetFragment<FragmentAboutBinding>() {
    override val bindingInflater: (inflater: LayoutInflater) -> FragmentAboutBinding
        get() = FragmentAboutBinding::inflate

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // initView
        binding.apply {
            // load avatar developer
            if (isNetworkAvailable) {
                developerAvatar.loadImage(ABOUT_URL_IMAGE)
            } else {
                developerAvatar.load(R.drawable.placeholder)
            }
            // about developer
            nameDeveloper.text = requireContext().getText(R.string.name_developer)
            // about
            titleAbout.text = requireContext().getText(R.string.title_about_p)
            // description
            descProject.text = requireContext().getText(R.string.description_about_p)
        }
    }
}
