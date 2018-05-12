import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
		
		public static void main(String[] args) {
			NetworkGraph g = new NetworkGraph();
			g.addRouter("111.111.111.111");
			g.addRouter("222.222.222.222");
			g.addRouter("333.333.333.333");
			g.addRouter("444.444.444.444");
			g.addRouter("555.555.555.555");
			
			g.addEdge("111.111.111.111", "222.222.222.222", 1);
			g.addEdge("111.111.111.111", "333.333.333.333", 1);
			g.addEdge("222.222.222.222", "444.444.444.444", 1);
			g.addEdge("222.222.222.222", "333.333.333.333", 1);
			g.addEdge("333.333.333.333", "444.444.444.444", 1);
			g.addEdge("444.444.444.444", "555.555.555.555", 1);
			
			while(true){
				clearConsole();
				showMenu();
				Scanner in = new Scanner(System.in);
				int input = in.nextInt();
				if(input == 1){
					System.out.println("Enter router's IP");
					String readInput = readFromConsole();
					if(readInput == null)
						System.out.println("IP is invalid");
					else{
						g.addRouter(readInput);
						System.out.println("IP " +readInput +" router created");
					}
				}
				if(input == 2){
					System.out.println("Enter edge source");
					String src = readFromConsole();
					if(src == null)
						System.out.println("IP is invalid");
					else{
						System.out.println("Enter edge destination");
						String dest = readFromConsole();
						if(dest == null)
							System.out.println("IP is invalid");
						else{
							System.out.println("Enter edge cost");
							int weight = in.nextInt();
							if(weight >= 0){
								g.addEdge(src, dest, weight);
								System.out.println("Edge creadet " + src + " " + dest + " " + weight);
							}
							
						}
					}
				}
				if(input == 3){
					g.printRouters();
				}
				if(input == 4){
					g.printEdges();
				}
				if(input == 5){
					System.out.println("Enter edge source");
					String src = readFromConsole();
					if(src == null)
						System.out.println("IP is invalid");
					else{
						System.out.println("Enter edge destination");
						String dest = readFromConsole();
						if(dest == null)
							System.out.println("IP is invalid");
						else{
							g.removeEdge(src, dest);
							System.out.println("Enter removed");
						}
					}
				}
				if(input == 6){
					System.out.println("Enter router's IP");
					String src = readFromConsole();
					if(src == null)
						System.out.println("IP is invalid");
					else{
						g.printTable(src);
					}
				}
				if(input == 7){
					System.out.println("Send from:");
					String src = readFromConsole();
					if(src == null)
						System.out.println("IP is invalid");
					else{
						System.out.println("Send to:");
						String dest = readFromConsole();
						if(dest == null)
							System.out.println("IP is invalid");
						else{
							g.sendPacket(src, dest);
						}
						
					}
				}
				if(input == 8) {
					return ;
				}
			}
		}

		private static String readFromConsole(){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				return br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		private static void showMenu(){
			System.out.println("1 Add router");
			System.out.println("2 Add edge");
			System.out.println("3 Routers list");
			System.out.println("4 Edges list");
			System.out.println("5 Remove edge");
			System.out.println("6 Show table");
			System.out.println("7 Send packet");
			
		}
		private static void clearConsole(){
			for(int i = 0; i < 30; i++)
				System.out.print("");
		}
			
}

/*
			g.addRouter("111.111.111.111");
			g.addRouter("222.222.222.222");
			g.addRouter("333.333.333.333");
			g.addRouter("444.444.444.444");
			g.addRouter("555.555.555.555");
			
			g.addEdge("111.111.111.111", "222.222.222.222", 1);
			g.addEdge("111.111.111.111", "333.333.333.333", 1);
			g.addEdge("222.222.222.222", "444.444.444.444", 1);
			g.addEdge("222.222.222.222", "333.333.333.333", 1);
			g.addEdge("333.333.333.333", "444.444.444.444", 10);
			g.addEdge("444.444.444.444", "555.555.555.555", 1);
			
			
			g.printTable("111.111.111.111");
			System.out.println("\n");
			g.printTable("222.222.222.222");
			System.out.println("\n");
			g.printTable("333.333.333.333");
			System.out.println("\n");
			g.printTable("444.444.444.444");
			System.out.println("\n");
			g.printTable("555.555.555.555");
			System.out.println("\n");
			
			g.addRouter("666.666.666.666");
			g.addEdge("111.111.111.111", "666.666.666.666", 1);
			
			
			
			
			g.addRouter("111.111.111.111");
			g.addRouter("222.222.222.222");
			g.addRouter("333.333.333.333");
			
			g.addEdge("111.111.111.111", "222.222.222.222", 1);
			g.addEdge("222.222.222.222", "333.333.333.333", 1);
			
			g.startRouter("111.111.111.111");
			g.startRouter("222.222.222.222");
			g.startRouter("333.333.333.333");
			
			
			
			g.printTable("111.111.111.111");
			System.out.println("\n");
			g.printTable("222.222.222.222");
			System.out.println("\n");
			g.printTable("333.333.333.333");
			System.out.println("\n\n\n\n");
			
			
			g.removeEdge("222.222.222.222", "333.333.333.333");
			
			g.getRouter("111.111.111.111").updateTable(g.getRouters());
			g.printTable("111.111.111.111");
			System.out.println("\n");
			g.printTable("222.222.222.222");
			System.out.println("\n");
			g.printTable("333.333.333.333");
			System.out.println("\n");
*/