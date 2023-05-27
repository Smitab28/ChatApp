import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	Socket socket;
	ServerSocket server;
	BufferedReader br;
	PrintWriter out;
	
	public Server() {
		try {
			server = new ServerSocket(7777);
			System.out.println("Server is ready to accept connection request");
			System.out.println("Waiting....");
			socket=server.accept();
			
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out= new PrintWriter(socket.getOutputStream());
			startReading();
			startWriting();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public void startReading() {
		Runnable r1 = ()->{
			System.out.println("Server start reading....");
			try {
				while(true) {
				
					String msg = br.readLine();
					if( msg.equalsIgnoreCase("bye")) {
						System.out.println("Client has stopped the chat...");
						socket.close();
						break;
					}
					System.out.println("Client: "+msg);
				}
			} catch (IOException e) {
				System.out.println("Connection is closed");
			}
			
		};
		new Thread(r1).start();
	}
	
	public void startWriting() {
		Runnable r2 = ()->{
			try {
				while(!socket.isClosed()) {
				BufferedReader brWrite = new BufferedReader(new InputStreamReader(System.in));
				
					String content = brWrite.readLine();
					out.println(content);
					out.flush();
					if(content.equalsIgnoreCase("bye")) {
						socket.close();
						break;
					}
				}
			} catch (IOException e) {
				System.out.println("Connection is closed...");
			}
		};
		new Thread(r2).start();
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
