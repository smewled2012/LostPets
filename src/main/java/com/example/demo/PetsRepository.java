package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface PetsRepository extends CrudRepository<Pets,Long> {

    Iterable<Pets> findAllByStatusContainingIgnoreCase(String status);

}
