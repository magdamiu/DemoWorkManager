package com.magdamiu.demoworkmanager

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class SyncDataViewModel(application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)

    internal fun applyWorkQuery(): LiveData<MutableList<WorkInfo>> {
        val sampleWorkQuery = WorkQuery.Builder
            .fromTags(listOf(Constants.TAG_SYNC))
            .addStates(
                listOf(
                    WorkInfo.State.FAILED,
                    WorkInfo.State.CANCELLED
                )
            )
            .addUniqueWorkNames(listOf(Constants.CLEAN_DATA))
            .build()

        return workManager.getWorkInfosLiveData(sampleWorkQuery)
    }

    internal fun startOneTimeRequestSyncData(userId: Int) {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiresStorageNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val userIdData = Data.Builder()
            .putInt(Constants.DATA_USER_ID, userId)
            .build()

        val syncOnlyOnce = OneTimeWorkRequestBuilder<SyncDataWorker>()
            .setConstraints(constraints)
            .setInputData(userIdData)
            .addTag(Constants.WORKER_SYNC)
            .addTag(Constants.TAG_SYNC)
            .setInitialDelay(15, TimeUnit.MINUTES)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MICROSECONDS
            )
            .build()

        workManager.enqueueUniqueWork(
            Constants.UNIQUE_NAME,
            ExistingWorkPolicy.KEEP,
            syncOnlyOnce
        )
    }

    internal fun startPeriodicRequest() {
        val syncPeriodically = PeriodicWorkRequestBuilder<SyncDataWorker>(1, TimeUnit.HOURS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            Constants.UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP, syncPeriodically
        )
    }

    internal fun cancelAllTasks() {
        workManager.cancelAllWork()
    }

    internal fun cancelAllWorkByTag(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    internal fun cancelByUniqueName(name: String) {
        workManager.cancelUniqueWork(name)
    }

    internal fun cancelById(id: String) {
        workManager.cancelWorkById(UUID.fromString(id))
    }
}