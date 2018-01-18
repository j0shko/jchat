package hr.jtomic.jchat.websocket

import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.UnicastProcessor

class ChatSocketHandler(private val eventPublisher: UnicastProcessor<String>, private val chatMessages: Flux<String>) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        val chatSubscriber = ChatSubscriber(eventPublisher)
        session.receive()
                .map({ it.payloadAsText })
                .subscribe(chatSubscriber::next, chatSubscriber::error, chatSubscriber::complete)
        return session.send(chatMessages.map({ session.textMessage(it) }))
    }

    private class ChatSubscriber(internal var eventPublisher: UnicastProcessor<String>) {

        fun next(s: String) {
            eventPublisher.onNext(s)
        }

        fun error(e: Throwable) {
            e.printStackTrace()
        }

        fun complete() {}
    }
}
