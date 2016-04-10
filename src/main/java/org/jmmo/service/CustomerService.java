package org.jmmo.service;

import org.javatuples.Triplet;
import org.jmmo.model.Customer;
import org.jmmo.model.TokenLog;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Singleton
@Startup
public class CustomerService {
    public static final String USER_MAIL = "fpi@bk.ru";
    public static final String USER_PASSWORD = "123123";

    public static final long TOKEN_EXPIRATION = TimeUnit.HOURS.toMillis(1);

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public Triplet<String, String, Instant> login(String mail, String password) {
        final Customer customer = findByMail(mail);
        if (customer == null) {
            return Triplet.with("customer.notFound", null, null);
        }
        if (!customer.getPassword().equals(password)) {
            return Triplet.with("customer.badPassword", null, null);
        }

        final String token = UUID.randomUUID().toString();
        final Instant instant = Instant.now();
        final Timestamp creation = Timestamp.from(instant);
        final Timestamp expiration = new Timestamp(creation.getTime() + TOKEN_EXPIRATION);

        if (customer.getToken() != null && customer.getTokenExpiration().after(creation)) {
            final TokenLog tokenLog = new TokenLog();
            tokenLog.setToken(customer.getToken());
            tokenLog.setOperation(TokenLog.Operation.DISMISSED);
            tokenLog.setTime(creation);
            em.persist(tokenLog);
        }

        customer.setToken(token);
        customer.setTokenCreation(creation);
        customer.setTokenExpiration(expiration);
        em.merge(customer);

        final TokenLog tokenLog = new TokenLog();
        tokenLog.setToken(token);
        tokenLog.setOperation(TokenLog.Operation.CREATED);
        tokenLog.setTime(creation);
        tokenLog.setExpiration(expiration);
        em.persist(tokenLog);

        return Triplet.with(null, customer.getToken(), instant);
    }

    public Customer findByMail(String mail) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        final Root<Customer> fromRoot = cq.from(Customer.class);

        cq.where(cb.equal(fromRoot.get("mail"), mail));

        try {
            return em.createQuery(cq).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @PostConstruct
    public void initDB() {
        if (findByMail(USER_MAIL) != null) {
            return;
        }

        log.info("Creating admin");

        final Customer customer = new Customer();
        customer.setMail(USER_MAIL);
        customer.setPassword(USER_PASSWORD);

        em.persist(customer);
    }

}
