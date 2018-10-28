package sample;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    //socket
    public Server_Echo server = new Server_Echo();
    public Client client = new Client();
    public Thread client_receiving_thread;
    public Object lock_txtChatLog = new Object();

    //controls
    public Button btnServerListen;
    public Button btnServerStop;
    public Button btnConnect;
    public Button btnDisconnect;
    public Button btnSend;
    public TextField txtServerPort;
    public TextField txtServerAddress;
    public TextField txtMessage;
    public TextArea txtChatLog;

    public void btnServerListenHandler_Click(){
        try {

            int port = Integer.valueOf(txtServerPort.getText());
            server.start(port);
        } catch (Exception e) {
            System.out.print("Error in btnServerListenHandler_Click");
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void btnServerStopHandler_Click() {
        server.stop();
    }

    public void btnConnectHandler_Click(){
        try {
            if (!client.isClosed()) {
                return;
            }

            String server_address = txtServerAddress.getText();
            int port = Integer.valueOf(txtServerPort.getText());

            client.connect(server_address, port);

            //start a thread here to read & display whatever input coming from the server
            client_receiving_thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!client.isClosed()) {
                        String message_from_server = client.receiveMessage();
                        if (message_from_server == null) {
                            break;
                        }

                        synchronized (lock_txtChatLog) {
                            txtChatLog.appendText("C:" + message_from_server + "\n");
                        }
                    }
                }
            });
            client_receiving_thread.start();

        } catch (Exception e) {
            System.out.print("Error in btnConnectHandler_Click");
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void btnDisconnectHandler_Click(){
        if (!client.isClosed()) {
            client.disconnect();
        }
    }

    public void btnSendHandler_Click() {
        String message = txtMessage.getText();
        if (message.isEmpty()) {
            return;
        }

        //send to server
        boolean is_send_ok = client.sendMessage(message + "\n");

        //clear message text box & update chat log
        if (is_send_ok) {
            txtMessage.clear();

            synchronized (lock_txtChatLog) {
                txtChatLog.appendText("C:" + message + "\n");
            }
        }
    }


}
