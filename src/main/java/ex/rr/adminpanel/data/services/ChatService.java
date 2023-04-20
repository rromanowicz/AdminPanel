package ex.rr.adminpanel.data.services;

import com.vaadin.flow.spring.security.AuthenticationContext;
import ex.rr.adminpanel.data.models.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Instant;

/**
 * Simple chat service just for fun. Not used by any other services/views.
 */
@Service
public
class ChatService {

    private final Sinks.Many<Message> messages = Sinks.many().multicast().directBestEffort();

    private final Flux<Message> messagesFlux = messages.asFlux();

    private final AuthenticationContext ctx;

    ChatService(AuthenticationContext ctx) {
        this.ctx = ctx;
    }

    public Flux<Message> join() {
        return this.messagesFlux;
    }

    public void add(String message) {
        var username = this.ctx.getPrincipalName().orElse("Anonymous");
        this.messages.tryEmitNext(new Message(username, message, Instant.now()));
    }

}
