package com.jefryjacky.auth.ui.updateuser

sealed interface UpdateUserEvent {
    data class TypingDisplayNameEvent(val displayName:String): UpdateUserEvent
    data object Submit: UpdateUserEvent
}