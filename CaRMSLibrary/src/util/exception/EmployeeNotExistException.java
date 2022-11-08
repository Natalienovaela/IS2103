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
public class EmployeeNotExistException extends Exception{

    /**
     * Creates a new instance of <code>EmployeeNotExistException</code> without
     * detail message.
     */
    public EmployeeNotExistException() {
    }

    /**
     * Constructs an instance of <code>EmployeeNotExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmployeeNotExistException(String msg) {
        super(msg);
    }
}
