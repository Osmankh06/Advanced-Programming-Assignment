package medicareclient;
import java.io.*;
import java.util.*;
import java.net.*;

public class MediCareClient implements Runnable
{
    public void run()
    {
        try
        {   
            // Creation of required objects
            Socket s = new Socket("localhost",3333); //connection request to server
            Scanner sc = new Scanner(System.in);
            DataOutputStream DOS = new DataOutputStream(s.getOutputStream());
            DataInputStream DIS = new DataInputStream(s.getInputStream()); 
            
            //Accepting data from the user 
            System.out.println("Enter ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            
            // Validation of id; should be positive
            if(id<=0)
            {
                System.out.println("ID should be greater than 0");
                return; //exits the entire program upon wrong input
            }
                
            System.out.println("Enter Visit Date(dd-MM-yyyy):");
            String visitDate = sc.nextLine();
            
            System.out.println("Enter Patient Type(Outpatient, Inpatient, Emergency ):");
            String patientType = sc.nextLine();
            // Checking if input given by the user matches the Types of patients 
            ArrayList<String> validTypes = new ArrayList<>(Arrays.asList("Outpatient","Inpatient","Emergency"));
            if(!validTypes.contains(patientType))
            {
                System.out.println("Invalid Patient Type");
                return; //exits the entire program upon wrong input
            }
            // HashMap for valid service codes with their description. code = key, description = value
            HashMap<String,String> services = new HashMap<>();
            services.put("CONS100","Consultation (General)");
            services.put("LAB210","Lab Test ");
            services.put("IMG330","X-Ray");
            services.put("US400","Ultrasound");
            services.put("MRI700","MRI");
            
            System.out.println("Enter Service Code");
            System.out.println(services);            
            String service = sc.nextLine();
            
            // Checking if input given by the user matches the list of Valid Service Codes
            if(!services.containsKey(service))
            {
                System.out.println("Invalid Service");
                return; //exits the entire program upon wrong input
            }

            //sending data to the server
            DOS.writeInt(id);
            DOS.writeUTF(visitDate);
            DOS.writeUTF(patientType);
            DOS.writeUTF(service);
            DOS.flush();
            
            // getting data from server
            Double serviceAmount = DIS.readDouble();
            String name = DIS.readUTF();
            int age = DIS.readInt();
            String insurancePlan = DIS.readUTF();
            Double discount = DIS.readDouble();
            Double extraCharge = DIS.readDouble();
            Double FinalBill = DIS.readDouble();
            
            // print data to user
            System.out.println("----------------Summary----------------");
            System.out.println("User id:  "+id);
            System.out.println("Name:  "+name);
            System.out.println("Age:  "+age);
            System.out.println("Date of visit:  "+visitDate);
            System.out.println("Patient type:  "+patientType);
            System.out.println("Service Code:  "+service);
            System.out.println("Service Name:  "+services.get(service)); // finding service name through given code
            System.out.println("Service charges:  "+serviceAmount+" OMR");
            System.out.println("Insurance plan:  "+insurancePlan);
            System.out.println("Discount:  "+discount+" OMR");
            System.out.println("Extra charges:  "+extraCharge+" OMR");
            System.out.println("Final Bill:  "+FinalBill+" OMR");
            
            //closing objects
            s.close(); // closing socket connection
            sc.close(); //closing scanner
            DOS.close(); //closing Output Stream
            DIS.close(); //closing Input Stream
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
            
    }
        
    public static void main(String[] args) 
    {
        MediCareClient m = new MediCareClient(); // Creating class object
        Thread th = new Thread(m); // Insantiation of thread
        th.start(); // Starting Thread
    }
    
}
