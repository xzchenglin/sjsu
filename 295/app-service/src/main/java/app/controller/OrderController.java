package app.controller;

import javafx.util.Pair;
import app.comm.JSONHelper;
import app.domain.model.Order;
import app.domain.model.User;
import app.domain.repository.OrderRepository;
import app.service.OrderService;
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

    /***
     * (post)sender place an order
     *
     * @param order
     * @return persisted order
     */
    @PostMapping(value = "/place")
    public Order place(@RequestBody Order order) throws Exception {
        return os.place(order).applyHash();
    }

    /***
     * (post)driver take the order online
     *
     * @param order
     * @return persisted order
     */
    @PostMapping(value = "/take")
    public Order take(@RequestBody Order order) throws Exception {
        return os.take(order).applyHash();
    }

    /***
     * (post)sender gives items to driver, or driver to next driver
     *
     * @param order
     * @return persisted order
     */
    @PostMapping(value = "/handover")
    public Order handover(@RequestBody Order order) throws Exception {
        return os.handover(order).applyHash();
    }

    /***
     * (post)driver deliver the item
     *
     * @param order
     * @return persisted order
     */
    @PostMapping(value = "/deliver")
    public Order deliver(@RequestBody Order order, @RequestParam(value="code") String code) throws Exception {
        return os.deliver(order, code).applyHash();
    }

    /***
     * (post)fetch the received orders
     *
     * @param pubkey own public ket
     * @return list of orders currently under my name
     */
    @PostMapping(value = "/fetch")
    public Collection<Order> fetch(@RequestBody String pubkey) {
        Collection<Order> orders = or.findByRaw(Collections.singletonList(new Pair("o.next_pubkey", pubkey)), true);
        return orders;
    }

    /***
     * order history
     *
     * @param id order ID
     * @return List of past user/drivers
     */
    @GetMapping("/history")
    public List<User> history(@RequestParam(value="id") Long id) {

        Order order = or.findById(id).orElseThrow(() -> new EntityNotFoundException(id + ""));
        List<User> ret = os.retrieveHistory(order);
        return ret;
    }

    /***
     * get an order's information
     *
     * @param id order ID
     * @return order
     */
    @GetMapping("/item")
    public Order item(@RequestParam(value="id") Long id) {

        Order ret = or.findById(id).orElseThrow(() -> new EntityNotFoundException(id + ""));
        return ret.applyHash();
    }

    /***
     * get a list of orders by different ids
     *
     * @param sid sender id
     * @param rid receiver id
     * @param did driver id
     * @return list of orders
     */
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

    /***
     * get a list of orders by query
     *
     * @param body list of key->value {@link Pair}, see {@link app.domain.repository.OrderRepositoryExtImpl for details}
     * @return list of orders
     */
    @PostMapping("/query")
    public Collection<Order> query(@RequestBody String body) {
        List<Pair> pairs = JSONHelper.fromJsonForObjList(body, Pair.class);
        Collection<Order> ret = or.findByRaw(pairs, true);
        return ret;
    }
}
