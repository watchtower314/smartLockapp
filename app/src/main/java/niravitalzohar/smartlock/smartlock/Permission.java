package niravitalzohar.smartlock.smartlock;

import java.security.PrivateKey;

/**
 * Created by zohar on 27/03/2017.
 */



public class Permission {


    private permission_type pertyp;
    private String lockid,userid, duration ;
    private long pysicalId;


    public Permission(){}

    public Permission(permission_type pertyp,String lockid,String userid,long pysicalId,String duration  ){

        this.pertyp=pertyp;
        this.lockid=lockid;
        this.userid=userid;
        this.pysicalId=pysicalId;
        this.duration=duration;

    }

    public  permission_type  getPertyp(){
        return pertyp;
    }

    public void setPertyp(permission_type pertyp){
        this.pertyp=pertyp;
    }

    public String getLockid(){
        return lockid;
    }

    public void setLockid(String lockid){
        this.lockid=lockid;
    }

    public String getUserid(){
        return  userid;
    }
    public void setUserid(String userid){
        this.userid=userid;
    }

    public String getDuration(){
        return duration;
    }

    public void setDuration(String duration){
        this.duration=duration;
    }

    public long getPysicalId(){
        return pysicalId;
    }

    public void setPysicalId(long pysicalId){

        this.pysicalId=pysicalId;
    }




}
