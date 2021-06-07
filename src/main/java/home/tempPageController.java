package home;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class tempPageController {
    private boolean chatIsOpened = false;

    @FXML
    void openChatopenChat(ActionEvent event) throws IOException {
        if (chatIsOpened == false) {
            Parent root = FXMLLoader.load(getClass().getResource("ChatClient.fxml"));
            Stage chatStage = new Stage();
            chatStage.setScene(new Scene(root));
            chatStage.setResizable(false);
            chatStage.setTitle("Chat Room");
            chatStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    ChatClientController.closeClient();
                    chatIsOpened = false;
                }
            });
            chatIsOpened = true;
            chatStage.show();
        }
        else {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("You have already open chat");
            a.show();
        }
    }

    public boolean isChatIsOpened() {
        return chatIsOpened;
    }
}

