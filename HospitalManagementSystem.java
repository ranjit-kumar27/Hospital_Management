package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="Ranjit@2024";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);
        try {
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection);
            while (true){
                System.out.println("HOSAPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add  Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Docters");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exist");
                System.out.println("Enter Your Choice:");
                int choice=scanner.nextInt();
                switch (choice){
                    case 1:
                        // Add Patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // View Patient
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        //view Doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //Book Appointment
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;

                    case 5:
                        return;
                    default:
                        System.out.println("Enter Valid Choice!!!");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient ID: ");
        int patientId=scanner.nextInt();
        System.out.print("Enter Doctor ID: ");
        int doctorId=scanner.nextInt();
        System.out.print("Enter Appointment date (YYYY-MM-DD): ");
        String appointmentdate=scanner.next();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId,appointmentdate,connection)){
                //String appointmentquery="insert into appointments(patient_id,appointment_date) values(?,?,?)";
                String appointmentquery="INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                try {
                    PreparedStatement preparedStatement= connection.prepareStatement(appointmentquery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentdate);
                    int rowsAffected=preparedStatement.executeUpdate();
                    if (rowsAffected>0){
                        System.out.println("Appointment Booked!");
                    }else {
                        System.out.println("Failed to Book Appointment!!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor not Available on this Date!!");
            }
        }else {
            System.out.println("Either doctor or patient Doesn't exist!!!");
        }
    }
    public static boolean checkDoctorAvailability(int doctorid,String appointmentDate,Connection connection){
        //String query="select count(*) appointment where doctor_id=? And appointment_date=?";
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            //preparedStatement.setInt(1,doctorid);
            preparedStatement.setInt(1, doctorid);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next()){
                int count =resultSet.getInt(1);
                if (count==0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
