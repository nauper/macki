import java.io.IOException;
import java.io.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private static int conID;
	private int workspaceID;
	private ArrayList<Connection> allConnections;
	private ArrayList<Workspace> workspaces;
	private int socketPort;
	private boolean open;

	public Server(int port) {
		conID = 1;
		workspaceID = 1;
		socketPort = port;
		allConnections = new ArrayList<Connection>();
		workspaces = new ArrayList<Workspace>();
	}

	public static void main(String[] args) throws IOException {
		int portNumber = 8990;
		Server server = new Server(portNumber);

		System.out.println("Server started!");
		server.start();
		System.out.println("Server stopped!");
	}

	private void start() throws IOException {
		open = true;
		ServerSocket ss = new ServerSocket(socketPort);

		while (open) {
			Connection temp = new Connection(ss.accept());
			System.out.println(temp.socket.getInetAddress());
			System.out.println("Connection made. ID: " + temp.userId);
			allConnections.add(temp);
			temp.start();
		}
		ss.close();

	}

	public void updateClients(String text, Connection ogUser) {
		for (int i = 0; i < allConnections.size(); i++) {
			Connection tempUser = allConnections.get(i);
			if (tempUser.socket.isConnected()) {
				if(tempUser != ogUser){
					tempUser.streamOutput.println(text);
				}
				
			} else {
				allConnections.remove(tempUser);
				System.out.println("User: " + tempUser.userId + " is gone");
			}
		}
	}
	//Seperate connection for each client
	private class Connection extends Thread {
		User user;
		Socket socket;
		BufferedReader streamInput;
		PrintWriter streamOutput;
		int userId;
		String instruction;
		boolean active = true;
		Workspace currentWorkspace;

		public Connection(Socket socket) throws IOException {
			userId = conID++;
			this.socket = socket;
			streamOutput = new PrintWriter(socket.getOutputStream(), true);
			streamInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}

		public void run() {
			//Create user, gets name and password
			String tempName = "user";
			String tempPass = "password";
			//try {
				//write("Enter username:");
				//tempName = streamInput.readLine();
			System.out.println(tempName);
				//write("Enter password:");
				//tempPass = streamInput.readLine();
			System.out.println(tempPass);
		//	} //catch (IOException e1) {
				// TODO Auto-generated catch block
			//	e1.printStackTrace();
			//}
			user = new User(tempName, tempPass, userId);
			action("<workspace>");

			//Start listening for instructions
			while (active) {
				try {
					instruction = streamInput.readLine();
					System.out.println(instruction);

				} catch (IOException e) {
					System.out.println("User: " + user.returnName() + " disconnected!");
					allConnections.remove(this);
					return;
				}
				int tempVal =action(instruction);
				if ( tempVal == 1 ) {
					updateClients(instruction, null);
				}else if(tempVal == 2){
					updateClients(instruction, this);
				}else{
					System.out.println("fail");
					write("Fail");
				}

			}
		}

		private int action(String instruction){
			String[] tempList;
			if(instruction.charAt(0) != '<' && instruction.charAt(instruction.length()-1) != '>'){
				return 0;
			}else{
				instruction = instruction.substring(1, instruction.length()-1);
				tempList = instruction.split("-");
			}

			switch (tempList[0]) {
			case "workspace":
				Workspace temp = new Workspace(workspaceID, user);
				workspaceID++;
				workspaces.add(temp);
				currentWorkspace = temp;
				break;
			case "tbox":
				try {
					currentWorkspace.addTextbox(Integer.parseInt(tempList[2]), Integer.parseInt(tempList[3]), Integer.parseInt(tempList[1]), tempList[4], tempList[5], user);
					return 1;
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			case "ping":
				break;
			case "mpos":
				break;
			case "writ":
				return 2;
			case "online":
				for(Connection item : allConnections){
					write(item.user.returnName());
				}
				break;
			case "printall":
				for (Workspace item : workspaces) {
					System.out.println(item.getInfo());
				}

				break;
			case "logoff":
				active = false;
				closeConnection();
				System.out.println("User: " + user.returnName() + " logoffed");
				break;
			case "move":
				return 2;
			case "remv":
				return 1;
			default:
				return 0;

			}
			return 1;
		}

		private void closeConnection(){
			try {
				if(streamOutput != null) streamOutput.close();
				if(streamInput != null) streamInput.close();
				if(socket != null) socket.close();
			}
			catch(Exception e) {}
		}

		private void write(String text) {
			streamOutput.println(text);
		}

	}

}
