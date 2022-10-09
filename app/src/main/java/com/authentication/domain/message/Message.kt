package com.authentication.domain.message

interface Message {
    fun messageErrorEmptyEmail():String
    fun messageErrorPasswordEmpty():String
    fun messageErrorInvalidEmail(): String
    fun messageErrorPasswordMinLength(passwordMinLength: Int): String
    fun messageErrorPasswordNoSpecialChars(): String
    fun passwordAndConfirmationPasswordDoesntMatch(): String
    fun messageErrorPasswordNoCapitalChar(): String
    fun messageErrorEmailHaveBeenRegistered(): String
}