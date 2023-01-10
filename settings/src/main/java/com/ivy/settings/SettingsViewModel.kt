package com.ivy.settings

import com.ivy.core.domain.SimpleFlowViewModel
import com.ivy.core.domain.action.settings.applocked.AppLockedFlow
import com.ivy.core.domain.action.settings.applocked.WriteAppLockedAct
import com.ivy.core.domain.action.settings.balance.HideBalanceFlow
import com.ivy.core.domain.action.settings.balance.WriteHideBalanceAct
import com.ivy.core.domain.action.settings.basecurrency.BaseCurrencyFlow
import com.ivy.core.domain.action.settings.basecurrency.WriteBaseCurrencyAct
import com.ivy.core.domain.action.settings.startdayofmonth.StartDayOfMonthFlow
import com.ivy.core.domain.action.settings.startdayofmonth.WriteStartDayOfMonthAct
import com.ivy.navigation.Navigator
import com.ivy.old.ImportOldJsonBackupAct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigator: Navigator,
    private val baseCurrencyFlow: BaseCurrencyFlow,
    private val writeBaseCurrencyAct: WriteBaseCurrencyAct,
    private val startDayOfMonthFlow: StartDayOfMonthFlow,
    private val writeStartDayOfMonthAct: WriteStartDayOfMonthAct,
    private val hideBalanceFlow: HideBalanceFlow,
    private val writeHideBalanceAct: WriteHideBalanceAct,
    private val appLockedFlow: AppLockedFlow,
    private val writeAppLockedAct: WriteAppLockedAct,
    private val importOldJsonBackupAct: ImportOldJsonBackupAct,
) : SimpleFlowViewModel<SettingsState, SettingsEvent>() {
    override val initialUi: SettingsState = SettingsState(
        baseCurrency = "",
        startDayOfMonth = 1,
        hideBalance = false,
        appLocked = false
    )

    override val uiFlow: Flow<SettingsState> = combine(
        baseCurrencyFlow(),
        startDayOfMonthFlow(),
        hideBalanceFlow(Unit),
        appLockedFlow(Unit)
    ) { baseCurrency, startDayOfMonth, hideBalance, appLocked ->
        SettingsState(
            baseCurrency = baseCurrency,
            startDayOfMonth = startDayOfMonth,
            hideBalance = hideBalance,
            appLocked = appLocked
        )
    }

    override suspend fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.Back -> navigator.back()
            is SettingsEvent.BaseCurrencyChange -> {
                writeBaseCurrencyAct(event.newCurrency)
            }
            is SettingsEvent.StartDayOfMonth -> {
                writeStartDayOfMonthAct(event.startDayOfMonth)
            }
            is SettingsEvent.HideBalance -> {
                writeHideBalanceAct(event.hideBalance)
            }
            is SettingsEvent.AppLocked -> {
                writeAppLockedAct(event.appLocked)
            }
            is SettingsEvent.ImportOldData -> handleImportOldData(event)
        }
    }

    private suspend fun handleImportOldData(event: SettingsEvent.ImportOldData) {
        val result = importOldJsonBackupAct(event.jsonZipUri)
        Timber.d("Import data result: $result")
    }

}