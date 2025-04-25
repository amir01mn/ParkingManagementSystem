package com.company;

public class FacultyMember extends User {

    public static final int rate = 8;
    public static final String TYPE = "Faculty Member";
    public static User user;

    public FacultyMember(){

        super();
        setType(TYPE);
    }

    @Override
    public void setType(String type) {
        super.setType(TYPE);
    }

    @Override
    public boolean validate() {
        String email = this.getEmail();
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        Manager manager = new Manager();
        String result = manager.validateYorkU(email);
        return result.equals("Faculty Member");
    }
}
