package com.example.mylists.framework.composable

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
import com.example.mylists.App
import com.example.mylists.NavigationScreen
import com.example.mylists.domain.model.Product
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.framework.presentation.add.AddProductFrag
import com.example.mylists.framework.presentation.add_to_do.AddToDoList
import com.example.mylists.framework.presentation.configuration.ConfigurationFrag
import com.example.mylists.framework.presentation.products.ProductsFrag
import com.example.mylists.framework.presentation.shopping.ShoppingFrag
import com.example.mylists.framework.presentation.to_do.ToDoListFrag
import com.example.mylists.framework.ui.main.MainViewModel
import com.example.mylists.framework.ui.theme.LightBlue
import com.example.mylists.framework.ui.theme.Navy
import com.example.mylists.framework.ui.theme.Teal10
import com.example.mylists.framework.ui.theme.TealBlack700
import org.koin.androidx.compose.koinViewModel


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomMenu(
    barcode: String?,
    contentPadding: PaddingValues,
    mainViewModel: MainViewModel = koinViewModel()
) {

    LaunchedEffect(mainViewModel){
        mainViewModel.checkProduct()
    }

    val navigationState by mainViewModel.navigationState.collectAsState()

    val navController = rememberNavController()

    val productState by mainViewModel.productState.collectAsState()
    val categoryState by mainViewModel.categoryState.collectAsState()

    barcode?.let {
        mainViewModel.getBarCodeInProduct(barcode) {
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
            containerColor = LightBlue,
            floatingActionButton = {
                if (navigationState.title == NavigationScreen.SHOPPING.label ||
                    navigationState.title == NavigationScreen.PRODUCTS.label ||
                    navigationState.title == NavigationScreen.TO_DO.label
                ) {
                    FloatingButton {
                        if (navigationState.title == NavigationScreen.TO_DO.label ) {
                            mainViewModel.setBarCode(null)
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
                    productBarCode = productState,
                    categoryState = categoryState,
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
                translationY = 100f
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
    productBarCode: Product? = null,
    categoryState: String? = null,
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
            ToDoListFrag()
        }
        composable(NavigationScreen.ADD_PRODUCT.label) {
            AddProductFrag(
                productBarCode = productBarCode,
                categoryState = categoryState,
                returnDest = returnDest
            )
        }
        composable(NavigationScreen.ADD_TO_DO.label) {
            AddToDoList()
        }
        composable(NavigationScreen.SETTINGS.label) {
            ConfigurationFrag()
        }
    }
}