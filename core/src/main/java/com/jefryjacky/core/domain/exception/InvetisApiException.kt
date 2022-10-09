package com.jefryjacky.core.domain.exception

class InvetisApiException(val code:Int,
                          val error:String,
                          message:String):Exception(message)