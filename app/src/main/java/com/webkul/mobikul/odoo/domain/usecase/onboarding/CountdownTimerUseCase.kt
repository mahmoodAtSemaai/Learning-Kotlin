package com.webkul.mobikul.odoo.domain.usecase.onboarding

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CountdownTimerUseCase @Inject constructor() {

    private lateinit var timer : Flow<Int>

    operator fun invoke(time: Int, interval : Long): Flow<Int> {
        timer = (time downTo 0)
                .asSequence()
                .asFlow()
                .onEach { delay(interval) }

        return timer
    }

}

