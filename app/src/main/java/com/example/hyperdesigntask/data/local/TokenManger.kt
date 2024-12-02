package com.example.hyperdesigntask.data.local
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "encrypted_prefs"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val encryptedSharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }


    // Save the access token individually
    fun saveAccessToken(accessToken: String) {
        encryptedSharedPreferences.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .apply()
    }

    // Save the refresh token individually
    fun saveRefreshToken(refreshToken: String) {
        encryptedSharedPreferences.edit()
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()
    }

    // Retrieve the access token
    fun getAccessToken(): String? {
        return encryptedSharedPreferences.getString(ACCESS_TOKEN, null)
    }

    // Retrieve the refresh token
    fun getRefreshToken(): String? {
        return encryptedSharedPreferences.getString(REFRESH_TOKEN, null)
    }
}