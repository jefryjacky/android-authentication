package com.jefryjacky.auth.domain.usecase

import com.jefryjacky.auth.domain.repository.UserRepository
import com.jefryjacky.core.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val userRepository: UserRepository,
): BaseUseCase() {

    fun execute(): Flow<Result> {
        return flow {
             if (executing) return@flow
            executing = true
            emit(Result.Loading(true))
            try {
                userRepository.deleteUser()
            } catch (e: Exception){
                checkError(e)
            } finally {
                emit(Result.Loading(false))
            }
            executing = false
            emit(Result.Success<Unit>(Unit))
        }
    }
}