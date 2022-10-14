package com.bios.sprint03.apiemail.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bios.sprint03.apiemail.model.EmailEnviado;

public interface EmailEnviadoRepository extends MongoRepository<EmailEnviado, String> {

}
