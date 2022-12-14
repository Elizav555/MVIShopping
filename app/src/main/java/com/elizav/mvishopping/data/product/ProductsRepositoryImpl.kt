package com.elizav.mvishopping.data.product

import com.elizav.mvishopping.data.client.ClientsRepositoryImpl.Companion.CLIENTS
import com.elizav.mvishopping.data.client.ClientsRepositoryImpl.Companion.PRODUCTS
import com.elizav.mvishopping.domain.model.AppException
import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.domain.product.ProductsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import com.elizav.mvishopping.data.product.Product as ProductData

class ProductsRepositoryImpl @Inject constructor(
    db: FirebaseFirestore,
    private val productMapper: ProductMapper
) :
    ProductsRepository {
    private val clientsCollection = db.collection(CLIENTS)

    override fun getAllProducts(clientId: String): Single<List<Product>> {
        return Single.create { emitter ->
            clientsCollection.document(clientId).collection(PRODUCTS).get()
                .addOnSuccessListener { result ->
                    emitter.onSuccess(
                        result.documents.mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject<ProductData>()
                                ?.let { product ->
                                    productMapper.map(product, documentSnapshot.id)
                                }
                        })
                }.addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    override fun observeProducts(clientId: String): Observable<List<Product>> {
        return Observable.create { emitter ->
            clientsCollection.document(clientId).collection(PRODUCTS)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        emitter.onError(error)
                    } else {
                        snapshot?.let { result ->
                            emitter.onNext(
                                result.documents.mapNotNull { documentSnapshot ->
                                    documentSnapshot.toObject<ProductData>()
                                        ?.let { product ->
                                            productMapper.map(product, documentSnapshot.id)
                                        }
                                })
                        } ?: emitter.onError(AppException.LoadingErrorException())
                    }
                }
        }
    }

    override fun getProduct(clientId: String, productId: String): Single<Product> {
        return Single.create { emitter ->
            clientsCollection.document(clientId).collection(PRODUCTS).document(productId).get()
                .addOnSuccessListener { documentSnapshot ->
                    documentSnapshot.toObject<ProductData>()
                        ?.let { product ->
                            emitter.onSuccess(productMapper.map(product, documentSnapshot.id))
                        }
                }.addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    override fun addProduct(clientId: String, productName: String): Single<String> =
        Single.create { emitter ->
            clientsCollection.document(clientId).collection(PRODUCTS).add(ProductData(productName))
                .addOnSuccessListener {
                    emitter.onSuccess(it.id)
                }.addOnFailureListener { ex ->
                    emitter.onError(ex)
                }
        }

    override fun updateProduct(clientId: String, product: Product): Single<Boolean> =
        Single.create { emitter ->
            clientsCollection.document(clientId).collection(PRODUCTS)
                .document(product.id).set(productMapper.map(product))
                .addOnSuccessListener {
                    emitter.onSuccess(true)
                }.addOnFailureListener { ex ->
                    emitter.onError(ex)
                }
        }

    override fun deleteProduct(clientId: String, productId: String): Single<Boolean> =
        Single.create { emitter ->
            clientsCollection.document(clientId).collection(PRODUCTS).document(productId).delete()
                .addOnSuccessListener {
                    emitter.onSuccess(true)
                }.addOnFailureListener { ex ->
                    emitter.onError(ex)
                }
        }
}