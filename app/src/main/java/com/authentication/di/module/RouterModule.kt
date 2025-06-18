package com.authentication.di.module

import com.authentication.router.EmailVerificationRouteImpl
import com.authentication.router.LoginRouterImpl
import com.authentication.router.RegisterRouteImpl
import com.authentication.router.UpdateUserRouteImpl
import com.jefryjacky.auth.ui.emailverification.EmailVerificationRoute
import com.jefryjacky.auth.ui.login.LoginRoute
import com.jefryjacky.auth.ui.register.RegisterRoute
import com.jefryjacky.auth.ui.updateuser.UpdateUserRoute
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class RouterModule {
    @Binds
    abstract fun bindLoginRoute(loginRouterImpl: LoginRouterImpl):LoginRoute

    @Binds
    abstract fun bindRegisterRoute(registerRouteImpl: RegisterRouteImpl):RegisterRoute

    @Binds
    abstract fun bindEmailRegistration(emailVerificationRouteImpl: EmailVerificationRouteImpl):EmailVerificationRoute

    @Binds
    abstract fun bindUpdateUserRoute(updateUserRouteImpl: UpdateUserRouteImpl):UpdateUserRoute
}