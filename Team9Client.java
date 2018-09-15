import java.io.*;
import java.net.*;
import java.util.*;

public class Team9Client {
	public static void main(String[] args) {
		//if(args.length < 2) return;
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("What is the network address?");
		String addr = input.nextLine();
		System.out.println("What port?");
		int port = input.nextInt();
		System.out.println("Enter operation to perform: ");
		String operation = input.next();
		System.out.println("Enter number of clients: ");
		int num = input.nextInt();
		
		long sum = 0;
		float average;
		
		ClientThread[] threads= new ClientThread[num];
		
		for(int i=0; i<num; i++) {
			threads[i] = new ClientThread(addr, port, operation);
			threads[i].start();
		}
		for(int i=0; i<num; i++) {
			try {
				threads[i].join();
			}catch(Exception e) {}
			sum += threads[i].getTime();
		}
		
		System.out.println("Total turn around time, in ms: " +sum);
		System.out.println("Average trun around time: " +sum/(float)num);
	}
}

class ClientThread extends Thread{
	private String addr, operation, response = "";
	private int port;
	private long time;

	public ClientThread(String addr, int port, String operation) {
		this.addr = addr;
		this.port = port;
		this.operation = operation;

	}
	
	
	
	@Override
	public void run() {
		try(Socket socket = new Socket(addr, port)) {
			
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			/*System.out.println("Enter Name :");
			writer.println(scanner.nextLine());
			String line = "";

			System.out.println("Enter command :");
			line = scanner.nextLine();*/
			
			Date start = new Date();
			
			
			writer.println(operation);
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			for(String result = reader.readLine(); !result.equals(""); result= reader.readLine())
				response += result + "\n";
			
			Date end = new Date();
			time = end.getTime() - start.getTime();
			
			
			System.out.println("Turn around time for request, in ms: " +time);

			//scanner.close();
			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public long getTime() {
		return time;
	}
}



