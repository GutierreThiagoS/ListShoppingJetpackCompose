package com.example.mylists.data.remote.request

import com.google.gson.annotations.SerializedName

data class RequestFiltroProductJSL(
        @SerializedName("produto") val produto: RequestProductAndEanJSL
)
//"produto":{
//              "codigo_subgrupo":"0103",
//              "ean":"",
//              "DESCRICAO_PRODUTO":"",
//              "filtro_automatico":"",
//              "lista_produto":null
//          }