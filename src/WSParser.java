import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.LinkedList;
import java.util.List;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

import java.text.DateFormat;

public class WSParser {
	
	/*
	 * Takes a string representation of a workspace and 
	 * creates a workspace object from it.
	 */
	public static Workspace parse(String content) {

		List<Textbox> boxes = new LinkedList<Textbox>();

		String date = "<Date=(\\d{8})>";
		String time = "<Time=(\\d{6})>";

		String boxID = "<ID=(\\d+)>";
		String pos = "<Pos=(\\d+),(\\d+)>";
		String col = "<Col=((?:[a-fA-F]|\\d){6})>"; //Hex color
		String text = "<Txt>(.*?)</Txt>";

		String box = "<Box>" + boxID + pos + col + text + "</Box>";

		Pattern dateP = Pattern.compile(date);
		Matcher dateM = dateP.matcher(content);
		dateM.find();
		String theDate = dateM.group(1);

		Pattern timeP = Pattern.compile(time);
		Matcher timeM = timeP.matcher(content);
		timeM.find(dateM.end());
		String theTime = timeM.group(1);

		Pattern boxesP = Pattern.compile(box, 32); //32 = DOTALL.
		Matcher boxesM = boxesP.matcher(content);

		int lastIndex = timeM.end(); //Tracks matcher's position.

		while( boxesM.find(lastIndex) ) {
			lastIndex = boxesM.end();
			int xpos = Integer.parseInt(boxesM.group(2));
			int ypos = Integer.parseInt(boxesM.group(3));
			Textbox tbox = new Textbox(xpos, ypos, boxesM.group(1), boxesM.group(4));
			tbox.setContent(boxesM.group(5));
			boxes.add(tbox);
		}
		return new Workspace(theDate, theTime, boxes);
	}

	/*
	 * Takes a workspace object and returns a string
	 * representation of it.
	 */
	public static String toString( Workspace ws ) {

		StringBuilder sb = new StringBuilder();
		String date = "<Date="+ws.getDate()+">";
		String time = "<Time="+ws.getTime()+">";
		sb.append(date + time);

		for( Textbox b: ws.getBoxes() ) {
			int x = b.getPos().x;	
			int y = b.getPos().y;
			sb.append("<Box>");
			sb.append("<ID="+b.getId()+">");
			sb.append("<Pos="+x+","+y+">");
			sb.append("<Col="+b.getColor()+">");
			sb.append("<Txt>");
			sb.append(b.getContent());
			sb.append("</Txt>");
			sb.append("</Box>");
		}
		return sb.toString();
	}

	/*
	 * Converts the content of a file to a string. 
	 * @param String fileName, name of file to be converted.
	 * @return A string representation of the file.
	 */
	public static String readFile(String fileName) throws IOException {

		FileInputStream fileIn = new FileInputStream(fileName); 
		int len = (int) new File(fileName).length(); 
		byte[] content = new byte[len];
		while( fileIn.read( content ) > 0 );
		fileIn.close();

		return new String(content);
	}
	/*
	 * Writes the string content to a new file named fileName. 
	 * @param Content, the string to be written to file.
	 * @param fileName, the name of the file to be created / written to.
	 */
	public static void writeFile(String content, String fileName) throws IOException {
		File target = new File(fileName);
		FileOutputStream fileOut = new FileOutputStream(target);
		fileOut.write( content.getBytes() );
		fileOut.close();
	}
} 
