/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathproject;

/*@author Owner*/
public class Edge{
    public final Node target;
    public double weight;
    public double oldWeight;
    public Edge(Node targetNode, double weightVal){
        target = targetNode;
        weight = weightVal;
        oldWeight = weightVal;
    }
}

