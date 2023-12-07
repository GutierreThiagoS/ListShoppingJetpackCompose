package com.gutierre.mylists.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseLoadProductJSL(
        @SerializedName("produto") val produto: List<ResponseProductItemJSL>,
        @SerializedName("status") val status: Boolean
)//[{ "produto": [{
//        "codigo": "7318302",
//        "descricao": "CONDICIONADOR CAPICILIN ANTICASPA 250ML TODOS OS TIPOS DE CABELO",
//        "unidade": "UN",
//        "unidadeDesc": "UNIDADES",
//        "descricaoUni": "Venda em: UNIDADE",
//        "codigobarra": "7896522033991",
//        "descMarca": "CAPICILIN",
//        "fornec": "00015500",
//        "desconto_maximo": "0",
//        "obs_desconto": "",
//        "ativador_campanha": "N",
//        "codigo_campanha": "",
//        "tipo_produto": "01",
//        "quantLimiteProd": 99999999,
//        "favorito": false,
//        "estoque": "162",
//        "avisame": true,
//        "previsao_estoque": "Possui Estoque",
//        "multiplo_venda": "3",
//        "quantidade_ocorrencia_produto": "0",
//        "galeria_produto": "http://midias.guarany.com.br/jsleiman/private/fotos/small/7896522033991_1.png",
//        "galeria_produto_medium": [
//        "http://midias.guarany.com.br/jsleiman/private/fotos/medium/7896522033991_1.png",
//        "http://midias.guarany.com.br/jsleiman/private/fotos/medium/7896522033991_2.png",
//        "http://midias.guarany.com.br/jsleiman/private/fotos/medium/7896522033991_3.png"
//        ],
//        "galeria_produto_large": [
//        "http://midias.guarany.com.br/jsleiman/private/fotos/large/7896522033991_1.png",
//        "http://midias.guarany.com.br/jsleiman/private/fotos/large/7896522033991_2.png",
//        "http://midias.guarany.com.br/jsleiman/private/fotos/large/7896522033991_3.png"
//        ],
//        "galeria_video_url": "",
//        "grupo": {
//        "codigo": "01",
//        "descricao": "CABELOS",
//        "imagem_grupo": "http://midias.guarany.com.br/jsleiman/private/fotos/small/01_1.png",
//        "imagem_grupo_medium": "http://midias.guarany.com.br/jsleiman/private/fotos/medium/01_1.png"
//    },
//        "subgrupo": {
//        "codigo": "0103",
//        "descricao": "CABELO - ANTICASPA",
//        "imagem_grupo": "http://midias.guarany.com.br/jsleiman/private/fotos/small/0103_1.png",
//        "imagem_grupo_medium": "http://midias.guarany.com.br/jsleiman/private/fotos/medium/0103_1.png"
//    },
//        "tabeladepreco": [{
//            "codigo_tabela_preco": "001",
//            "produto_id": "7318302",
//            "valor_tabela_preco": "16.19"
//        }],
//        "agrupamento": "",
//        "preco_escalonado": [],
//        "sugestao": []
//  },],
//  "status": true
//}]