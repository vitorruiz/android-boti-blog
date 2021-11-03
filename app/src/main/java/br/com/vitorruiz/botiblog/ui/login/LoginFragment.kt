package br.com.vitorruiz.botiblog.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import br.com.vitorruiz.botiblog.core.BaseFragment
import br.com.vitorruiz.botiblog.core.ResultWrapper
import br.com.vitorruiz.botiblog.databinding.FragmentLoginBinding
import br.com.vitorruiz.botiblog.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val _viewModel: LoginViewModel by viewModel()

    override fun getBindingComponent(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun initComponents(rootView: View, savedInstanceState: Bundle?) {
        setupViews()
        setupLiveData()
    }

    private fun setupViews() {
        binding.edtEmail.setText(_viewModel.lastLoggedUser)

        binding.btLogin.setOnClickListener {
            _viewModel.login(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
        }

        binding.btRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun setupLiveData() {
        _viewModel.loginResult.observe(this) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.includeLoading.loadingContainer.isVisible = true
                    binding.btLogin.isEnabled = false
                }
                is ResultWrapper.Success -> {
                    activity?.finish()
                    startActivity(Intent(context, MainActivity::class.java))
                }
                is ResultWrapper.Error -> {
                    binding.includeLoading.loadingContainer.isVisible = false
                    binding.btLogin.isEnabled = true
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}