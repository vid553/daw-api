package web.application.development.exception;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class ErrorLog {

	public String WriteErorLog (Exception ex) {
		Date date = new Date();
		String timeStamp = (new Timestamp(date.getTime())).toString();
		try(FileWriter fw = new FileWriter("errors.log", true); 
				 PrintWriter out = new PrintWriter(fw)) {
			out.println(timeStamp);
			ex.printStackTrace(out);
		}
		catch (Exception e) {	
		}
		return timeStamp;
	}

	public ErrorLog() {
		super();
	}
}
