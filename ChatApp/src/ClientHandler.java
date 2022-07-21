import java.util.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.*;
import java.util.Scanner;

public class ClientHandler implements Runnable {
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String clientUsername;
	
	
	public ClientHandler(Socket socket)
	{
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername = bufferedReader.readLine();
			clientHandlers.add(this);
			broadcastMessage("SERVER: "+ clientUsername + "has entered the chat!");

		}catch(IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String tempMessageFromClient;
		
		while(socket.isConnected()) {
			try {
				tempMessageFromClient = bufferedReader.readLine();
				broadcastMessage(tempMessageFromClient);
			} catch(IOException e) {
				closeEverything(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}

	
	public void broadcastMessage(String sendMessage)
	{
		for(ClientHandler clientHandler : clientHandlers) {
			try {
				if(!clientHandler.clientUsername.equals(clientUsername)) {
					clientHandler.bufferedWriter.write(sendMessage);
					clientHandler.bufferedWriter.newLine();
					clientHandler.bufferedWriter.flush();
				}
			}catch(IOException e) {
				closeEverything(socket, bufferedReader, bufferedWriter);
			}
		}
	}
	
	public void removeClient() {
		clientHandlers.remove(this);
		broadcastMessage("SERVER: "+ clientUsername+ "has left the chat!");
	}
	public void closeEverything(Socket s, BufferedReader bufferedReade, BufferedWriter bufferedWritier)
	{
		removeClient();
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
}
