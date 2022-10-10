package com.elizav.mvishopping.data.client

import com.elizav.mvishopping.data.mappers.ClientMapper.toData
import com.elizav.mvishopping.data.mappers.ClientMapper.toDomain
import com.elizav.mvishopping.domain.client.ClientsRepository
import com.elizav.mvishopping.domain.model.Client
import com.elizav.mvishopping.domain.model.Product
import com.elizav.mvishopping.utils.Constants
import com.elizav.mvishopping.utils.Constants.CART
import com.elizav.mvishopping.utils.Constants.NAME
import com.elizav.mvishopping.utils.Constants.PRODUCTS
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import com.elizav.mvishopping.data.model.Client as ClientData

class ClientsRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ClientsRepository {
    private val clientsCollection = db.collection(Constants.CLIENTS)

    override fun getAllClients(): Single<List<Client>> {
        return Single.create { emitter ->
            clientsCollection.get().addOnSuccessListener { result ->
                emitter.onSuccess(result.toObjects(ClientData::class.java).map { it.toDomain() })
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
                    snapshot?.let { querySnapshot ->
                        emitter.onNext(
                            querySnapshot.toObjects(ClientData::class.java).map { it.toDomain() })
                    } ?: emitter.onError(Exception())
                }
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
}