package home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML
    private TableView<?> projectTable;

    @FXML
    private TableColumn<?, ?> project_id;

    @FXML
    private TableColumn<?, ?> project_name;

    @FXML
    private TableColumn<?, ?> project_issues;

    @FXML
    void getAddView(MouseEvent event) {

    }

    @FXML
    void overview(ActionEvent event) {

    }

    @FXML
    void refreshTable(MouseEvent event) {

    }

    @FXML
    void search(MouseEvent event) {

    }

}
