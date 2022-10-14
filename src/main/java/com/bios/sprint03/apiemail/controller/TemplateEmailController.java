package com.bios.sprint03.apiemail.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bios.sprint03.apiemail.model.EmailTemplate;
import com.bios.sprint03.apiemail.repository.TemplateEmailRepository;

@RestController
@RequestMapping("/template")
public class TemplateEmailController {

	@Autowired
	private TemplateEmailRepository templateRepository;

	@GetMapping("/{chave}")
	public ResponseEntity<EmailTemplate> obterPorchave(@PathVariable String chave) {
		Optional<EmailTemplate> template = templateRepository.obterPorChave(chave);

		return new ResponseEntity<EmailTemplate>(template.get(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody EmailTemplate template) {
		Optional<EmailTemplate> templateExistente = templateRepository.obterPorChave(template.getChave());

		if (templateExistente.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Template já criado");

		}

		templateRepository.save(template);
		return ResponseEntity.status(HttpStatus.CREATED).body(template);
	}

}
