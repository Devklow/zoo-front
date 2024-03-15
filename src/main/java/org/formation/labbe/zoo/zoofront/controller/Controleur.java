package org.formation.labbe.zoo.zoofront.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.formation.labbe.zoo.dto.*;
import org.formation.labbe.zoo.zoofront.bus.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class Controleur {
    private String backend;
    @Autowired
    private Environment env;

    @Autowired
    private Runner runner;
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
        runner.Send("Toto s'est connecté");
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

    @GetMapping("/entrerVisiteur")
    public ModelAndView entrer(){
        ModelAndView modelAndView = new ModelAndView("forward:/");
        HttpStatusCode status = client.post()
                .uri(String.join("/",backend,"entrerVisiteur"))
                .exchangeToMono(e-> Mono.just(e.statusCode())).block();
        Message notif = null;
        if (status != null && !status.isError()) {
            notif = new Message("info", "Visiteur entrée!");
        } else {
            notif = new Message("error", "Impossible de faire entrer un visiteur");
        }
        modelAndView.addObject("notif",notif);
        return modelAndView;
    }

    @GetMapping("/sortirVisiteur")
    public ModelAndView sortir(){
        ModelAndView modelAndView = new ModelAndView("forward:/");
        HttpStatusCode status = client.post()
                .uri(String.join("/",backend,"sortirVisiteur"))
                .exchangeToMono(e-> Mono.just(e.statusCode())).block();
        Message notif = null;
        if (status != null && !status.isError()) {
            notif = new Message("info", "Visiteur sortie!");
        } else {
            notif = new Message("error", "Impossible de faire sortir un visiteur");
        }
        modelAndView.addObject("notif",notif);
        return modelAndView;
    }

    @PostMapping("/devorer")
    public ModelAndView devorer(@RequestParam Map<String, String> params){
        ModelAndView modelAndView = new ModelAndView("forward:/");
        int mangeur = Integer.parseInt(params.get("mangeur"));
        Integer mange = 0;
        boolean visiteur = false;
        Message notif = null;
        if(params.get("mange").equals("visiteur")){
            mange = client.get()
                    .uri(String.join("/",backend,"visiteurs"))
                    .retrieve().bodyToMono(Integer.class).block();
            visiteur = true;
            if(mange != null && mange==0){
                notif = new Message("error", "Il n'y a pas de visiteurs à dévorer");
                modelAndView.addObject("notif",notif);
                return modelAndView;
            }
            if(mange!=null){
                mange--;
            } else {
                mange = 0;
            }
        } else {
            mange = Integer.parseInt(params.get("mange"));
        }
        Devorer devorer = new Devorer(mangeur, mange, visiteur);
        String message = client.post()
                .uri(String.join("/",backend,"devorer"))
                .body(BodyInserters.fromValue(devorer))
                .retrieve().bodyToMono(String.class).block();
        if (message!=null && message.equals("Miam miam")) {
            notif = new Message("success", message);
        } else {
            notif = new Message("error", message);
        }
        modelAndView.addObject("notif",notif);
        return modelAndView;
    }

    @PostMapping("/ajouterCage")
    public ModelAndView ajouterCage(@RequestParam Map<String, String> params) {
        ModelAndView modelAndView = new ModelAndView("forward:/");
        int x = Integer.parseInt(params.get("x"));
        int y = Integer.parseInt(params.get("y"));
        CageVide cage = new CageVide(x, y);
        HttpStatusCode status = client.post()
                .uri(String.join("/",backend,"ajouterCage"))
                .body(BodyInserters.fromValue(cage))
                .exchangeToMono(e-> Mono.just(e.statusCode())).block();
        Message notif = null;
        if (status != null && !status.isError()) {
            notif = new Message("success", "Cage ajouté!");
        } else {
            notif = new Message("error", "Impossible d'ajouter une cage");
        }
        modelAndView.addObject("notif",notif);
        return modelAndView;
    }

    @PostMapping("/entrerAnimal")
    public ModelAndView entrerAnimal(@RequestParam Map<String, String> params) {
        ModelAndView modelAndView = new ModelAndView("forward:/");
        int id = Integer.parseInt(params.get("addAnimalCageId"));
        String nom = params.get("addAnimalName");
        int age = Integer.parseInt(params.get("addAnimalAge"));
        double poids = Double.parseDouble(params.get("addAnimalWeight"));
        int lgCornes = 0;
        String type = params.get("addAnimalClasse");
        if(params.get("addAnimalLgCornes") != null && !params.get("addAnimalLgCornes").isEmpty()){
            lgCornes = Integer.parseInt(params.get("addAnimalLgCornes"));
        }
        AnimalInfo animalInfo = new AnimalInfo(id,nom,age,poids,type,lgCornes);
        HttpStatusCode status = client.post()
                .uri(String.join("/",backend,"entrerAnimal"))
                .body(BodyInserters.fromValue(animalInfo))
                .exchangeToMono(e-> Mono.just(e.statusCode())).block();
        Message notif = null;
        if (status != null && !status.isError()) {
            notif = new Message("success", "Animal entré!");
        } else {
            notif = new Message("error", "Impossible de faire entrer l'animal");
        }
        modelAndView.addObject("notif",notif);
        return modelAndView;
    }
}
