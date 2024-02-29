package org.formation.labbe.zoo.zoofront.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.formation.labbe.zoo.dto.CageInfos;
import org.formation.labbe.zoo.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class Controleur {
    private String backend;
    @Autowired
    private Environment env;

    private WebClient client;

    @PostConstruct
    private void init(){
        backend = String.join("","http://",
                env.getProperty("url.backend"),":",
                env.getProperty("port.backend"));
        client = WebClient.create();
    }

    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("principale");
        CageInfos[] cagesInfos = client.get()
                .uri(String.join("/",backend,"lireTous"))
                .retrieve().bodyToMono(CageInfos[].class).block();
        Integer visiteurs = client.get()
                .uri(String.join("/",backend,"visiteurs"))
                .retrieve().bodyToMono(Integer.class).block();
        String[] classes = client.get()
                .uri(String.join("/",backend,"getTypesDispo"))
                .retrieve().bodyToMono(String[].class).block();
        List<CageInfos> cages =  Arrays.stream(cagesInfos).toList();
        modelAndView.addObject("cages",cages);
        modelAndView.addObject("visiteurs",visiteurs);
        modelAndView.addObject("classes",classes);
        return modelAndView;
    }

    @GetMapping("/nourrir")
    public ModelAndView nourrir(){
        ModelAndView modelAndView = new ModelAndView("forward:/");
        HttpStatusCode status = client.post()
                .uri(String.join("/",backend,"nourrir"))
                .exchangeToMono(e-> Mono.just(e.statusCode())).block();
        Message notif = null;
        if (status != null && !status.isError()) {
            notif = new Message("success", "Les animaux ont bien été nourris");
        } else {
            notif = new Message("error", "Personne n'as mangé!");
        }
        modelAndView.addObject("notif",notif);
        return modelAndView;
    }
}
