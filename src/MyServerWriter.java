import java.io.*;
import java.util.Map;

class MyServerWriter extends Thread {
    private DataOutputStream dos;
    public MyServerWriter() {

    }
    public MyServerWriter(DataOutputStream dos) {
        this.dos = dos;
    }

    public void run() {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String info;
        try {
            while (true) {
                info = br.readLine();
                String[] par = info.split(" ");
                if(par[0].equals("@SystemBroadcast")){
                    if(par.length>1) {
                        int end = info.trim().indexOf(" ");
                        String message = info.trim().substring(end+1);
                        //String message = par[1];
                        SysBroadcast(message);
                        System.out.println("[系统广播]"+message);
                    }else{
                        System.out.println("系统广播不能为空！");
                    }
                }else if(par[0].equals("@Specific")){ //9
                    if(par.length>=3){
                        String name = par[1];
                        int len = name.length();
                        String message = info.trim().substring(11+len);
                        Sys_User(name,message);
                    }else if(par.length == 1){
                        System.out.println("目标用户不得为空");
                    }else if(par.length == 2){
                        System.out.println("消息不得为空");
                    }
                }else{
                    SysBroadcast(info);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void SysBroadcast(String message){
        for(Map.Entry<String, PrintWriter> entity : Server.outMap.entrySet()){
            entity.getValue().println("[系统广播]"+message);
        }
    }
    public synchronized void Sys_User(String nickName, String message){
        if(Server.outMap.containsKey(nickName)){
            PrintWriter target = Server.outMap.get(nickName);
            if(target!=null){
                target.println("[系统消息]"+message);
                System.out.println("[私聊用户"+nickName+"]"+message);
            }else{
                System.out.println("目标用户不存在！");
            }
        }else{
            System.out.println("用户不存在！");
        }
    }
}
