package sample;

import sample.Controll.PasswordHashing;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.regex.*;

public class DBServer {
    private static final String URL = "jdbc:mysql://localhost:3306/browser_account";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();

                // Lắng nghe khi client đóng kết nối
                Thread listenForDisconnect = new Thread(() -> {
                    try {
                        clientHandler.join(); // Chờ cho đến khi clientHandler kết thúc
                        System.out.println("Client disconnected");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                listenForDisconnect.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream();
                 ObjectInputStream objectInput = new ObjectInputStream(input);
                 ObjectOutputStream objectOutput = new ObjectOutputStream(output)) {

                String action = (String) objectInput.readObject();
                String username = (String) objectInput.readObject();
                String password = (String) objectInput.readObject();

                switch (action) {
                    case "register":
                        boolean registerSuccess = registerUser(username, password);
                        objectOutput.writeObject(registerSuccess);
                        break;
                    case "login":
                        boolean loginSuccess = logIn(username, password);
                        objectOutput.writeObject(loginSuccess);
                        break;
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    socket.close(); // Đóng kết nối với client khi xử lý kết thúc
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean registerUser(String username, String password) {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement checkUser = connection.prepareStatement("SELECT * FROM browser_account WHERE username = ?");
                 PreparedStatement psInsert = connection.prepareStatement("INSERT INTO browser_account (username, password) VALUES (?, ?)")) {

                checkUser.setString(1, username);
                ResultSet resultSet = checkUser.executeQuery();

                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

                if (username.equals("") || password.equals("")) {
                    return false;
                } else if (!username.matches(EMAIL_PATTERN)) {
                    return false;
                } else if (!password.matches(PASSWORD_PATTERN)) {
                    return false;
                } else if (resultSet.next()) {
                    return false;
                } else {
                    String hashedPassword = PasswordHashing.sha1(password);
                    psInsert.setString(1, username);
                    psInsert.setString(2, hashedPassword);
                    psInsert.executeUpdate();
                    return true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        private boolean logIn(String username, String password) {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM browser_account WHERE username = ?")) {

                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.isBeforeFirst()) {
                    return false;
                } else {
                    while (resultSet.next()) {
                        String getPass = resultSet.getString("password");
                        String hashedInputPass = PasswordHashing.sha1(password);
                        if (getPass.equals(hashedInputPass)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }
    }
}
