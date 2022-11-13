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
public class ModelNotInTheSearchListException extends Exception {

    /**
     * Creates a new instance of <code>ModelNotInTheSearchListException</code>
     * without detail message.
     */
    public ModelNotInTheSearchListException() {
    }

    /**
     * Constructs an instance of <code>ModelNotInTheSearchListException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelNotInTheSearchListException(String msg) {
        super(msg);
    }
}
