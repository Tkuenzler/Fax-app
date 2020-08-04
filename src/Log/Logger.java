package Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	public File file;
	BufferedWriter bw;
	BufferedReader br;
	public Logger(Log log) {
		file = new File(log.file);	
	}
	public StringBuilder readFile() {
		StringBuilder content = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while( (line=br.readLine())!=null) {
				content.append(line);
				content.append("\n");
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				br.close();
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
		}
		return content;
	}
	public void log(String title,String message) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		try {
			bw = new BufferedWriter(new FileWriter(file,true));
			bw.write(dateFormat.format(cal.getTime()));
			bw.newLine();
			bw.write(title);
			bw.newLine();
			bw.write(message);
			bw.flush();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				bw.close();
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
		}
	}
	public void clearLogFile() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			writer.print("");
			writer.close();
		} catch(IOException ioe) {
			
		} finally {
			writer.close();
		}
	}
}
