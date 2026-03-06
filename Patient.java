package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;
    public Patient(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;

    }
    public void addPatient(){
        System.out.print("Enter Patient Name: ");
        String name=scanner.next();
        System.out.println("Enter Patient Age: ");
        int age =scanner.nextInt();
        System.out.println("Enter Patient Gender: ");
        String gender=scanner.next();
        System.out.println("Enter patient Address: ");
        String Address=scanner.next();

        try{
            String query = "insert into patients(name, age, gender, address) values (?, ?, ?, ?)";

            //String query ="insert into patients(name ,age,gender,address) values(?,?,?,?)";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            preparedStatement.setString(4,Address);
            int affectedRRows=preparedStatement.executeUpdate();
            if(affectedRRows>0){
                System.out.println("Patient Added Sucessfully!!");
            }else {
                System.out.println("Failed to add Patient!!");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public void viewPatient(){
        String query="select * from patients";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("patients: ");
            System.out.println("+-----------+-------------------+---------+------------+------------------------------+");
            System.out.println("| patient id | Name             | Age     | Gender     | Address                       ");
            System.out.println("+-----------+-------------------+---------+------------+------------------------------+");
            while (resultSet.next()){
                int id=resultSet.getInt("id");
                String name =resultSet.getString("name");
                int age =resultSet.getInt("age");
                String gender =resultSet.getString("gender");
                String Address =resultSet.getString("Address");
                System.out.printf("|%-13s|%-19s|%-10s|%-13s|%-32s|\n",id,name,age,gender,Address);
                System.out.println("+-----------+-------------------+---------+------------+------------------------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getPatientById(int id){
        String query="select * from patients where id=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
