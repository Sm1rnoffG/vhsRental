package com.example.vhsrental.ui.screens.spine

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import com.example.vhsrental.R
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.UpdateAccountActions

@Composable
fun BottomBar(vm: LoginViewModel) {
    val state = vm.uiStateFlow.collectAsState().value
    if (state !is LoginUiState.LoggedIn || state.selectedTab == Tab.AccountDetail) return

    val navBarItems = mutableListOf(
        Pair(painterResource(id = R.drawable.baseline_menu_book_24), "Movie Catalogue"),
        Pair(painterResource(id = R.drawable.baseline_checklist_24), "My Orders")
    )
    if (state.user.role == Role.Employee) {
        navBarItems.addAll(listOf(
            Pair(painterResource(id = R.drawable.baseline_storefront_24), "Manage Orders"),
            Pair(painterResource(id = R.drawable.baseline_manage_accounts_24), "Manage Users")
        ))
    }

    NavigationBar {
        navBarItems.forEachIndexed { index, pair ->
            NavigationBarItem(
                icon = { Icon(painter = pair.first, contentDescription = "icon $index") },
                label = { Text(text = pair.second) },
                selected = state.selectedTab.ordinal == index,
                onClick = { vm.emitActionLoggedIn(UpdateAccountActions.OnSwitchTab(Tab.entries[index])) }
            )
        }
    }
}