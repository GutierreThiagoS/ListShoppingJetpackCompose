package com.example.mylists.framework.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mylists.NavigationScreen
import com.example.mylists.framework.presentation.add.AddProductFrag
import com.example.mylists.framework.presentation.products.ProductsFrag
import com.example.mylists.framework.presentation.shopping.ShoppingFrag
import com.example.mylists.framework.ui.theme.LightBlue
import com.example.mylists.framework.ui.theme.Navy
import com.example.mylists.framework.ui.theme.Teal10
import com.example.mylists.framework.ui.theme.TealBlack700
import org.koin.androidx.compose.koinViewModel

@Composable
fun BottomMenu(contentPadding: PaddingValues) {
/*
    val sizeListState by mainViewModel.sizeListState.collectAsState()

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(mainViewModel) {
        mainViewModel.getSizerLists()
    }

    if (isLoading) {
        ProgressSpinner()
    }
    */
    ListBottomMenuNavigation(contentPadding)

    /*when (sizeListState) {
        is BottomMenuState.Loading -> {
            isLoading = true
        }
        is BottomMenuState.Success -> {
            isLoading = false
            val products = (sizeListState as BottomMenuState.Success).products
            val shoppingCart = (sizeListState as BottomMenuState.Success).shoppingCart
            Log.e("Error", "$products $shoppingCart ")
            ListBottomMenuNavigation(contentPadding)
        }
        is BottomMenuState.Error -> {
            isLoading = false
            val errorMessage = (sizeListState as BottomMenuState.Error).message
            Log.e("Error", "$errorMessage ")
            // Tratar o erro de alguma forma
        }
    }*/
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ListBottomMenuNavigation(
    contentPadding: PaddingValues,
    mainViewModel: MainViewModel = koinViewModel()
) {

    val navigationState by mainViewModel.navigationState.collectAsState()

    val navController = rememberNavController()


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Teal10,
                ) {
                    mainViewModel.items.forEachIndexed { index, item ->
                        NavigationBarItem (
                            selected = navigationState.indexSelected == index,
                            onClick = {
                                mainViewModel.setNavigation(item.title, index)
                                navController.navigate(item.title)
                            },
                            label = {
                                Text(text = item.title)
                            },
                            alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        val navigationBadgeCount by mainViewModel.navigationBadgeCount(item.title).collectAsState(null)
                                        item.badgeCount = navigationBadgeCount
                                        if(item.badgeCount != null) {
                                            Badge {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        } else if(item.hasNews) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (navigationState.indexSelected == index) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            },
            contentColor = TealBlack700,
            containerColor = LightBlue,
            floatingActionButton = {
                if (navigationState.title == NavigationScreen.SHOPPING.label || navigationState.title == NavigationScreen.PRODUCTS.label) {
                    FloatingButton {
                        mainViewModel.setNavigation(NavigationScreen.ADD.label)
//                        navController.navigate(NavigationScreen.ADD.label)
                    }
                }
            }
        ) {
            Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding(), bottom = it.calculateBottomPadding())) {
                NavHostApp(navController)
            }
        }
    }
}

@Composable
fun FloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        containerColor = Navy,
        contentColor = Color.White
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Adicionar")
    }
}

@Composable
fun NavHostApp(
    navController: NavHostController
) {
    NavHost(navController, startDestination = NavigationScreen.SHOPPING.label) {
        composable(NavigationScreen.SHOPPING.label) {
            ShoppingFrag()
        }
        composable(NavigationScreen.PRODUCTS.label) {
            ProductsFrag()
        }
        composable(NavigationScreen.ADD.label) {
            AddProductFrag()
        }
        composable(NavigationScreen.SETTINGS.label) {
            FragmentMessage()
        }
    }
}