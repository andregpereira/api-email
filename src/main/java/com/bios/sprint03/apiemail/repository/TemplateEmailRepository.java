package com.bios.sprint03.apiemail.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.bios.sprint03.apiemail.model.EmailTemplate;

public interface TemplateEmailRepository extends MongoRepository<EmailTemplate, String> {

	@Query("{chave: '?0'}")
	Optional<EmailTemplate> obterPorChave(String chave);

}
