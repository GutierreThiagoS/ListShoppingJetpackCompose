package com.example.mylists.data.remote.request

import com.google.gson.annotations.SerializedName

data class RequestLoadProductJSL(
        @SerializedName("chave") val chave: String = "02705102",
        @SerializedName("cpf") val cpf: String = "04778597397",
        @SerializedName("tag") val tag: String = "produto",
        @SerializedName("empresa_id") val empresa_id: Int,
        @SerializedName("versao") val version: String = if (empresa_id == 2) "1.0.63" else "2.0.83",
        @SerializedName("offset") val offset: Int = 0,
        @SerializedName("filtro") val filtro: List<RequestFiltroProductJSL>,
)
// [{"chave":"02705102",
//  "cpf":"04778597397",
//  "tag":"produto",
//  "versao":"1.0.35",
//  "empresa_id":2,
//  "offset":0,
//  "filtro":[{
//          "produto":{
//              "codigo_subgrupo":"0103",
//              "ean":"",
//              "DESCRICAO_PRODUTO":"",
//              "filtro_automatico":"",
//              "lista_produto":null
//          }
//   }]
// }]
