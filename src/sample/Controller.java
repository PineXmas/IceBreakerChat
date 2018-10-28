package sample;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    //socket
    public Server_Echo server = new Server_Echo();
    public Client client = new Client();

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
            if (server.isListening()) {
                System.out.println("Server is still listening");
                return;
            }

            int port = Integer.valueOf(txtServerPort.getText());
            server.start(port);
        } catch (Exception e) {
            e.printStackTrace();
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

            client.disconnect();

            String server_address = txtServerAddress.getText();
            int port = Integer.valueOf(txtServerPort.getText());

            client.connect(server_address, port);
        } catch (Exception e) {
            e.printStackTrace();
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
        boolean is_send_ok = client.send_message(message);

        //clear message text box & update chat log
        if (is_send_ok) {
            txtMessage.clear();
            txtChatLog.appendText("C:" + message + "\n");
        }
    }


}
