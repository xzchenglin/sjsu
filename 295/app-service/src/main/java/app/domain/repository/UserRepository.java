package app.domain.repository;

import app.domain.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "user")
public interface UserRepository extends CrudRepository<User, Long> {

    @Cacheable
    @Query("SELECT u FROM User u LEFT JOIN fetch u.addresses WHERE u.id = (:id)")
    Optional<User> findByIdEager(@Param("id") Long id);

    @Cacheable
    Optional<User> findById(@Param("id") Long id);

    @Cacheable
    @Query("SELECT u FROM User u LEFT JOIN fetch u.addresses WHERE u.name = (:name)")
    Optional<User> findByNameEager(@Param("name") String name);

    @Cacheable
    Optional<User> findByName(@Param("name") String name);

    @Query("SELECT u FROM User u LEFT JOIN fetch u.addresses")
    List<User> findAllEager();

    @Cacheable
    @Transactional
    @Query("SELECT u FROM User u LEFT JOIN fetch u.addresses WHERE u.pubkey = (:pubkey)")
    Optional<User> findByPubkeyEager(@Param("pubkey") String pubkey);

    @Cacheable
    @Transactional
    Optional<User> findByPubkey(@Param("pubkey") String pubkey);

    @Caching(evict = {
        @CacheEvict(key = "#p0.pubkey"),
        @CacheEvict(key = "#p0.id"),
        @CacheEvict(key = "#p0.name")
    } )
    @Override
    <S extends User> S save(S s);

    @CacheEvict(allEntries = true)
    @Override
    <S extends User> Iterable<S> saveAll(Iterable<S> iterable);

    @CacheEvict
    @Override
    void deleteById(Long aLong);

    @Caching(evict = {
            @CacheEvict(key = "#p0.pubkey"),
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "#p0.name")
    } )
    @Override
    void delete(User user);

    @CacheEvict(allEntries = true)
    @Override
    void deleteAll(Iterable<? extends User> iterable);

    @CacheEvict(allEntries = true)
    @Override
    void deleteAll();
}
