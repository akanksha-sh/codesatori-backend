package uk.co.codesatori.backend.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.codesatori.backend.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
  Optional<User> findUserByEmail(String email);
}