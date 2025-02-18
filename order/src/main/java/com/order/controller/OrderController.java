package com.order.controller;

import com.order.exception.exceptionHandlingInSpringboot.usingResponseStatus.ResponseStatusExample;
import com.order.exception.exceptionHandlingInSpringboot.usingRestControllerAdvice.OrderNotFoundException;
import com.order.model.Orders;
import com.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //@PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN')")
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
        try {
            Orders order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusExample("Product with ID " + id + " not found"); // Or return a response status .
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Orders createOrder(@RequestBody Orders order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Orders updateOrder(@PathVariable Long id, @RequestBody Orders order) {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }



    @GetMapping("/data")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> getData() {
        return ResponseEntity.ok(orderService.getExpensiveData());
    }
}
