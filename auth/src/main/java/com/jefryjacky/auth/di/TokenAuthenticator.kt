package com.jefryjacky.auth.di

import com.jefryjacky.auth.api.user.UserService
import com.jefryjacky.auth.domain.repository.database.UserDatabase
import dagger.Lazy
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val userDatabase: UserDatabase,
    private val userServiceLazy: Lazy<UserService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // If it's already the token refresh request that failed with 401, don't try to refresh again
        if (response.request.url.encodedPath.contains("/api/oauth/token")) {
            return null
        }

        val failedRequestToken = response.request.header("Authorization")

        synchronized(this) {
            val currentToken = userDatabase.getToken()

            // If token has already been refreshed by another thread/request, retry with it
            if (currentToken != null && currentToken.accessToken != failedRequestToken) {
                return response.request.newBuilder()
                    .header("Authorization", currentToken.accessToken)
                    .build()
            }

            // Otherwise, perform the refresh token request
            if (!currentToken?.refreshToken.isNullOrEmpty()) {
                try {
                    // Use blockingGet() since authenticate() runs on a background thread and requires a synchronous response.
                    // Lazy<UserService> is used to avoid Dagger circular dependency (UserService depends on OkHttpClient).
                    val tokenResponse = userServiceLazy.get().refreshToken("refresh_token", currentToken!!.refreshToken).blockingGet()
                    
                    if (tokenResponse != null) {
                        val newUserToken = tokenResponse.toUserToken()
                        userDatabase.saveToken(newUserToken)

                        return response.request.newBuilder()
                            .header("Authorization", newUserToken.accessToken)
                            .build()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    userDatabase.deleteAll()
                }
            }
            return null
        }
    }
}
