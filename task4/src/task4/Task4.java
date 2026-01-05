package task4;
import java.io.*;
import java.util.*;
public class Task4 
{

    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in); //Scanner for getting user input
        HashMap<String,String> services = new HashMap<>(); // creating an object of utility class HashMap
         
        while(true) //while loop for menu drive program. Loops until exit
        {
            //Different menu options under heading
            System.out.println("MediCare Oman Services Menu"); 
            System.out.println("Select action:");
            System.out.println("1. Add Service");
            System.out.println("2. Remove Service");
            System.out.println("3. Display Available Services");
            System.out.println("0. Exit \n");
            String selection = sc.nextLine();
            
            // if conditions depending upon user choice
            
            if(selection.equals("1"))
            {
                //For adding an element 
                System.out.println("Enter new Service code: ");
                String code =sc.nextLine(); //key input for HashMap
                System.out.println("Enter name of Service: ");
                String name =sc.nextLine(); //value input for HashMap

                //checking if HashMap already contains the service code or service name
                if(services.containsKey(code)|| services.containsValue(name))
                {
                    System.out.println("Service Code or Name already present!");
                }
                else
                {
                    // adding service code and name as key value pair after validation 
                    services.put(code, name);
                    System.out.println("Service "+code+" : "+name+ " added successfully");
                }
                
            }
            else if(selection.equals("2"))
            {
                // For removing specific service by referring it through its code
                System.out.println("Enter code of service you want to remove: ");
                
                String r = sc.nextLine();
                if(services.containsKey(r))
                {
                System.out.println("Removing "+r+" "+services.get(r));
                services.remove(r);
                System.out.println("Service removed successfully");
                }
                else
                    System.out.println("No service for code "+r+" exists");
                
            }
            else if(selection.equals("3"))
            {
                // For displaying all services present in the hashmap
                System.out.println("Service Code  ||  Service Name");
                System.out.println("-----------------------------");
                for(Map.Entry m : services.entrySet()) //iterating through the hashmap
                    System.out.println(m.getKey()+" || "+m.getValue()); //displaying service name + code
                
            }
            else if(selection.equals("0"))
            {
                //For exiting the whole program
                System.out.println("Exiting Services Menu....");
                return;
                
            }
            else
            {
                // displays a message if the user enters invalid selection
                System.out.println("Please select a valid action!");
            }
            System.out.println("--------------------------------");
        }
    }
    
}
