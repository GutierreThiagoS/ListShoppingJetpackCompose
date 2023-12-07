package com.gutierre.mylists.domain.model.JSL

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Gutierre Guimarães on 01/07/2022.
 */
@Entity
data class SubGroupJSL(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("codigo") val subGroupId: String,
    @SerializedName("descricao") val descricao: String,
    @SerializedName("grupo") val groupId: String,
    @SerializedName("quantidade") val quantity: String = "",
    @SerializedName("imagem_grupo_medium") var imagemGrupoMedium: String,
    @SerializedName("imagem_grupo") var imagemUrl: String,
)
//        "codigo": "0307",
//        "descricao": "DESODORANTES - AEROSOL",
//        "grupo": "03",
//        "quantidade": "197",
//        "imagem_grupo": "http://midias.guarany.com.br/jsleiman/private/fotos/small/0307_1.png",
//        "imagem_grupo_medium": "http://midias.guarany.com.br/jsleiman/private/fotos/medium/0307_1.png"
