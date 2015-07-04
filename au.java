package au;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;

public class au {

	public static void main(String[] args) {

		ServerSocket ss = null;
		Socket s = null;
		au1 server;

		File f = new File("聊天室");
		f.mkdir();
		File f2 = new File(f + "/user.txt");
		try {
			f2.createNewFile();
		} catch (IOException e) {
			System.out.println(e.toString());
		}

		save save = new save();
		enterchatuser enterchatuser = new enterchatuser();

		try {

			ss = new ServerSocket(8888);

			while (true) {
				s = ss.accept();
				System.out.println(s.getLocalAddress());
				server = new au1(s, f2, save, enterchatuser);
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
	logUser log;
	chat ct;
	save save;
	enterchatuser enterchatuser;

	au1(Socket s, File f2, save save, enterchatuser enterchatuser) {
		this.s = s;
		this.f2 = f2;
		this.save = save;
		this.enterchatuser = enterchatuser;
	}

	public void run() {

		log = new logUser(f2, s, save);
		log.a();

		ct = new chat(save, s, enterchatuser);
		ct.startchat();

	}
}

class logUser {

	Socket s = null;
	File f2 = null;
	String name;
	ArrayList<String> loguser = new ArrayList<String>();
	save saves;

	logUser(File f2, Socket s, save saves) {
		this.s = s;
		this.f2 = f2;
		this.saves = saves;
	}

	public void a() {

		File f = new File("聊天室/用戶");
		f.mkdir();
		try {

			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();

			ArrayList<String> user = new ArrayList<String>();
			ArrayList<String> save = new ArrayList<String>();

			FileWriter fw;
			BufferedReader br;

			byte[] namebyte = new byte[256];
			byte[] passwordbyte = new byte[256];
			int namebytelong, passwordbytelong;
			byte[] message;
			String username;
			String bruser;

			int a = 0, i = 0, j = 0;

			br = new BufferedReader(new FileReader(f2));

			while ((bruser = br.readLine()) != null) {
				save.add(bruser);
			}

			while (a == 0) {
				username = "";
				namebytelong = is.read(namebyte);

				for (i = 0; i < namebytelong; i++) {
					username += new String(namebyte, "UTF-8").charAt(i);
				}

				if (username.indexOf("_M") != -1) {

					username = username.replaceAll("_M", "");
					this.name = username;
					passwordbytelong = is.read(passwordbyte);
					username += "	";
					for (i = 0; i < passwordbytelong; i++) {
						username += new String(passwordbyte, "UTF-8").charAt(i);
					}
					for (i = 0; i < save.size(); i++) {

						if (save.get(i).indexOf(username) != -1) {
							message = "用戶已存在，不能新建".getBytes();
							os.write(message);
							break;

						}
					}
					if (i == save.size() || save.size() == 0) {
						saves.savename(name);
						saves.savesocket(s);
						fw = new FileWriter(f2, true);
						f = new File(f + "/" + name + ".txt");
						try {
							f.createNewFile();
						} catch (IOException e) {
							System.out.println(e.toString());
						}
						fw.write(username + "\r\n");
						fw.close();
						message = "創建成功".getBytes();
						os.write(message);
						os.flush();
						a = 1;
					}
				} else {
					this.name = username;
					System.out.println();
					passwordbytelong = is.read(passwordbyte);
					username += "	";
					for (i = 0; i < passwordbytelong; i++) {
						username += new String(passwordbyte, "UTF-8").charAt(i);
					}
					synchronized (this) {
						i = 0;
						j = 0;
						loguser = saves.a();
						if (save.size() == 0) {
							message = "帳號或密碼錯誤".getBytes();
							os.write(message);
						} else {
							for (j = 0; j < loguser.size(); j++) {

								if (loguser.get(j).indexOf(name) != -1) {
									message = "帳號已登入".getBytes();
									os.write(message);
									break;
								}

							}
							if (j == loguser.size()) {
								for (i = 0; i < save.size(); i++) {
									if (save.get(i).indexOf(username) != -1) {
										saves.savename(name);
										saves.savesocket(s);
										message = "成功登入".getBytes();
										os.write(message);
										a = 1;
										break;
									}
								}
								if (i == save.size()) {
									message = "帳號或密碼錯誤".getBytes();
									os.write(message);
								}
							}

						}
					}

				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}

class chat {

	ArrayList<Socket> connectsocket = new ArrayList<Socket>();
	Socket s = null;
	String Username;
	save save;
	int j = 0;
	enterchatuser enterchatuser;

	chat(save save, Socket s, enterchatuser enterchatuser) {
		this.save = save;
		this.connectsocket = save.b();
		this.s = s;
		this.enterchatuser = enterchatuser;
		for (int i = 0; i < connectsocket.size(); i++) {
			if (s.equals(connectsocket.get(i))) {
				this.Username = save.getuser(i);
				j = i;
				break;
			}
		}
		if (j == 0) {
			enterchatuser.chatuser(save.getuser(j));
			enterchatuser.chatsocket(s);
		}

	}

	public void startchat() {
		File f = new File("聊天室/用戶");
		box box;
		text text;
		try {

			int i;

			InputStream is = s.getInputStream();
			OutputStream os;
			box = new box(s, Username, enterchatuser);
			text = new text(s, Username, save, enterchatuser);
			box = new box(s, Username, enterchatuser);
			for (i = 0; i < save.gg(); i++) {
				box = new box(save.getsocket(i), Username, enterchatuser);
				box.boxes();
			}
			if ((enterchatuser.waitusernumber() + enterchatuser.getusernumber()) < save
					.usernumber()) {
					for (i = (enterchatuser.waitusernumber() + enterchatuser
							.getusernumber()); i < save.usernumber(); i++) {
						enterchatuser.waituser(save.getuser(i));
						enterchatuser.waitsocket(save.getsocket(i));
					}
				for (i = 0; i < save.gg(); i++) {
					box = new box(save.getsocket(i), Username, enterchatuser);
					box.boxes();
				}
			}

			while (true) {
				
				
				
				text.texts();

			}
		} catch (Exception e) {

			System.out.println(e.toString());

		}
	}
}

class box {

	Socket s;
	String Username;
	OutputStream os;
	byte[] message = new byte[256];
	byte[] nummess;
	enterchatuser enterchatuser;
	box box;

	box(Socket s, String Username, enterchatuser enterchatuser) {
		this.s = s;
		this.Username = Username;
		this.enterchatuser = enterchatuser;
	}

	public void boxes() {

		int i = 0;
		int num;

		try {
			InputStream is = s.getInputStream();
			int chatnumber;
			String userchat;
			byte[] namebyte = new byte[1];

			synchronized (this) {
				os = s.getOutputStream();

				if (enterchatuser.getusernumber() >= 0) {
					message = ("list2").getBytes();
					os.write(message);
					Thread.sleep(1000);

					String a = Integer.toString(enterchatuser.getusernumber());

					nummess = (a).getBytes();
					os.write(nummess);
					Thread.sleep(1000);

					for (i = 0; i < enterchatuser.getusernumber(); i++) {

						message = (enterchatuser.getuser(i)).getBytes();
						os.write(message);
						Thread.sleep(1000);
					}
				}
				if (enterchatuser.waitusernumber() >= 0) {
					message = ("list1").getBytes();
					os.write(message);
					Thread.sleep(1000);

					String a = Integer.toString(enterchatuser.waitusernumber());

					nummess = (a).getBytes();
					os.write(nummess);
					Thread.sleep(1000);

					for (i = 0; i < enterchatuser.waitusernumber(); i++) {

						message = (enterchatuser.getwaituser(i)).getBytes();
						os.write(message);
						Thread.sleep(1000);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}
}

class text {

	Socket s;
	String Username;
	OutputStream os;
	save save;
	enterchatuser enterchatuser;
	box box;

	text(Socket s, String Username, save save, enterchatuser enterchatuser) {
		this.s = s;
		this.Username = Username;
		this.save = save;
		this.enterchatuser = enterchatuser;
		box = new box(s, Username, enterchatuser);
	}

	public void texts() {
		try {

			InputStream is = s.getInputStream();
			OutputStream os;
			int i, chatnumber, k = enterchatuser.getusernumber()+1;
			String userchat, deletename = null;
			byte[] namebyte = new byte[256];
			byte[] message;

			for (i = 0; i < enterchatuser.getusernumber(); i++) {
				if (Username.equals(enterchatuser.getuser(i))) {
					k = i;
					break;
				}
			}
			userchat = "";
			chatnumber = is.read(namebyte);

			for (i = 0; i < chatnumber; i++) {
				userchat += new String(namebyte, "UTF-8").charAt(i);
			}
			if (userchat.indexOf("關閉close") == 0) {
				for (i = 0; i < save.usernumber(); i++) {
					if (Username.equals(save.getuser(i))) {
						deletename = save.getuser(i);
						save.removeuser(i);
						save.removesocket(i);
						break;
					}
				}
				for (i = 0; i < enterchatuser.getusernumber(); i++) {
					if (enterchatuser.getuser(i).equals(deletename)) {
						enterchatuser.removegetsocket(i);
						enterchatuser.removegetuser(i);
					}
				}
				for (i = 0; i < enterchatuser.waitusernumber(); i++) {
					if (enterchatuser.getwaituser(i).equals(deletename)) {
						enterchatuser.removewaitsocket(i);
						enterchatuser.removewaituser(i);
					}
				}
				for (i = 0; i < enterchatuser.getusernumber(); i++) {
					box = new box(enterchatuser.getsocket(i), Username,
							enterchatuser);
					box.boxes();
				}
			} else if (k < enterchatuser.getusernumber() && userchat != "") {
				if (k == 0) {
					if (userchat.indexOf("增加add") == 0) {
						userchat = "";
						chatnumber = is.read(namebyte);

						for (i = 0; i < chatnumber; i++) {
							userchat += new String(namebyte, "UTF-8").charAt(i);
						}
						for (i = 0; i < enterchatuser.waitusernumber(); i++) {
							if (enterchatuser.getwaituser(i).equals(userchat)) {
								enterchatuser.chatuser(userchat);
								enterchatuser.chatsocket(enterchatuser
										.getwaitsocket(i));
								enterchatuser.removewaitsocket(i);
								enterchatuser.removewaituser(i);
							}
						}
						for (i = 0; i < enterchatuser.getusernumber(); i++) {
							box = new box(enterchatuser.getsocket(i), Username,
									enterchatuser);
							box.boxes();
						}
					} else if (userchat.indexOf("刪除delete") == 0) {
						userchat = "";
						chatnumber = is.read(namebyte);
						for (i = 0; i < chatnumber; i++) {
							userchat += new String(namebyte, "UTF-8").charAt(i);
						}
						for (i = 0; i < enterchatuser.getusernumber(); i++) {
							if (enterchatuser.getuser(i).equals(userchat)) {
								enterchatuser.waituser(userchat);
								enterchatuser.waitsocket(enterchatuser
										.getsocket(i));
								enterchatuser.removegetsocket(i);
								enterchatuser.removegetuser(i);
							}
						}
						for (i = 0; i < enterchatuser.getusernumber(); i++) {
							box = new box(enterchatuser.getsocket(i), Username,
									enterchatuser);
							box.boxes();
						}
					} else {
						for (i = 0; i < enterchatuser.getusernumber(); i++) {
							os = enterchatuser.getsocket(i).getOutputStream();
							message = ("房主_" + Username + ":" + userchat)
									.getBytes();
							os.write(message);
						}
					}
				} else {
					if (userchat.indexOf("增加add") == 0) {
						userchat = "";
						chatnumber = is.read(namebyte);

						for (i = 0; i < chatnumber; i++) {
							userchat += new String(namebyte, "UTF-8").charAt(i);
						}
					} else if (userchat.indexOf("刪除delete") == 0) {
						userchat = "";
						chatnumber = is.read(namebyte);

						for (i = 0; i < chatnumber; i++) {
							userchat += new String(namebyte, "UTF-8").charAt(i);
						}
					} else {
						for (i = 0; i < enterchatuser.getusernumber(); i++) {
							os = enterchatuser.getsocket(i).getOutputStream();
							message = (Username + ":" + userchat).getBytes();
							os.write(message);
						}
					}

				}
			}
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
}

class enterchatuser {

	ArrayList<String> enteruser = new ArrayList<String>();
	ArrayList<Socket> entersocket = new ArrayList<Socket>();
	ArrayList<String> waituser = new ArrayList<String>();
	ArrayList<Socket> waitsocket = new ArrayList<Socket>();

	public void waituser(String name) {
		waituser.add(name);
	}

	public String getwaituser(int i) {
		return waituser.get(i);
	}

	public void removewaituser(int i) {
		waituser.remove(i);
	}

	public int waitusernumber() {
		return waituser.size();
	}

	public void chatuser(String name) {
		enteruser.add(name);
	}

	public String getuser(int i) {
		return enteruser.get(i);
	}

	public void removegetuser(int i) {
		enteruser.remove(i);
	}

	public int getusernumber() {
		return enteruser.size();
	}

	public void chatsocket(Socket s) {
		entersocket.add(s);
	}

	public Socket getsocket(int i) {
		return entersocket.get(i);
	}

	public void removegetsocket(int i) {
		entersocket.remove(i);
	}

	public void waitsocket(Socket s) {
		waitsocket.add(s);
	}

	public Socket getwaitsocket(int i) {
		return waitsocket.get(i);
	}

	public void removewaitsocket(int i) {
		waitsocket.remove(i);
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

	public String getuser(int i) {
		return loguser.get(i);
	}

	public Socket getsocket(int i) {
		return connectsocket.get(i);
	}

	public void removeuser(int j) {
		loguser.remove(j);
	}

	public void removesocket(int j) {
		connectsocket.remove(j);
	}

	public ArrayList<Socket> b() {
		return connectsocket;
	}

	public int usernumber() {
		return loguser.size();
	}

	public int gg() {
		return connectsocket.size();
	}
}
class getdata{
	public void getdata(){
        
        ServerSocket ss=null;
		Socket cs=null;
		DataInputStream is=null;
		DataOutputStream dw;
		FileOutputStream fw=null;
		File f1=null;
		int filettypestatus=0;
		String Filemessage="";
		byte fm=0;
		char ftype;
		
		try{
			 ss=new ServerSocket(8787);
			while(true){
				cs=ss.accept();
				is=new DataInputStream(cs.getInputStream());

				byte[] buf = new byte[8192];
				int passedlen= 0;
				long len =0;
				int data=0;

				Filemessage+=is.readUTF();
				len = is.readLong();

				
				f1=new File("2.jpg");
				fw=new FileOutputStream(f1);
				BufferedOutputStream bw=new BufferedOutputStream(fw);
				dw=new DataOutputStream(bw);
				
				while(data!=-1){
					if(is!=null)
						data = is.read(buf);

					passedlen += data;

					dw.write(buf,0,data);
				}
				dw.close();

			}	

		}catch(Exception ex){

			System.out.println(ex.toString());
		}
}
