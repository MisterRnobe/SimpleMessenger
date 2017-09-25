package server;

import messages.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
    public static final int PORT = 6789;
    private ServerSocket serverSocket;
    HashMap<String, ObjectOutputStream> clients;
    private Server()
    {
         clients = new HashMap<>();
        try{
            serverSocket = new ServerSocket(PORT);
            while (true)
            {
                Socket socket = serverSocket.accept();
                Handler h = new Handler(socket);
                Thread t = new Thread(h);
                String login = h.getLogin();
                clients.put(login, new ObjectOutputStream(socket.getOutputStream()));
                System.out.println("Got login: " + login);
                t.start();
                System.out.println("Connected: "+login+" with " + socket.getInetAddress().toString());
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    private void sendAll(Message msg)
    {
        for(Map.Entry entry: clients.entrySet())
        {
            ObjectOutputStream stream = (ObjectOutputStream) entry.getValue();
            try {
                stream.writeObject(msg);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static final void start()
    {
        new Server();
    }

    class Handler implements Runnable
    {
        ObjectInputStream reader;
        String ip;
        Handler(Socket s)
        {
            try
            {
                reader = new ObjectInputStream(s.getInputStream());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            ip = s.getInetAddress().toString();
        }
        String getLogin() throws IOException, ClassNotFoundException
        {
            return (String) reader.readObject();
        }
        @Override
        public void run() {
            Message message;
            try
            {
                while ((message = (Message) reader.readObject()) != null)
                {
                    sendAll(message);
                    System.out.println(message.getLogin()+" " + message.getMessage());
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("Something goes wrong...");
                e.printStackTrace();
            }
            System.out.println(ip+" disconnected!");

        }
    }
}
