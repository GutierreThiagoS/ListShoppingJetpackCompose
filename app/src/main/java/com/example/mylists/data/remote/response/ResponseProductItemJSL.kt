package com.example.mylists.data.remote.response

import com.example.mylists.domain.model.JSL.GroupJSL
import com.example.mylists.domain.model.JSL.SubGroupJSL
import com.example.mylists.domain.model.JSL.TablePriceJSL
import com.google.gson.annotations.SerializedName

data class ResponseProductItemJSL(
    @SerializedName("codigo") val productId: String,
    @SerializedName("descricao") val descricao: String,
    @SerializedName("unidade") val unidade: String,
    @SerializedName("unidadeDesc") val unidadeDesc: String,
    @SerializedName("descricaoUni") val descricaoUni: String,
    @SerializedName("descricao_lista") val descricao_lista: String?,
    @SerializedName("codigobarra") val codigobarra: String,
    @SerializedName("descMarca") val descMarca: String,
    @SerializedName("fornec") val fornec: String,
    @SerializedName("desconto_maximo") val desconto_maximo: String?,
    @SerializedName("obs_desconto") val obs_desconto: String,
    @SerializedName("ativador_campanha") val ativador_campanha: String,
    @SerializedName("codigo_campanha") val codigo_campanha: String,
    @SerializedName("tipo_produto") val tipo_produto: String,
    @SerializedName("quantLimiteProd") val quantLimiteProd: Int,
    @SerializedName("favorito") val favorito: Boolean,
    @SerializedName("estoque") val estoque: String,
    @SerializedName("avisame") val avisame: Boolean,
    @SerializedName("previsao_estoque") val previsao_estoque: String,
    @SerializedName("multiplo_venda") val multiplo_venda: String,
    @SerializedName("quantidade_ocorrencia_produto") val quantidade_ocorrencia_produto: String,
    @SerializedName("galeria_produto") val galeria_produto: String,
    @SerializedName("galeria_produto_medium") val galeria_produto_medium: List<String>,
    @SerializedName("galeria_produto_large") val galeria_produto_large: List<String>,
    @SerializedName("galeria_video_url") val galeria_video_url: String,
    @SerializedName("grupo") val group: GroupJSL,
    @SerializedName("subgrupo") val subGroup: SubGroupJSL,
    @SerializedName("tabeladepreco") val tabelaDePreco: List<TablePriceJSL>,
    @SerializedName("agrupamento") val agrupamento: String,
    @SerializedName("preco_escalonado") val preco_escalonado: List<ResponsePriceEscalonadoJSL>,
    @SerializedName("sugestao") val sugestao: List<ResponseProductItemJSL>?,
)
//        "galeria_produto_large": [
//        "http://midias.guarany.com.br/jsleiman/private/fotos/large/7896522033991_1.png",
//        "http://midias.guarany.com.br/jsleiman/private/fotos/large/7896522033991_2.png",
//        "http://midias.guarany.com.br/jsleiman/private/fotos/large/7896522033991_3.png"
//        ],
//        "galeria_video_url": "",
//       "grupo": {
//         "codigo": "01",
//         "descricao": "CABELOS",
//         "imagem_grupo": "http://midias.guarany.com.br/jsleiman/private/fotos/small/01_1.png",
//         "imagem_grupo_medium": "http://midias.guarany.com.br/jsleiman/private/fotos/medium/01_1.png"
//    },
//      "subgrupo": {
//         "codigo": "0103",
//         "descricao": "CABELO - ANTICASPA",
//         "imagem_grupo": "http://midias.guarany.com.br/jsleiman/private/fotos/small/0103_1.png",
//         "imagem_grupo_medium": "http://midias.guarany.com.br/jsleiman/private/fotos/medium/0103_1.png"
//    },
//        "tabeladepreco": [
//        {
//            "codigo_tabela_preco": "001",
//            "produto_id": "7318302",
//            "valor_tabela_preco": "16.19"
//        }
//        ],
//        "agrupamento": "",
//        "preco_escalonado": [],
//        "sugestao": []
//    },
