package com.example.mylists.domain.model.JSL

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Gutierre Guimarães on 01/07/2022.
 */
@Entity
data class GroupJSL(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @SerializedName("codigo") val groupId: String,
    @SerializedName("descricao") val descricao: String,
    @SerializedName("imagem_grupo_medium") var imagemGrupoMedium: String,
    @SerializedName("imagem_grupo") var imagemUrl: String,
)

// "codigo": "01",
// "descricao": "CABELOS",
// "imagem_grupo": "http://midias.guarany.com.br/jsleiman/private/fotos/small/01_1.png",
// "imagem_grupo_medium": "http://midias.guarany.com.br/jsleiman/private/fotos/medium/01_1.png"