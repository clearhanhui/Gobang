package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GobangServer {
    public static int MAX_USER = 10;
    public static List<User> list = new ArrayList<User>();
    public static void main(String[] args) {
        long no = 0;
        int port = 7777;

        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started");

            // 主线程不断接收新的client，子线程用来不断对相应的客户端响应 e
            while (true) {
                Socket socket = serverSocket.accept();
                no++;

                if(list.size() >= MAX_USER) {
                    System.out.println("Reach maximum user");
                    // do something to prevent user add
                }
                User user = new User(no, socket);
                list.add(user);
                System.out.println("Create new user " + no);

                ServerThread thread = new ServerThread(user);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Connection closed");
        }
    }
    public static User getUserById(long id) {
        for (User user: list) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
}

