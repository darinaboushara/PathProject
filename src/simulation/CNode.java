/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;
import java.util.Random;

/**
 *
 * @author Owner
 */
public class CNode {

    double p=0.0;
    Random random;
    public CNode(double p)
    {
        this.p = p;
        random = new Random();
    }

    public boolean isAvailable()
    {

        double r = random.nextDouble();
        if(r<p)
            return true;
        else
            return false;
        // return random.nextDouble()< p;
    }
}
