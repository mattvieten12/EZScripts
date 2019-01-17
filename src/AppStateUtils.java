import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AppStateUtils {

	public static void saveAppState(ApplicationWindow app) {
		PrintWriter fw = null;
	    try {
	        fw = new PrintWriter("users.txt");
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(tfUsername.getText());
	        bw.newLine();
	        bw.write(tfPassword.getText());
	    } catch (IOException e) {
	        e.printStackTrace();
	        fw.close();
	    }
	}
}
