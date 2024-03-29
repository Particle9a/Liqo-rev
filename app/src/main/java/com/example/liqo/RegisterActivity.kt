package com.example.liqo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    companion object {
        private val TAG = "RegisterClass"
        private val RC_SIGN_IN = 9001
    }
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnSignUp: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var btnGoogleLogin: SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mDatabaseRef = FirebaseDatabase.getInstance().getReference()
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this,  MainActivity::class.java))
        }

        btnRegister = findViewById(R.id.btn_register)
        inputEmail = findViewById(R.id.email_register)
        inputPassword = findViewById(R.id.password)
        inputConfirmPassword = findViewById(R.id.passwordConfirm)
        btnSignUp = findViewById(R.id.link_signup)



        btnRegister.setOnClickListener{
            registerButtonOnClick()
        }

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun registerButtonOnClick(){
        val n_email = inputEmail.text.toString()
        val n_password = inputPassword.text.toString()
        val n_confirmPassword = inputConfirmPassword.text.toString()

        if (TextUtils.isEmpty(n_email)) {
            Toast.makeText(applicationContext, "Email can't be Empty !!", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(n_password)) {
            Toast.makeText(applicationContext, "Password can't be Empty !!", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(n_confirmPassword)){
            Toast.makeText(applicationContext, "Password don't match", Toast.LENGTH_SHORT).show()
            return
        }

        if (!n_password.equals(n_confirmPassword)){
            Toast.makeText(applicationContext, "Password don't match", Toast.LENGTH_SHORT).show()
            return
        }

        emailRegister(n_email,n_password)

    }

    private fun emailRegister(n_email: String, n_password: String) {
        Log.d(RegisterActivity.TAG, "SignIn")
        if (!validateForm()) {
            return
        }
        println("Masuk")

        auth.createUserWithEmailAndPassword(n_email, n_password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult>() { task: Task<AuthResult> ->
                try {
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Couldn't Register, try again", Toast.LENGTH_SHORT).show()
                    }
                }
                catch(e : Exception){
                    e.printStackTrace()
                }
            })
    }

    private fun validateForm(): Boolean {
        var result = true
        if (TextUtils.isEmpty(inputEmail.text)) {
            inputEmail.setError("Required")
            result = false
        } else {
            inputEmail.setError(null)
        }

        if (TextUtils.isEmpty(inputPassword.text.toString())) {
            inputPassword.setError("Required")
            result = false
        } else {
            inputPassword.setError(null)
        }
        if (TextUtils.isEmpty(inputConfirmPassword.text.toString())) {
            inputConfirmPassword.setError("Required")
            result = false
        } else {
            inputPassword.setError(null)
        }
        return result
    }

}
