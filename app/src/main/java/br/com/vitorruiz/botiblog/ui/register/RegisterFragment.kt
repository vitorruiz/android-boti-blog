package br.com.vitorruiz.botiblog.ui.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import br.com.vitorruiz.botiblog.R
import br.com.vitorruiz.botiblog.core.BaseFragment
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.databinding.FragmentRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val _viewModel: RegisterViewModel by viewModel()

    override fun getBindingComponent(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun initComponents(rootView: View, savedInstanceState: Bundle?) {
        setupViews()
        setupLiveData()
    }

    private fun setupViews() {
        binding.btRegister.setOnClickListener {
            if (isFormValid()) {
                _viewModel.register(
                    binding.edtName.text.toString(),
                    binding.edtEmail.text.toString(),
                    binding.edtPassword.text.toString()
                )
            }
        }
    }

    private fun setupLiveData() {
        _viewModel.registerResult.observe(this) {
            when (it) {
                is ResultWrapper.Success -> {
                    binding.includeLoading.loadingContainer.isVisible = false
                    Toast.makeText(context, R.string.register_message_success, Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigateUp()
                }
                is ResultWrapper.Loading -> {
                    binding.includeLoading.loadingContainer.isVisible = true
                    binding.btRegister.isEnabled = false
                }
                is ResultWrapper.Error -> {
                    binding.includeLoading.loadingContainer.isVisible = false
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        var result = true

        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val passwordConfirm = binding.edtPasswordConfirmation.text.toString()

        if (name.isEmpty()) {
            result = false
            binding.tiName.error = getString(R.string.register_field_error_mandatory)
        } else {
            binding.tiName.error = null
            binding.tiName.isErrorEnabled = false
        }

        if (email.isEmpty()) {
            result = false
            binding.tiEmail.error = getString(R.string.register_field_error_mandatory)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = false
            binding.tiEmail.error = getString(R.string.register_field_error_invalid_email)
        } else {
            binding.tiEmail.error = null
            binding.tiEmail.isErrorEnabled = false
        }

        if (password.isEmpty()) {
            result = false
            binding.tiPassword.error = getString(R.string.register_field_error_mandatory)
        } else {
            binding.tiPassword.error = null
            binding.tiPassword.isErrorEnabled = false
        }

        if (passwordConfirm.isEmpty()) {
            result = false
            binding.tiPasswordConfirmation.error = getString(R.string.register_field_error_mandatory)
        } else {
            binding.tiPasswordConfirmation.error = null
            binding.tiPasswordConfirmation.isErrorEnabled = false
        }

        if (password.isNotEmpty() && passwordConfirm.isNotEmpty()) {
            if (password != passwordConfirm) {
                result = false
                binding.tiPasswordConfirmation.error = getString(R.string.register_field_error_password_not_match)
            } else {
                binding.tiPasswordConfirmation.error = null
                binding.tiPasswordConfirmation.isErrorEnabled = false
            }
        }

        return result
    }
}