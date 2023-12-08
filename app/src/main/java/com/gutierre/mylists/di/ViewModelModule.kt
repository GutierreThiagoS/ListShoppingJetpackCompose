package com.gutierre.mylists.di

import com.gutierre.mylists.framework.presentation.add.AddNewProductViewModel
import com.gutierre.mylists.framework.presentation.configuration.ConfigurationViewModel
import com.gutierre.mylists.framework.presentation.products.ProductViewModel
import com.gutierre.mylists.framework.presentation.shopping.ShoppingViewModel
import com.gutierre.mylists.framework.presentation.to_do.ToDoViewModel
import com.gutierre.mylists.framework.ui.firebase.FireBaseViewModel
import com.gutierre.mylists.framework.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        ProductViewModel(
            repository = get(),
            repositoryFireBase = get()
        )
    }

    viewModel {
        ShoppingViewModel(
            repository = get()
        )
    }

    viewModel {
        AddNewProductViewModel(
            repository = get(),
            repositoryCategory = get()
        )
    }

    viewModel {
        ToDoViewModel(
            repository = get()
        )
    }

    viewModel {
        ConfigurationViewModel(
            repository = get()
        )
    }

    viewModel {
        MainViewModel(
            shoppingRepository = get(),
            toDoRepository = get()
        )
    }

    viewModel {
        FireBaseViewModel(
            fireStoreRepository = get()
        )
    }

}