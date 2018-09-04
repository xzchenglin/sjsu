package logi.domain.repository;

import javafx.util.Pair;
import logi.domain.helper.DbUtil;
import logi.domain.model.Address;
import logi.domain.model.Order;
import logi.domain.model.User;
import logi.domain.model.state.State;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepositoryExtImpl implements OrderRepositoryExt {

    @PersistenceContext
    EntityManager em;

    static final Map<String, Integer> sel2IndexMap = new HashMap<>();

    @Override
    public Collection<Order> findByRaw(List<Pair> pairs) {

        String sels = "id,name,state,creation_time,last_modified_time,entity_version,receiver_phone,dest_addr_id,driver_id,receiver_id,sender_id,sender_addr_id,chain,next_pubkey," +
                "id,name,role,pwd,creation_time,last_modified_time,entity_version,email,phone,pubkey," +
                "id,creation_time,last_modified_time,addr,city,country,is_primary,state,zip,user_id,contact";
        String[] alias = {"o", "u", "a"};
        sels = DbUtil.prepareSels(sels, sel2IndexMap, alias);

        String sql = "SELECT " + sels +
                " FROM bc_order o " +
                "LEFT JOIN bc_user u on o.driver_id = u.id or u.id=o.sender_id or u.id = o.receiver_id " +
                "LEFT JOIN bc_addr a on o.dest_addr_id = a.id or a.id = o.sender_addr_id";

        Query query = DbUtil.buildQuery(sql, pairs, em);

        List<Object[]> list = query.getResultList();
        Map<Long, Order> orderMap = new HashMap<>();
        Map<Long, Address> addrMap = new HashMap<>();
        Map<Long, User> userMap = new HashMap<>();

        for(Object[] objs : list){
            Long uid = objs[i4u("id")] == null ? null : ((BigInteger)objs[i4u("id")]).longValue();
            if(uid != null && userMap.get(uid) == null){
                User u = new User();
                u.setId(uid);
                u.setName((String) objs[i4u("name")]);
                u.setRole(User.Role.valueOf((String) objs[i4u("role")]));
                u.setEmail(objs[i4u("email")] == null ? null : (String) objs[i4u("email")]);
                u.setPhone(objs[i4u("phone")] == null ? null : (String) objs[i4u("phone")]);

                userMap.put(uid, u);
            }

            Long aid = objs[i4a("id")] == null ? null : ((BigInteger)objs[i4a("id")]).longValue();
            if(aid != null && addrMap.get(aid) == null){
                Address a = new Address();
                a.setId(aid);
                a.setAddr(objs[i4a("addr")] == null ? null : (String) objs[i4a("addr")]);
                a.setCity(objs[i4a("city")] == null ? null : (String) objs[i4a("city")]);
                a.setCountry(objs[i4a("country")] == null ? null : (String) objs[i4a("country")]);
                a.setZip(objs[i4a("zip")] == null ? null : (String) objs[i4a("zip")]);
                a.setState(objs[i4a("state")] == null ? null : (String) objs[i4a("state")]);
                a.setContact(objs[i4a("contact")] == null ? null : (String) objs[i4a("contact")]);

                addrMap.put(aid, a);
            }
        }

        for(Object[] objs : list){
            Order o = null;
            Long id  = ((BigInteger)objs[i4o("id")]).longValue();
            if(orderMap.get(id) != null){
                continue;
            }

            o = new Order();
            o.setId(id);
            o.setName((String) objs[i4o("name")]);
            o.setState(State.valueOf((String) objs[i4o("state")]));
            o.setCreationTime(((BigInteger) objs[i4o("creation_time")]).longValue());
            o.setLastModified(((BigInteger) objs[i4o("last_modified_time")]).longValue());
            o.setEntityVersion(((BigInteger) objs[i4o("entity_version")]).longValue());
            o.setReceiverPhone(objs[i4o("receiver_phone")] == null ? null : (String) objs[i4o("receiver_phone")]);
            o.setChain(objs[i4o("chain")] == null ? null : (String) objs[i4o("chain")]);
            o.setNextPubkey(objs[i4o("next_pubkey")] == null ? null : (String) objs[i4o("next_pubkey")]);

            Long aid = objs[i4o("dest_addr_id")] == null ? null : ((BigInteger)objs[i4o("dest_addr_id")]).longValue();
            if(aid != null){
                o.setDestAddr(addrMap.get(aid));
            }
            aid = objs[i4o("sender_addr_id")] == null ? null : ((BigInteger)objs[i4o("sender_addr_id")]).longValue();
            if(aid != null){
                o.setSenderAddr(addrMap.get(aid));
            }
            Long uid = objs[i4o("driver_id")] == null ? null : ((BigInteger)objs[i4o("driver_id")]).longValue();
            if(uid != null){
                o.setDriver(userMap.get(uid));
            }
            uid = objs[i4o("receiver_id")] == null ? null : ((BigInteger)objs[i4o("receiver_id")]).longValue();
            if(uid != null){
                o.setReceiver(userMap.get(uid));
            }
            uid = objs[i4o("sender_id")] == null ? null : ((BigInteger)objs[i4o("sender_id")]).longValue();
            if(uid != null){
                o.setSender(userMap.get(uid));
            }

            orderMap.put(id, o);
        }

        return orderMap.values();
    }

    private int i4o(String f){
        return sel2IndexMap.get("o_" + f);
    }

    private int i4u(String f){
        return sel2IndexMap.get("u_" + f);
    }

    private int i4a(String f){
        return sel2IndexMap.get("a_" + f);
    }
}
