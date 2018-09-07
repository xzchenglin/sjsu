package app.domain.repository;

import app.domain.model.Address;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "addr")
public interface AddressRepository extends CrudRepository<Address, Long> {
    @Override
    @Cacheable
    Optional<Address> findById(Long aLong);

    @Override
    @Cacheable
    Iterable<Address> findAll();

    @Override
    @Cacheable
    Iterable<Address> findAllById(Iterable<Long> iterable);

    @Override
    @CacheEvict
    <S extends Address> S save(S s);

    @Override
    @CacheEvict
    <S extends Address> Iterable<S> saveAll(Iterable<S> iterable);

    @Override
    @CacheEvict
    void deleteById(Long aLong);

    @Override
    @CacheEvict
    void delete(Address address);

    @Override
    @CacheEvict
    void deleteAll(Iterable<? extends Address> iterable);

    @Override
    @CacheEvict
    void deleteAll();
}
