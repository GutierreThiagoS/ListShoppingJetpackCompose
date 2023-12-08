package com.gutierre.mylists.framework.composable

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gutierre.mylists.App
import com.gutierre.mylists.NavigationScreen
import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import com.gutierre.mylists.framework.presentation.add.AddProductFrag
import com.gutierre.mylists.framework.presentation.add_to_do.AddToDoList
import com.gutierre.mylists.framework.presentation.configuration.ConfigurationFrag
import com.gutierre.mylists.framework.presentation.products.ProductsFrag
import com.gutierre.mylists.framework.presentation.shopping.ShoppingFrag
import com.gutierre.mylists.framework.presentation.to_do.ToDoListFrag
import com.gutierre.mylists.framework.ui.main.MainViewModel
import com.gutierre.mylists.framework.ui.theme.Navy
import com.gutierre.mylists.framework.ui.theme.Teal30
import com.gutierre.mylists.framework.ui.theme.TealBlack700
import com.gutierre.mylists.framework.ui.theme.VeryLightGray

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomMenu(
    contentPadding: PaddingValues,
    mainViewModel: MainViewModel
) {
    val barCodeState by mainViewModel.barCodeState.collectAsState()

    LaunchedEffect(mainViewModel){
        mainViewModel.checkProduct()
    }

    val navigationState by mainViewModel.navigationState.collectAsState()

    val navController = rememberNavController()

    barCodeState?.let { barCode ->
        mainViewModel.getBarCodeInProduct(barCode) {
            if (it.isBlank()) {
                mainViewModel.setNavigation(NavigationScreen.ADD_PRODUCT.label)
                navController.navigate(NavigationScreen.ADD_PRODUCT.label)
            } else Toast.makeText(App.context, it, Toast.LENGTH_LONG).show()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White
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
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = Color.Black,
                                indicatorColor = Teal30,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                                ),
                            alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        val navigationBadgeCount by mainViewModel.navigationBadgeCount(item.title).collectAsState(null)
                                        item.badgeCount = navigationBadgeCount?.takeIf { it > 0 }
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
            containerColor = VeryLightGray,
            floatingActionButton = {
                if (navigationState.title == NavigationScreen.SHOPPING.label ||
                    navigationState.title == NavigationScreen.PRODUCTS.label ||
                    navigationState.title == NavigationScreen.TO_DO.label
                ) {
                    FloatingButton {
                        if (navigationState.title == NavigationScreen.TO_DO.label ) {
                            mainViewModel.setBarCode(null)
                            mainViewModel.setToDoEdit(null)
                            mainViewModel.setNavigation(NavigationScreen.ADD_TO_DO.label)
                            navController.navigate(NavigationScreen.ADD_TO_DO.label)
                        } else {
                            mainViewModel.setNavigation(NavigationScreen.ADD_PRODUCT.label)
                            navController.navigate(NavigationScreen.ADD_PRODUCT.label)
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) {
            Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding(), bottom = it.calculateBottomPadding())) {
                NavHostApp(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    returnDest =  { destination ->
                        mainViewModel.setBarCode(null)
                        if (destination.isNotBlank()) {
                            navController.navigate(destination)
                            mainViewModel.setNavigation(destination)
                        }
                    },
                    editProductClick = { product ->
                        mainViewModel.setProductEdit(
                            product = Product(
                                idProduct = product.idProduct,
                                description = product.description,
                                imgProduct = product.imgProduct,
                                brandProduct = product.brandProduct,
                                idCategoryFK = product.idCategory,
                                ean = product.ean,
                                price = product.price
                            ),
                            categoryName = product.nameCategory
                        )
                        mainViewModel.setNavigation(NavigationScreen.ADD_PRODUCT.label)
                        navController.navigate(NavigationScreen.ADD_PRODUCT.label)
                    }
                )
            }
        }
    }
}

@Composable
fun FloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier
            .size(56.dp)
            .graphicsLayer(
                translationY = 80f
            ),
        shape = CircleShape,
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
    navController: NavHostController,
    mainViewModel: MainViewModel,
    returnDest: (destination: String) -> Unit,
    editProductClick: (product: ProductOnItemShopping) -> Unit
) {
    NavHost(navController, startDestination = NavigationScreen.SHOPPING.label) {
        composable(NavigationScreen.SHOPPING.label) {
            ShoppingFrag(editProductClick = editProductClick)
        }
        composable(NavigationScreen.PRODUCTS.label) {
            ProductsFrag(editProductClick = editProductClick)
        }
        composable(NavigationScreen.TO_DO.label) {
            ToDoListFrag(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
        composable(NavigationScreen.ADD_PRODUCT.label) {
            AddProductFrag(
                mainViewModel = mainViewModel,
                returnDest = returnDest
            )
        }
        composable(NavigationScreen.ADD_TO_DO.label) {
            AddToDoList(
                mainViewModel = mainViewModel,
            )
        }
        composable(NavigationScreen.SETTINGS.label) {
            ConfigurationFrag()
        }
    }
}