package playground.service;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.PathParam;
import java.util.Collection;
import java.util.List;

import playground.model.Greeting;
import playground.model.NewGreeting;

@ApplicationScoped
public class GreetingService {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Collection<Greeting> get() {
        return em.createQuery("select g from Greeting g").getResultList();
    }

    @Transactional
    public Greeting get(@PathParam("id") long id) throws RuntimeException {
        List<Greeting> list = em.createQuery("select g from Greeting g where id = :id")
                     .setParameter("id", id)
                    .getResultList();
        return list.stream()
                 .findFirst()
                 .orElseThrow(() -> new RuntimeException("Not found with id " + id));
    }

    @Transactional
    public Greeting create(NewGreeting newGreeting) {
        Greeting greeting = new Greeting();
        greeting.text = newGreeting.text;
        em.persist(greeting);
        return greeting;
    }

    @Transactional
    public Greeting update(@PathParam("id") long id, Greeting greeting) {
        Greeting existingGreeting = get(id);
        existingGreeting.text = greeting.text;
        em.persist(existingGreeting);
        return existingGreeting;
    }

    @Transactional
    public Greeting delete(@PathParam("id") long id) {
        Greeting candidate = get(id);
        em.createQuery("delete from Greeting where id = :id").setParameter("id", id).executeUpdate();
        return candidate;
    }
}
