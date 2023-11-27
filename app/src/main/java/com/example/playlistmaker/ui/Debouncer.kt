package com.example.playlistmaker.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayInMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean,
    functionToDebounce: (T) -> Unit
) : (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted == true || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayInMillis)
                functionToDebounce(param)
            }
        }
    }
}