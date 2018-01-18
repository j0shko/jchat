package hr.jtomic.jchat

import hr.jtomic.jchat.websocket.ChatSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor

import java.util.HashMap

@Configuration
class WebSocketConfig {

    @Bean
    fun eventPublisher(): UnicastProcessor<String> {
        return UnicastProcessor.create()
    }

    @Bean
    fun events(eventPublisher: UnicastProcessor<String>): Flux<String> {
        return eventPublisher
                .replay(25)
                .autoConnect()
    }

    @Bean
    fun webSocketMapping(eventPublisher: UnicastProcessor<String>, events: Flux<String>): HandlerMapping {
        val map = HashMap<String, WebSocketHandler>()
        map["/ws-chat"] = ChatSocketHandler(eventPublisher, events)

        val mapping = SimpleUrlHandlerMapping()
        mapping.order = 10
        mapping.urlMap = map
        return mapping
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter {
        return WebSocketHandlerAdapter()
    }
}
