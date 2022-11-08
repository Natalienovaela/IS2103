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
public class ModelNotExistException extends Exception {

    /**
     * Creates a new instance of <code>ModelNotExistException</code> without
     * detail message.
     */
    public ModelNotExistException() {
    }

    /**
     * Constructs an instance of <code>ModelNotExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelNotExistException(String msg) {
        super(msg);
    }
}
