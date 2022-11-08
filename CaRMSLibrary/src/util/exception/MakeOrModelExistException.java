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
public class MakeOrModelExistException extends Exception {

    /**
     * Creates a new instance of <code>MakeOrModelExistException</code> without
     * detail message.
     */
    public MakeOrModelExistException() {
    }

    /**
     * Constructs an instance of <code>MakeOrModelExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MakeOrModelExistException(String msg) {
        super(msg);
    }
}
