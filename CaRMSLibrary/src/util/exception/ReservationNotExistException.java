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
public class ReservationNotExistException extends Exception {

    /**
     * Creates a new instance of <code>ReservationNotExistException</code>
     * without detail message.
     */
    public ReservationNotExistException() {
    }

    /**
     * Constructs an instance of <code>ReservationNotExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationNotExistException(String msg) {
        super(msg);
    }
}
