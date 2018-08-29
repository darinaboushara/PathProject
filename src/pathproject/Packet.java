/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathproject;

import java.util.List;

/**
 *
 * @author AhmedMousa
 */
public class Packet {

    public String msg;
    public List<Node> nl;
    public int delay=0;
    public Node[] path;
    public Node sender;
    public Node reciever;
    public String info;
    Packet(Node sender,String msg, List<Node> nl){
        this.msg = msg;
        this.nl = nl;
        this.path = nl.toArray(new Node[nl.size()]);
        try{
            delay+= sender.getDelay(nl.get(0));
        }
        catch(Exception ex)
        {
            System.out.println("\r\nException\r\nException\r\nException: "+sender.value+" "+msg+" "+nl.size());

        }
        for(int i =0;i<nl.size()-1;i++)
        {
            delay+= nl.get(i).getDelay(nl.get(i+1));
        }

        info = "Sender: "+sender.value + " - Path: ["+sender.value+",";
        for(int i =0;i<path.length;i++)
        {
            info+=(path[i].value);
            if(i<path.length-1)
            {
                info+=(",");
            }
        }
        info+=("] Delay: "+delay+"ms");

    }
}
