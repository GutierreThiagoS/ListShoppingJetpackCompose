package com.example.mylists.di

import com.example.mylists.framework.presentation.add.AddNewProductViewModel
import com.example.mylists.framework.presentation.configuration.ConfigurationViewModel
import com.example.mylists.framework.presentation.products.ProductViewModel
import com.example.mylists.framework.presentation.shopping.ShoppingViewModel
import com.example.mylists.framework.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        ProductViewModel(
            repository = get()
        )
    }

    viewModel {
        ShoppingViewModel(
            repository = get()
        )
    }

    viewModel {
        AddNewProductViewModel(
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
            shoppingRepository = get()
        )
    }

    /*viewModel { SettingViewModel() }*/
}