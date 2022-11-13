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
public class CarsNotAvailableException extends Exception {

    /**
     * Creates a new instance of <code>CarsNotAvailableException</code> without
     * detail message.
     */
    public CarsNotAvailableException() {
    }

    /**
     * Constructs an instance of <code>CarsNotAvailableException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarsNotAvailableException(String msg) {
        super(msg);
    }
}
