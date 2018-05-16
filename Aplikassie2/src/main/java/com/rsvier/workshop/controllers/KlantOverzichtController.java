package com.rsvier.workshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rsvier.workshop.domein.Persoon;

@Controller
@RequestMapping("/klantenoverzicht")
public class KlantOverzichtController {
	
	
	
	@GetMapping
	public String getKlantOverzicht(Model model) {
		Boolean attributeTest = model.containsAttribute("persoon");
		model.addAttribute("attributeTest", attributeTest);
		return "klantenoverzicht";
	}
}
