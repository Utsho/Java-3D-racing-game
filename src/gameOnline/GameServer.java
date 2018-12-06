package gameOnline;

import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends JFrame{
	ServerSocket server;
	ServerRun serverRun;
	JLabel label;
	JButton create;
	JButton close;
	JButton play;
	JTextArea area;
	List<Client> clients=new ArrayList<Client>();
	List<Send> sends=new ArrayList<Send>();
	int nextId=0;
	long infoNo;

	int nextPort=6201;
	GameServer(){
		super("ServerDemo");		
		this.getContentPane().setLayout(new GridLayout(2,1));
		label=new JLabel();
		create=new JButton("Create Server");
		close=new JButton("Close Server");
		play=new JButton("Play");
		close.setEnabled(false);
		area=new JTextArea();
		JPanel jp=new JPanel();
		jp.setLayout(new GridLayout(4,1));
		jp.add(label);
		jp.add(create);
		jp.add(close);
		jp.add(play);
		add(jp);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		create.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					server=new ServerSocket(6200);
					label.setText("Server Created:"+InetAddress.getLocalHost());
					create.setEnabled(false);
					close.setEnabled(true);
					serverRun=new ServerRun();
				} catch (IOException e) {
					label.setText("Server not started:"+e.toString());
				}
			}
			
		});
		close.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				label.setText("Server Closed");
				serverRun.close();
				create.setEnabled(true);
				close.setEnabled(false);
				try {
					server.close();
					area.append("Server Closed"+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		play.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				serverRun.close();
				serverRun.stop();
				for (Send s:sends){
					s.playGame();
				}
				new Watcher();
			}
			
		});
		JScrollPane jsp=new JScrollPane();
		
		area.add(jsp);
		add(area);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700,400);
		this.setVisible(true);
	}

	class Watcher extends Thread{
		long st;
		long et;
		long stinfoNo;
		Watcher(){
			start();
		}
		public void run(){
			while(true){
				st=System.currentTimeMillis();
				stinfoNo=infoNo;
				Thread t=new Thread();
				try {
					t.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				label.setText("Info Per Seconds:"+((float)(infoNo-stinfoNo)/(float)(System.currentTimeMillis()-st))*1000);
			}
		}
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
					Client client=new Client(sclient,nextId,new Vector3f(200,0,nextId*40),new Vector3f(0,0,0),nextPort);
					Send send=new Send(sclient,nextId);
					clients.add(client);
					sends.add(send);
					send.sendPort(nextPort);
					nextPort++;
					area.append("Client found:"+sclient.toString()+" ID:"+nextId+"\n");
					nextId++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void close(){
			isRunning=false;
		}
		
		
	}
	class Send {
		Socket client;
		private boolean isRunning;
		DataOutputStream dout;
		private int id;
		Send(Socket client,int id){
			this.client=client;
			this.id=id;
			try {
				dout=new DataOutputStream(client.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			isRunning=true;
		}
		public  void sendPort(int port){
            String str="port "+String.valueOf(port);
            try {
                dout.writeUTF(str);
                dout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


		public void playGame(){
			for (Client c:clients){
				String str;
				if(c.getId()==id){
					str="player ";
				}
				else str="other ";
				str+=c.getX()+" ";
				str+=c.getY()+" ";
				str+=c.getZ()+" ";
				str+=c.getRx()+" ";
				str+=c.getRy()+" ";
				str+=c.getRz()+" ";
                str+=c.getIp()+" ";
                str+=c.getPort();
				try {
					dout.writeUTF(str);
					dout.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				dout.writeUTF("play");
				dout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isRunning=true;
			
		}
		

	}
	class Client implements Runnable{
		Socket client;
		private boolean isOK;
		DataInputStream din;
		int port;
		float x;
		float y;
		float z;
		float rx;
		float ry;
		float rz;
		int id;
		String ip;
		public Client(Socket client,int id,Vector3f pos,Vector3f rot,int port){
			this.client=client;
			this.id=id;
			x=pos.x;
			y=pos.y;
			z=pos.z;
			rx=rot.x;
			ry=rot.y;
			rx=rot.z;
			this.port=port;
			try {
				din=new DataInputStream(client.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Thread(this).start();
		}

		
		public Socket getClient(){
			return client;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public float getZ() {
			return z;
		}

		public float getRx() {
			return rx;
		}

		public float getRy() {
			return ry;
		}

		public float getRz() {
			return rz;
		}

		public long getId() { return id; }

        public int getPort() {
            return port;
        }

        public String getIp() {
            return ip;
        }

		@Override
		public void run() {
			while (true){
				try {
					String str=din.readUTF();
					if (str.startsWith("ip ")){
						ip=str.split(" ")[1];
					}
					else if(str.equals("ready")){
						isOK=true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new GameServer();
	}

}
