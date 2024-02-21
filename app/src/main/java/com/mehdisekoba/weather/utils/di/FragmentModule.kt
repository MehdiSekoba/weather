package com.mehdisekoba.weather.utils.di

import androidx.fragment.app.Fragment
import com.mehdisekoba.weather.ui.location.adapters.PagerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {
    @Provides
    fun provideFragment(fragment: Fragment) = PagerAdapter(fragment.parentFragmentManager, fragment.lifecycle)
}
