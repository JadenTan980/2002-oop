package entity;

import java.util.Scanner;

public abstract class User {
    private String id;
    private String name;
    private String password;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) {
        // if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        // to be added later, returning any exceptions good for edge cases
        this.name = name;
    }
    public void setPassword(String password){this.password = password;}
    public boolean login() { return true; }
    public boolean verifyPassword(String userpassword) {
<<<<<<< HEAD
        if (userpassword.equals(password)){
            return true;
        }
        else {
            return false;
        }
        }
    public void changePassword() {
=======
        return userpassword.equals(password);
    }
    public void changePassword(String oldPw,String newPw) {
>>>>>>> 692d4a9c5ba3485451fa9cda9805bdff8badbeb2
        System.out.println("Enter old password: ");
        Scanner scanner = new Scanner(System.in);
        String pw = scanner.nextLine(); 
        if (!pw.equals(this.password)) {
            System.out.println("Incorrect old password. Password change failed.");
        }
        else {
            System.out.print("Enter new password");
            this.password = scanner.nextLine();
            System.out.println("Password changed successfully.");
        }
    }
}

