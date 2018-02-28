package connection;

import dataframe.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenServer {
    ServerSocket serverSocket;

    public ListenServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            int localPort = serverSocket.getLocalPort();
            System.out.print("Hệ thống đã lắng nghe kết nối từ cổng "
                    + localPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket accept() {
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Packet receiveMessage(Socket socket) {

        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            try {
                Packet packet = (Packet) input.readObject();
                return packet;
            } catch (ClassNotFoundException e) {
            }
        } catch (IOException e) {

        }
        return null;
    }

    public void sendMessage(Packet packet, Socket socket) {

        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(packet);
            output.flush();
        } catch (IOException e) {
        }
    }

}