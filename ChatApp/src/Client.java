import java.io.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	
	public Client(Socket socket, String username) {
		try {
		this.socket = socket;
		this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.username = username;
		}catch(IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
		
		
	}
	
	public void sendMessage() {
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			Scanner scanner = new Scanner(System.in);
			while(socket.isConnected()) {
				String newMessage = scanner.nextLine();
				bufferedWriter.write(username + " " + newMessage);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		} catch(IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}
	
	public void listenBroadcasts() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String messageGroup;
				while(socket.isConnected()) {
					try {
						messageGroup = bufferedReader.readLine();
						System.out.println(messageGroup);	
					} catch (IOException e) {
						closeEverything(socket, bufferedReader, bufferedWriter);
					}
				}
			}
		}).start();
	}
	public void closeEverything(Socket s, BufferedReader bufferedReade, BufferedWriter bufferedWritier)
	{
		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}if(bufferedWriter != null) {
				bufferedWriter.close();
			}if(socket !=null) {
				socket.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner userName = new Scanner(System.in);
		System.out.println("Enter your username: ");
		String user = userName.nextLine();
		Socket socket = new Socket("localhost", 8080);
		Client client = new Client(socket, user);
		client.listenBroadcasts();
		client.sendMessage();
	}

}
