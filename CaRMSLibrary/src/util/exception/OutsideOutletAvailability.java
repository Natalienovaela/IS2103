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
public class OutsideOutletAvailability extends Exception{

    /**
     * Creates a new instance of <code>OutsideOutletAvailability</code> without
     * detail message.
     */
    public OutsideOutletAvailability() {
    }

    /**
     * Constructs an instance of <code>OutsideOutletAvailability</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OutsideOutletAvailability(String msg) {
        super(msg);
    }
}
