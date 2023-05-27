import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.*;

public class Client {

	Socket socket;
	BufferedReader br;
	PrintWriter out;
	//Required for GUI
	private JLabel heading = new JLabel("Client Area");
	private JTextArea messageArea = new JTextArea();
	private JTextField inputMessage = new JTextField();
	//private Font font = new Font("Roboto",FONT.PLAIN,20);
	
	public Client() {
		System.out.println("Sending request");
		
		try {
			socket = new Socket("127.0.0.1",7777);
			System.out.println("Connection done");

			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out= new PrintWriter(socket.getOutputStream());
			
			
			startReading();
			startWriting();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startReading() {
		Runnable r1 = ()->{
			System.out.println("Client start reading....");
			try {
				while(true) {
				
					String msg = br.readLine();
					if(msg.equalsIgnoreCase("bye")) {
						System.out.println("Server has stopped the chat...");
						socket.close();
						break;
					}
					System.out.println("Server: "+msg);
						
				}
			} catch (IOException e) {
				System.out.println("Connection is closed..");
			}
		};
		new Thread(r1).start();
	}
	
	public void startWriting() {
		Runnable r2 = ()->{
			try {
				while(true) {
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
				System.out.println("Connection is closed..");
			}
			
		};
		new Thread(r2).start();
	}
	
	public static void main(String[] args) {
		new Client();
	}
	
}
