package edu.school21.sockets.client;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender {
    private final PrintWriter writer;

    public MessageSender(Socket socket) throws IOException {
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(JSONData message) {
        writer.println(JSONUtility.toJSON(message).toJSONString());

    }
}
