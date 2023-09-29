package com.example.mylists.data.remote.request

import com.google.gson.annotations.SerializedName

data class RequestProductAndEanJSL(
        @SerializedName("codigo_subgrupo") val codigo_subgrupo: String = "",
        @SerializedName("ean") val ean: String,
        @SerializedName("DESCRICAO_PRODUTO") val descricao_produto: String = "",
        @SerializedName("filtro_automatico") val filtro_automatico: String = "",
        @SerializedName("lista_produto") val lista_produto: List<String> = listOf(),
)
//"produto":{
//              "codigo_subgrupo":"0103",
//              "ean":"",
//              "DESCRICAO_PRODUTO":"",
//              "filtro_automatico":"",
//              "lista_produto":null
//          }