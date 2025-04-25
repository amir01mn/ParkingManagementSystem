package com.company;

public class NonFacultyStaff extends User{

    public static final int rate = 10;
    public static final String TYPE = "Non-Faculty Staff";
    public static User user;

    public NonFacultyStaff(){
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
        return result.equals("Non-Faculty Staff");
    }

    @Override
    public void setType(String type) {
        super.setType(TYPE);
    }

}
