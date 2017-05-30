package niravitalzohar.smartlock.smartlock;

/**
 * Created by zohar on 19/03/2017.
 */

public class User {
    //int userid;
    String phonenumber,password,email ,userid;
    //String userName;

    public User(){}
 //   public User(String userid,String userName,String phonenumber,String password,String email){
        public User(String userid,String phonenumber,String password,String email){

            this.userid=userid;
       // this.userName=userName;
        this.phonenumber=phonenumber;
        this.password=password;
        this.email=email;
    }

  /* public String getUserName(){
        return userName;

    }


    public void setUsername(String userName){
        this.userName=userName;
    }*/

    public String getPhonenumber(){
        return phonenumber;
    }

    public void setPhonenumber( String phonenumber){
        this.phonenumber=phonenumber;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getUserid(){
        return userid;
    }

    public void setUserid(String userid){
        this.userid=userid;
    }





}
