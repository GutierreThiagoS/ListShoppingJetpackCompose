package com.example.mylists.data.repository

import android.util.Log
import com.example.mylists.NavigationScreen
import com.example.mylists.data.local.dao.CategoryDao
import com.example.mylists.data.local.dao.ItemShoppingDao
import com.example.mylists.data.local.dao.ProductDao
import com.example.mylists.domain.model.Category
import com.example.mylists.domain.model.ItemShopping
import com.example.mylists.domain.model.Product
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.domain.repository.ShoppingRepository
import com.example.mylists.sealed_class.BottomMenuState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.Exception

class ShoppingRepositoryImp(
    private val itemShoppingDao: ItemShoppingDao,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
): ShoppingRepository {

    override fun getList(): Flow<List<ProductOnItemShopping>> {
        return itemShoppingDao.getListProductOnItemShoppingFlow()
    }

    override fun getTotal(): Flow<Float?> {
        return try {
            itemShoppingDao.getTotalProductOnItemShopping()
        } catch (e: Exception) {
            Log.e("Error", "$e")
            flowOf(0f)
        }
    }

    override fun update(itemShopping: ItemShopping): Int{
        if (itemShopping.quantity <= 0)
            itemShoppingDao.delete(itemShoppingDao.consultItemShopping(itemShopping.idProductFK)!!)
        else if (itemShoppingDao.update(itemShopping) == 0)
            itemShoppingDao.insert(itemShopping)
        return itemShoppingDao.update(itemShopping)
    }

    override fun editPriceProduct(productOnItemShopping: ProductOnItemShopping): Int {
        with(productOnItemShopping) {
            return productDao.update(
                Product(
                    idProduct,
                    description,
                    imgProduct,
                    brandProduct,
                    idCategory,
                    ean,
                    price
                )
            )
        }
    }

    override suspend fun sizeListsProdAndShopping(): BottomMenuState {
        return try {
           /* if (productDao.listAll().isEmpty()) {
                productDao.insertAll(*mockProducts().toTypedArray())
            }
            if (categoryDao.getList().isEmpty()) {
                categoryDao.insertAll(*categories.toTypedArray())
            }*/
            BottomMenuState.Success(
                shoppingCart = itemShoppingDao.getListProductOnItemShopping(),
                products = productDao.listAllProductOnItemShopping()
            )
        } catch (e: Exception) {
            Log.e("Error", "sizeListsProdAndShopping $e")
            BottomMenuState.Error("Erro ")
        }
    }

    override fun navigationBadgeCount(title: String): Flow<Int?> {
        return when(title) {
            NavigationScreen.SHOPPING.label -> itemShoppingDao.countProductOnItemShopping()
            NavigationScreen.PRODUCTS.label -> productDao.countProductOnItemShopping()
            else -> flowOf(null)
        }
    }


    fun mockProducts(): List<Product> {
        return listOf(
            Product(
                description = "Arroz 1kg",
                imgProduct = "url_arroz.png",
                brandProduct = "Tio João",
                idCategoryFK = 1, // Suponhamos que a categoria "Alimento" tenha id 1
                ean = "1234567890123",
                price = 5.99f
            ),
            Product(
                description = "Leite Integral 1L",
                imgProduct = "url_leite.png",
                brandProduct = "Nestlé",
                idCategoryFK = 1,
                ean = "2345678901234",
                price = 3.49f
            ),
            // ... adicione mais produtos aqui
            Product(
                description = "Sabonete Líquido 250ml",
                imgProduct = "url_sabonete.png",
                brandProduct = "Dove",
                idCategoryFK = 2, // Suponhamos que a categoria "Higiene" tenha id 2
                ean = "3456789012345",
                price = 2.99f
            ),
            Product(
                description = "Óleo de Soja 900ml",
                imgProduct = "url_oleo.png",
                brandProduct = "Liza",
                idCategoryFK = 1,
                ean = "4567890123456",
                price = 2.99f
            ),
            Product(
                description = "Frango Congelado 1kg",
                imgProduct = "url_frango.png",
                brandProduct = "Sadia",
                idCategoryFK = 1,
                ean = "5678901234567",
                price = 10.99f
            ),
            Product(
                description = "Maçã - 1 unidade",
                imgProduct = "url_maca.png",
                brandProduct = "Fazenda Feliz",
                idCategoryFK = 10, // Suponhamos que a categoria "Frutas" tenha id 2
                ean = "6789012345678",
                price = 1.49f
            ),
            Product(
                description = "Pão de Forma - 500g",
                imgProduct = "url_pao.png",
                brandProduct = "Wickbold",
                idCategoryFK = 3, // Suponhamos que a categoria "Padaria" tenha id 3
                ean = "7890123456789",
                price = 3.29f
            ),
            Product(
                description = "Sabonete Líquido 250ml",
                imgProduct = "url_sabonete.png",
                brandProduct = "Dove",
                idCategoryFK = 2, // Suponhamos que a categoria "Higiene" tenha id 4
                ean = "8901234567890",
                price = 2.99f
            ),
            Product(
                description = "Detergente Líquido 500ml",
                imgProduct = "url_detergente.png",
                brandProduct = "Ypê",
                idCategoryFK = 4, // Suponhamos que a categoria "Limpeza" tenha id 5
                ean = "9012345678901",
                price = 1.89f
            ),
            Product(
                description = "Champô 400ml",
                imgProduct = "url_shampoo.png",
                brandProduct = "Pantene",
                idCategoryFK = 4,
                ean = "0123456789012",
                price = 8.99f
            ),Product(
                description = "Leite Condensado 395g",
                imgProduct = "url_leite_condensado.png",
                brandProduct = "Nestlé",
                idCategoryFK = 6, // Suponhamos que a categoria "Leites e Derivados" tenha id 6
                ean = "1234567890123",
                price = 3.79f
            ),
            Product(
                description = "Iogurte Natural 200g",
                imgProduct = "url_iogurte.png",
                brandProduct = "Danone",
                idCategoryFK = 6,
                ean = "2345678901234",
                price = 1.29f
            ),
            Product(
                description = "Café em Pó 250g",
                imgProduct = "url_cafe.png",
                brandProduct = "3 Corações",
                idCategoryFK = 7, // Suponhamos que a categoria "Bebidas" tenha id 7
                ean = "3456789012345",
                price = 8.99f
            ),
            Product(
                description = "Refrigerante Cola 2L",
                imgProduct = "url_refrigerante.png",
                brandProduct = "Coca-Cola",
                idCategoryFK = 7,
                ean = "4567890123456",
                price = 4.49f
            ),
            Product(
                description = "Pasta de Dentes 100g",
                imgProduct = "url_pasta_dentes.png",
                brandProduct = "Colgate",
                idCategoryFK = 4,
                ean = "5678901234567",
                price = 2.99f
            ),
            Product(
                description = "Sabão em Pó 1kg",
                imgProduct = "url_sabao_po.png",
                brandProduct = "Omo",
                idCategoryFK = 4,
                ean = "6789012345678",
                price = 7.79f
            ),
            Product(
                description = "Batata Chips 100g",
                imgProduct = "url_batata_chips.png",
                brandProduct = "Ruffles",
                idCategoryFK = 7, // Suponhamos que a categoria "Snacks" tenha id 8
                ean = "7890123456789",
                price = 3.99f
            ),
            Product(
                description = "Creme de Avelã 200g",
                imgProduct = "url_creme_avela.png",
                brandProduct = "Nutella",
                idCategoryFK = 8, // Suponhamos que a categoria "Doces" tenha id 9
                ean = "8901234567890",
                price = 10.59f
            ),
            Product(
                description = "Espaguete 500g",
                imgProduct = "url_espaguete.png",
                brandProduct = "Barilla",
                idCategoryFK = 9, // Suponhamos que a categoria "Massas" tenha id 10
                ean = "9012345678901",
                price = 2.29f
            ),
            Product(
                description = "Creme Dental 150g",
                imgProduct = "url_creme_dental.png",
                brandProduct = "Sensodyne",
                idCategoryFK = 2,
                ean = "0123456789012",
                price = 4.99f
            ),
            Product(
                description = "Baguete 250g",
                imgProduct = "url_baguete.png",
                brandProduct = "Panificadora Delícia",
                idCategoryFK = 3,
                ean = "1234567890123",
                price = 2.29f
            ),
            Product(
                description = "Desinfetante 500ml",
                imgProduct = "url_desinfetante.png",
                brandProduct = "Veja",
                idCategoryFK = 4,
                ean = "2345678901234",
                price = 3.89f
            ),
            Product(
                description = "Água Mineral 1,5L",
                imgProduct = "url_agua.png",
                brandProduct = "Crystal",
                idCategoryFK = 5,
                ean = "3456789012345",
                price = 1.99f
            ),
            Product(
                description = "Manteiga 200g",
                imgProduct = "url_manteiga.png",
                brandProduct = "Qualy",
                idCategoryFK = 6,
                ean = "4567890123456",
                price = 5.49f
            ),
            Product(
                description = "Cookies 150g",
                imgProduct = "url_cookies.png",
                brandProduct = "Nestlé",
                idCategoryFK = 7,
                ean = "5678901234567",
                price = 3.79f
            ),
            Product(
                description = "Chocolate ao Leite 100g",
                imgProduct = "url_chocolate.png",
                brandProduct = "Lacta",
                idCategoryFK = 8,
                ean = "6789012345678",
                price = 4.99f
            ),
            Product(
                description = "Queijo Mussarela 250g",
                imgProduct = "url_queijo.png",
                brandProduct = "Tirolez",
                idCategoryFK = 6,
                ean = "7890123456789",
                price = 7.99f
            ),
            Product(
                description = "Macarrão Penne 500g",
                imgProduct = "url_macarrao.png",
                brandProduct = "Barilla",
                idCategoryFK = 9,
                ean = "8901234567890",
                price = 1.49f
            ),
            Product(
                description = "Banana - 1 unidade",
                imgProduct = "url_banana.png",
                brandProduct = "Fazenda Feliz",
                idCategoryFK = 10,
                ean = "9012345678901",
                price = 0.89f
            ),
        )
    }

    val categories = listOf(
        Category(idCategory = 1, nameCategory = "Alimento"),
        Category(idCategory = 2, nameCategory = "Higiene"),
        Category(idCategory = 3, nameCategory = "Padaria"),
        Category(idCategory = 4, nameCategory = "Limpeza"),
        Category(idCategory = 5, nameCategory = "Bebidas"),
        Category(idCategory = 6, nameCategory = "Leites e Derivados"),
        Category(idCategory = 7, nameCategory = "Snacks"),
        Category(idCategory = 8, nameCategory = "Doces"),
        Category(idCategory = 9, nameCategory = "Massas"),
        Category(idCategory = 10, nameCategory = "Frutas")
    )
}