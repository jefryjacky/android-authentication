package com.jefryjacky.auth.api.user.request

import com.google.gson.annotations.SerializedName
import com.jefryjacky.auth.domain.entity.User

data class UpdateUserRequest(
    @SerializedName("display_name")
    val displayName: String
) {
    companion object{
        fun create(user: User): UpdateUserRequest{
            return UpdateUserRequest(
                displayName = user.displayName
            )
        }
    }
}