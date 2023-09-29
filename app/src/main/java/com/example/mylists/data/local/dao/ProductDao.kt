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

    @Query("SELECT * FROM Product")
    fun getAll(): List<Product>

    @Query("SELECT DISTINCT(brandProduct) FROM Product ORDER BY brandProduct ASC")
    fun getAllBrand(): Flow<List<String>>

    @Update
    fun update(product: Product): Int

    @Query("UPDATE Product SET description = :description, price = :price WHERE idProduct = :productId")
    fun updatePrice(productId: Int, description: String, price: Float): Int

    @Query("DELETE FROM Product WHERE idProduct = :productId")
    fun deleteId(productId: Int)

    @Query("""
        SELECT * FROM Product 
        WHERE description = :description
        ORDER BY description ASC
    """)
    fun getProduct(description: String): Product?

    @Query("""
        SELECT * FROM Product 
        WHERE ean = :barcode
    """)
    fun getProductBarCode(barcode: String): Product?

    @Query("SELECT * FROM Product WHERE idProduct = :id")
    fun getProduct(id: Int): Product?

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
        WHERE p.ean = :barcode OR :barcode = ''
        GROUP BY P.idProduct
        ORDER BY description ASC
        """)
    fun listAllProductOnItemShopping(barcode: String = ""): List<ProductOnItemShopping>

    @Query("SELECT COUNT(*) FROM Product P")
    fun countProductOnItemShopping(): Flow<Int?>
}