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
public class RentalRateNotExistException extends Exception {

    /**
     * Creates a new instance of <code>RentalRateNotExistException</code>
     * without detail message.
     */
    public RentalRateNotExistException() {
    }

    /**
     * Constructs an instance of <code>RentalRateNotExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RentalRateNotExistException(String msg) {
        super(msg);
    }
}
