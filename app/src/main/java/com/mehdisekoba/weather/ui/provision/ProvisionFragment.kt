package com.mehdisekoba.weather.ui.provision

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.data.stored.DataStorePreference
import com.mehdisekoba.weather.databinding.FragmentProvisionBinding
import com.mehdisekoba.weather.databinding.LayoutPrivacyBinding
import com.mehdisekoba.weather.utils.base.BaseFragment
import com.mehdisekoba.weather.utils.extensions.isLocationEnabled
import com.mehdisekoba.weather.utils.extensions.openLocationSettings
import com.mehdisekoba.weather.utils.extensions.permissionsRequester
import com.mehdisekoba.weather.utils.extensions.setClickableSpan
import com.mehdisekoba.weather.utils.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProvisionFragment : BaseFragment<FragmentProvisionBinding>() {
    override val bindingInflater: (inflater: LayoutInflater) -> FragmentProvisionBinding
        get() = FragmentProvisionBinding::inflate

    @Inject
    lateinit var dataStore: DataStorePreference

    // other
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding.btnAccept.setOnClickListener {
            requestPermission()
        }
    }

    /**
     * Requests location permissions and handles the result.
     */
    private fun requestPermission() {
        viewLifecycleOwner.lifecycleScope.launch {
            permissionsRequester.requestPermissions(
                viewLifecycleOwner,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            ) { permissionResult ->
                if (permissionResult.isGranted) {
                    if (requireContext().isLocationEnabled()) {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION,
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            val result =
                                mFusedLocationClient.getCurrentLocation(
                                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                                    CancellationTokenSource().token,
                                )
                            result.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    handleSuccessfulLocationUpdate()
                                }
                            }
                        } else {
                            // Permission not granted, handle it appropriately
                        }
                    } else {
                        requireContext().openLocationSettings()
                    }
                }
            }
        }
    }

    /**
     * Initialize the views and set up click listeners.
     */
    private fun initViews() {
        binding.apply {
            bgImageview.load(R.drawable.bg_location) {
                crossfade(false)
            }
            // set textview onclick
            val spannableString = SpannableString(getString(R.string.txt_Rules))
            spannableString.setClickableSpan(160, spannableString.length, onClickAction = {
                privacyDialog()
            })
            txtTitle.apply {
                text = spannableString
                movementMethod = LinkMovementMethod.getInstance()
                highlightColor = Color.TRANSPARENT
            }
        }
    }

    private fun handleSuccessfulLocationUpdate() {
        lifecycleScope.launch {
            dataStore.saveSession(true)
            findNavController().popBackStack(R.id.provisionFragment, true)
            val direction = ProvisionFragmentDirections.actionProvisionToLocation()
            findNavController().navigate(direction)
        }
    }

    /**
     * Displays a custom privacy dialog.
     */
    private fun privacyDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val customDialogView = layoutInflater.inflate(R.layout.layout_privacy, null)
        val bindingDialog = LayoutPrivacyBinding.bind(customDialogView)
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setView(bindingDialog.root)
            .setCancelable(false)
        val dialog = builder.create()

        bindingDialog.apply {
            txtTitle.text = getString(R.string.title_privacy)
            txtDesc.text = getString(R.string.txt_Privacy)
            txtAccept.text = getString(R.string.accepting_rules)
            btnAccept.text = getString(R.string.txt_accept)
            btnAccept.setOnClickListener {
                if (checkbox.isChecked) {
                    dialog.dismiss()
                } else {
                    root.showSnackBar(getString(R.string.reception))
                }
            }
        }
        dialog.show()
    }
}
