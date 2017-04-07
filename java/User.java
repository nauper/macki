

import java.util.ArrayList;

public class User {
	private int id;
	private String userName;
	private String password;
	private ArrayList<Integer> workspaces;

	public User(String userName, String password, int id) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		workspaces = new ArrayList<Integer>();
	}

	public boolean checkPassword(String passwordTest) {
		return password.equals(passwordTest);
	}

	public boolean changePW(String newPass, String oldPass) {
		if (checkPassword(oldPass)) {
			userName = newPass;
			return true;
		} else {
			return false;
		}
	}
	
	public void addToWork(int workspaceID){
		workspaces.add(workspaceID);
	}
	
	public String returnName(){
		return userName;
	}
	public int returnID(){
		return id;
	}
}


