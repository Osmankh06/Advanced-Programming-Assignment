package medicareserver;
//importing necessary packages
import java.io.*;
import java.net.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; 

//implementing thread through Runnable interface
public class MediCareServer implements Runnable{
    public void run()
    {
        try
        {   
            // Instantiation of required objects
            ServerSocket ss = new ServerSocket(3333);
            Socket s = ss.accept(); // accepting connection request
            DataOutputStream DOS = new DataOutputStream(s.getOutputStream());
            DataInputStream DIS = new DataInputStream(s.getInputStream()); 
            
            // Getting data from client through Input Stream
            int id = DIS.readInt();
            String visitDate = DIS.readUTF();
            String patientType = DIS.readUTF();
            String service = DIS.readUTF();
            System.out.println("Data received successfully");
            
            //Converting date in proper format for inserting into database table
            DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(visitDate, DF);
            Date formattedDate = Date.valueOf(date);
            
            //Making connection to the database
            System.out.println("Connecting to Database...");
            String host= "jdbc:derby://localhost:1527/MediCare Oman Patient Billing";
            String un= "Osman"; //database username
            String pwd = "1234"; //database password
            Connection con = DriverManager.getConnection(host,un,pwd);
            System.out.println("Connected to database successfully");
            
            //Retrieving patient details and Insurance Plan through patient ID
            
            // creating variables for incoming data
            String name = "";
            int age = 0;
            String Insurance="";
            // Creating statement for SQL for for retreieving data
            Statement selectstmt = con.createStatement();
            String query = "select * from PATIENT where ID ="+id;
            selectstmt.execute(query);
            ResultSet rs = selectstmt.getResultSet();
            if(rs.next())
            {
                name = rs.getString("NAME");
                age = rs.getInt("AGE");
                Insurance = rs.getString("INSURANCETYPE");
                System.out.println("Data retrieved successfully from database");
            }
            
            //Assigning charges according to Service
            Double basefee=0.0;
            if (service.equals("CONS100"))     
            {
                basefee =12.00;
            }
            else if(service.equals("LAB210"))
            {
                basefee =8.50;
            }
            else if(service.equals("IMG330"))
            {
                basefee =25.00;
            }
            else if(service.equals("US400"))
            {
                basefee =35.00;
            }
            else if(service.equals("MRI700"))
            {
                basefee =180.00;
            }
            
            
            // Assigning values according to Insurance Plan
            Double DB=0.0;  // Discount on Base
            Double PVD=0.0; // Per Visit Discount
            if(Insurance.equals("Premium"))
            {
                DB =0.15;
                PVD =5.00;
            }
            else if(Insurance.equals("Standard"))
            {
                DB =0.10;
                PVD =8.00;
                
            }
            else if(Insurance.equals("Basic"))
            {
                DB =0.0;
                PVD =10.00;
                
            }
            
            
            //Assigning extra charges according to patient type
            Double chargeP = 0.0;
            if(patientType.equals("Outpatient"))
            {
                chargeP = 0.0;
            }
            else if(patientType.equals("Inpatient"))
            {
                chargeP = 0.05;    
            }
            else if(patientType.equals("Emergency"))
            {
                chargeP = 0.15;    
            }
            
            // Calculating Final Bill
            Double discount = PVD+(basefee*DB); // Discount = Per Visit Discount + Discount on Base Fee
            Double discountedBill = basefee - discount; 
            Double extraCharge = discountedBill*chargeP; // Calculation of Extra Charge on Discounted Bill
            Double BillAmount = discountedBill +extraCharge; // Final Bill Amount After deduction of discount and addition of Charges based on patient type
            System.out.println("Bill calculated successfully");
            
            
                      
            
            //Inserting record with values(patientID, visitDate and Bill Amount) to PATIENTBILLS table
            // Creating statement for SQL for for inserting data
            String insertstmt = "Insert into PATIENTBILLS Values(?,?,?)";
            PreparedStatement pStmt = con.prepareStatement(insertstmt);
            pStmt.setInt(1, id);
            pStmt.setDate(2, formattedDate);
            pStmt.setDouble(3, BillAmount);
            pStmt.execute();
            
            System.out.println("Data inserted to database table successfully");
            
            //Sending values back to client using Output Stream
            DOS.writeDouble(basefee); // Service Amount
            DOS.writeUTF(name); // patient details
            DOS.writeInt(age); 
            DOS.writeUTF(Insurance); //Insurance Plan
            DOS.writeDouble(discount); //Discount based on Insurance
            DOS.writeDouble(extraCharge); // extra charge based on patient type 
            DOS.writeDouble(BillAmount); // Final Bill Amount
            DOS.flush();
            System.out.println("Data sent successfully");
            
            
            //closing created objects
            con.close(); // closing database connection
            s.close(); // closing socket connection
            ss.close(); //closing server socket
            DOS.close(); // closing Data Output Stream
            DIS.close(); // closing Data Input Stream
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
            
    }
    public static void main(String[] args) {
        MediCareServer m = new MediCareServer(); // creating object of class
        Thread th = new Thread(m);  //Insantiation of thread object
        th.start(); // Starting thread
    }
    
}
