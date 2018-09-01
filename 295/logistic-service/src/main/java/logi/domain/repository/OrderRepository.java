package logi.domain.repository;

import logi.domain.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, OrderRepositoryCust {
    List<Order> findByNextPubkey(@Param("nextPubkey") String nextPubkey);
}
