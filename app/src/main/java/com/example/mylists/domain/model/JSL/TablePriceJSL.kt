package com.example.mylists.domain.model.JSL

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Gutierre Guimarães on 05/08/2022.
 */
@Entity
data class TablePriceJSL(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @SerializedName("codigo_tabela_preco") val codigo_tabela_preco: String,
        @SerializedName("produto_id") val produto_id: String,
        @SerializedName("valor_tabela_preco") val valor_tabela_preco: String,
        @SerializedName("tipo") var tipo: String? = "",
        @SerializedName("tipoSugestao") var tipoSugestao: String? = "",
)
//            "codigo_tabela_preco": "001",
//            "produto_id": "7318302",
//            "valor_tabela_preco": "16.19"
