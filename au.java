import java.net.*;
import java.util.*;
import java.io.*;

public class au {

	public static void main(String[] args) {

		ServerSocket ss = null;
		Socket s = null;
		au1 server;

		File f = new File("²á¤Ñ«Ç");
		f.mkdir();
		File f2 = new File(f + "/user.txt");
		try {
			f2.createNewFile();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		
		try {
			
			
			ss = new ServerSocket(8888);

			while (true) {
				s = ss.accept();
				server = new au1(s,f2);
				server.start();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

}

class au1 extends Thread {

	File f2 = null;
	Socket s;
	ArrayList<Socket> connectsocket = new ArrayList<Socket>();
	
	au1(Socket s,File f2) {
		this.s = s;
		this.f2 = f2;
		connectsocket.add(s);
	}

	public void run() {

	}
}


