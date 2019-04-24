
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class UI {
    private final static String QUEUE_NAME = "hello";
    void run(Stage stage) throws IOException, TimeoutException {
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
       // messenger.setOut(response);
        response.setPrefColumnCount(15);
        response.setPromptText("Response: ");
        GridPane.setConstraints(response, 0, 2);
        grid.getChildren().add(response);
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        // create a tabpane
        TabPane tabpane = new TabPane();
        int counter = 0;
        for (int i = 0; i < 5; i++) {

            // create Tab
            Tab tab = new Tab("Tab_" + (int)(counter + 1));

            // create a label
            Label label = new Label("This is Tab: "
                    + (int)(counter + 1));

            counter++;

            // add label to the tab
            tab.setContent(label);

            // add tab
            tabpane.getTabs().add(tab);
        }

        // create a tab which
        // when pressed creates a new tab
        Tab newtab = new Tab();

        // action event
        EventHandler<Event> event =
                new EventHandler<Event>() {

                    public void handle(Event e)
                    {
                        if (newtab.isSelected())
                        {

                            // create Tab
                            Tab tab = new Tab("Tab_" + (int)(counter + 1));

                            // create a label
                            Label label = new Label("This is Tab: "
                                    + (int)(counter + 1));

                            counter++;

                            // add label to the tab
                            tab.setContent(label);

                            // add tab
                            tabpane.getTabs().add(
                                    tabpane.getTabs().size() - 1, tab);

                            // select the last tab
                            tabpane.getSelectionModel().select(
                                    tabpane.getTabs().size() - 2);
                        }
                    }
                };

        // set event handler to the tab
        newtab.setOnSelectionChanged(event);

        // add newtab
        tabpane.getTabs().add(newtab);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String message = msg.getText();
                try {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(" [x] Sent '" + message + "'");
                //messenger.sendMessage(msg.getText());
            }
        });

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            response.appendText(message + "\n");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        System.out.println("sDVSDvsADFVasdbvasb");
        Scene scene = new Scene(grid, 300, 500);
        stage.setScene(scene);
        stage.show();
    }
}