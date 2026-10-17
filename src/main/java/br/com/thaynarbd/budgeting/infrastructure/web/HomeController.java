package br.com.thaynarbd.budgeting.infrastructure.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "status", "ok",
                "servico", "API de orcamento",
                "mensagem", "Servico no ar. O perfil render usa H2 em memoria e deixa a IA desligada."
        );
    }
}
