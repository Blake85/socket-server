import java.io.*;
import java.net.*;
import java.util.*;

public class Team9Server {

        public static void main(String[] args) {
                if (args.length < 1) return;

                int port = Integer.parseInt(args[0]);

                try (ServerSocket serverSocket = new ServerSocket(port)){
                        System.out.println("Server is listening on port "+port);

                        while(true) {
                                Socket socket = serverSocket.accept();
                                System.out.println("New client connected");
                                
                                new ServerThread(socket).start();
                               

                        }
                       
		        } catch (IOException e) {
		                System.out.println("Server exception: " +e.getMessage());
		                e.printStackTrace();
		        }

}

}
class ServerThread extends Thread{
	private Socket socket; 
	private Process process;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			 OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true);

             InputStream input = socket.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(input));
             String command = br.readLine();
             System.out.println("Running Command: " +command);

             writer.println(new Date().toString());

             String response = "", line = "";

             try {
                     process = Runtime.getRuntime().exec(command);
                     BufferedReader read = new BufferedReader(new InputStreamReader(process.getInputStream()));
                     while((line = read.readLine()) != null) {
                             System.out.println(line);
                             response += line +"\n";
                     }
                     process.waitFor();
                     //System.out.println("exit value: " + process.exitValue());
                     process.destroy();
             }catch(Exception ex) {
                     System.out.println("exception");
             }

             writer.println(response);
	    }
		catch(Exception e) {
			 System.out.println("Server exception: " +e.getMessage());
             e.printStackTrace();
		}
	}
}


