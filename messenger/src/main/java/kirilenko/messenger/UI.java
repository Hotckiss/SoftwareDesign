package kirilenko.messenger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UI {
    void run(Stage stage, Messenger messenger) {
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
//Defining the Name text field
        final TextField msg = new TextField();
        msg.setPromptText("Enter your first msg.");
        msg.setPrefColumnCount(10);
        msg.getText();
        GridPane.setConstraints(msg, 0, 0);
        grid.getChildren().add(msg);
        final TextArea response = new TextArea();
        messenger.setOut(response);
        response.setPrefColumnCount(15);
        response.setPromptText("Response: ");
        GridPane.setConstraints(response, 0, 2);
        grid.getChildren().add(response);
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);
        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                messenger.sendMessage(msg.getText());
            }
        });
        Scene scene = new Scene(grid, 300, 500);
        stage.setScene(scene);
        stage.show();
    }
}
