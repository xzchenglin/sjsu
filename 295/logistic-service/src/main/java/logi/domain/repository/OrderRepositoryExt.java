package logi.domain.repository;

import javafx.util.Pair;
import logi.domain.model.Order;

import java.util.Collection;
import java.util.List;

public interface OrderRepositoryExt {
    Collection<Order> findByRaw(List<Pair> pairs);
}
