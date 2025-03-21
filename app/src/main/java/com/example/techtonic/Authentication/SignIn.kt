//package com.example.techtonic.Authentication
//
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.os.Bundle
//import android.util.Patterns
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.techtonic.Activity.MainActivity
//import com.example.techtonic.R
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//
//class SignIn : AppCompatActivity() {
//
//    private lateinit var emailEditText: EditText
//    private lateinit var passwordEditText: EditText
//    private lateinit var loginButton: Button
//    private lateinit var registerButton: TextView
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_in)
//
//
//
//
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance().reference
//        emailEditText = findViewById(R.id.emailEditText)
//        passwordEditText = findViewById(R.id.passwordEditText)
//        loginButton = findViewById(R.id.signInButton)
//        registerButton = findViewById(R.id.signup)
//
//        loginButton.setOnClickListener {
//            loginUser()
//        }
//
//        registerButton.setOnClickListener {
//            val intent = Intent(this, SignUp::class.java)
//            startActivity(intent)
//        }
//    }
//
//    private fun loginUser() {
//        val email = emailEditText.text.toString().trim()
//        val password = passwordEditText.text.toString().trim()
//
//        if (email.isEmpty()) {
//            emailEditText.error = "Email is required"
//            emailEditText.requestFocus()
//            return
//        }
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailEditText.error = "Please enter a valid email"
//            emailEditText.requestFocus()
//            return
//        }
//
//        if (password.isEmpty()) {
//            passwordEditText.error = "Password is required"
//            passwordEditText.requestFocus()
//            return
//        }
//
//        if (password.length < 6) {
//            passwordEditText.error = "Password should be at least 6 characters long"
//            passwordEditText.requestFocus()
//            return
//        }
//
//        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val user = auth.currentUser
//                if (user != null && user.isEmailVerified) {
//                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                } else {
//
//                    Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//}







//package com.example.techtonic.Authentication
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Patterns
//import android.widget.*
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import com.example.techtonic.Activity.MainActivity
//import com.example.techtonic.R
//import com.google.android.gms.auth.api.signin.*
//import com.google.android.gms.common.SignInButton
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.*
//
//class SignIn : AppCompatActivity() {
//
//    private lateinit var emailEditText: EditText
//    private lateinit var passwordEditText: EditText
//    private lateinit var loginButton: Button
//    private lateinit var googleSignInButton: SignInButton
//    private lateinit var registerButton: TextView
//    private lateinit var auth: FirebaseAuth
//    private lateinit var googleSignInClient: GoogleSignInClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_in)
//
//        auth = FirebaseAuth.getInstance()
//        emailEditText = findViewById(R.id.emailEditText)
//        passwordEditText = findViewById(R.id.passwordEditText)
//        loginButton = findViewById(R.id.signInButton)
//        googleSignInButton = findViewById(R.id.googleSignInButton)
//        registerButton = findViewById(R.id.signup)
//
//        // Configure Google Sign-In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        loginButton.setOnClickListener {
//            loginUser()
//        }
//
//        googleSignInButton.setOnClickListener {
//            signInWithGoogle()
//        }
//
//        registerButton.setOnClickListener {
//            val intent = Intent(this, SignUp::class.java)
//            startActivity(intent)
//        }
//    }
//
//    private fun loginUser() {
//        val email = emailEditText.text.toString().trim()
//        val password = passwordEditText.text.toString().trim()
//
//        if (email.isEmpty()) {
//            emailEditText.error = "Email is required"
//            emailEditText.requestFocus()
//            return
//        }
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailEditText.error = "Please enter a valid email"
//            emailEditText.requestFocus()
//            return
//        }
//
//        if (password.isEmpty()) {
//            passwordEditText.error = "Password is required"
//            passwordEditText.requestFocus()
//            return
//        }
//
//        if (password.length < 6) {
//            passwordEditText.error = "Password should be at least 6 characters long"
//            passwordEditText.requestFocus()
//            return
//        }
//
//        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val user = auth.currentUser
//                if (user != null && user.isEmailVerified) {
//                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    private fun signInWithGoogle() {
//        val signInIntent = googleSignInClient.signInIntent
//        googleSignInLauncher.launch(signInIntent)
//    }
//
//    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        try {
//            val account = task.getResult(ApiException::class.java)!!
//            firebaseAuthWithGoogle(account.idToken!!)
//        } catch (e: ApiException) {
//            Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(this, "Google sign-in successful!", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            } else {
//                Toast.makeText(this, "Google sign-in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}



