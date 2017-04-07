
//
public class Textbox {
	private Position pos;
	private String text;
	private String color;
	private int id;
	private User owner;

	// position class to store x, y
	public class Position {
		public int x, y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public Textbox(int x, int y, int id, String color, String text, User owner) {
		this.pos = new Position(x, y);
		this.id = id;
		this.color = color;
		this.owner = owner;
		this.text = text;
	}

	// Get-set position
	public Position getPos() {
		return pos;
	}

	public void setPos(int x, int y) {
		pos = new Position(x, y);
	}

	// Get-set color
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	// Get-set-add content
	public String getContent() {
		return text;
	}

	public void setContent(String text) {
		this.text = text;
	}

	// Get-set-add id
	public int getId() {
		return id;
	}

	public User getOwner() {
		return owner;
	}

	public String getInfo(){
		return "X: " + pos.x + " Y: " + pos.y + " ID: " + id + " Color: "
	+ color + " Owner: " + owner.returnName();
 	}

}
