/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Natalienovaela
 */
public class TransitDriverDispatchNotExistException extends Exception {

    /**
     * Creates a new instance of
     * <code>TransitDriverDispatchNotExistException</code> without detail
     * message.
     */
    public TransitDriverDispatchNotExistException() {
    }

    /**
     * Constructs an instance of
     * <code>TransitDriverDispatchNotExistException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public TransitDriverDispatchNotExistException(String msg) {
        super(msg);
    }
}
