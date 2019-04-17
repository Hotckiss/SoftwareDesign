package kirilenko.messenger;


import java.io.PrintStream;
import java.sql.Timestamp;

import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/***
 * Реализация чата, поддерживающего соездинение с другим пользователем напрямую (peer-to-peer).
 */
public class Messenger {
    private String author;
    private PrintStream outputStream;
    private Logger logger = Logger.getLogger(Messenger.class.getName());
    private MessengerGrpc.MessengerBlockingStub stub;
    private Server server;

    /**
     * Конструктор.
     * @param author имя пользователя чата
     * @param port порт пользователя
     * @param peerAddress адрес соединения
     * @param peerPort порт соединения
     * @param outputStream место, куда производить вывод сообщений собеседника.
     *                     Нужен для удобства тестирования.
     * @throws IOException в случае, если не удалось подключиться
     */
    public Messenger(String author, int port, String peerAddress, int peerPort,
                     PrintStream outputStream) throws IOException {
        this.author = author;
        this.outputStream = outputStream;
        server = ServerBuilder.forPort(port).addService(new MessengerService()).build().start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.shutdown()));
        ManagedChannel channel = ManagedChannelBuilder.forAddress(peerAddress, peerPort).usePlaintext().build();
        logger.info("Started channel.");
        stub = MessengerGrpc.newBlockingStub(channel);
        logger.info("Created messenger.");
    }

    /**
     * Отправляет сообщение в чат.
     * @param content текст сообщения
     * @return true, если сообщение успешно отправлено.
     */
    public Boolean sendMessage(String content) {
        Date date = new Date();
        Message message = Message.newBuilder().setAuthor(author).setContent(content)
                .setDate((new Timestamp(date.getTime())).toString()).build();
        try {
            stub.sendMessage(message);
        } catch (Exception exception) {
            logger.warning(exception.getMessage());
            logger.exiting(Messenger.class.getName(), "sendMessage", false);
            return false;
        }
        logger.exiting(Messenger.class.getName(), "sendMessage", true);
        logger.info("Message sent");
        return true;
    }

    /**
     * Закрывает соединение. Используется при тестировании.
     */
    public void shutDown() {
        try {
            server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            logger.info("Shut down client's server.");
        } catch (InterruptedException exception) {
            logger.warning("Error occurred during shutting down channel" + exception.getMessage());
        }
    }

    private class MessengerService extends MessengerGrpc.MessengerImplBase {
        private Logger logger = Logger.getLogger(Messenger.class.getName());

        @Override
        public void sendMessage(Message request, StreamObserver<Message> responseObserver) {
            logger.entering(MessengerService.class.getName(), "sendMessage");
            outputStream.println(String.format("Date: %s\tAuthor: %s\n%s",
                    request.getDate(), request.getAuthor(), request.getContent()));
            responseObserver.onNext(Message.newBuilder().build());
            responseObserver.onCompleted();
            logger.exiting(MessengerService.class.getName(), "sendMessage");
        }
    }
}
