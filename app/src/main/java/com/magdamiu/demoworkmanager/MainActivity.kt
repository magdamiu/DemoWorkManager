package com.magdamiu.demoworkmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), LifecycleOwner {
    override fun getLifecycle(): Lifecycle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiresStorageNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        var userId = 33

        val userIdData = Data.Builder()
            .putInt(Constants.DATA_USER_ID, userId)
            .build()

        // add delay
        // backoff delay and policy
        val syncOnlyOnce = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setInputData(userIdData)
            .addTag(Constants.WORKER_SYNC)
            .addTag(Constants.TAG_SYNC)
            .setInitialDelay(15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MICROSECONDS)
            .build()

        val syncPeriodically = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS)
            .build()


        val task1 = OneTimeWorkRequestBuilder<SyncWorker>().build()
        val task2 = OneTimeWorkRequestBuilder<SyncWorker>().build()
        val task3 = OneTimeWorkRequestBuilder<SyncWorker>().build()
        val task4 = OneTimeWorkRequestBuilder<SyncWorker>().build()
        val task5 = OneTimeWorkRequestBuilder<SyncWorker>().build()

        val liniarChain = WorkManager.getInstance().beginWith(task1)
            .then(task2)
            .then(listOf(task3, task1))
            .enqueue()


        WorkManager.getInstance()
            .beginWith(listOf(task1, task2))
            .then(task3)
            .enqueue()

        val mergeWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputMerger(ArrayCreatingInputMerger::class.java)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance()
            .beginUniqueWork(Constants.UNIQUE_NAME, ExistingWorkPolicy.REPLACE, task1)
            .then(task2)
            .then(task3)
            .enqueue()

        val leftChain = WorkManager.getInstance().beginWith(task1).then(task2)
        val rightChain = WorkManager.getInstance().beginWith(task3).then(task4)
        val resultChain = WorkContinuation.combine(listOf(leftChain, rightChain)).then(task5)
        resultChain.enqueue()

        WorkManager.getInstance().cancelAllWork()
        WorkManager.getInstance().cancelAllWorkByTag(Constants.TAG_SYNC)
        WorkManager.getInstance().cancelUniqueWork(Constants.UNIQUE_NAME)
        WorkManager.getInstance().cancelWorkById(UUID.fromString(Constants.UNIQUE_UDID))

        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                Constants.UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.KEEP, syncPeriodically
            )
        Logging.show(this, "aa")

        getStatusByIdForever(syncOnlyOnce)
        getStatusById(syncOnlyOnce)
        getStatusByTag()
        getStatusByUniqueName()

        val periodicRefreshRequest = PeriodicWorkRequest.Builder(
            SyncWorker::class.java, // worker class
            8, // repeating interval
            TimeUnit.HOURS,
            15, // flex interval - worker will run somehow within this period of time, but at the end of repeating interval
            TimeUnit.MINUTES
        )
    }

    private fun getStatusByIdForever(syncOnlyOnce: OneTimeWorkRequest) {
        WorkManager.getInstance().getWorkInfoByIdLiveData(syncOnlyOnce.id)
            .observeForever { workInfo ->
                if (workInfo?.state?.isFinished!!) {
                    when (workInfo?.state) {
                        WorkInfo.State.SUCCEEDED -> displayMessage("success")
                        else -> {
                            displayMessage("fail")
                        }
                    }
                }
            }
    }

    private fun getStatusById(syncOnlyOnce: OneTimeWorkRequest) {
        WorkManager.getInstance().getWorkInfoByIdLiveData(syncOnlyOnce.id)
            .observe(this, Observer { workInfo ->
                if (workInfo?.state?.isFinished!!) {
                    when (workInfo?.state) {
                        WorkInfo.State.SUCCEEDED -> displayMessage("success")
                        else -> {
                            displayMessage("fail")
                        }
                    }
                }
            })
    }

    private fun getStatusByTag() {
        WorkManager.getInstance().getWorkInfosByTagLiveData(Constants.TAG_SYNC)
            .observe(this, Observer<List<WorkInfo>> { workInfoList ->
                val currentWorkStatus = workInfoList?.getOrNull(0)
                if (currentWorkStatus?.state?.isFinished == true) {
                    displayMessage("Sync finished!")
                }
            })
    }

    private fun getStatusByUniqueName() {
        WorkManager.getInstance().getWorkInfosForUniqueWorkLiveData(Constants.UNIQUE_NAME)
            .observe(this, Observer<List<WorkInfo>> { workInfoList ->
                val currentWorkStatus = workInfoList?.getOrNull(0)
                if (currentWorkStatus?.state?.isFinished == true) {
                    displayMessage("Sync finished!")
                }
            })
    }


    private fun displayMessage(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}

