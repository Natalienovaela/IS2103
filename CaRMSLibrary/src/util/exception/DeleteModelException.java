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
public class DeleteModelException extends Exception{

    /**
     * Creates a new instance of <code>DeleteModelException</code> without
     * detail message.
     */
    public DeleteModelException() {
    }

    /**
     * Constructs an instance of <code>DeleteModelException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteModelException(String msg) {
        super(msg);
    }
}
