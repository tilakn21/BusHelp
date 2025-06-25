package com.example.busbee.GoogleSignInHelper

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInHelper(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            context.applicationContext, // Use applicationContext to avoid memory leaks
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("772298359589-0r43ln25v210c7q37aluf6ph37lrdjiu.apps.googleusercontent.com")
                .requestEmail()
                .build()
        )
    }


    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }


    fun handleSignInResult(data: Intent?, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account?.idToken != null) {
                firebaseAuthWithGoogle(account.idToken!!, onSuccess, onFailure)
            } else {
                onFailure("Google Sign-In failed: ID Token is null.")
            }
            Log.d("GoogleSignIn", "Google Account: ${account.email}")

        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Google Sign-In failed with code: ${e.statusCode}")
            when (e.statusCode) {
                GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> onFailure("Sign-in was canceled by the user.")
                GoogleSignInStatusCodes.SIGN_IN_FAILED -> onFailure("Google Sign-In failed due to an unknown error.")
                GoogleSignInStatusCodes.NETWORK_ERROR -> onFailure("Network error. Check your internet connection.")
                else -> onFailure("Google Sign-In failed: ${e.message}")
            }
        }
    }



    private fun firebaseAuthWithGoogle(idToken: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess("Google Sign-In successful!")
                } else {
                    onFailure("Firebase authentication failed: ${task.exception?.message}")
                }
            }
    }

    fun logout() {
        auth.signOut()
        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).revokeAccess()
    }

}
