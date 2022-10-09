package com.authentication.message

import android.content.Context
import com.authentication.R
import com.authentication.domain.message.Message

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
}