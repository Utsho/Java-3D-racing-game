package gameOnline;

import com.sun.nio.sctp.SendFailedNotification;
import entities.Player;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Game extends JFrame{
	JTextField ip,port;
	JButton connect;
	JLabel label;
	JButton close;
	static Socket socket;
	ServerSocket server;
	private static List<Send> sends=new ArrayList<>();
	static Vector3f playerPos;
	static Vector3f playerRot;
	static List<Vector3f> otherPos=new ArrayList<Vector3f>();
	static List<Vector3f> otherRot=new ArrayList<Vector3f>();
    static boolean isUpdated=false;
    static long lastUpdate=0;
	static boolean isOK=false;
	Game(){
		super("Game Race");
		this.setSize(600, 200);
		ip=new JTextField(20);
		
		port=new JTextField(4);
		close=new JButton("Close");
		connect=new JButton("Connect");
		label=new JLabel();
		this.getContentPane().setLayout(new GridLayout(4,1));
		JPanel jp=new JPanel();
		jp.setLayout(new GridLayout(1,3));
		jp.add(ip);
		jp.add(port);
		jp.add(connect);
		add(jp);
		add(label);
		add(close);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ip.setText("192.168.0.104");
		port.setText("6200");
		connect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String ipAddress=ip.getText();
				int portNumber=Integer.parseInt(port.getText());
				try {
					socket=new Socket(ipAddress,portNumber);
					socket.setSoTimeout(1000000000);
					label.setText("Connected ! Now Waitig for server step");
					new Initi();
				} catch (IOException e) {
					label.setText("Connection failed");
					e.printStackTrace();
				}				
			}
			
		});
		close.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					server.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		this.setVisible(true);
	}

	public static boolean isOK() {
		return isOK;
	}

	class ServerRun extends Thread{

		private boolean isRunning=false;
		public ServerRun(){
			this.isRunning=true;
			start();
		}

		public void run(){
			while(isRunning){
				Socket sclient;
				try {
					sclient=server.accept();
					Send s=new Send(sclient);
                    System.out.println("Game Server   Run");
					sends.add(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void close(){
			isRunning=false;
		}


	}
	
	class Initi extends Thread{
		DataInputStream din;
		DataOutputStream dout;
		boolean isRunning;
		int nextId=0;
		public Initi(){
			try {
				din=new DataInputStream(socket.getInputStream());
				dout=new DataOutputStream(socket.getOutputStream());

			} catch (IOException e) {
				e.printStackTrace();
			}
			isRunning=true;
			start();
		}
		
		public void run(){
			while(isRunning){
				String str;
		
				try {
					str=din.readUTF();
					if (str.startsWith("port")){
						String s=str.split(" ")[1];
						int port=Integer.parseInt(s);
						server=new ServerSocket(port);
                        new ServerRun();
						System.out.println(port+" "+InetAddress.getLocalHost().toString().split("/")[1]+" "+server.toString());
						if (server!=null){
							String ip= InetAddress.getLocalHost().toString().split("/")[1];
							dout.writeUTF("ip "+ip);
							dout.flush();
						}
					}
					if (str.startsWith("player")){
						String []st=str.split(" ");
						float x=Float.parseFloat(st[1]);
						float y=Float.parseFloat(st[2]);
						float z=Float.parseFloat(st[3]);
						float rx=Float.parseFloat(st[4]);
						float ry=Float.parseFloat(st[5]);
						float rz=Float.parseFloat(st[6]);
						playerPos=new Vector3f(x,y,z);
						playerRot=new Vector3f(rx,ry,rz);
                        System.out.println("Player info receive");
					}
					else if (str.startsWith("other")){
						String []st=str.split(" ");
						float x=Float.parseFloat(st[1]);
						float y=Float.parseFloat(st[2]);
						float z=Float.parseFloat(st[3]);
						float rx=Float.parseFloat(st[4]);
						float ry=Float.parseFloat(st[5]);
						float rz=Float.parseFloat(st[6]);
						String ip=st[7];
						int port=Integer.parseInt(st[8]);
						new Receive(nextId,ip,port);
						Vector3f Pos=new Vector3f(x,y,z);
						Vector3f Rot=new Vector3f(rx,ry,rz);
						otherPos.add(Pos);
						otherRot.add(Rot);
					}
					else if (str.equals("play")){
						new MainGame(playerPos,playerRot,otherPos,otherRot);
						isRunning=false;
					}
					else if (str.equals("ready all")){
						isOK=true;
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	class Send{
		Socket client;
		DataOutputStream dout;
		private boolean isRunning;
		Send(Socket client){
			this.client=client;
			try {
				dout=new DataOutputStream(client.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void send(float dx,float dz){
				String str;
				str=playerPos.x+" "+playerPos.y+" "+playerPos.z+" "+playerRot.x+" "+playerRot.y+" "+playerRot.z+" "+dx+" "+dz+" "+System.currentTimeMillis();
				try {
					dout.writeUTF(String.valueOf(str));
					dout.flush();
				} catch (IOException e) {
					e.printStackTrace();
					
				}
		}
		
	}
	class Receive extends Thread{
		DataInputStream din;
		private boolean isRunning;
		int id;
		Socket socket;
		Receive(int id,String ip,int port){

			try {
				socket=new Socket(ip,port);
                System.out.println("Receive Starts");
                din=new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			isRunning=true;
			start();
		}
		
		public void run(){
			while(isRunning){
				long t0=System.currentTimeMillis();
				String str;
				try {
					str=din.readUTF();
						String st[];
						st=str.split(" ");
						float x=Float.parseFloat(st[0]);
						float y=Float.parseFloat(st[1]);
						float z=Float.parseFloat(st[2]);
						float rx=Float.parseFloat(st[3]);
						float ry=Float.parseFloat(st[4]);
						float rz=Float.parseFloat(st[5]);
						float dx=Float.parseFloat(st[6]);
						float dz= Float.parseFloat(st[7]);
						lastUpdate=Long.parseLong(st[8]);
						otherPos.get(id).x=x+dx*.1f;
						otherPos.get(id).y=y;
						otherPos.get(id).z=z+dz*.1f;
						otherRot.get(id).x=rx;
						otherRot.get(id).y=ry;
						otherRot.get(id).z=rz;

				} catch (IOException e) {
					e.printStackTrace();
				}
				long t1=System.currentTimeMillis();
				label.setText(String.valueOf(t1-t0));
                isUpdated=true;
			}
		}
		
		public void close(){
			isRunning=false;
		}
	}

	static public void setPlayer(Player p) {
		playerPos.x = p.getPosition().x;
		playerPos.y = p.getPosition().y;
		playerPos.z = p.getPosition().z;
		playerRot.x=p.getRotX();
		playerRot.y=p.getRotY();
		playerRot.z=p.getRotZ();
		for (Send s:sends){
			s.send(p.getSpeedDirection().x,p.getSpeedDirection().z);
		}

	}
	public static void sendOK() throws IOException {
		DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
		dout.writeUTF("ready");
		dout.flush();
	}

	static public List<Vector3f> getOtherPos() {
		return otherPos;
	}
	static public List<Vector3f> getOtherRot() {
		return otherRot;
	}
    public static Long getUpdateInfo(){
        if (isUpdated){
            isUpdated=false;
            return lastUpdate;
        }
        return Long.valueOf(-1);
    }

	public static void main(String[] args){
		new Game();
	}


}