//package com.example.techtonic.Authentication
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.util.Base64
//import android.util.Log
//import android.util.Patterns
//import android.widget.*
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import com.example.techtonic.Activity.MainActivity
//import com.example.techtonic.R
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.facebook.*
//import com.facebook.appevents.AppEventsLogger
//import com.facebook.login.LoginManager
//import com.facebook.login.LoginResult
//import com.google.android.gms.common.SignInButton
//import com.google.firebase.auth.FacebookAuthProvider
//import org.json.JSONObject
//import java.security.MessageDigest
//
//class SignIn : AppCompatActivity() {
//
//    private lateinit var emailEditText: EditText
//    private lateinit var passwordEditText: EditText
//    private lateinit var loginButton: Button
//    private lateinit var googleSignInButton: SignInButton
//    private lateinit var facebookSignInButton: ImageView
//    private lateinit var registerButton: TextView
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference
//    private lateinit var googleSignInClient: GoogleSignInClient
//    private lateinit var callbackManager: CallbackManager
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_in)
//
//        try {
//            val info = packageManager.getPackageInfo(
//                "com.example.techtonic",
//                PackageManager.GET_SIGNING_CERTIFICATES
//            )
//            for (signature in info.signingInfo.apkContentsSigners) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT)
//                Log.d("KeyHash", keyHash) // Check Logcat for the result
//            }
//        } catch (e: Exception) {
//            Log.e("KeyHash Error", "Error while generating key hash: ${e.message}")
//        }
//
//        // Initialize Firebase and Facebook SDKs
//        FacebookSdk.sdkInitialize(applicationContext)
//        AppEventsLogger.activateApp(application)
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance().reference
//
//        // Initialize views
//        emailEditText = findViewById(R.id.emailEditText)
//        passwordEditText = findViewById(R.id.passwordEditText)
//        loginButton = findViewById(R.id.signInButton)
//        googleSignInButton = findViewById(R.id.googleSignInButton)
//        facebookSignInButton = findViewById(R.id.facebookSignInButton)
//        registerButton = findViewById(R.id.signup)
//
//        // Google Sign-In setup
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        // Sign out and revoke access to reset Google sign-in
//        googleSignInClient.signOut().addOnCompleteListener {
//            googleSignInClient.revokeAccess().addOnCompleteListener {
//                googleSignInButton.setOnClickListener {
//                    val signInIntent = googleSignInClient.signInIntent
//                    startActivityForResult(signInIntent, 100)
//                }
//            }
//        }
//
//        // Facebook Login setup
//        callbackManager = CallbackManager.Factory.create()
//        facebookSignInButton.setOnClickListener {
//            LoginManager.getInstance().logOut()
//
//            val loginManager = LoginManager.getInstance()
//            loginManager.logInWithReadPermissions(
//                this,
//                listOf("email", "public_profile")
//            )
//        }
//        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onSuccess(result: LoginResult) {
//                handleFacebookAccessToken(result.accessToken)
//            }
//            override fun onCancel() {
//                Toast.makeText(this@SignIn, "Facebook sign-in cancelled.", Toast.LENGTH_SHORT).show()
//            }
//            override fun onError(error: FacebookException) {
//                Toast.makeText(this@SignIn, "Facebook sign-in failed: ${error.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        // Email/password sign-in
//        loginButton.setOnClickListener { loginUser() }
//        registerButton.setOnClickListener {
//            startActivity(Intent(this, SignUp::class.java))
//        }
//    }
//
//    private fun loginUser() {
//        val email = emailEditText.text.toString().trim()
//        val password = passwordEditText.text.toString().trim()
//
//        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailEditText.error = "Enter a valid email"
//            emailEditText.requestFocus()
//            return
//        }
//        if (password.isEmpty() || password.length < 6) {
//            passwordEditText.error = "Password must be at least 6 characters"
//            passwordEditText.requestFocus()
//            return
//        }
//
//        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful && auth.currentUser?.isEmailVerified == true) {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            } else {
//                Toast.makeText(this, "Login failed or email not verified.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//
//    private fun handleFacebookAccessToken(token: AccessToken) {
//        val credential = FacebookAuthProvider.getCredential(token.token)
//        auth.signInWithCredential(credential).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            } else {
//                Toast.makeText(this, "Facebook sign-in failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 100) { // Google Sign-In
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            val account = task.result
//            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
//            auth.signInWithCredential(credential).addOnCompleteListener {
//                if (it.isSuccessful) {
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this, "Google sign-in failed: ${it.exception?.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//        } else {
//            callbackManager.onActivityResult(requestCode, resultCode, data) // Facebook callback
//        }
//    }
//}
package com.example.techtonic.Authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.techtonic.Activity.MainActivity
import com.example.techtonic.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FacebookAuthProvider
import org.json.JSONObject
import java.security.MessageDigest

class SignIn : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var googleSignInButton: SignInButton
    private lateinit var facebookSignInButton: ImageView
    private lateinit var registerButton: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        try {
            val info = packageManager.getPackageInfo(
                "com.example.techtonic",
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            for (signature in info.signingInfo.apkContentsSigners) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("KeyHash", keyHash)
            }
        } catch (e: Exception) {
            Log.e("KeyHash Error", "Error while generating key hash: ${e.message}")
        }

        // Initialize Firebase and Facebook SDKs
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.signInButton)
        googleSignInButton = findViewById(R.id.googleSignInButton)
        facebookSignInButton = findViewById(R.id.facebookSignInButton)
        registerButton = findViewById(R.id.signup)

        // Google Sign-In setup
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }

        // Facebook Login setup
        callbackManager = CallbackManager.Factory.create()

        facebookSignInButton.setOnClickListener {
            LoginManager.getInstance().logOut()
            auth.signOut()// ✅ Ensure previous sessions are cleared

            val loginManager = LoginManager.getInstance()
            loginManager.logInWithReadPermissions(
                this,
                listOf("email", "public_profile")
            )
            loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }
                override fun onCancel() {
                    Toast.makeText(this@SignIn, "Facebook sign-in cancelled.", Toast.LENGTH_SHORT).show()
                }
                override fun onError(error: FacebookException) {
                    Toast.makeText(this@SignIn, "Facebook sign-in failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


        loginButton.setOnClickListener { loginUser() }
        registerButton.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
        private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Enter a valid email"
            emailEditText.requestFocus()
            return
        }
        if (password.isEmpty() || password.length < 6) {
            passwordEditText.error = "Password must be at least 6 characters"
            passwordEditText.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful && auth.currentUser?.isEmailVerified == true) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Login failed or email not verified.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        LoginManager.getInstance().logOut()
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Facebook sign-in failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) { // Google Sign-In
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Google sign-in failed: ${it.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
