package pathproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import static java.lang.Thread.sleep;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

public class PathProject {

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


    public static double runSimulation()
    {
        //long start = System.nanoTime();
        //long end = System.nanoTime();
        //double timeTotal = (end - start) / 1000000;
        //System.out.println("time : "+ timeTotal +" ms");

        //initialize the graph base on the Romania map
        Node mcell = new Node("mcell");

        Node ap1 = new Node("AP1");
        Node ap2 = new Node("AP2");
        Node ap3 = new Node("AP3");
        Node ap4 = new Node("AP4");
        Node ap5 = new Node("AP5");
        Node ap6 = new Node("AP6");
        Node ap7 = new Node("AP7");
        Node ap8 = new Node("AP8");
        Node ap9 = new Node("AP9");

        List<Node> net = new ArrayList<Node>();
        net.add(ap1);
        net.add(ap2);
        net.add(ap3);
        net.add(ap4);
        net.add(ap5);
        net.add(ap6);
        net.add(ap7);
        net.add(ap8);
        net.add(ap9);

        for (Node n1 : net) {
            n1.adjacencies = new Edge[8];
            int edgeCount = 0;
            for (int i = 0; i < net.size(); i++) {
                if(!n1.equals(net.get(i)))
                {
                    int x1 = net.get(i).x;
                    int y1 = net.get(i).y;
                    int x2 = n1.x;
                    int y2 = n1.y;
                    double delay = Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
                    if(delay<200)
                        n1.adjacencies[edgeCount] = new Edge(net.get(i),delay);
                    else
                        n1.adjacencies[edgeCount] = new Edge(net.get(i),Double.MAX_VALUE);
                    edgeCount++;
                }
            }
        }



        String closestToOrigin ="";
        double distanceFromOrigin = Double.MAX_VALUE;
        for (Node n1 : net) {
                 
                /* System.out.println(n1.value);
                 System.out.println("x: "+n1.x + "  y:"+n1.y);
                 for(Edge e:n1.adjacencies)
                 {
                     System.out.println("Edge to :" +e.target.value + " delay:"+e.weight);
                 }*/
            int x1 = 0;
            int y1 = 0;
            int x2 = n1.x;
            int y2 = n1.y;
            double delay = Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
            if(delay<distanceFromOrigin)
            {
                distanceFromOrigin = delay;
                closestToOrigin = n1.value;
            }
        }
        net.add(mcell);

        final Network myNetwork = new Network(net);
        //Update new connection to mcell from the closest AP
        Edge[] closestNodeEdges = myNetwork.getNode(closestToOrigin).adjacencies;
        Edge[] newEdges = new Edge[closestNodeEdges.length+1];
        for(int i =0 ;i<closestNodeEdges.length;i++)
        {
            newEdges[i] = closestNodeEdges[i];
        }
        newEdges[closestNodeEdges.length] = new Edge(myNetwork.getNode("mcell"), distanceFromOrigin);
        myNetwork.getNode(closestToOrigin).adjacencies = newEdges;
        // Update mcell with link to closest AP
        myNetwork.getNode(mcell).adjacencies = new Edge[]{
                new Edge(myNetwork.getNode(closestToOrigin),distanceFromOrigin)
        };


        myNetwork.displayValues();
        System.out.println("--> Nearest Node to MCELL "+closestToOrigin);


        //Simulate a connection drop between nodes
        // n2.Disconnect(n9);
        // n1.Disconnect(n9);


        //Simulate a connection retrieval
        //  n2.Connect(n9);
        // n1.Connect(n9);

        //  Node[] nodes = {ap1,ap2,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19,n20};

        //compute paths - SOURCE NODE n4 here as example

        //print shortest paths
            /*
            for(Node n: nodes){
                    System.out.println("Distance to " + 
                            n + ": " + n.shortestDistance);
            List<Node> path = getShortestPathTo(n);
            System.out.println("Path: " + path);
            }*/

        //DESTINATION NODE : n13



// System.out.println("Updated Path: " + path + " "+path.remove(0)); 


        final AtomicInteger totalPackets = new AtomicInteger();
        final AtomicInteger successPackets = new AtomicInteger();
        final AtomicInteger failPackets = new AtomicInteger();
        totalPackets.set(0);
        successPackets.set(0);
        failPackets.set(0);
       
            /*
              computePaths(n2);
            List<Node> path = getShortestPathTo(ap1);
            System.out.println(n1.getDelay(n2));
            System.out.println("Path: " + path + " "+path.remove(0));
            */
        // Node tmp = myNetwork.getRandomNode();
        // processMsg(tmp, ap1,"Hello " + ((int)(Math.random() * 10000 )));
        Thread t1 = new Thread() {
            @Override
            public void run()
            {
                int i = 0;

                while(true)
                {
                    if(!myNetwork.getNode("AP1").q.isEmpty())
                    {
                        totalPackets.getAndIncrement();
                        Packet d = myNetwork.getNode("AP1").q.poll();
                        myNetwork.getNode("AP1").recieveWithRoute(d);

                    }
                    try
                    {
                        sleep((long)100);
                    }catch(Exception ex){};

                    for (; i<10; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }
                }
                    
                   /* for (int i = 0; i<20; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }*/

            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run()
            {
                int i = 1;

                while(true)
                {
                    if(!myNetwork.getNode("AP2").q.isEmpty())
                    {
                        totalPackets.getAndIncrement();
                        Packet d = myNetwork.getNode("AP2").q.poll();
                        myNetwork.getNode("AP2").recieveWithRoute(d);

                    }
                    try
                    {
                        sleep((long)100);
                    }catch(Exception ex){};

                    for (; i<10; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }
                }
                    
                   /* for (int i = 0; i<20; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }*/

            }
        };
        Thread t3 = new Thread() {
            @Override
            public void run()
            {
                int i = 1;

                while(true)
                {
                    if(!myNetwork.getNode("AP3").q.isEmpty())
                    {
                        totalPackets.getAndIncrement();
                        Packet d = myNetwork.getNode("AP3").q.poll();
                        myNetwork.getNode("AP3").recieveWithRoute(d);

                    }
                    try
                    {
                        sleep((long)100);
                    }catch(Exception ex){};

                    for (; i<10; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }
                }
                    
                   /* for (int i = 0; i<20; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }*/

            }
        };
        Thread t4 = new Thread() {
            @Override
            public void run()
            {
                int i = 10;

                while(true)
                {
                    if(!myNetwork.getNode("AP4").q.isEmpty())
                    {
                        totalPackets.getAndIncrement();
                        Packet d = myNetwork.getNode("AP4").q.poll();
                        myNetwork.getNode("AP4").recieveWithRoute(d);

                    }
                    try
                    {
                        sleep((long)100);
                    }catch(Exception ex){};

                    for (; i<1; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }
                }
                    
                   /* for (int i = 0; i<20; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }*/

            }
        };
        Thread t5 = new Thread() {
            @Override
            public void run()
            {
                int i = 10;

                while(true)
                {
                    if(!myNetwork.getNode("AP5").q.isEmpty())
                    {
                        totalPackets.getAndIncrement();
                        Packet d = myNetwork.getNode("AP5").q.poll();
                        myNetwork.getNode("AP5").recieveWithRoute(d);

                    }
                    try
                    {
                        sleep((long)100);
                    }catch(Exception ex){};

                    for (; i<1; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }
                }
                    
                   /* for (int i = 0; i<20; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }*/

            }
        };
        Thread t6 = new Thread() {
            @Override
            public void run()
            {
                int i = 10;

                while(true)
                {
                    if(!myNetwork.getNode("AP6").q.isEmpty())
                    {
                        totalPackets.getAndIncrement();
                        Packet d = myNetwork.getNode("AP6").q.poll();
                        myNetwork.getNode("AP6").recieveWithRoute(d);

                    }
                    try
                    {
                        sleep((long)100);
                    }catch(Exception ex){};

                    for (; i<1; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }
                }
                    
                   /* for (int i = 0; i<20; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }*/

            }
        };
        Thread t7 = new Thread() {
            @Override
            public void run()
            {
                int i = 10;

                while(true)
                {
                    if(!myNetwork.getNode("AP7").q.isEmpty())
                    {
                        totalPackets.getAndIncrement();
                        Packet d = myNetwork.getNode("AP7").q.poll();
                        myNetwork.getNode("AP7").recieveWithRoute(d);

                    }
                    try
                    {
                        sleep((long)100);
                    }catch(Exception ex){};

                    for (; i<1; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }
                }
                    
                   /* for (int i = 0; i<20; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }*/

            }
        };
        Thread t8 = new Thread() {
            @Override
            public void run()
            {
                int i = 10;

                while(true)
                {
                    if(!myNetwork.getNode("AP8").q.isEmpty())
                    {
                        totalPackets.getAndIncrement();
                        Packet d = myNetwork.getNode("AP8").q.poll();
                        myNetwork.getNode("AP8").recieveWithRoute(d);

                    }
                    try
                    {
                        sleep((long)100);
                    }catch(Exception ex){};

                    for (; i<1; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }
                }
                    
                   /* for (int i = 0; i<20; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }*/

            }
        };
        Thread t9 = new Thread() {
            @Override
            public void run()
            {
                int i = 10;

                while(true)
                {
                    if(!myNetwork.getNode("AP9").q.isEmpty())
                    {
                        totalPackets.getAndIncrement();
                        Packet d = myNetwork.getNode("AP9").q.poll();
                        myNetwork.getNode("AP9").recieveWithRoute(d);

                    }
                    try
                    {
                        sleep((long)100);
                    }catch(Exception ex){};

                    for (; i<1; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }
                }
                    
                   /* for (int i = 0; i<20; i++)
                    {
                        totalPackets.getAndIncrement();
                        Node sender = myNetwork.getRandomNode();
                        System.out.println(sender.value);
                        if(processMsg(sender, myNetwork.getNode(mcell),"Hello " + ((int)(Math.random() * 10000 ))))
                        {
                            successPackets.getAndIncrement();
                        }
                        else
                        {
                            failPackets.getAndIncrement();
                        }
                    }*/

            }
        };
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();

        while(t3.isAlive() || t2.isAlive() || t1.isAlive())
        {

        }
        //DONT DELETE COMMENTS
        System.out.println("\r\nTotal Packets:"+totalPackets.get());
        System.out.println("Passed Packets:"+successPackets.get() + "  " + "Failed Packets:"+failPackets.get());

        double ratio = (double)successPackets.get()/(double)totalPackets.get();
        System.out.println("Ratio: " +df2.format(ratio* 100.0)  +"%");

        return ratio*100;
        // return 0;
    }
    static DecimalFormat df2 = new DecimalFormat(".##");

    public static boolean processMsg(Node sender,Node reciever, String msg)
    {
        computePaths(sender);
        List<Node> pathForMsg = getShortestPathTo(reciever);
        pathForMsg.remove(0);
        return sender.sendWithRoute(new Packet(sender,msg,pathForMsg));

    }
    public static void main(String[] args){

        int numberOfRuns = 1;
        double[] experiments = new double[numberOfRuns];
        for(int i=0;i<experiments.length;i++)
        {
            experiments[i]= runSimulation();
        }

        double average=0;
        for(int i=0;i<experiments.length;i++)
        {
            average += experiments[i];
        }

        average/=experiments.length;
        System.out.println("The average ratio is: "+df2.format(average) +"%");


    }

}
class Network {

