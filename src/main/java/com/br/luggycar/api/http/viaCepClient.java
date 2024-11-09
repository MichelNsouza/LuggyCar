package com.br.luggycar.api.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCep", url = "viacep.com.br/ws/")
public interface viaCepClient {

    @GetMapping("/{cep}/json/")
    String validaCep(@PathVariable String cep);
}
