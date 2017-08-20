package niravitalzohar.smartlock.smartlock;

/**
 * Created by zohar on 02/04/2017.
 */

public class Lock {

    private String lockid,ip,port,lockStatus,mangerCode,memberCode;

    public Lock(){}
    public Lock(String lockid,String ip,String port,String lockStatus,String mangerCode,String memberCode){
        this.lockid=lockid;
        this.ip=ip;
        this.port=port;
        this.lockStatus=lockStatus;
        this.mangerCode=mangerCode;
        this.memberCode=memberCode;
    }



    public void setLockid(String lockid) {
        this.lockid = lockid;
    }

    public String getLockid() {
        return lockid;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getMangerCode() {
        return mangerCode;
    }

    public void setMangerCode(String mangerCode) {
        this.mangerCode = mangerCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

}
