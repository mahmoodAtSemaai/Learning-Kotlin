package com.webkul.mobikul.odoo.ui.signUpOnboarding.fragment

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.request.UserDetailsRequest
import com.webkul.mobikul.odoo.databinding.FragmentUserDetailsBinding
import com.webkul.mobikul.odoo.databinding.ItemDialogDiscardChangesBinding
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.UserDetailsEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.UserDetailsIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.UserOnboardingIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.UserDetailsState
import com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel.UserDetailsViewModel
import com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel.UserOnboardingViewModel
import com.webkul.mobikul.odoo.utils.CameraUtils
import com.webkul.mobikul.odoo.utils.CameraV1Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class UserDetailsFragment @Inject constructor() :
    BindingBaseFragment<FragmentUserDetailsBinding>(),
    IView<UserDetailsIntent, UserDetailsState, UserDetailsEffect> {

    override val layoutId: Int = R.layout.fragment_user_details
    private val viewModel: UserDetailsViewModel by viewModels()
    lateinit var userOnboardingViewModel : UserOnboardingViewModel
    private val cameraUtils = CameraUtils()
    private val cameraV1Utils = CameraV1Utils()

    private lateinit var dialog: Dialog
    private lateinit var dialogBinding: ItemDialogDiscardChangesBinding

    companion object {
        fun newInstance() = UserDetailsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setObservers()
        setListners()

        getUserIdCustomerId()
        setSharedViewModel()
    }

    private fun setViews(){
        dialog = Dialog(requireContext())
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.item_dialog_discard_changes,
            null,
            false
        )
        dialogBinding.tvLocation.text = requireActivity().applicationContext.getString(R.string.exit_the_app)
        dialogBinding.btnKeepEditing.text = requireActivity().applicationContext.getString(R.string.no)
        dialogBinding.btnDiscard.text = requireActivity().applicationContext.getString(R.string.yes)
    }

    private fun setSharedViewModel() {
        activity?.let {
            userOnboardingViewModel = ViewModelProviders.of(it).get(UserOnboardingViewModel::class.java)
        }
    }

    private fun getUserIdCustomerId() {
        triggerIntent(UserDetailsIntent.GetUserIdCustomerId)
    }

    private fun setListners() {
        binding.etGroupName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                triggerIntent(
                    UserDetailsIntent.VerifyFields(
                        binding.etName.text.toString(),
                        s.toString()
                    )
                )
            }
        })

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                triggerIntent(
                    UserDetailsIntent.VerifyFields(
                        s.toString(),
                        binding.etGroupName.text.toString()
                    )
                )
            }
        })

        binding.etReferralCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                triggerIntent(UserDetailsIntent.VerifyReferralCode(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                triggerIntent(UserDetailsIntent.HideInvalidReferralCode)
            }
        })

        binding.btnContinue.setOnClickListener {
            triggerIntent(
                UserDetailsIntent.UserDetails(
                    UserDetailsRequest(
                        name = binding.etName.text.toString(),
                        groupName = binding.etGroupName.text.toString(),
                        referralCode = binding.etReferralCode.text.toString()
                    )
                )
            )
        }

        binding.ivPicture.setOnClickListener {
            triggerIntent(UserDetailsIntent.SetUserPicture)
        }

        binding.ivEditPicture.setOnClickListener {
            triggerIntent(UserDetailsIntent.SetUserPicture)
        }

        dialogBinding.btnDiscard.setOnClickListener {
            triggerIntent(UserDetailsIntent.CloseApp)
        }

        dialogBinding.btnKeepEditing.setOnClickListener {
            dialog.dismiss()
        }

        handleBack()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launch {
            viewModel.effect.collect {
                render(it)
            }
        }
    }

    override fun render(state: UserDetailsState) {
        when (state) {
            is UserDetailsState.Idle -> dismissProgressDialog()
            is UserDetailsState.Loading -> showProgressDialog()
            is UserDetailsState.Error -> {
                dismissProgressDialog()
                if(state.failureStatus == FailureStatus.NO_INTERNET){
                    noInternetState()
                }else{
                    showErrorState(state.failureStatus, state.message)
                }
            }
            is UserDetailsState.SetUpViews -> setUpViews(state.viewDetails)
            is UserDetailsState.ValidReferralCode -> {
                dismissProgressDialog()
            }
            is UserDetailsState.InvalidReferralCode -> {
                dismissProgressDialog()
                showInvalidReferralCode()
            }
            is UserDetailsState.EnableContinue -> enableContinueBtn(true)
            is UserDetailsState.DisableContinue -> enableContinueBtn(false)
            is UserDetailsState.CompletedUserDetails -> {
                dismissProgressDialog()
                setCompleted()
            }
            is UserDetailsState.FetchViews -> {
                dismissProgressDialog()
                fetchViews()
            }
            is UserDetailsState.UserPicture -> {
                openCameraInterface()
            }
            is UserDetailsState.CloseApp -> closeApp()
        }
    }

    private fun closeApp() {
        dialog.dismiss()
        requireActivity().finish()
    }

    private fun noInternetState() {
        triggerActivityIntent(UserOnboardingIntent.Refresh)
    }

    private fun openCameraInterface() {
        //cameraUtils.checkCameraPermission(requireActivity())
        cameraV1Utils.changeProfileImage(requireActivity())
    }

    private fun setCompleted() {
        val completedStage = listOf(getString(R.string.details_stage))
        triggerActivityIntent(UserOnboardingIntent.StageCompleted(completedStage))
    }

    private fun showInvalidReferralCode() {
        binding.tvInvalidReferral.makeVisible()
        binding.tvInvalidReferral.text = getString(R.string.invalid_referral_code)
    }

    private fun enableContinueBtn(enable: Boolean) {
        binding.btnContinue.isEnabled = enable
    }

    private fun fetchViews() {
        triggerIntent(UserDetailsIntent.FetchViews)
    }

    private fun setUpViews(viewDetails: Pair<Boolean, List<String>>) {
        if(viewDetails.first){
            binding.tvGroupName.makeGone()
            binding.etGroupName.makeGone()
        }
        binding.tvToolBar.text = viewDetails.second[0]
        binding.tvDetails.text = viewDetails.second[1]
        binding.tvGroupName.text = viewDetails.second[2]
        binding.tvPhoto.text = viewDetails.second[3]
    }

    override fun render(effect: UserDetailsEffect) {
        when(effect) {
            is UserDetailsEffect.HideInvalidReferralCode -> {
                binding.tvInvalidReferral.makeGone()
            }
        }
    }

    override fun triggerIntent(intent: UserDetailsIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    fun triggerActivityIntent(intent: UserOnboardingIntent){
        lifecycleScope.launch {
            userOnboardingViewModel.intents.send(intent)
        }
    }

    fun setImageFromUri(uri: Uri) {
        binding.ivGallary.visibility = View.GONE
        binding.ivPicture.background = null
        binding.ivPicture.setImageURI(uri)
        binding.ivEditPicture.makeVisible()
        triggerIntent(UserDetailsIntent.Base64UserPicture(cameraUtils.getBase64String(uri,requireActivity())))
    }

    fun closeCamera(){
        triggerIntent(UserDetailsIntent.CloseCamera)
    }

    private fun handleBack(){
        activity?.onBackPressedDispatcher?.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showDialog()
                }
            })

        binding.toolbar.setNavigationOnClickListener {
            showDialog()
        }
    }

    fun showDialog() {
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun onResume() {
        super.onResume()
        triggerIntent(UserDetailsIntent.CloseCamera)
    }
}