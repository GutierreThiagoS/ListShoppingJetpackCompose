package com.gutierre.mylists.framework.ui.firebase

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierre.mylists.domain.model.ProductFireBase
import com.gutierre.mylists.domain.repository.FireStoreRepository
import com.gutierre.mylists.framework.utils.coroutineExceptionHandler
import com.gutierre.mylists.framework.utils.logE
import com.gutierre.mylists.state.StateInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FireBaseViewModel(
    private val fireStoreRepository: FireStoreRepository
): ViewModel() {

//    private val _mutableFireStore = MutableStateFlow<List<ProductFireBase>>(emptyList())

    val flowFireBaseStore: Flow<List<ProductFireBase>> = fireStoreRepository.getFireBaseAllFlow()

    fun getFireBaseStore() {
        fireStoreRepository.getFirebaseStore(
            viewModelScope,
        ) {
            logE("Error $it")
        }
    }

    fun saveProductFireBase(product: ProductFireBase, context: Context) {
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