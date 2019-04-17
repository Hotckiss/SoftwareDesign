package kirilenko.messenger;

import java.io.IOException;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private static String[] input_args;

    /**
     * args[0] - имя пользователя
     * args[1] - порт пользователя
     * args[2] - адрес подключения
     * args[3] - порт подключения
     */
    public static void main(String[] args) {
        input_args = args;
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Messenger messenger;
        try {
            String author = input_args[0];
            int port = Integer.parseInt(input_args[1]);
            String peerAddress = input_args[2];
            int peerPort = Integer.parseInt(input_args[3]);
            messenger = new Messenger(author, port, peerAddress, peerPort);
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.err.println("Please, provide four argument! \'author port connection_address connection_port\'");
            return;
        } catch (IOException exception) {
            System.err.println("Failed to bind.");
            return;
        }
        new UI().run(primaryStage, messenger);
    }
}
