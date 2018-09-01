package logi.domain.model;

import logi.Application;
import logi.comm.JSONHelper;
import logi.comm.Utils;
import logi.domain.bc.Chain;
import logi.domain.model.state.OrderState;
import logi.domain.model.state.State;
import org.springframework.context.ApplicationContext;

import javax.persistence.*;

/**
 * Created by Lin Cheng
 */
@Entity
@Table(name = "bc_order")
public class Order extends VersionedEntity {

    String name;
    @Column(name = "next_pubkey", length = 65536)
    String nextPubkey;
    @Column(length = 65536)
    String chain;
    @Enumerated(EnumType.STRING)
    State state = State.Init;
    @Column(name = "receiver_phone")
    String receiverPhone;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="sender_addr_id")
    Address senderAddr;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="dest_addr_id")
    Address destAddr;

    @ManyToOne
    @JoinColumn(name="sender_id")
    User sender;
    @ManyToOne
    @JoinColumn(name="receiver_id")
    User receiver;
    @ManyToOne
    @JoinColumn(name="driver_id")
    User driver;

    @Transient
    String payload;
    @Transient
    String hash;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextPubkey() {
        return nextPubkey;
    }

    public void setNextPubkey(String nextPubkey) {
        this.nextPubkey = nextPubkey;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Address getSenderAddr() {
        return senderAddr;
    }

    public void setSenderAddr(Address senderAddr) {
        this.senderAddr = senderAddr;
    }

    public Address getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(Address destAddr) {
        this.destAddr = destAddr;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public void setCtx(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public State getState() {
        if(state == null){
            state = State.Init;
        }
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Order applyHash(){
        try {
            this.setHash(Utils.md5(JSONHelper.toJson(JSONHelper.fromJson2(chain, Chain.class).getLast())));
            this.setChain(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void place(){
        instantiateState().place(this);
    }

    public void take(){
        instantiateState().take(this);
    }

    public void pick(){
        instantiateState().pick(this);
    }

    public void deliver(){
        instantiateState().deliver(this);
    }

    public void cancel(){
        instantiateState().cancel(this);
    }

    public void fail(){
        instantiateState().fail(this);
    }

    @Override
    public String toString() {
        return "Order{" +
                "name='" + name + '\'' +
                ", state=" + state +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", senderAddr=" + senderAddr +
                ", destAddr=" + destAddr +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", driver=" + driver +
                '}';
    }

    //for unit testing
    @Transient
    ApplicationContext ctx;
    public Order ctx(ApplicationContext ctx){
        this.ctx = ctx;
        return this;
    }
    public ApplicationContext getCtx(){
        return ctx;
    }
    
    private OrderState instantiateState(){
        ctx = ctx == null ? Application.getCtx() : ctx;
        return ctx.getBean(state.getState());
    }
}
