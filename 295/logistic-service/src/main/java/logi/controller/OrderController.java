package logi.controller;

import javafx.util.Pair;
import logi.domain.model.Order;
import logi.domain.model.User;
import logi.domain.repository.OrderRepository;
import logi.domain.service.OrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    static Log logger = LogFactory.getLog(OrderController.class);

    @Autowired
    private OrderService os;
    @Autowired
    OrderRepository or;

    @PostMapping(value = "/place")
    public Order register(@RequestBody Order order) throws Exception {
        return os.place(order);
    }

    @PostMapping(value = "/take")
    public Order take(@RequestBody Order order) throws Exception {
        return os.take(order);
    }

    @PostMapping(value = "/pick")
    public Order pick(@RequestBody Order order) throws Exception {
        return os.handover(order);
    }

    @PostMapping(value = "/deliver")
    public Order deliver(@RequestBody Order order) throws Exception {
        return os.deliver(order);
    }

    @PostMapping(value = "/fetch")
    public Collection<Order> fetch(@RequestBody String body) {
        String pk = body;
        Collection<Order> orders = or.findByRaw(Collections.singletonList(new Pair("o.next_pubkey", pk)));
        if(orders != null && orders.size()>0){
            orders.stream().map(order1 -> order1.applyHash()).collect(Collectors.toList());
        }
        return orders;
    }

    @GetMapping("/history")
    public List<User> history(@RequestParam(value="id") Long id) {

        Order order = or.findById(id).orElseThrow(() -> new EntityNotFoundException(id + ""));
        List<User> ret = os.retrieveHistory(order);
        return ret;
    }

    @GetMapping("/list")
    public List<Order> list() {
        List<Order> is = new ArrayList<>();
        or.findAll().forEach(is::add);
        return is;
    }

}
