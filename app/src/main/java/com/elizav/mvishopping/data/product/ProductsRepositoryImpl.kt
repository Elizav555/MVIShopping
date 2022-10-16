package com.elizav.mvishopping.data.product

import com.elizav.mvishopping.data.client.ClientsRepositoryImpl.Companion.CLIENTS
import com.elizav.mvishopping.data.client.ClientsRepositoryImpl.Companion.PRODUCTS
import com.elizav.mvishopping.data.product.ProductMapper.toData
import com.elizav.mvishopping.data.product.ProductMapper.toDomain
import com.elizav.mvishopping.domain.model.AppException
import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.domain.product.ProductsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import com.elizav.mvishopping.data.product.Product as ProductData

class ProductsRepositoryImpl @Inject constructor(db: FirebaseFirestore) :
    ProductsRepository {
    private val clientsCollection = db.collection(CLIENTS)

    override fun getAllProducts(clientId: String): Single<List<Product>> {
        return Single.create { emitter ->
            clientsCollection.document(clientId).collection(PRODUCTS).get()
                .addOnSuccessListener { result ->
                    emitter.onSuccess(
                        result.documents.mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject<ProductData>()
                                ?.toDomain(documentSnapshot.id.toIntOrNull() ?: 0)
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
                                        ?.toDomain(documentSnapshot.id.toIntOrNull() ?: 0)
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
                        ?.toDomain(documentSnapshot.id.toIntOrNull() ?: 0)?.let {
                            emitter.onSuccess(
                                it
                            )
                        }
                }.addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    override fun addProduct(clientId: String, product: Product): Single<Boolean> =
        Single.create { emitter ->
            clientsCollection.document(clientId).collection(PRODUCTS)
                .document(product.id.toString()).set(product.toData())
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