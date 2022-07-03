package com.ivy.wallet.ui.transaction

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import com.ivy.data.Account
import com.ivy.data.Category
import com.ivy.data.transaction.Transaction
import com.ivy.data.transaction.TransactionType
import com.ivy.frp.view.FRP
import com.ivy.frp.view.navigation.Screen

sealed class TransactionScreen : Screen {
    data class NewTransaction(
        val type: TransactionType,
        val account: Account?,
        val category: Category?
    ) : TransactionScreen()

    data class EditTransaction(
        val transaction: Transaction
    ) : TransactionScreen()
}

@Composable
fun BoxWithConstraintsScope.TransactionScreen(screen: TransactionScreen) {
    FRP<TrnState, TrnEvent, TransactionViewModel>(
        initialEvent = when (screen) {
            is TransactionScreen.EditTransaction -> TrnEvent.LoadTransaction(screen.transaction)
            is TransactionScreen.NewTransaction -> TrnEvent.NewTransaction(
                type = screen.type,
                account = screen.account,
                category = screen.category
            )
        }
    ) { state, onEvent ->
        UI(state = state, onEvent = onEvent)
    }
}

@Composable
private fun UI(
    state: TrnState,

    onEvent: (TrnEvent) -> Unit
) {

}