package om.controller;

import om.data.OrderRepository;
import om.domain.Order;
import om.domain.OrderResource;
import om.domain.OrderResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@ExposesResourceFor(Order.class)
@RequestMapping(value = "/order", produces = "application/json")
public class OrderController {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderResourceAssembler assembler;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<OrderResource>> findAllOrders() {
        List<Order> orders = repository.findAll();
        return new ResponseEntity<>(assembler.toResourceCollection(orders), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<OrderResource> createOrder(@RequestBody Order order) {
        Order createdOrder = repository.create(order);
        return new ResponseEntity<>(assembler.toResource(createdOrder), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<OrderResource> findOrderById(@PathVariable Long id) {
        Optional<Order> order = repository.findById(id);
        if(order.isPresent()) {
            return new ResponseEntity<>(assembler.toResource(order.get()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //deleteOrder

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        boolean wasDeleted = repository.delete(id);
        HttpStatus responseStatus = wasDeleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(responseStatus);
    }

    //updateOrder
    @RequestMapping(value = "/{id}", consumes = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<OrderResource> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        boolean wasUpdated = repository.update(id, updatedOrder);

        if(wasUpdated) {
            return findOrderById(id);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
