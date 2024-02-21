@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.mehdisekoba.weather.ui.splash

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehdisekoba.weather.BuildConfig
import com.mehdisekoba.weather.R
import com.mehdisekoba.weather.data.stored.DataStorePreference
import com.mehdisekoba.weather.databinding.DisconnectInternetBinding
import com.mehdisekoba.weather.databinding.FragmentSplashBinding
import com.mehdisekoba.weather.utils.base.BaseFragment
import com.mehdisekoba.weather.utils.extensions.openSettingDataMobile
import com.mehdisekoba.weather.utils.extensions.openSettingWifi
import com.mehdisekoba.weather.utils.network.MyReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@Suppress("DEPRECATION")
class SplashFragment :
    BaseFragment<FragmentSplashBinding>(),
    MyReceiver.ConnectivityReceiverListener {
    override val bindingInflater: (inflater: LayoutInflater) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    @Inject
    lateinit var dataStore: DataStorePreference

    // other
    private val myReceiver = MyReceiver()

    private var dialog: AlertDialog? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().registerReceiver(
            myReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION),
        )
    }

    // Shows the dialog for internet disconnection
    private fun showDialog() {
        if (dialog == null || !dialog!!.isShowing) {
            val layoutInflater = LayoutInflater.from(requireContext())
            val customDialogView = layoutInflater.inflate(R.layout.disconnect_internet, null)
            val bindingDialog = DisconnectInternetBinding.bind(customDialogView)
            dialog =
                MaterialAlertDialogBuilder(requireContext())
                    .setView(bindingDialog.root)
                    .setCancelable(false)
                    .create()

            bindingDialog.apply {
                turnData.setOnClickListener { requireContext().openSettingDataMobile() }
                turnWifi.setOnClickListener { requireContext().openSettingWifi() }
                imgClose.setOnClickListener { dialog?.dismiss() }
            }

            dialog?.show()
        }
    }

    // Dismisses the dialog
    private fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }

    // Updates the connection UI based on the provided flag
    private fun updateViewConnection(isShown: Boolean) {
        binding.apply {
            txtVersion.isVisible = isShown
            txtAppName.isVisible = isShown
            DotProgressBar.isVisible = isShown
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onNetworkConnectionChanger(isConnected: Boolean) {
        binding.apply {
            if (isConnected) {
                dismissDialog()
                lifecycleScope.launch {
                    updateViewConnection(true)
                    txtAppName.text = getString(R.string.app_name)
                    txtVersion.text = "${getString(R.string.version)} : ${BuildConfig.VERSION_NAME}"
                    // check
                    checkSession()
                }
            } else {
                showDialog()
                updateViewConnection(false)
            }
        }
    }

    /**
     * Checks the session status before navigating to the appropriate destination.
     * If the session is active, navigates to the location fragment,
     * otherwise navigates to the provision fragment.
     */
    private fun checkSession() {
        lifecycleScope.launch {
            val session = dataStore.getSession.first()
            findNavController().popBackStack(R.id.splashFragment, true)
            if (session == false) {
                delay(3000)
                findNavController().navigate(R.id.actionSplashToProvision)
            } else {
                delay(3000)
                findNavController().navigate(R.id.actionProvisionToLocation)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        MyReceiver.connectivityReceiverListener = this
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(myReceiver)
    }
}
