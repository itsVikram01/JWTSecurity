/*
package com.order.exception.throwable;

//ArithmeticException
public class ThrowableExample {
    public static void main(String[] args) {
        try {
            int result = 10 / 0; // This line will throw an ArithmeticException
            System.out.println(result);
        } catch (Throwable t) {
            //System.out.println(t); // java.lang.ArithmeticException: / by zero

            //t.printStackTrace(); // java.lang.ArithmeticException: / by zero

            System.out.println("Exception type: " + t.getClass().getName()); // Exception type: java.lang.ArithmeticException

            System.out.println("Error message: " + t.getMessage()); // Error message: / by zero

            //throw t; // Exception in thread "main" java.lang.ArithmeticException: / by zero

            //throw new ArithmeticException("Arithmetic exception"); // Exception in thread "main" java.lang.ArithmeticException: Arithmetic exception
        } finally {
            System.out.println("Finally block will always execute either any exception occur or not");
        }
    }
}
*/
