import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    // Store all clients to loop over them to transfer information across all of them
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static ArrayList<String> clientLanguages = new ArrayList<>();
    private Socket socket;  // Socket represents an individual connection Server-Client
    private BufferedReader bufferedReader;  // Read messages sent from server
    private BufferedWriter bufferedWriter;  // Send messages to the server
    private String clientUsername;
    private String clientLanguage;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            // In Java there are two types of streams: bytes and chars.  OutputStreamWriter -> character string and
            // getOutputStream -> byte string
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.clientLanguage = bufferedReader.readLine();
            clientHandlers.add(this);
            clientLanguages.add(clientLanguage);
            broadcastMessage(
                    "SERVER: " + clientUsername + " has entered the chat talking " + clientLanguage,
                    false
            );
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        // Listening for messages is a blocking operation. That is why we need to make a separate thread waiting for
        // messages and another one handling the whole tool
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient, true);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error reading a message from the server. Closing socket...");
                closeEverything(socket, bufferedReader, bufferedWriter);
                break; // To exit from infinite while loop
            }
        }
    }

    public void broadcastMessage(String messageToSend, boolean addUsername) {

        String messageToSendTranslated;

        for (ClientHandler clientHandler : clientHandlers) {
            try {
                int clientIndex = clientHandlers.indexOf(clientHandler);
                String clientLanguage = clientLanguages.get(clientIndex);
                messageToSendTranslated = translateMessage(messageToSend, clientLanguage);

                // Send message to all clients except to itself (the sender)
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    if (addUsername) {
                        clientHandler.bufferedWriter.write(
                                clientUsername + ": " + messageToSendTranslated
                        );
                    } else {
                        clientHandler.bufferedWriter.write(messageToSendTranslated);
                    }

                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();  // Buffer does not clear itself automatically
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error sending a message to the server. Closing socket...");
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    private String translateMessage(String messageToSend, String language) {
        // TODO: Do translation here just before broadcasting the message
        return messageToSend;
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat", false);
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();  // Closing the buffered we close everything related to it (eg. streams)
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error closing socket. Closing socket...");
        }
    }
}