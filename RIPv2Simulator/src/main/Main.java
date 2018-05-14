package main;

import network.NetworkGraph;
import network.Router;
import network.RoutingTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        NetworkGraph networkGraph = new NetworkGraph();
/*
        networkGraph.addRouter("90.68.53.225");
        networkGraph.addRouter("234.191.239.11");
        networkGraph.addRouter("29.237.71.205");
        networkGraph.addRouter("93.205.217.103");
        networkGraph.addRouter("152.145.34.204");

        networkGraph.addEdge("90.68.53.225", "234.191.239.11", 1);
        networkGraph.addEdge("90.68.53.225", "29.237.71.205", 1);
        networkGraph.addEdge("234.191.239.11", "93.205.217.103", 1);
        networkGraph.addEdge("234.191.239.11", "29.237.71.205", 1);
        networkGraph.addEdge("29.237.71.205", "93.205.217.103", 1);
        networkGraph.addEdge("93.205.217.103", "152.145.34.204", 1);*/

        networkGraph.addRouter("111.111.111.111");
        networkGraph.addRouter("222.222.222.222");
        networkGraph.addRouter("333.333.333.333");
        networkGraph.addRouter("444.444.444.444");
        networkGraph.addRouter("555.555.555.555");

        networkGraph.addEdge("111.111.111.111", "222.222.222.222", 1);
        networkGraph.addEdge("111.111.111.111", "333.333.333.333", 1);
        networkGraph.addEdge("222.222.222.222", "444.444.444.444", 1);
        networkGraph.addEdge("222.222.222.222", "333.333.333.333", 1);
        networkGraph.addEdge("333.333.333.333", "444.444.444.444", 1);
        networkGraph.addEdge("444.444.444.444", "555.555.555.555", 1);

        showMenu();
        Scanner in = new Scanner(System.in);
        while (true) {
            int input = in.nextInt();

            switch (input) {
                case 0:
                    showMenu();
                    break;
                case 1:
                    addRouter(networkGraph);
                    break;
                case 2:
                    addEdge(in, networkGraph);
                    break;
                case 3:
                    networkGraph.printRouters();
                    break;
                case 4:
                    networkGraph.printEdges();
                    break;
                case 5:
                    showTable(networkGraph);
                    break;
                case 6:
                    removeEdge(networkGraph);
                    break;
                case 7:
                    sendPacket(networkGraph);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Wrong input. Type 0 to see the menu");
                    break;

            }
        }
    }

    private static String readFromConsole() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void showMenu() {
        System.out.println("0 to see this list again");
        System.out.println("1 Add a router");
        System.out.println("2 Add an edge");
        System.out.println("3 See routers' list");
        System.out.println("4 See edges' list");
        System.out.println("5 See router's routing table");
        System.out.println("6 Remove an edge");
        System.out.println("7 Send a packet");

    }

    /*
    option 1 - /ar
    */
    private static void addRouter(NetworkGraph networkGraph) {
        System.out.println("Enter router's IP");
        String readInput = readFromConsole();
        if (readInput == null)
            System.out.println("IP is invalid");
        else {
            networkGraph.addRouter(readInput);
            System.out.println("IP " + readInput + " router created");
        }
    }

    /*
    option 2 - /ae
    */
    private static void addEdge(Scanner in, NetworkGraph networkGraph) {
        System.out.println("Enter edge source");
        String src = readFromConsole();
        if (src == null)
            System.out.println("IP is invalid");
        else {
            System.out.println("Enter edge destination");
            String dest = readFromConsole();
            if (dest == null)
                System.out.println("IP is invalid");
            else {
                System.out.println("Enter edge cost");
                int weight = in.nextInt();
                if (weight >= 0) {
                    networkGraph.addEdge(src, dest, weight);
                    System.out.println("network.RouterEdge created " + src + " " + dest + " " + weight);
                }

            }
        }
    }

    /*
    option 5 - /re
    */
    private static void removeEdge(NetworkGraph networkGraph) {
        System.out.println("Enter edge source");
        String src = readFromConsole();
        if (src == null)
            System.out.println("IP is invalid");
        else {
            System.out.println("Enter edge destination");
            String dest = readFromConsole();
            if (dest == null)
                System.out.println("IP is invalid");
            else {
                networkGraph.removeEdge(src, dest);
                System.out.println("Edge removed");
            }
        }
    }

    /*
    option 6 - /st
    */
    private static void showTable(NetworkGraph networkGraph) {
        System.out.println("Enter router's IP");
        String src = readFromConsole();
        if (src == null)
            System.out.println("IP is invalid");
        else {
            networkGraph.printTable(src);
        }

    }

    /*
    option 7 - /sp
     */
    private static void sendPacket(NetworkGraph networkGraph) {
        System.out.println("Send from:");
        String src = readFromConsole();
        if (src == null)
            System.out.println("IP is invalid");
        else {
            System.out.println("Send to:");
            String dest = readFromConsole();
            if (dest == null)
                System.out.println("IP is invalid");
            else {
                networkGraph.sendPacket(src, dest);
            }

        }

    }

}