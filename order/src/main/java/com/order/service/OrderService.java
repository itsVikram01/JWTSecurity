package com.order.service;

import com.order.exception.exceptionHandlingInSpringboot.usingRestControllerAdvice.OrderNotFoundException;
import com.order.model.Orders;
import com.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    public Orders createOrder(Orders order) {
        return orderRepository.save(order);
    }

    public Orders updateOrder(Long id, Orders order) {
        Orders existingOrder = getOrderById(id); // Reuse exception handling
        existingOrder.setProduct(order.getProduct());
        existingOrder.setQuantity(order.getQuantity());
        existingOrder.setPrice(order.getPrice());
        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Cacheable("exampleData")
    public String getExpensiveData() {
        try {
            Thread.sleep(3000); // Simulate delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Expensive Result";
    }

    @CircuitBreaker(name = "exampleService", fallbackMethod = "fallback")
    public String callExternalService() {
        // Simulate external service call
        return "Success";
    }

    public String fallback(Throwable t) {
        return "Fallback Response";
    }

}