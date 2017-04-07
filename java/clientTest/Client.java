import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {		
		Scanner sc = new Scanner(System.in);
		System.out.println("Press enter to connect");
		sc.nextLine();
		Socket s = new Socket("54.195.245.121", 443);
		System.out.println("nnect");
		//ObjectInputStream streamInput = new ObjectInputStream(s.getInputStream());
		ObjectOutputStream streamOutput =  new ObjectOutputStream(s.getOutputStream());
		
		//Listener gotInstructions = new Listener(s);
		//gotInstructions.start();
		System.out.println("nnect");
		while(true){
			System.out.println("\nEnter any text");
			String text = sc.nextLine();
			streamOutput.writeObject(text);
			
		}
		
		
		
	}
	
	
	private static class Listener extends Thread{
		ObjectInputStream streamInput;
		
		public Listener(Socket s) throws IOException{
			streamInput = new ObjectInputStream(s.getInputStream());
		}
		public void run(){
			while(true){
				String temp = "Fel";
				try {
					temp = (String) streamInput.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(temp);
			}
		}
		
	}
}
