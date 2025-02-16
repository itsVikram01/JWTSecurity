package com.order.exception.error;

//OutOfMemoryErrorExample
public class ErrorExample {
    public static void main(String[] args) {
        try {
            // Code that intentionally allocates a massive amount of memory,
            // potentially causing an OutOfMemoryError
            byte[] largeArray = new byte[Integer.MAX_VALUE];
        } catch (Error e) { // Not recommended to catch Error directly
            System.out.println("Error type: " + e.getClass().getName());
            System.out.println("Error message: " + e.getMessage());
        }
    }
}
