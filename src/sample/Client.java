package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {
    private Socket socket;
    private OutputStreamWriter socket_writer;
    private InputStreamReader socket_reader;

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

            //prepare the output stream writer & input stream reader
            socket_writer = new OutputStreamWriter(socket.getOutputStream());
            socket_reader = new InputStreamReader(socket.getInputStream());

            System.out.println("Client connected to server: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        } catch (Exception e) {
            System.out.print("Error in connect:");
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void disconnect() {
        try {
            if (socket != null){
                socket_writer.close();
                socket_reader.close();
                socket.close();

                System.out.println("Client has disconnected");
            }
        } catch (Exception e) {
            System.out.print("Error in disconnect:");
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * Try to send message via the connected socket.
     * @param message
     * @return TRUE if message is sent successfully, otherwise return FALSE
     */
    public boolean sendMessage(String message) {
        if (socket == null){
            System.out.println("Client socket is not created, cannot send message");
            return false;
        }

        if (!socket.isConnected() || socket.isClosed()) {
            System.out.println("Client socket is closed or disconnected, cannot send message");
            return false;
        }

        try {
            socket_writer.write(message);
            socket_writer.flush();

            return true;
        } catch (Exception e) {
            System.out.print("Error in sendMessage:");
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    public String receiveMessage() {
        if (socket == null) {
            System.out.println("Client socket is not created, cannot receive message");
            return null;
        }

        if (!socket.isConnected() || socket.isClosed()) {
            System.out.println("Client socket is closed or disconnected, cannot receive message");
            return null;
        }

        try {
            BufferedReader buffered_reader = new BufferedReader(socket_reader);
            return buffered_reader.readLine();

        } catch (Exception e) {
            System.out.print("Error in receiveMessage:");
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}
