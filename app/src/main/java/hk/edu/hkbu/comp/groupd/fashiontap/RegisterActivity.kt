package hk.edu.hkbu.comp.groupd.fashiontap

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hk.edu.hkbu.comp.groupd.fashiontap.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_login.*

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.contentRegister.profile = RegisterViewModel()

        FirebaseAuth.getInstance().currentUser?.let { user ->
            binding.contentRegister.profile?.let { profile ->
                profile.email?.set(user.email.orEmpty())
                profile.displayName?.set(user.displayName.orEmpty())
                profile.phoneNumber?.set(user.phoneNumber.orEmpty())
            }
        }

        action_submit.setOnClickListener { onSubmit()}
        textViewLinkLogin.setOnClickListener { backLogin() }
    }

    private fun backLogin() {
        val intentRegister = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intentRegister)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish()
    }

    private fun onSubmit() {
        binding.contentRegister.profile?.let { profile ->
            if (profile.password?.get().equals(profile.passwordConfirm?.get()) && "${profile.password?.get()}".length >= 6) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword("${profile?.email?.get()}", "${profile?.password?.get()}").addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Login Successfully.",
                                Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else
                Toast.makeText(baseContext, "Password is not matched or less than 8 characters.", Toast.LENGTH_SHORT).show()
        }
    }
}
