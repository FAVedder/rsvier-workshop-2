package com.rsvier.workshop.controllers;

import com.rsvier.workshop.dao.ArtikelRepository;
import com.rsvier.workshop.domein.Artikel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/artikeledit")
public class ArtikelEditController {
    CrudRepository repository;
	
    @Autowired
    public ArtikelEditController(ArtikelRepository artikelRepository) {
        repository = artikelRepository;
    }
    
    @PostMapping
    public Artikel registerArtikel(Artikel artikel) {
        artikel.setArtikelStatus(Artikel.ArtikelStatus.ACTIEF);
        repository.save(artikel);
        return artikel;
    }
}
