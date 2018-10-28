package sample;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {
    private Socket socket;

    public  void Client() {
    }

    public boolean isClosed() {
        if (socket != null){
            return socket.isClosed();
        }

        return true;
    }

    public void connect(String server_address, int server_port) {
        try {
            socket = new Socket();
            SocketAddress server_socket_address = new InetSocketAddress(server_address, server_port);
            socket.connect(server_socket_address);

            System.out.println("Client connected to server: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (socket != null){
                socket.close();

                System.out.println("Client has disconnected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Try to send message via the connected socket.
     * @param message
     * @return TRUE if message is sent successfully, otherwise return FALSE
     */
    public boolean send_message(String message){
        if (socket == null){
            System.out.println("Client socket is not created, cannot send message");
            return false;
        }

        if (!socket.isConnected() || socket.isClosed()) {
            System.out.println("Client socket is closed or disconnected, cannot send message");
            return false;
        }

        try {
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
