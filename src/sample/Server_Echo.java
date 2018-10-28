package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Echo {

    private ServerSocket socket;
    private Socket socket_client;
    private boolean is_listening;
    private Object server_lock;
    private Thread server_thread;

    public Server_Echo() {
        is_listening = false;
        server_lock = new Object();
    }

    public void start(int port) {
        try {

            if (isListening()) {
                System.out.println("Server is still listening");
                return;
            }

            //mark the server is running
            synchronized (server_lock) {
                is_listening = true;
            }

            //open socket with the given port
            socket = new ServerSocket(port);

            //start new thread to listen for client connection
            server_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket_client = socket.accept();
                        System.out.print("Server connected to client: ");
                        System.out.println(socket_client.getInetAddress().getHostAddress() + ":" + socket_client.getPort());

                        //waiting for input from client and echo back
                        InputStreamReader input_stream_reader = new InputStreamReader(socket_client.getInputStream());
                        BufferedReader reader = new BufferedReader(input_stream_reader);
                        OutputStreamWriter output_stream_writer = new OutputStreamWriter(socket_client.getOutputStream());
                        BufferedWriter writer = new BufferedWriter(output_stream_writer);
                        while (true) {
                            String line = reader.readLine();
                            if (line.equals("end_connection")) {
                                break;
                            }

                            //echo
                            System.out.println("Msg from client:" + line);
                            writer.write(line);
                            writer.flush();
                        }
                        reader.close();
                        writer.close();

                        //close connection to client
                        socket_client.close();

                        //mark as server has stop listening
                        synchronized (server_lock) {
                            is_listening = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (socket != null) {
                                socket.close();
                                synchronized (server_lock) {
                                    is_listening = false;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            server_thread.start();
            System.out.println("Server is listening");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isListening() {
        synchronized (server_lock) {
            return is_listening;
        }
    }

    public void stop(){
//        if (isListening()) {
//            System.out.println("Server is still listening, cannot stop");
//            return;
//        }

        try {
            if (socket !=null) {
                socket.close();

                synchronized (server_lock) {
                    is_listening = false;
                }

                System.out.println("Server has stopped listening");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Test() {

    }
}
