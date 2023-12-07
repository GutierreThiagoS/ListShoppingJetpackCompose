package com.gutierre.mylists.framework.ui.firebase

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.domain.repository.FireStoreRepository
import com.gutierre.mylists.framework.utils.coroutineExceptionHandler
import com.gutierre.mylists.framework.utils.logE
import com.gutierre.mylists.state.StateInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FireBaseViewModel(
    private val fireStoreRepository: FireStoreRepository
): ViewModel() {

    private val _mutableFireStore = MutableStateFlow<List<Product>>(emptyList())

    val flowFireBaseStore: StateFlow<List<Product>> = _mutableFireStore

    fun getFireBaseStore() {
        fireStoreRepository.getFirebaseStore(result = {
            _mutableFireStore.tryEmit(it)
        }, error = {
            logE("Error $it")
        })
    }

    fun saveProductFireBase(product: Product, context: Context) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler("saveProductFireBase")){
            val state = fireStoreRepository.saveProductDataBase(product)
            withContext(Dispatchers.Main) {
                when(state) {
                    is StateInfo.Success -> Toast.makeText(context, state.info, Toast.LENGTH_SHORT).show()
                    is StateInfo.Error -> AlertDialog.Builder(context).apply {
                        setTitle("Atenção!")
                        setMessage(state.info + "${state.exception ?: ""}")
                        setNeutralButton("OK") { d, _ ->
                            d.dismiss()
                        }
                        show()
                    }
                }
            }
        }
    }
}