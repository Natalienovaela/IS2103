/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author PERSONAL
 */
public class RentalRatesNotExistException extends Exception {

    /**
     * Creates a new instance of <code>RentalRatesNotFoundException</code>
     * without detail message.
     */
    public RentalRatesNotExistException() {
    }

    /**
     * Constructs an instance of <code>RentalRatesNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RentalRatesNotExistException(String msg) {
        super(msg);
    }
    
}
