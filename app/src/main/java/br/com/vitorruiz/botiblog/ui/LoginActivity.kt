package br.com.vitorruiz.botiblog.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.vitorruiz.botiblog.data.source.local.storage.GlobalStorage
import br.com.vitorruiz.botiblog.databinding.ActivityLoginBinding
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val _globalStorage: GlobalStorage by inject()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (_globalStorage.loggedUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}