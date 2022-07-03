//package com.zdl.tcptest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner scanner;
    public Client(){
        try {
            socket = new Socket("localhost",8080);
            System.out.println("连接已创建！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void start(){
        System.out.println("欢迎进入ChattingRoom！");
        try {
            //开启输入流，接收聊天信息
            new Thread(new ClientHandler(socket)).start();

            //获取输出流
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os,"UTF-8");
            PrintWriter pw = new PrintWriter(osw,true);
            scanner = new Scanner(System.in);
            //输入昵称（第一行）
            System.out.println("请输入昵称");
            pw.println(scanner.nextLine());
            System.out.println("昵称创建成功!");
            while(true){
                pw.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Client user = new Client();
        user.start();
    }

    class ClientHandler implements Runnable{
        private Socket socket;
        public ClientHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String message = null;
                while((message = br.readLine())!=null){
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}


