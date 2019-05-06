package Server;

import java.io.*;
import java.util.List;

public class ServerThread extends Thread {
    private User user;

    public ServerThread(User user) {
        this.user = user;
    }

    public void run() {
        String send_msg;
        String receive_msg;
        boolean online = true;
        try {
            receive_msg = user.getDis().readUTF();
            user.setNickname(receive_msg);
            while(online) {
                receive_msg = user.getDis().readUTF();
                List<User> list = GobangServer.list;
                // 解析指令，并执行
                switch (receive_msg.split(",")[0]) {
                    case "list":
                        send_msg = "list_respond,Total " + list.size() + "\n";
                        for (int i = 0; i < list.size(); i++) {
                            send_msg +=  list.get(i).getId() + " " +
                                    list.get(i).getNickname() + " " +
                                    list.get(i).getStatus() + "\n";
                        }
                        user.getDos().writeUTF(send_msg.trim());
                        user.getDos().flush();
                        System.out.println("Send list info to user " + user.getId());
                        break;
                    case "match":
                        long target = Long.valueOf(receive_msg.split(",")[1]);
                        if (GobangServer.getUserById(target) != null) {
                            if (GobangServer.getUserById(target).getStatus().equals("Idle")) {
                                user.getDos().writeUTF("Idle"); // 返回给申请者
                                GobangServer.getUserById(target).getDos().writeUTF("match_request_from," + user.getId()); // 向目标转发请求
                                GobangServer.getUserById(target).getDos().flush();
                            }else {
                                user.getDos().writeUTF("Busy");
                            }
                        } else {
                            user.getDos().writeUTF("no_user");
                        }
                        user.getDos().flush();
                        break;
                    case "accept":
                        long source = Long.valueOf(receive_msg.split(",")[1]);
                        user.getDos().writeUTF("start_game"); //
                        GobangServer.getUserById(source).getDos().writeUTF("accept_game"); // 申请者
                        user.getDos().flush();
                        GobangServer.getUserById(source).getDos().flush();
                        user.setStatus("Busy");
                        GobangServer.getUserById(source).setStatus("Busy");
                        System.out.println("Matched user " + user.getId() + " and user " + source);
                        break;
                    case "reject":
                        GobangServer.getUserById(Long.valueOf(receive_msg.split(",")[1])).getDos().writeUTF("reject_game");
                        GobangServer.getUserById(Long.valueOf(receive_msg.split(",")[1])).getDos().flush();
                        break;
                    case "adhere":
                        GobangServer.getUserById(Integer.valueOf(receive_msg.split(",")[3])).getDos().writeUTF(receive_msg);
                        GobangServer.getUserById(Integer.valueOf(receive_msg.split(",")[3])).getDos().flush();
                        System.out.println("User " + user.getId() + " send coordinate to user " +
                                GobangServer.getUserById(Integer.valueOf(receive_msg.split(",")[3])).getId());
                        break;
                    case "end":
                        GobangServer.getUserById(Integer.valueOf(receive_msg.split(",")[1])).getDos().writeUTF("you_lose");
                        GobangServer.getUserById(Integer.valueOf(receive_msg.split(",")[1])).getDos().flush();
                        user.setStatus("Idle");
                        GobangServer.getUserById(Integer.valueOf(receive_msg.split(",")[1])).setStatus("Idle");
                        break;
                    case "quit":
                        list.remove(user);
                        System.out.println("Remove user " + user.getId() + " from list");
                        online = false;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
