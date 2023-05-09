package ex.rr.adminpanel.data.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class RestService {

    private WebClient webClient;

    @PostConstruct
    private void init() {
        this.webClient = WebClient.create();
    }

    public Mono<String> get(String uri) throws URISyntaxException {
        return webClient.get()
                .uri(new URI(uri))
                .retrieve()
                .bodyToMono(String.class);
    }

    public void postTeamsMessage(String url, String message) throws URISyntaxException {
        webClient.post()
            .uri(new URI(url))
            .body(message, String.class)
            .retrieve();
    }

}
