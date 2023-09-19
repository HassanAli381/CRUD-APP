import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    public static boolean validateDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }

        int year = Integer.parseInt(inDate.substring(0, 4));
        if(year <= 1950 || year >= 2025) {
            System.out.println("Logical Error!");
            return false;
        }

        return true;
    }

    public static String getDate(Scanner sc) {
        String newDate = sc.next();
        while(!validateDate(newDate)) {
            System.out.println("Wrong Date Format Please Enter date in this format 'yyyy-MM-dd'");
            newDate = sc.next();
        }
        return newDate;
    }

    public static void main(String[] args) {
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // Load the Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connection
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/My_Company","root", "");
            System.out.println("Connection Success!");

            // Create Statement Object
            statement = dbConnection.createStatement();
            Scanner sc = new Scanner(System.in);

            while(true) {
                System.out.println("Choose Number From Menu : ");
                System.out.println("1 - Insert Data");
                System.out.println("2 - Delete Data");
                System.out.println("3 - Update Data");
                System.out.println("4 - Fetch Data");
                System.out.println("5 - Terminate the Program!");

                int valueChosen = sc.nextInt();

                if(valueChosen == 1) {
                    // INSERT
                    System.out.println("Enter Name");
                    String name = sc.next();
                    System.out.println("Enter Salary");
                    int salary = sc.nextInt();
                    System.out.println("Enter Address");
                    String Address = sc.next();
                    System.out.println("Enter Birth Date in this format 'yyyy-MM-dd'");
                    String bdate = getDate(sc);
                    System.out.println("Enter Hiring Date in this format 'yyyy-MM-dd'");
                    String hdate = getDate(sc);

                    // Execute Query!
                    int numOfRowsAffected = statement.executeUpdate("INSERT INTO EMP(EMP_NAME, EMP_SALARY, bDate, Hiring_Date, Address) VALUES('"+name+"', "+salary+", '"+bdate+"', '"+hdate+"', '"+Address+"')");
                    // executeUpdate => return number of rows affected!
                    if(numOfRowsAffected > 0)
                        System.out.println("Record has been Updated!");
                    else
                        System.out.println("Something Went Wrong!");
                }
                else if(valueChosen == 2) {
                    // Delete
                    System.out.println("Do you want to delete all database or just a record if all press 1 else press 2");
                    int choose = sc.nextInt();
                    if(choose == 1) {
                        int numOfRowsAffected = statement.executeUpdate("DELETE FROM EMP");
                        if(numOfRowsAffected > 0)
                            System.out.println("Data Base Formatted Successfully!");
                        else
                            System.out.println("There is Something Wrong!");
                    }
                    else if(choose == 2){
                        System.out.println("Enter Employee ID to Remove");
                        int idToDelete = sc.nextInt();
                        int numOfRowsAffected = statement.executeUpdate("DELETE FROM EMP WHERE EMP_ID = " + idToDelete);
                        if (numOfRowsAffected > 0)
                            System.out.println("Record Deleted Successfully!");
                        else
                            System.out.println("No Such Id!");
                    }
                    else {
                        System.out.println("Wrong Choice!");
                    }
                }
                else if(valueChosen == 3) {
                    System.out.println("Enter Employee Id");
                    int empId = sc.nextInt();
                    System.out.println("Choose What you want to Edit : ");
                    System.out.println("1 - Name");
                    System.out.println("2 - Address");
                    System.out.println("3 - Salary");
                    System.out.println("4 - Birth Date");
                    System.out.println("5 - Hiring Date");
                    int qry = 0;
                    int choose = sc.nextInt();
                    if(choose == 1) {
                        System.out.println("Enter New Name");
                        String newName = sc.next();
                        qry = statement.executeUpdate("UPDATE EMP SET EMP_NAME ='"+newName+"' WHERE EMP_ID ="+empId+"");
                    }
                    else if(choose == 2) {
                        System.out.println("Enter New Address");
                        String newAdress = sc.next();
                        qry = statement.executeUpdate("UPDATE EMP SET Address ='"+newAdress+"' WHERE EMP_ID ="+empId+"");
                    }
                    else if(choose == 3) {
                        System.out.println("Enter New Salary");
                        int newSalary = sc.nextInt();
                        qry = statement.executeUpdate("UPDATE EMP SET EMP_SALARY = "+newSalary+" WHERE EMP_ID = "+empId+" " );
                    }
                    else if(choose == 4) {
                        System.out.println("Enter Birth Date in YYYY-MM-DD Format");
                        String newBirthDate = getDate(sc);
                        qry = statement.executeUpdate("UPDATE EMP SET bDate ='"+newBirthDate+"' WHERE EMP_ID ="+empId+"");
                    }
                    else if(choose == 5) {
                        System.out.println("Enter Hiring Date in YYYY-MM-DD Format");
                        String newHiringDate = getDate(sc);
                        qry = statement.executeUpdate("UPDATE EMP SET Hiring_Date ='"+newHiringDate+"' WHERE EMP_ID ="+empId+"");
                    }
                    else {
                        System.out.println("Not Valid Answer!");
                    }

                    if(qry > 0)
                        System.out.println("Employee Data Updated Successfully!");
                    else
                        System.out.println("Wrong Query!");

                }
                else if(valueChosen == 4) {
                    // Retrieving Data
                    // This Query will fetch all records

                    resultSet = statement.executeQuery("SELECT * FROM EMP");
                    while(resultSet.next()) {
                        System.out.println("Id = " + resultSet.getInt(1));
                        System.out.println("Name = " + resultSet.getString("Emp_Name"));
                        System.out.println("Salary " + resultSet.getDouble(3));
                        System.out.println("Address " + resultSet.getString("Address"));
                        System.out.println("Birth Date " + resultSet.getString("bDate"));
                        System.out.println("Hiring Date " + resultSet.getString("Hiring_Date"));
                        System.out.println("------------------");
                    }
                }
                else {
                    System.out.println("Thanks for using my App, See You Soon :)");
                    break;
                }
            }

        }
        catch (Exception exception){
            System.out.println("Exception : " + exception.toString());
        }
    }
}
// Al-Hassan Ali Badawy
// Thanks ! :)