package com.example.rest_security_api.repository;

import com.example.rest_security_api.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {


    Optional<File> getByName(String fileName);
}
