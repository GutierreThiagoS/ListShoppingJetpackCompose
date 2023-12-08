package com.gutierre.mylists.data.repository

import com.gutierre.mylists.data.local.dao.CategoryDao
import com.gutierre.mylists.data.local.dao.ProductDao
import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.domain.model.CategoryAndCountProduct
import com.gutierre.mylists.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImp(
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao
): CategoryRepository {

    override fun consultCategory(name: String): Category? {
        return categoryDao.consultCategory(name)
    }

    override fun consultCategoryList(): Flow<List<Category>> {
        return categoryDao.categoryList()
    }

    override fun consultCategoryAndCountProductList(): Flow<List<CategoryAndCountProduct>> {
        return categoryDao.categoryAndCountProductList()
    }

    override fun insertCategory(category: Category): Long{
        return categoryDao.insert(category)
    }


    override fun removerCategoryCheckProducts(category: CategoryAndCountProduct): String {
        val products = productDao.getAll().filter { it.idCategoryFK == category.idCategory }
        return if (products.isEmpty()) {
            if (categoryDao.delete(Category(category.idCategory, category.nameCategory)) == 0) ""
            else "Categoria não deletada!"
        } else "Existe Produtos nessa categoria você deve removelos!"
    }

    override fun editCategory(newName: String, category: CategoryAndCountProduct): String {
        val categoryConsult = categoryDao.consultCategory(newName)
        return if (categoryConsult == null) {
            if (categoryDao.update(Category(idCategory = category.idCategory, nameCategory = newName)) == 0)
                "Não foi possivel Editar!"
            else "Editado com Sucesso!"
        } else "Este nome está sendo usado!"
    }
}