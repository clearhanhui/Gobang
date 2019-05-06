package Client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        String receive_msg;
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            while (true) {
                // 接收请求
                receive_msg = dis.readUTF();
                switch (receive_msg.split(",")[0]) {
                    case "list_respond":
                        System.out.println(receive_msg.split(",")[1]);
                        break;
                    case "match_request_from":
                        System.out.println("Receive match request from user " +
                                receive_msg.split(",")[1] +
                                ", accept/reject?");
                        // 被请求者将请求者得信息存起来
                        GobangClient.target = Integer.valueOf(receive_msg.split(",")[1]);
                        GobangClient.respondable = true;
                        break;
                    case "accept_game":
                        System.out.println("You are accepted");
                        GobangClient.ingame = true; // 进入游戏
                        GobangClient.your_turn = true;
                        GobangClient.gobang = new Gobang();
                        GobangClient.gobang.printBoard();
                        GobangClient.toss = 1;
                        break;
                    case "reject_game":
                        System.out.println("You are rejected");
                        break;
                    case "locate" :
                        break;
                    case "Idle":
                        System.out.println("Waiting for respond");
                        break;
                    case "Busy":
                        System.out.println("User is busy");
                        break;
                    case "no_user":
                        System.out.println("No such user");
                        break;
                    case "adhere":
                        // System.out.println("yifang" + receive_msg);
                        GobangClient.gobang.adhere(Integer.valueOf(receive_msg.split(",")[1]),
                                Integer.valueOf(receive_msg.split(",")[2]),
                                -GobangClient.toss);
                        GobangClient.gobang.printBoard();
                        GobangClient.your_turn = !GobangClient.your_turn;
                        break;
                    case "you_lose":
                        System.out.println("You lose, match close");
                        GobangClient.gobang.initializeBoard();
                        GobangClient.ingame = false;
                        GobangClient.your_turn = false;
                        GobangClient.respondable = false;
                        GobangClient.target = 0;
                        GobangClient.toss = 0;
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
