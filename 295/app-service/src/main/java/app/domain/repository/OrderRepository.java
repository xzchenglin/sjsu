package app.domain.repository;

import app.domain.model.Order;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "order")
public interface OrderRepository extends CrudRepository<Order, Long>, OrderRepositoryExt {
    @Cacheable
    @Override
    Optional<Order> findById(Long aLong);

    @Cacheable
    @Override
    Iterable<Order> findAll();

    @Override
    @CacheEvict(key = "#p0.id")
    <S extends Order> S save(S s);

    @Override
    @CacheEvict
    <S extends Order> Iterable<S> saveAll(Iterable<S> iterable);

    @Override
    @CacheEvict
    void deleteById(Long aLong);

    @Override
    @CacheEvict
    void delete(Order order);

    @Override
    @CacheEvict
    void deleteAll(Iterable<? extends Order> iterable);

    @Override
    @CacheEvict
    void deleteAll();
}
