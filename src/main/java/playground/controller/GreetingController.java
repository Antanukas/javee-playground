package playground.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

import playground.model.Greeting;
import playground.model.NewGreeting;
import playground.service.GreetingService;

/**
 * Hello world!
 *
 */
@ApplicationScoped
@Path("/greetings")
@Produces(MediaType.APPLICATION_JSON)
//@Api("Greetings")
public class GreetingController {

    @Inject
    GreetingService service;

    @GET
   //@ApiOperation(value = "Gets all greetings", response = Greeting.class, responseContainer = "List")
    public Collection<Greeting> get() {
        return service.get();
    }

    @GET
    @Path("/{id}")
    //@ApiOperation(value = "Gets concrete greeting", response = Greeting.class)
    public Greeting get(@PathParam("id") long id) {
        return service.get(id);
    }

    @POST
    //@ApiOperation(value = "Finds Pets by status", response = Greeting.class)
    //@ApiImplicitParam(paramType = "body", dataType = "playground.NewGreeting")
    public Greeting create(NewGreeting greeting) {
        return service.create(greeting);
    }

    @PUT
    @Path("/{id}")
    //@ApiOperation(value = "Updates existing greeting", response = Greeting.class)
    //@ApiImplicitParam(paramType = "body", dataType = "playground.Greeting")
    public Greeting update(@PathParam("id") long id, Greeting greeting) {
        return service.update(id, greeting);
    }

    @DELETE
    //@ApiOperation(value = "Deletes greeting", response = Greeting.class)
    @Path("/{id}")
    public Greeting delete(@PathParam("id") long id) {
        return service.delete(id);
    }
}
