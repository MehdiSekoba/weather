package com.mehdisekoba.weather.ui.setting

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.data.stored.DataStorePreference
import com.mehdisekoba.weather.databinding.FragmentSettingBinding
import com.mehdisekoba.weather.databinding.RateDialogBinding
import com.mehdisekoba.weather.ui.MainActivity
import com.mehdisekoba.weather.utils.base.BaseFragment
import com.mehdisekoba.weather.utils.extensions.addOnBackPressedCallback
import com.mehdisekoba.weather.utils.extensions.alert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override val bindingInflater: (inflater: LayoutInflater) -> FragmentSettingBinding
        get() = FragmentSettingBinding::inflate

    // other
    private var selectedLanguageIndex = 0
    private var selectedUnitIndex = 0

    @Inject
    lateinit var dataStore: DataStorePreference

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // initViews
        binding.apply {
            // arrow back
            arrowBack.setOnClickListener {
                findNavController().navigate(R.id.locationFragment)
            }
            // get data
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    dataStore.getChoseLanguageItem.collect {
                        selectedLanguageIndex = it
                    }
                }
            }
            // set language
            txtLanguage.setOnClickListener {
                val languages = requireContext().resources.getStringArray(R.array.languages_array)
                requireContext().alert {
                    setTitle(requireContext().getString(R.string.language_selection))
                    setSingleChoiceItems(languages, selectedLanguageIndex) { dialog, which ->
                        selectedLanguageIndex = which // Store selected language index
                        when (which) {
                            0 -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    requireContext().getSystemService(LocaleManager::class.java)
                                        .applicationLocales = LocaleList.forLanguageTags("en")
                                } else {
                                    AppCompatDelegate.setApplicationLocales(
                                        LocaleListCompat.forLanguageTags(
                                            "en",
                                        ),
                                    )
                                }
                            }

                            1 -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    requireContext().getSystemService(LocaleManager::class.java)
                                        .applicationLocales = LocaleList.forLanguageTags("fa")
                                } else {
                                    AppCompatDelegate.setApplicationLocales(
                                        LocaleListCompat.forLanguageTags(
                                            "fa",
                                        ),
                                    )
                                }
                            }
                        }
                        lifecycleScope.launch {
                            dataStore.saveChoseItem(selectedLanguageIndex)
                        }
                        dialog.dismiss()
                    }
                }
            }
            // unit weather
            txtUnit.setOnClickListener {
                val unit = requireContext().resources.getStringArray(R.array.unit_array)

                requireContext().alert {
                    setTitle(requireContext().getString(R.string.unit_preferences))
                        .setSingleChoiceItems(unit, selectedUnitIndex) { dialog, which ->
                            selectedUnitIndex = which
                            dialog.dismiss()
                        }
                }
            }
            // rating app
            txtRating.setOnClickListener {
                customRatingDialog()
            }
            // about me
            txtAbout.setOnClickListener {
                val direction = SettingFragmentDirections.actionToAboutFragment()
                findNavController().navigate(direction)
            }
        }
    }

    private fun customRatingDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val customRatingView = layoutInflater.inflate(R.layout.rate_dialog, null)
        val bindingDialog = RateDialogBinding.bind(customRatingView)
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setView(bindingDialog.root)
            .setCancelable(false)
        val dialog = builder.create()
        bindingDialog.apply {
            // Set the default image
            ratingImg.load(R.drawable.five_star)

            // rating
            appRating.setOnRatingBarChangeListener { _, rating, _ ->
                val drawableId =
                    when {
                        rating <= 1 -> R.drawable.one_star
                        rating <= 2 -> R.drawable.two_star
                        rating <= 3 -> R.drawable.three_star
                        rating <= 4 -> R.drawable.four_star
                        else -> R.drawable.five_star
                    }
                ratingImg.load(drawableId)
                animateImage(ratingImg)
            }
            // positive
            rateNow.setOnClickListener { dialog.dismiss() }
            // negative
            mayBeLater.setOnClickListener { dialog.dismiss() }
        }
        dialog.show()
    }

    private fun animateImage(ratingImageView: ImageView) {
        val scaleAnimation =
            ScaleAnimation(
                0F,
                1f,
                0F,
                1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
            )
        scaleAnimation.fillAfter = true
        scaleAnimation.duration = 200
        ratingImageView.startAnimation(scaleAnimation)
    }

    override fun onResume() {
        super.onResume()
        addOnBackPressedCallback {
            findNavController().navigate(R.id.locationFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
    }
}
