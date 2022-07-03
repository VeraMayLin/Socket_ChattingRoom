
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    public static Map<String,PrintWriter> outMap = new HashMap<>();
    public static Map<String,OutputStream> fileMap = new HashMap<>();
    public Server(){
        try {
            System.out.println("8080端口服务等待创建！");
            serverSocket = new ServerSocket(8080);
            System.out.println("8080端口服务已创建！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        System.out.println("等待连接");
        Socket socket = null;
        try {
            while(true){
                socket = serverSocket.accept();
                ExecutorService pool = Executors.newFixedThreadPool(10);
                pool.execute(new ServerHandler(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        new MyServerWriter().start();
        server.start();
    }


    public synchronized void sendAll(String message){
        for(Entry<String, PrintWriter> entity : outMap.entrySet()){
            entity.getValue().println("[群聊]"+message);
        }
    }

    public synchronized void addMap(String nickName , PrintWriter pw){
        outMap.put(nickName, pw);
    }
    public synchronized void addFileMap(String nickName , OutputStream os){
        fileMap.put(nickName, os);
    }
    public synchronized void removeMap(String nickName){
        outMap.remove(nickName);
    }
    public synchronized void sendOther(String nickName , String message , PrintWriter myself, String myName){
        PrintWriter other = outMap.get(nickName);

        if(nickName.equals(myName)){
            myself.println("不可以和自己聊天哟～");
        }else if(other!=null){
            other.println("["+myName+"私聊]"+message);
            myself.println("[私聊"+nickName+"]"+message);
        } else{
            sendAll(message);
        }
    }

    public synchronized void sendSystem(String message,PrintWriter myself,String myName){
        myself.println("[系统]"+message);
        System.out.println("[来自用户"+myName+"的私聊]"+message);
    }


    class ServerHandler implements Runnable{
        private Socket socket;
        private String address;
        private String nickName;
        public  FileInfo sendFile;
        public JFileChooser jfc = new JFileChooser();
        public ServerHandler(Socket socket){
            this.socket = socket;
            address = socket.getInetAddress().getHostAddress();
            System.out.println(address+":"+socket.getPort()+" 已上线");
        }

        @Override
        public void run() {
            PrintWriter pw = null;
            try {
                //获取输出流
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os,"UTF-8");
                pw = new PrintWriter(osw,true);

                //获取输入流
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                //获取昵称（第一行）
                nickName = br.readLine();
                addMap(nickName, pw);
                addFileMap(nickName,os);
                System.out.println("聊天室在线人数为："+outMap.size());
                //获取聊天信息
                String info = null;
                while((info = br.readLine())!=null){
                    String[] par = info.split(" ");
                    if(par[0].equals("@Specific")){
                        if(par.length>=3){
                            String targetName = par[1];
                            int len = targetName.length();
                            if(targetName.equals(nickName)){
                                pw.println("不可以和自己聊天哟～");
                                continue;
                            }
                            if(!outMap.containsKey(nickName)){
                                pw.println("用户不存在");
                            }
                            String message = info.trim().substring(11+len);
                            //Sys_User(name,message);
                            sendOther(targetName,message,pw,nickName);
                        }else if(par.length == 1){
                            pw.println("目标用户不得为空");
                        }else if(par.length == 2){
                            String targetName = par[1];
                            if(targetName.equals(nickName)){
                                pw.println("不可以和自己聊天哟～");
                                continue;
                            }else {
                                pw.println("消息不得为空");
                            }
                        }
                    }else if(par[0].equals("@System")){
                        int end = info.trim().indexOf(" ");
                        String message = info.trim().substring(end+1);
                        sendSystem(message,pw,nickName);
                    }else if(par[0].equals("@file")){
//                        JFileChooser jfc = new JFileChooser();
                        FileSystemView fsv = FileSystemView.getFileSystemView();
                        jfc.setCurrentDirectory(fsv.getHomeDirectory());
                        jfc.setVisible(true);
                        //jfc.setup();
                        jfc.showOpenDialog(null);
                        //if (jfc == JFileChooser.APPROVE_OPTION) {
                            File file = jfc.getSelectedFile();
                            sendFile = new FileInfo();
//                            sendFile.setFromUser(DataBuffer.currentUser);
//                            sendFile.setToUser(selectedUser);
                            try {
                                sendFile.setSrcName(file.getCanonicalPath());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            sendFile.setSendTime(new Date());

                        String targetName = par[1];
                        BufferedInputStream bis = null;
                        BufferedOutputStream bos = null;
                        //Socket socket = null;
                        try {
                            bis = new BufferedInputStream(new FileInputStream(sendFile.getSrcName()));//文件读入
                            String path = sendFile.getDestName();
                            pw.println("本地文件路径为："+sendFile.getSrcName());
                            String[] fileName = path.split("/");

                            bos = new BufferedOutputStream(new FileOutputStream(path+"/"+file.getName()));
                            byte[] buffer = new byte[1024];
                            int n = -1;
                            while ((n = bis.read(buffer)) != -1){
                                bos.write(buffer, 0, n);
                            }
                            bos.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally{
                            bis.close();
                            //bos.close();
                            //socket.close();
                        }

                        pw.println("向"+targetName+"发送文件["+file.getName()+"]");
                        PrintWriter targetPw = outMap.get(targetName);
                        targetPw.println("成功接受来自"+nickName+"的文件"+file.getName());
                    } else{
                        sendAll(nickName+":"+info);
                    }
                }
            } catch (IOException e) {

            } finally {
                try {
                    if(pw!=null){
                        removeMap(nickName);
                    }
                    System.out.println("聊天室在线人数为："+outMap.size());
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}

