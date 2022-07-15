package com.ivy.wallet

import android.annotation.SuppressLint
import android.app.Application
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.ivy.base.GlobalProvider
import com.ivy.base.RootIntent
import com.ivy.common.BuildConfig
import com.ivy.data.transaction.TransactionType
import com.ivy.wallet.ui.RootActivity
import com.ivy.wallet.ui.RootViewModel
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

/**
 * Created by iliyan on 24.02.18.
 */
@HiltAndroidApp
class IvyAndroidApp : Application(), Configuration.Provider {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context
    }


    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        GlobalProvider.appContext = this
        GlobalProvider.rootIntent = object : RootIntent {
            override fun getIntent(context: Context): Intent =
                Intent(context, RootActivity::class.java)

            override fun addTransactionStart(context: Context, type: TransactionType): Intent =
                Intent(context, RootActivity::class.java).apply {
                    putExtra(RootViewModel.EXTRA_ADD_TRANSACTION_TYPE, type)
                }
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}