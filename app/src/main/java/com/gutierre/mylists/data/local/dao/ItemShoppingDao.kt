package com.gutierre.mylists.data.local.dao

import androidx.room.*
import com.gutierre.mylists.domain.model.ItemShopping
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemShoppingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemShopping: ItemShopping): Long

    @Update
    fun update(itemShopping: ItemShopping): Int

    @Delete
    fun delete(itemShopping: ItemShopping)

    @Query("""
        SELECT P.price * I.quantity FROM ItemShopping I LEFT JOIN Product P 
        ON I.idProductFK = P.idProduct
        WHERE I.selected = 1
    """)
    fun priceProductShopping(): Float

    @Query("SELECT COUNT(*) FROM ItemShopping WHERE idProductFK = :productId")
    fun consultItemShoppingCount(productId: Int): Int

    @Query("""
        SELECT * FROM ItemShopping 
        WHERE idProductFK = :productId
        LIMIT 1
    """)
    fun consultItemShopping(productId: Int): ItemShopping?

    @Query("""
        SELECT S.idItem, P.idProduct, P.description, P.imgProduct, P.brandProduct,
        C.idCategory, C.nameCategory, P.ean, P.price, S.quantity, S.selected
        FROM ItemShopping S INNER JOIN Product P 
        ON S.idProductFK = P.idProduct INNER JOIN Category C
        ON P.idCategoryFK = C.idCategory
    """)
    fun getListProductOnItemShopping(): List<ProductOnItemShopping>

    @Query("SELECT COUNT(*)FROM ItemShopping")
    fun countProductOnItemShopping(): Flow<Int?>

    @Query("""
        SELECT S.idItem, P.idProduct, P.description, P.imgProduct, P.brandProduct,
        C.idCategory, C.nameCategory, P.ean, P.price, S.quantity, S.selected
        FROM ItemShopping S INNER JOIN Product P 
        ON S.idProductFK = P.idProduct INNER JOIN Category C
        ON P.idCategoryFK = C.idCategory
    """)
    fun getListProductOnItemShoppingFlow(): Flow<List<ProductOnItemShopping>>

    @Query("""
        SELECT SUM(P.price * S.quantity)
        FROM ItemShopping S INNER JOIN Product P 
        ON S.idProductFK = P.idProduct INNER JOIN Category C
        ON P.idCategoryFK = C.idCategory
    """)
    fun getTotalProductOnItemShopping(): Flow<Float?>
}