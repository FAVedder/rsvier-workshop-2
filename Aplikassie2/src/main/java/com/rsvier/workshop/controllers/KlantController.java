package com.rsvier.workshop.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.rsvier.workshop.dao.AdresRepository;
import com.rsvier.workshop.dao.PersoonRepository;
import com.rsvier.workshop.domein.Adres;
import com.rsvier.workshop.domein.Bestelling;
import com.rsvier.workshop.domein.Persoon;
import com.rsvier.workshop.domein.Adres.AdresSoort;
import com.rsvier.workshop.domein.Persoon.AccountSoort;
import com.rsvier.workshop.domein.Persoon.PersoonStatus;

@Controller
@RequestMapping("/klant")
@SessionAttributes("persoon")
public class KlantController {
	
	@Autowired
	PersoonRepository persoonRepository;
	
	@Autowired
	AdresRepository adresRepository;
	
	@ModelAttribute("persoon")
	public Persoon getPersoon() {
		return new Persoon();
	}
	
	@ModelAttribute("adres")
	public Adres getAdres() {
		return new Adres();
	}
	
	@ModelAttribute("andereAdressen")
	public Boolean getAdrssen() {
		return new Boolean(false);
	}
	
	@GetMapping
	public String getKlantMenu() {
		return "klant";
	}
	
	@GetMapping("/klantformulier")
	public String getFormulier(Model model) {
		return "/klantformulier";
	}
	
	@PostMapping("/register")
	public ModelAndView registerKlant(@ModelAttribute("persoon") Persoon persoon, Model model) {
		persoon.setPersoonStatus(PersoonStatus.ACTIEF);
		persoon.setAccountSoort(AccountSoort.KLANT);
		setWoonAdresSoort(persoon);
		persoonRepository.save(persoon);
		persoon = new Persoon();
		model.addAttribute("persoon", persoon);
		return new ModelAndView("redirect:/klant");
	}
	
	@GetMapping("/klantenoverzicht")
	public String getKlantenOverzicht(@ModelAttribute("persoon") Persoon persoon, Model model) {
		Iterable<Persoon> persoonList = new ArrayList<Persoon>();
		PersoonStatus status = persoon.getPersoonStatus();
		if(status != null) {
			persoonList = persoonRepository.findByPersoonStatus(status);
		} else {
			persoonList = persoonRepository.findAll();
		}
		model.addAttribute("persoonList", persoonList);
		return "klantenoverzicht";
	}
	
	@GetMapping(value="/adresoverzicht")
    public String adresOverzicht(@RequestParam(value="id", required=true) Long id, Model model) {
		List<Adres> adresList = new ArrayList<>();
		Optional<Persoon> persoonOptional = persoonRepository.findById(id);
		Persoon persoon = persoonOptional.get();
		adresList = (List<Adres>) persoon.getAdresCollection();
		model.addAttribute("adresList", adresList);
		model.addAttribute("persoonId", persoon.getId());
		return "adresoverzicht";
	}
	
	@GetMapping("/adresaangeven")
	public String adresAangeven(@RequestParam(value="id", required=true) Long id, Model model) {
		Adres adres = new Adres();
		model.addAttribute("persoonId", id);
		model.addAttribute("adres", adres);
		return "adresaangeven";
	}
	
	@PostMapping("/adresupdate")
	public ModelAndView updateAdres(@RequestParam(value="id", required=true) Long persoonId, @ModelAttribute("adres") Adres adres, Model model) {
		Optional<Persoon> optionaalPersoon = persoonRepository.findById(persoonId);
		Persoon persoon = optionaalPersoon.get();
		addAdres(persoon, adres);
		persoonRepository.save(persoon);
		return new ModelAndView("redirect:/klant");
	}
	
	@GetMapping("/delete")
	public ModelAndView deleteKlant(@RequestParam(value="id", required=true) Long persoonId, Model model) {
		Optional<Persoon> optionaalPersoon = persoonRepository.findById(persoonId);
		Persoon persoon = optionaalPersoon.get();
		if(persoon.getPersoonStatus().equals(PersoonStatus.INACTIEF)) {
			persoonRepository.delete(persoon);
		}
		if(persoon.getPersoonStatus().equals(PersoonStatus.ACTIEF)) {
			persoon.setPersoonStatus(PersoonStatus.INACTIEF);
			persoonRepository.save(persoon);
		}
		return new ModelAndView("redirect:/klant");
	}
	
	@GetMapping("/edit")
	public String editFormulierPersoon(@RequestParam(value="id", required=true) Long persoonId, Model model) {
		Optional<Persoon> optionaalPersoon = persoonRepository.findById(persoonId);
		Persoon persoon = optionaalPersoon.get();
		model.addAttribute("persoon", persoon);
		return "editpersoon";
	}
	
	@PostMapping("/persoonupdate")
	public ModelAndView persoonUpdate(@ModelAttribute("persoon") Persoon persoon, Model model) {
		persoonRepository.save(persoon);
		return new ModelAndView("redirect:/klant");
	}
	
	private void setWoonAdresSoort(Persoon persoon) {
		Collection<Adres> collection = persoon.getAdresCollection();
		Iterator<Adres> iterator = collection.iterator();
		iterator.next().setAdresSoort(AdresSoort.WOONADRES);
	}
	
	private void addAdres(Persoon persoon, Adres adres) {
		Collection<Adres> collection = persoon.getAdresCollection();
		Iterator<Adres> iterator = collection.iterator();
		if(iterator.hasNext()) {
			if(iterator.next().getAdresSoort().equals(adres.getAdresSoort())) {
				iterator.remove();
			}
		}
		collection.add(adres);
		adresRepository.save(adres);
	}
	
	

}
