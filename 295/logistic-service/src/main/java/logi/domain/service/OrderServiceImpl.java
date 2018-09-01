package logi.domain.service;

import logi.Application;
import logi.comm.JSONHelper;
import logi.domain.bc.Block;
import logi.domain.bc.Chain;
import logi.domain.model.Order;
import logi.domain.model.User;
import logi.domain.repository.OrderRepository;
import logi.domain.repository.UserRepository;
import org.apache.commons.lang.NotImplementedException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository or;
    @Autowired
    private UserRepository ur;

    //for unit testing
    private User currentUser;
    public OrderServiceImpl user(User currentUser) {
        this.currentUser = currentUser;
        return this;
    }

    @Override
    public Order place(Order order) throws Exception {

        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            currentUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        order.setSender(currentUser);

        Block b0 = new Block(order.getName(), order.getNextPubkey(), order.getPayload());
        Chain chain = new Chain();
        chain.append(b0);
        order.setChain(JSONHelper.toJson(chain));

        order.place();
        order = or.save(order);

        return order.applyHash();
    }

    @Override
    public Order take(Order order) {
        Order ex = getExisting(order);
        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            currentUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        ex.setDriver(currentUser);

        ex.take();
        order = or.save(ex);

        return order.applyHash();
    }

    @Override
    public Order handover(Order order) throws Exception {
        Order ex = getExisting(order);
        User next = ur.findByPubkey(order.getNextPubkey());
        if(next == null){
            throw new RuntimeException("Public key of next user not found.");
        }
        ex.setDriver(next);

        Chain c = JSONHelper.fromJson2(ex.getChain(), Chain.class);
        Block b = new Block(order.getName(), order.getNextPubkey(), order.getPayload());
        c.append(b);
        ex.setChain(JSONHelper.toJson(c));

        ex.pick();
        order = or.save(ex);

        return order.applyHash();
    }

    @Override
    public Order deliver(Order order) {
        Order ex = getExisting(order);
        ex.deliver();
        order = or.save(ex);

        return order.applyHash();
    }

    @Override
    public Order cancel(Order order) {
        Order ex = getExisting(order);
        ex.cancel();
        order = or.save(ex);

        return order.applyHash();
    }

    @Override
    public Order fail(Order order) {
        throw new NotImplementedException();
    }

    @Override
    public List<User> retrieveHistory(Order order){

        List<User> ret = new ArrayList<>();

        Chain c = JSONHelper.fromJson2(order.getChain(), Chain.class);
        List<String> keys;
        if(c.verify()) {
            keys = c.getBlocks().stream().map(b -> b.pukNext).collect(Collectors.toList());
        } else {
            throw new RuntimeException("Failed to verify chain.");
        }
        for(String key : keys){
            User u = ur.findByPubkey(key);
            ret.add(u);
        }

        return ret;
    }

    private Order getExisting(Order order){
        Long id = order.getId();
        Order ex = or.findById(id).orElseThrow(() -> new EntityNotFoundException(id + ""));

        //handle both app and unit testing
        ex.ctx(order.getCtx() == null ? Application.getCtx() : order.getCtx());
        return ex;
    }
}
