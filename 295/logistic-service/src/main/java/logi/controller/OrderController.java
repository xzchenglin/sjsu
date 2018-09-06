package logi.controller;

import javafx.util.Pair;
import logi.comm.JSONHelper;
import logi.domain.model.Order;
import logi.domain.model.User;
import logi.domain.repository.OrderRepository;
import logi.service.OrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        return os.place(order).applyHash();
    }

    @PostMapping(value = "/take")
    public Order take(@RequestBody Order order) throws Exception {
        return os.take(order).applyHash();
    }

    @PostMapping(value = "/pick")
    public Order pick(@RequestBody Order order) throws Exception {
        return os.handover(order).applyHash();
    }

    @PostMapping(value = "/deliver")
    public Order deliver(@RequestBody Order order, @RequestParam(value="code") String code) throws Exception {
        return os.deliver(order, code).applyHash();
    }

    @PostMapping(value = "/fetch")
    public Collection<Order> fetch(@RequestBody String body) {
        String pk = body;
        Collection<Order> orders = or.findByRaw(Collections.singletonList(new Pair("o.next_pubkey", pk)), true);
        return orders;
    }

    @GetMapping("/history")
    public List<User> history(@RequestParam(value="id") Long id) {

        Order order = or.findById(id).orElseThrow(() -> new EntityNotFoundException(id + ""));
        List<User> ret = os.retrieveHistory(order);
        return ret;
    }

    @GetMapping("/item")
    public Order getById(@RequestParam(value="id") Long id) {

        Order ret = or.findById(id).orElseThrow(() -> new EntityNotFoundException(id + ""));
        return ret.applyHash();
    }

    @GetMapping("/list")
    public Collection<Order> list(@RequestParam(value="sid", required=false) Long sid,
                                  @RequestParam(value="rid", required=false) Long rid,
                                  @RequestParam(value="did", required=false) Long did) {
        Collection<Order> ret = new ArrayList<>();
        List<Pair> pairs = null;
        if(sid != null){
            pairs = Collections.singletonList(new Pair("o.sender_id", sid));
        }
        if(did != null){
            pairs = Collections.singletonList(new Pair("o.driver_id", did));
        }
        if(rid != null){
            pairs = Collections.singletonList(new Pair("o.receiver_id", rid));
        }
        ret = or.findByRaw(pairs, true);
        return ret;
    }

    @PostMapping("/query")
    public Collection<Order> query(@RequestBody String body) {
        List<Pair> pairs = JSONHelper.fromJsonForObjList(body, Pair.class);
        Collection<Order> ret = or.findByRaw(pairs, true);
        return ret;
    }
}
