package pathproject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Node implements Comparable<Node> {
    public final String value;
    public Edge[] adjacencies;
    Queue<Packet> q = new LinkedList<Packet>();

    public double shortestDistance = Double.POSITIVE_INFINITY;
    public Node parent;
    public boolean available;
    public int x;
    public int y;
    public Node(String val){
        value = val;
        available = true;
        if(!val.startsWith("mcell"))
        {
            Random randomNum = new Random();
            x = randomNum.nextInt(300);
            y = randomNum.nextInt(300);
        }
        else
        {
            x=0;
            y=0;
        }
    }

    public boolean isAvailable()
    {
        return available;
    }

    public String Recieve(String msg)
    {
        this.available = false;
        // System.out.println("(Node "+ value +") is Recieving a msg");
        System.out.println("(Node "+ value +") received: " + msg);

        this.available = true;

        return msg;
    }

    public void recieveWithRoute(Packet data)
    {
        this.available = false;
        // System.out.println("(Node "+ value +") is Recieving a msg");
        System.out.println("(Node "+ value +") received: " + data.msg);
        this.available = true;
        if(data.nl.size()>0)
        {
            data.nl.remove(0);
            this.sendWithRoute(data);
        }
    }

    public boolean Send(String msg, Node n) {
        if(this.isAvailable())
        {
            this.available = false;
            if(n.isAvailable())
            {
                n.Recieve(msg);
                System.out.println(this.value + "is sending to node "+n.value);
                this.available = true;
                return true;
            }
            else
            {
                System.out.println("Packet Drop: Node "+ n.value + " is not available to recieve from " + this.value);
                this.available = true;
                return false;
            }


        }
        else
        {
            System.out.println("Packet Drop: Node "+ this.value + " is not available to send to "+ n.value);
            return false;
        }
    }
    // N5 N3 n4 n1 ap1
    public boolean sendWithRoute(Packet data) {
        boolean returnVal = false;
        if(data.nl.size()>0){
            if(this.isAvailable())
            {

                if(data.nl.get(0).isAvailable())
                {
                    System.out.println(this.value + " is sending to node "+data.nl.get(0).value + " :remainingPATH:"+data.nl);
                    //data.nl.get(0).recieveWithRoute(data);
                    data.nl.get(0).q.add(data);
                    System.out.println(data.nl.get(0).value+" queue size now is: "+ q.size());
                    this.available = true;
                    returnVal= true;
                }
                else
                {
                    System.out.println("Packet Drop: Node "+ data.nl.get(0).value + " is not available to recieve from " + this.value);
                    this.available = true;
                    returnVal= false;
                }


            }
            else
            {
                System.out.println("Packet Drop: Node "+ this.value + " is not available to send to "+ data.nl.get(0).value);
                returnVal= false;
            }
        }

        String printer= "\r\n"+"Transmission completed!! "+data.info+"\r\n";
        try {
            Files.write(Paths.get(System.getProperty("user.home") + "/Desktop/topologyResults.txt" ), printer.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        System.out.print("\r\n"+"Transmission completed!! ");
        System.out.println(data.info+"\r\n");
        System.out.flush();
        return returnVal;
    }
    public String toString(){
        return value;
    }

    public int compareTo(Node other){
        return Double.compare(shortestDistance, other.shortestDistance);
    }

    public int getDelay(Node b)

    {
        for(Edge e : this.adjacencies)
        {
            if(e.target.value == b.value)
            {
                if(e.oldWeight==e.weight)
                    return (int)e.weight;
            }
        }
        return -1;
    }
    public boolean Disconnect(Node b)
    {

        for(Edge e : this.adjacencies)
        {
            if(e.target.value == b.value)
            {
                e.weight = Integer.MAX_VALUE;

            }
        }
        for(Edge e : b.adjacencies)
        {
            if(e.target.value == this.value)
            {
                e.weight = Integer.MAX_VALUE;
                return true;
            }
        }
        return false;
    }

    public boolean Connect (Node b)
    {

        for(Edge e : this.adjacencies)
        {
            if(e.target.value == b.value)
            {
                e.weight = e.oldWeight;

            }
        }
        for(Edge e : b.adjacencies)
        {
            if(e.target.value == this.value)
            {
                e.weight = e.oldWeight;
                return true;
            }
        }
        return false;
    }

    public static void computePaths(Node source){
        source.shortestDistance=0;

        //implement a priority queue
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        queue.add(source);

        while(!queue.isEmpty()){
            Node u = queue.poll();

			/*visit the adjacencies, starting from 
			the nearest node(smallest shortestDistance)*/

            for(Edge e: u.adjacencies){

                Node v = e.target;
                double weight = e.weight;

                //relax(u,v,weight)
                double distanceFromU = u.shortestDistance+weight;
                if(distanceFromU<v.shortestDistance){

					/*remove v from queue for updating 
					the shortestDistance value*/
                    queue.remove(v);
                    v.shortestDistance = distanceFromU;
                    v.parent = u;
                    queue.add(v);

                }
            }
        }
    }

    public static List<Node> getShortestPathTo(Node target){

        //trace path from target to source
        List<Node> path = new ArrayList<Node>();
        for(Node node = target; node!=null; node = node.parent){
            path.add(node);
        }


        //reverse the order such that it will be from source to target
        Collections.reverse(path);

        return path;
    }


}