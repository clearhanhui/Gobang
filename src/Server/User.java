package Server;

import java.io.*;
import java.net.Socket;

// 服务器对于用户的抽象
public class User {
    private long id;
    private String nickname;
    private String status;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public User(long id, Socket socket) throws IOException {
        this.id = id;
        this.status = "Idle";
        this.socket = socket;
        this.nickname = "guest";
        this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getDis() {
        return dis;
    }
    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }
    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }
}
