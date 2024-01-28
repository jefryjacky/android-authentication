package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.entity.UserToken
import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.scheduler.Schedulers
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulers: Schedulers
) : AuthBaseUseCase(userRepository, schedulers) {

    fun execute():UserToken? {
           return userRepository.getToken()
    }

}