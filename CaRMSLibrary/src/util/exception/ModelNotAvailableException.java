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
public class ModelNotAvailableException extends Exception {

    /**
     * Creates a new instance of <code>ModelNotAvailableException</code> without
     * detail message.
     */
    public ModelNotAvailableException() {
    }

    /**
     * Constructs an instance of <code>ModelNotAvailableException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelNotAvailableException(String msg) {
        super(msg);
    }
}
