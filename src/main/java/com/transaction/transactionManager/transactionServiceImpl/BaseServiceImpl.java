package com.transaction.transactionManager.transactionServiceImpl;

import com.transaction.transactionManager.exception.ConnectionRefusedException;
import com.transaction.transactionManager.exception.InvalidTokenException;
import com.transaction.transactionManager.exception.ServerErrorException;
import com.transaction.transactionManager.payload.response.ValidTokenResponse;
import com.transaction.transactionManager.transactionService.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Divakar Verma
 * @created_at : 12/02/2024 - 4:24 pm
 * @mail_to: vermadivakar2022@gmail.com
 */

@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    @Qualifier("getAuthWebClient")
    private WebClient webClient;
    @Override
    public Mono<ValidTokenResponse> validateToken(String token) {
        return webClient.post().uri("/validateToken")
                .body(Mono.just(token),String.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new InvalidTokenException("Invalid token"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ServerErrorException("Server error"))
                )
                .onStatus(HttpStatusCode :: isError, clientResponse -> {
                    if(clientResponse.statusCode()== HttpStatus.SERVICE_UNAVAILABLE){
                        return Mono.error(new ConnectionRefusedException("Connection refused"));
                    }
                    else {
                        return  clientResponse.createException();
                    }
                })
                .bodyToMono(ValidTokenResponse.class);
    }
}
