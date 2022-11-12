/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author Natalienovaela
 */
@Remote
public interface EjbTimerSessionBeanRemote {
    
    public void allocateCars();

    public void allocateCars(Date date);
}
