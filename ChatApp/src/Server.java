import java.net.*;
import java.io.*;

public class Server {
	
	private ServerSocket serverSocket;
	private final int port;
	
	public Server(int port) {
		this.port = port;
		try {
			this.serverSocket = new ServerSocket(this.port);
		}catch(IOException e) {
			e.printStackTrace();
		}
}
	
	public void startServer() {
		try {
			while(!serverSocket.isClosed())
			{
				Socket socket = serverSocket.accept();
				System.out.println("A new client has connected!");
				ClientHandler clientHandler = new ClientHandler(socket);
				
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		}catch(IOException e) {
			
		}
	}
	
	public void closeServerSocket() {
		try {
			if(serverSocket != null)
			{
				serverSocket.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int port = 8080;
		// TODO Auto-generated method stub
		Server server = new Server(port);
		server.startServer();
		
	}

}
