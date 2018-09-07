package app.domain.repository;

import javafx.util.Pair;
import app.domain.model.Order;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;
import java.util.List;

@CacheConfig(cacheNames = "order")
public interface OrderRepositoryExt {

//    @Cacheable
    Collection<Order> findByRaw(List<Pair> pairs, boolean hash);
}
