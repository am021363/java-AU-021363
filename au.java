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

		save save = new save();

		try {

			ss = new ServerSocket(8888);

			while (true) {
				s = ss.accept();
				System.out.println(s.getLocalAddress());
				server = new au1(s, f2, save);
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

	save save ;

	au1(Socket s, File f2, save save) {
		this.s = s;
		this.f2 = f2;
		this.save=save;
		save.savesocket(s);
	}

	public void run() {


		 

	}
}

class save {

	ArrayList<String> loguser = new ArrayList<String>();
	ArrayList<Socket> connectsocket = new ArrayList<Socket>();

	public void savename(String name) {
		this.loguser.add(name);
	}

	public void savesocket(Socket s) {
		this.connectsocket.add(s);
	}

	public ArrayList<String> a() {

		return loguser;

	}

	public ArrayList<Socket> b() {

		return connectsocket;

	}
	public int si () {
		return loguser.size();
	}
}
