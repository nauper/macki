

import java.util.ArrayList;

public class Workspace {
	private int id;
	private ArrayList<Textbox> boxes;
	private User owner;
	private ArrayList<User> users;
	private int boxId;


	public Workspace(int id, User owner){
		this.id = id;
		boxId = 1;
		this.owner = owner;
		boxes = new ArrayList<Textbox>();
		users = new ArrayList<User>();
		users.add(owner);

	}


	public void addTextbox(int x, int y, int id, String color, String text, User owner){
		boxes.add(new Textbox(x, y, boxId, color, text, owner));
		boxId++;
	}

	public void addUser(User user){
		users.add(user);
		user.addToWork(id);
	}

	public int getId(){
		return id;
	}


	public String getInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append("ID: "+id+" Name: " + owner.returnName());
		for(Textbox item : boxes){
			sb.append("\n\t Textbox: " + item.getInfo());
		}
		return sb.toString();
	}


}
