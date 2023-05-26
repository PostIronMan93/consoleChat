package client;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9123);
            Scanner scanner = new Scanner(System.in);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String message;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                        try {
                            while (true)
                            System.out.println(in.readUTF());
                        } catch (IOException e) {
                            System.out.println("Потеряно соединение с сервером");
                        }
                }
            });
            thread.start();
            JSONObject jsonObject = new JSONObject();
            while (true) {

                message = scanner.nextLine();
                if(message.indexOf("/m") ==0){
                    String[] words = message.split(" ");
                    int skipStr = 3+words[1].length();
                    String msg = message.substring(skipStr);
                    jsonObject.put("public", false);
                    jsonObject.put ("id", words[1]);
                    jsonObject.put("msg", msg);
                } else {
                    jsonObject.put("public", true);
                    jsonObject.put("msg", message);
                }
                    out.writeUTF(jsonObject.toJSONString());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
