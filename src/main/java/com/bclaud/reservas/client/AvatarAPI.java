package com.bclaud.reservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@FeignClient(name = "dog", url = "https://some-random-api.ml/img/dog")
public interface AvatarAPI {

    @GetMapping
    public LinkImagem getImagem();
}
