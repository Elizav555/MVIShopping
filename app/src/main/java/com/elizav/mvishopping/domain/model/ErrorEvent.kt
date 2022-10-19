package com.elizav.mvishopping.domain.model

import android.os.Handler
import android.os.Looper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject


class ErrorEvent {
    private lateinit var exceptionSubject: PublishSubject<AppException>

    private fun getSubject(): PublishSubject<AppException> {
        if (!this::exceptionSubject.isInitialized) {
            exceptionSubject = PublishSubject.create()
        }
        return exceptionSubject
    }

    private val compositeDisposableMap = HashMap<Any, CompositeDisposable>()

    private fun getCompositeSubscription(anyObject: Any): CompositeDisposable {
        var compositeSubscription: CompositeDisposable? = compositeDisposableMap[anyObject]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeDisposable()
            compositeDisposableMap[anyObject] = compositeSubscription
        }
        return compositeSubscription
    }

    fun publish(AppException: AppException) {
        Handler(Looper.getMainLooper())
            .post { getSubject().onNext(AppException) }
    }

    fun register(lifecycle: Any, onNext: (AppException) -> Unit) {
        val disposable = getSubject().subscribeOn(AndroidSchedulers.mainThread()).subscribeBy(onNext = onNext)
        getCompositeSubscription(lifecycle).add(disposable)
    }

    fun unregister(lifecycle: Any) {
        val compositeSubscription = compositeDisposableMap.remove(lifecycle)
        compositeSubscription?.dispose()
    }
}