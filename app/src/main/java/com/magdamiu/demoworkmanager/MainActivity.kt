package com.magdamiu.demoworkmanager

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import androidx.work.WorkManager
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun getStatusByIdForever(syncOnlyOnce: OneTimeWorkRequest) {
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(syncOnlyOnce.id)
            .observeForever { workInfo ->
                if (workInfo?.state?.isFinished!!) {
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> display("success")
                        else -> display("fail")
                    }
                }
            }
    }

    private fun getStatusById(syncOnlyOnce: OneTimeWorkRequest) {
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(syncOnlyOnce.id)
            .observe(this, Observer { workInfo ->
                if (workInfo?.state?.isFinished!!) {
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> display("success")
                        else -> display("fail")
                    }
                }
            })
    }

    private fun getStatusByTag() {
        WorkManager.getInstance(this)
            .getWorkInfosByTagLiveData(Constants.TAG_SYNC)
            .observe(this, Observer<List<WorkInfo>> { workInfoList ->
                val currentWorkStatus = workInfoList?.getOrNull(0)
                if (currentWorkStatus?.state?.isFinished == true) {
                    display("Sync finished!")
                }
            })
    }

    private fun getStatusByUniqueName() {
        WorkManager.getInstance(this)
            .getWorkInfosForUniqueWorkLiveData(Constants.UNIQUE_NAME)
            .observe(this, Observer<List<WorkInfo>> { workInfoList ->
                val currentWorkStatus = workInfoList?.getOrNull(0)
                if (currentWorkStatus?.state?.isFinished == true) {
                    display("Sync finished!")
                }
            })
    }


    private fun display(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}

