package com.elizav.mvishopping.data.client

import com.elizav.mvishopping.data.client.ClientMapper.toData
import com.elizav.mvishopping.data.client.ClientMapper.toDomain
import com.elizav.mvishopping.domain.client.ClientsRepository
import com.elizav.mvishopping.domain.model.AppException
import com.elizav.mvishopping.domain.model.Client
import com.elizav.mvishopping.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import com.elizav.mvishopping.data.client.Client as ClientData

class ClientsRepositoryImpl @Inject constructor(
    db: FirebaseFirestore,
) : ClientsRepository {
    private val clientsCollection = db.collection(CLIENTS)

    override fun getAllClients(): Single<List<Client>> {
        return Single.create { emitter ->
            clientsCollection.get().addOnSuccessListener { result ->
                emitter.onSuccess(
                    result.documents.mapNotNull { documentSnapshot ->
                        documentSnapshot.toObject<ClientData>()?.toDomain(documentSnapshot.id)
                    })
            }.addOnFailureListener { e ->
                emitter.onError(e)
            }
        }
    }

    override fun observeClients(): Observable<List<Client>> {
        return Observable.create { emitter ->
            clientsCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    emitter.onError(error)
                } else {
                    snapshot?.let { result ->
                        emitter.onNext(
                            result.documents.mapNotNull { documentSnapshot ->
                                documentSnapshot.toObject<ClientData>()
                                    ?.toDomain(documentSnapshot.id)
                            })
                    } ?: emitter.onError(AppException.LoadingErrorException())
                }
            }
        }
    }

    override fun getClient(clientId: String): Single<Client> {
        return Single.create { emitter ->
            clientsCollection.document(clientId).get().addOnSuccessListener { documentSnapshot ->
                documentSnapshot.toObject<ClientData>()?.toDomain(documentSnapshot.id)
            }.addOnFailureListener { e ->
                emitter.onError(e)
            }
        }
    }

    override fun addClient(client: Client): Single<Boolean> = Single.create { emitter ->
        clientsCollection.document(client.id).set(client.toData())
            .addOnSuccessListener {
                emitter.onSuccess(true)
            }.addOnFailureListener { ex ->
                emitter.onError(ex)
            }
    }

    override fun deleteClient(clientId: String): Single<Boolean> = Single.create { emitter ->
        clientsCollection.document(clientId).delete()
            .addOnSuccessListener {
                emitter.onSuccess(true)
            }.addOnFailureListener { ex ->
                emitter.onError(ex)
            }
    }

    override fun updateProducts(clientId: String, products: List<Product>): Single<Boolean> =
        Single.create { emitter ->
            clientsCollection.document(clientId).update(PRODUCTS, products)
                .addOnSuccessListener {
                    emitter.onSuccess(true)
                }.addOnFailureListener { ex ->
                    emitter.onError(ex)
                }
        }

    override fun updateCart(clientId: String, cart: List<Int>): Single<Boolean> =
        Single.create { emitter ->
            clientsCollection.document(clientId).update(CART, cart)
                .addOnSuccessListener {
                    emitter.onSuccess(true)
                }.addOnFailureListener { ex ->
                    emitter.onError(ex)
                }
        }

    override fun updateName(clientId: String, name: String): Single<Boolean> =
        Single.create { emitter ->
            clientsCollection.document(clientId).update(NAME, name)
                .addOnSuccessListener {
                    emitter.onSuccess(true)
                }.addOnFailureListener { ex ->
                    emitter.onError(ex)
                }
        }

    companion object {
        //Collection Reference
        const val CLIENTS = "clients"

        //Client fields
        const val NAME = "name"
        const val PRODUCTS = "products"
        const val CART = "cart"
    }
}