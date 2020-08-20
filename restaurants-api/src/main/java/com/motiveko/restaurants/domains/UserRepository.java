package com.motiveko.restaurants.domains;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{

	Optional<User> findByEmail(String email);

}
