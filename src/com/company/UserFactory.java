package com.company;

public class UserFactory {

    public static User createUser(String type){

        if (type == FacultyMember.TYPE)
            return new FacultyMember();


        else if (type == Student.TYPE)
            return new Student();


        else if (type == NonFacultyStaff.TYPE)
            return new NonFacultyStaff();


        else
            return new Visitor();
    }
}
