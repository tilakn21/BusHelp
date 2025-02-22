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

    fun getGoogleSignInIntent(): Intent {
        val googleSignInClient = GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("772298359589-squv0er27olnjo8gk521ca1r44hehutj.apps.googleusercontent.com")
                .requestEmail()
                .build()
        )
        return googleSignInClient.signInIntent
    }

    fun handleSignInResult(data: Intent?, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d("GoogleSignIn", "Google Account: ${account.email}")
            firebaseAuthWithGoogle(account.idToken!!, onSuccess, onFailure)
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Google Sign-In failed with code: ${e.statusCode}")
            onFailure("Google Sign-In failed: ${e.message}")
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
        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
    }
}
