package com.example.mylists.data.local.dao

import androidx.room.*
import com.example.mylists.domain.model.Product
import com.example.mylists.domain.model.ProductOnItemShopping
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg product: Product)

    @Update
    fun update(product: Product): Int

    @Query("DELETE FROM Product WHERE idProduct = :productId")
    fun deleteId(productId: Int)

    @Query("""
        SELECT * FROM Product 
        WHERE description = :description
        ORDER BY description ASC
    """)
    fun getProduct(description: String): Product?

    @Query("""
        SELECT S.idItem as idItem, P.idProduct, P.description, P.imgProduct, P.brandProduct,
        C.idCategory, C.nameCategory, P.ean, P.price, S.quantity, S.selected FROM Product P 
        LEFT JOIN ItemShopping S ON (S.idProductFK = P.idProduct)
        LEFT JOIN Category C ON C.idCategory = P.idCategoryFK
        GROUP BY P.idProduct
        ORDER BY description ASC
        """)
    fun getAllProductOnItemShoppingFlow(): Flow<List<ProductOnItemShopping>>

    @Query("""
        SELECT S.idItem as idItem, P.idProduct, P.description, P.imgProduct, P.brandProduct,
        C.idCategory, C.nameCategory, P.ean, P.price, S.quantity, S.selected FROM Product P 
        LEFT JOIN ItemShopping S ON (S.idProductFK = P.idProduct)
        LEFT JOIN Category C ON C.idCategory = P.idCategoryFK
        GROUP BY P.idProduct
        ORDER BY description ASC
        """)
    fun listAllProductOnItemShopping(): List<ProductOnItemShopping>

    @Query("SELECT COUNT(*) FROM Product P")
    fun countProductOnItemShopping(): Flow<Int?>
}