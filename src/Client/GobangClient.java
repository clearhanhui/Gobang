package Client;

import java.util.Scanner;
import java.io.*;
import java.net.Socket;

public class GobangClient {
	// 用来标志自己现在得状态
	public static boolean ingame = false;
	public static boolean your_turn = false;
	public static boolean respondable = false;
	public static Gobang gobang;
	public static int toss = 0;
	public static int target = 0; // 存储对方的信息
	public static void main(String args[]) {

		int port = 7777;
		String host = "localhost";
		Socket socket;
		Scanner sc = new Scanner(System.in);
		String send_msg;
		boolean online = true;
		try{
			// 创建socket
			socket = new Socket(host, port);
			System.out.println("Client started");
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

			// 输入用户昵称
			System.out.println("Input nickname(do not contain ',')");
			String nickname = sc.nextLine();
			dos.writeUTF(nickname.split(",")[0]);
			dos.flush();

			// 开启线程，监听请求
			ClientThread clientThread = new ClientThread(socket);
			clientThread.setDaemon(true);
			clientThread.start();


			// 发送指令
			System.out.println("Input instruction(type 'help' to get help info)");
			while (online) {
				String str = sc.nextLine();
				switch(str.split(" ")[0]) {
					case "help":
						System.out.println("list\tShow current list info of server\n" +
								"match n\tSend match request message to user 'n'\n" +
								"accept\tAccept match request" +
								"reject\tReject match request" +
								"adhere r c\tAdhere (row, col) in board" +
								"quit\tQuit the program\n");
						break;
					case "list":
						send_msg = "list,";
						dos.writeUTF(send_msg);
						dos.flush();
						break;
					case "quit":
						send_msg = "quit,";
						dos.writeUTF(send_msg);
						online = false;
						dos.flush();
						break;
					case "match":
						// 甲方发送请求
						target = Integer.valueOf(str.split(" ")[1]);
						send_msg = "match," + target;
						dos.writeUTF(send_msg);
						dos.flush();
						break;
					case "accept":
						// 乙方接受
						if (respondable) {
							dos.writeUTF("accept," + target);
							dos.flush();
							ingame = true;
							gobang = new Gobang();
							gobang.printBoard();
							toss = -1;
						} else {
							System.out.println("No one request");
						}
						break;
					case "reject":
						// 乙方拒绝
						if (respondable) {
							dos.writeUTF("reject," + target);
							dos.flush();
							target = 0;
							respondable = false;
							System.out.println("You reject the request");
						} else {
							System.out.println("No one request");
						}
						break;
					case "adhere":
						// 甲乙公用函数
						if (ingame) {
							if (your_turn) {
								int r, c;
								r = Integer.valueOf(str.split(" ")[1]);
								c = Integer.valueOf(str.split(" ")[2]);
								gobang.adhere(r, c, toss);
								gobang.printBoard();
								your_turn = !your_turn;
								System.out.println("adhere," + r + "," + c + "," + target + your_turn);
								dos.writeUTF("adhere," + r + "," + c + "," + target);
								if(gobang.checkFiveInLine(r, c, toss)) {
									// 自己五子连珠后
									System.out.println("You win, match close");
									dos.writeUTF("end," + target);// 向服务器报告游戏结束
									// 游戏结束，清空自己的状态
									gobang.initializeBoard();
									ingame = false;
									your_turn = false;
									respondable = false;
									target = 0;
									toss = 0;
								}
								dos.flush();
							} else {
								System.out.println("Not your turn");
							}
						} else {
							System.out.println("Game does not start");
						}
						break;
					default:
						System.out.println("Wrong instruction");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect server");
		} finally {
			System.out.println("Connection closed");
		}



	}
//	public static void callback() {
//		exchange_info.setFlag(true);
//	}
}
