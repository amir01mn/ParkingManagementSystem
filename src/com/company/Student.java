package com.company;

public class Student extends User {

    public static final int rate = 5;
    public static final String TYPE = "Student";

    public Student() {
        super();
        setType(TYPE);
    }

    @Override
    public boolean validate() {
        String email = this.getEmail();
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        Manager manager = new Manager();
        String result = manager.validateYorkU(email);
        return result.equals("Student");
    }
}
