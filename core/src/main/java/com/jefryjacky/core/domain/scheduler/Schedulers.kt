package com.jefryjacky.core.domain.scheduler

import io.reactivex.Scheduler

interface Schedulers {
    fun mainThread(): Scheduler
    fun diskThread(): Scheduler
    fun netWorkThread(): Scheduler

    fun throttle():Long
}