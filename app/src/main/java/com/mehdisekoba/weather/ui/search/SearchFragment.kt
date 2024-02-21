package com.mehdisekoba.weather.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.data.model.ResponseSearch
import com.mehdisekoba.weather.databinding.FragmentSearchBinding
import com.mehdisekoba.weather.ui.MainActivity
import com.mehdisekoba.weather.ui.search.adapter.SearchAdapter
import com.mehdisekoba.weather.utils.base.BaseFragment
import com.mehdisekoba.weather.utils.extensions.addOnBackPressedCallback
import com.mehdisekoba.weather.utils.extensions.setupRecyclerview
import com.mehdisekoba.weather.utils.extensions.showSnackBar
import com.mehdisekoba.weather.utils.network.NetworkRequest
import com.mehdisekoba.weather.viewmodel.SearchCityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override val bindingInflater: (inflater: LayoutInflater) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate

    @Inject
    lateinit var searchAdapter: SearchAdapter

    // other
    private val viewModel by viewModels<SearchCityViewModel>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // InitViews
        binding.apply {
            emptyLay.isVisible = true
            if (isNetworkAvailable) {
                // Search
                searchEdt.addTextChangedListener { search ->
                    if (search.toString().length > 2) {
                        viewModel.callSearchApi(
                            search.toString(),
                        )
                    }
                    // Empty
                    if (search.toString().isEmpty()) {
                        emptyLay.isVisible = true
                        searchList.isVisible = false
                    }
                }
                // show data
                loadSearchData()
            } else {
                root.showSnackBar(getString(R.string.checkYourNetwork))
            }
        }
    }

    private fun loadSearchData() {
        binding.apply {
            viewModel.searchData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                    }

                    is NetworkRequest.Success -> {
                        response.data?.let { data ->
                            val dataList = listOf(data)
                            if (dataList.isNotEmpty()) {
                                emptyLay.isVisible = false
                                searchList.isVisible = true
                                initRecentRecycler(dataList)
                            } else {
                                emptyLay.isVisible = true
                                searchList.isVisible = false
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        root.showSnackBar(response.error.toString())
                    }
                }
            }
        }
    }

    private fun initRecentRecycler(data: List<ResponseSearch>) {
        searchAdapter.setData(data)
        binding.searchList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            searchAdapter,
        )
        searchAdapter.setOnItemClickListener { item ->
            val direction = SearchFragmentDirections.actionToInfo(item)
            findNavController().navigate(direction)
        }
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
