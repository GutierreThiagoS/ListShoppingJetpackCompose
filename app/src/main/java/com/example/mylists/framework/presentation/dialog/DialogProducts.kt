package com.example.mylists.framework.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.framework.composable.ShoppingProductListItem

@Composable
fun DialogProducts(
    dialogList: List<ProductOnItemShopping>,
    onDismiss: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (dialogList.size > 1) "Produto" else "Produtos",
                            style = MaterialTheme.typography.titleMedium
                        )

                        LazyColumn {

                            itemsIndexed(dialogList) { index, product ->
                                ShoppingProductListItem(
                                    product = product,
                                    offsetXGlobal = offsetX,
                                    setOffsetXGlobal = { o, i -> offsetX = o },
                                    index = index,
                                    isVisibleCheck = false)
                            }
                        }

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    )
}