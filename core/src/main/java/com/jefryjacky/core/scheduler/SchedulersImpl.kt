package com.jefryjacky.core.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class SchedulersImpl: com.jefryjacky.core.domain.scheduler.Schedulers {
    private val diskThread = Executors.newFixedThreadPool(1)
    private val networkThread = Executors.newFixedThreadPool(2)

    override fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun diskThread(): Scheduler {
        return Schedulers.from(diskThread)
    }

    override fun netWorkThread(): Scheduler {
        return Schedulers.from(networkThread)
    }

    override fun throttle(): Long {
        return 15000
    }
}