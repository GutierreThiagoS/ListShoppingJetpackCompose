package com.gutierre.mylists.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponsePriceEscalonadoJSL(
        @SerializedName("codigo_tabela_preco") val codigo_tabela_preco: String,
        @SerializedName("percentual_desconto") val percentual_desconto: String,
        @SerializedName("produto_id") val produto_id: String,
        @SerializedName("quantidade_faixa_final") val quantidade_faixa_final: String,
        @SerializedName("quantidade_faixa_inicial") val quantidade_faixa_inicial: String,
)
