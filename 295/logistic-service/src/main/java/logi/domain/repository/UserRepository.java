package logi.domain.repository;

import logi.domain.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN fetch u.addresses WHERE u.id = (:id)")
    Optional<User> findByIdEager(@Param("id") Long id);

    Optional<User> findById(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN fetch u.addresses WHERE u.name = (:name)")
    Optional<User> findByNameEager(@Param("name") String name);

    Optional<User> findByName(@Param("name") String name);

    @Query("SELECT u FROM User u LEFT JOIN fetch u.addresses")
    List<User> findAllEager();

    @Query("SELECT u FROM User u LEFT JOIN fetch u.addresses WHERE u.pubkey = (:pubkey)")
    Optional<User> findByPubkeyEager(@Param("pubkey") String pubkey);

    Optional<User> findByPubkey(@Param("pubkey") String pubkey);
}
