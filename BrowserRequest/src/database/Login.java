package database;

public class Login {
    private Database database = Database.newInstance("root", "2972358040WCJwcj");
    private boolean isLogin = false, isRegister = false;
    private String fileName;
    private String account;
    private String password;

    public String getFileName() {
        if (isLogin) {
            fileName = "database.html";
        } else fileName = requireRegister();
        return fileName;
    }

    public boolean requireLogin() {
        String password = database.select(account);

        if (this.password.equals(password)) isLogin = true;
        else {
            //...
            //—ØŒ  «∑Ò◊¢≤·’À∫≈
            isLogin = false;
        }
        return isLogin;
    }

    public String requireRegister(){
        fileName = "register.js";
        return fileName;
    }

    private void register() {
        database.insert(account, password);
    }
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRegister() {
        return isRegister;
    }
}
