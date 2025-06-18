package com.jefryjacky.auth.message

import android.content.Context
import com.jefryjacky.auth.R
import com.jefryjacky.auth.domain.message.Message

class MessageImpl(private val context:Context): Message {
    override fun messageErrorEmptyEmail(): String {
        return context.getString(R.string.message_error_empty_email)
    }

    override fun messageErrorPasswordEmpty(): String {
        return context.getString(R.string.message_error_empty_password)
    }

    override fun messageErrorInvalidEmail(): String {
        return context.getString(R.string.email_format_invalid)
    }

    override fun messageErrorPasswordMinLength(passwordMinLength: Int): String {
        return context.getString(R.string.password_cant_less_than_minlength_characters, passwordMinLength)
    }

    override fun messageErrorPasswordNoSpecialChars(): String {
        return context.getString(R.string.password_must_contains_special_character)
    }

    override fun passwordAndConfirmationPasswordDoesntMatch(): String {
        return context.getString(R.string.password_and_confirmation_password_doesnt_match)
    }

    override fun messageErrorPasswordNoCapitalChar(): String {
        return context.getString(R.string.password_must_contains_capital_character)
    }

    override fun messageErrorEmailHaveBeenRegistered(): String {
        return context.getString(R.string.email_have_been_registered)
    }

    override fun messageErrorsEmailHaveNotBeenRegitered(): String {
        return context.getString(R.string.email_have_not_been_registered)
    }

    override fun messageErrorsResetPasswordLinkInvalid(): String {
        return context.getString(R.string.reset_password_link_invalid)
    }

    override fun messageWrongPassword(): String {
        return context.getString(R.string.message_wrong_password)
    }

    override fun messagePasswordIsNotSameWithAbove(): String {
        return context.getString(R.string.message_password_is_not_same_with_above)
    }

    override fun messageErrorEmptyDisplayName(): String {
        return context.getString(R.string.message_error_empty_display_name)
    }
}