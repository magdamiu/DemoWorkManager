package com.magdamiu.demoworkmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class SyncDataWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // get the data
        val userIdInput = inputData.getInt(Constants.DATA_USER_ID, 0)

        val isDataSent = loadData()

        // output
        val outputData = workDataOf(Constants.DATA_USER_Name to isDataSent)
        // or Data.Builder().putBoolean(Constants.DATA_SENT, isDataSent).build()


        return Result.success()
    }

    private fun loadData(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return true
    }


}


