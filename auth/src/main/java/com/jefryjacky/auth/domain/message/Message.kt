package com.jefryjacky.auth.domain.message

interface Message {
    fun messageErrorEmptyEmail():String
    fun messageErrorPasswordEmpty():String
    fun messageErrorInvalidEmail(): String
    fun messageErrorPasswordMinLength(passwordMinLength: Int): String
    fun messageErrorPasswordNoSpecialChars(): String
    fun passwordAndConfirmationPasswordDoesntMatch(): String
    fun messageErrorPasswordNoCapitalChar(): String
    fun messageErrorEmailHaveBeenRegistered(): String
    fun messageErrorsEmailHaveNotBeenRegitered():String
    fun messageErrorsResetPasswordLinkInvalid():String
    fun messageWrongPassword():String
    fun messagePasswordIsNotSameWithAbove(): String
    fun messageErrorEmptyDisplayName(): String
}