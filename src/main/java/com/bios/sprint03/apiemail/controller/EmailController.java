package com.bios.sprint03.apiemail.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bios.sprint03.apiemail.model.Email;
import com.bios.sprint03.apiemail.model.EmailEnviado;
import com.bios.sprint03.apiemail.model.EmailTemplate;
import com.bios.sprint03.apiemail.repository.EmailEnviadoRepository;
import com.bios.sprint03.apiemail.repository.TemplateEmailRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8084")
@RequestMapping("/email")
public class EmailController {

	@Autowired
	private TemplateEmailRepository templateRepository;

	@Autowired
	private EmailEnviadoRepository emailEnviadoRepository;

	@Autowired
	private JavaMailSender emailSender;

	@GetMapping("/lista")
	public ResponseEntity<List<EmailEnviado>> lista() {
		List<EmailEnviado> listaEmail = emailEnviadoRepository.findAll();

		return ResponseEntity.status(HttpStatus.OK).body(listaEmail);
	}

	@GetMapping("/{dataEnvio}")
	public ResponseEntity<List<EmailEnviado>> obterPorDataEnvio(@PathVariable String dataEnvio) {
		List<EmailEnviado> listaEmail = emailEnviadoRepository.findAll();

		List<EmailEnviado> listaEmailFiltrado = listaEmail.stream()
				.filter(item -> item.getDataEnvio().matches(dataEnvio + "(.*)")).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(listaEmailFiltrado);
	}

	@PostMapping
	public ResponseEntity<?> enviar(@RequestBody Email email) {
		if (email.getDados() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("É necessário informar o dicionário de dados.");
		}

		Optional<EmailTemplate> templateExistente = templateRepository.obterPorChave(email.getChave());

		if (!templateExistente.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		for (String chave : email.getDados().keySet()) {
			templateExistente.get().enriquecerEmail(chave, email.getDados().get(chave));
		}

		EmailEnviado emailEnviado = new EmailEnviado();

		emailEnviado.setRemetente(templateExistente.get().getRemetente());
		emailEnviado.setChave(templateExistente.get().getChave());
		emailEnviado.setAssuntoEmail(templateExistente.get().getAssunto());
		emailEnviado.setCorpoEmail(templateExistente.get().getCorpoEmail());
		emailEnviado.setDestinatarios(email.getDestinatarios());
		emailEnviado.setDestinatariosCopia(email.getDestinatariosCopia());
		emailEnviado.setDataEnvio(java.time.LocalDateTime.now().toString());
		emailEnviado.setDados(email.getDados());

		String[] destinatarios = new String[emailEnviado.getDestinatarios().size()];
		destinatarios = emailEnviado.getDestinatarios().toArray(destinatarios);

		String[] destinatariosCopia = new String[emailEnviado.getDestinatariosCopia().size()];
		destinatariosCopia = emailEnviado.getDestinatariosCopia().toArray(destinatariosCopia);

		SimpleMailMessage emailMessage = new SimpleMailMessage();

		try {
			emailMessage.setFrom(templateExistente.get().getRemetente());
			emailMessage.setTo(destinatarios);
			emailMessage.setCc(destinatariosCopia);
			emailMessage.setSubject(emailEnviado.getAssuntoEmail());
			emailMessage.setText(emailEnviado.getCorpoEmail());

			emailSender.send(emailMessage);
		} catch (MailException ex) {
			emailEnviado.setEnviadoComSucesso(false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emailEnviado);
		}

		emailEnviado.setEnviadoComSucesso(true);
		emailEnviadoRepository.save(emailEnviado);
		return ResponseEntity.status(HttpStatus.OK).body(emailEnviado);
	}

}