    private List<Node> mesh = Collections.synchronizedList(new ArrayList<Node>());

    public Network(List<Node> net){
        mesh = net;
        // mylist.add("abc");
        //   mylist.add("def");
        //   mylist.add("ghi");
        //   mylist.add("jkl");
    }
    public void addNode(Node n) {
        mesh.add(n);
    }
    public Node getNode(Node n) {

        return mesh.get(mesh.indexOf(n));
    }

    public Node getNode(String n) {

        for(Node node: mesh)
        {
            if(node.value==n)
                return node;
        }
        return null;
    }
    public Node getRandomNode()
    {
        Random r = new Random();
        return mesh.get( r.nextInt(mesh.size()-1  ) );

    }
    public void displayValues() {
        System.out.println("--------");
        for (Node n1 : mesh) {

            System.out.println(n1.value);
            System.out.println("x: "+n1.x + "  y:"+n1.y);
            for(Edge e:n1.adjacencies)
            {
                System.out.println("Edge to :" +e.target.value + " delay:"+e.weight);
            }
        }
      /*  for (int i = 0; i < mylist.size(); i++) {
            System.out.println("value is " + mylist.get(i) + " at " + i);
        }*/

        //Draw Topology
        BufferedImage bufferedImage = new BufferedImage(600, 600,
                BufferedImage.TYPE_INT_RGB);

        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, 600, 600);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.BOLD, 12));

        for (Node n1 : mesh) {
            int x = (n1.x*2);
            int y = (n1.y*2);
            if (x <10)
                x+=10;
            else if(x>270)
                x-=60;
            if (y <15)
                y+=15;
            else if(y>280)
                y-=20;

            graphics.drawString(n1.value +" ["+n1.x+","+n1.y+"]", x, y);
        }

        try{
            ImageIO.write(bufferedImage, "jpg", new File(
                    System.getProperty("user.home") + "/Desktop/topology.jpg"));
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteString(int i) {
        mesh.remove(i);
        for (int ij = 0; ij < mesh.size(); ij++) {
            System.out.println("value is " + mesh.get(ij) + " at " + ij);
        }
    }
}