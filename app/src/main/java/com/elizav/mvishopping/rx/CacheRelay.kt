package com.elizav.mvishopping.rx

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observer
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Relay that buffers values when no Observer subscribed and replays them to Observer as requested. Such values are not replayed
 * to any other Observer.
 *
 * This relay holds an unbounded internal buffer.
 *
 * @param <T> the value type received and emitted by this Relay subclass </T>
 */
class CacheRelay<T : Any> private constructor() : Relay<T>() {
    companion object {
        fun <T : Any> create(): Relay<T> {
            return CacheRelay()
        }
    }

    private val relay = PublishRelay.create<T>()
    private val cache = ConcurrentLinkedQueue<T>()

    @Synchronized
    override fun accept(value: T) {
        if (hasObservers()) {
            relay.accept(value)
        } else {
            cache.add(value)
        }
    }

    override fun hasObservers() = relay.hasObservers()

    @Synchronized
    override fun subscribeActual(observer: Observer<in T>) {
        relay.subscribe(observer)
        while (true) {
            cache.poll()?.run(relay::accept) ?: break
        }
    }
}