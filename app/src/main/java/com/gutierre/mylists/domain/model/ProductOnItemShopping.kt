package com.gutierre.mylists.domain.model

import android.os.Parcel
import android.os.Parcelable

data class ProductOnItemShopping(
    val idItem: Int = 0,
    val idProduct: Int,
    val description: String,
    val imgProduct: String,
    val brandProduct: String,
    val idCategory: Int,
    val nameCategory: String,
    val ean: String,
    val price: Float,
    var quantity: Int = 0,
    var selected: Boolean = false
): Parcelable {

    constructor(parcel: Parcel): this(
        idItem = parcel.readInt(),
        idProduct = parcel.readInt(),
        description =  parcel.readString() ?: "",
        imgProduct =  parcel.readString() ?: "",
        brandProduct =  parcel.readString() ?: "",
        idCategory = parcel.readInt(),
        nameCategory =  parcel.readString() ?: "",
        ean =  parcel.readString() ?: "",
        price = parcel.readFloat(),
        quantity = parcel.readInt(),
        selected = parcel.readByte() != 0.toByte()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flag: Int) {
        parcel.writeInt(idItem)
        parcel.writeInt(idProduct)
        parcel.writeString(description)
        parcel.writeString(imgProduct)
        parcel.writeString(brandProduct)
        parcel.writeInt(idCategory)
        parcel.writeString(nameCategory)
        parcel.writeString(ean)
        parcel.writeFloat(price)
        parcel.writeInt(quantity)
        parcel.writeByte(if (selected) 1 else 0)
    }

    companion object CREATOR: Parcelable.Creator<ProductOnItemShopping> {
            override fun createFromParcel(parcel: Parcel): ProductOnItemShopping {
                return ProductOnItemShopping(parcel)
            }

            override fun newArray(size: Int): Array<ProductOnItemShopping?> {
                return arrayOfNulls(size)
            }
        }

}
