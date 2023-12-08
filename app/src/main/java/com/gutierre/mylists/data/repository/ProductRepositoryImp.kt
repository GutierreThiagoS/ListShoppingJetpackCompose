package com.gutierre.mylists.data.repository

import com.gutierre.mylists.data.local.dao.ItemShoppingDao
import com.gutierre.mylists.data.local.dao.ProductDao
import com.gutierre.mylists.domain.model.ItemShopping
import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import com.gutierre.mylists.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImp(
    private val productDao: ProductDao,
    private val itemShoppingDao: ItemShoppingDao
): ProductRepository {

    override fun insertProduct(product: Product): Long {
        return if (productDao.update(product) == 0)
            productDao.insert(product)
        else 1L
    }

    override fun productList(): Flow<List<ProductOnItemShopping>> {
        return productDao.getAllProductOnItemShoppingFlow()
    }

    override fun getProduct(description: String): Product? {
        return productDao.getProduct(description)
    }

    override fun getProductId(id: Int): Product? {
        return productDao.getProduct(id)
    }

    override fun getAllBrand(): Flow<List<String>> {
        return productDao.getAllBrand()
    }

    override fun insertShopping(shopping: ItemShopping): ItemShopping? {

        if (shopping.quantity <= -1) {
            val itemDelete = itemShoppingDao.consultItemShopping(shopping.idProductFK)
            if (itemDelete != null) {
                itemShoppingDao.delete(itemDelete)
            }
        }
        else if(itemShoppingDao.update(shopping) == 0)
            itemShoppingDao.insert(shopping)
        return itemShoppingDao.consultItemShopping(shopping.idProductFK)
    }

    override fun removerProduct(product: ProductOnItemShopping): String {
        val itemShopping = itemShoppingDao.consultItemShopping(productId = product.idProduct)
        return if (itemShopping == null){
            productDao.deleteId(productId = product.idProduct)
            "Produto Deletado!"
        } else {
            itemShoppingDao.delete(itemShopping)
            "Esse produto esta no Carrinho!"
        }
    }
}