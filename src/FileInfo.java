import java.io.Serializable;
import java.util.Date;

public class FileInfo implements Serializable {
    /** 源文件名 */
    private String srcName;
    /** 发送时间 */
    private Date sendTime;
    /** 目标地IP */
    private String destIp;
    /** 目标地端口 */
    private int destPort;
    /** 目标文件名 */
    private String destName ="/Users/vera/Desktop";
    public String getSrcName() {
        return srcName;
    }
    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }
    public Date getSendTime() {
        return sendTime;
    }
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
    public String getDestIp() {
        return destIp;
    }
    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }
    public int getDestPort() {
        return destPort;
    }
    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
    public String getDestName() {
        return destName;
    }
    public void setDestName(String destName) {
        this.destName = destName;
    }
}
