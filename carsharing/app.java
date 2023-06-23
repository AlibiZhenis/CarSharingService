package carsharing;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class app {
    Scanner in;
    DB db;
    public app(String filename) throws SQLException, ClassNotFoundException {
        db = new DB(filename);
        in = new Scanner(System.in);
        run();
    }

    public void run() throws SQLException {
        int command=-1;
        String input;
        while(command!=0){
            System.out.println("1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");
            command = in.nextInt();
            System.out.println();
            switch(command){
                case 0: {
                    break;
                }
                case 1: {
                    loginManager();
                    break;
                }
                case 2:{
                    loginCustomer();
                    break;
                } case 3:{
                    System.out.println("Enter the customer name:");
                    do{
                        input = in.nextLine();
                    } while (input.equals("\n") || input.isEmpty());
                    db.addCustomer(input);
                    System.out.println("The customer was added!\n");
                    break;
                }
            }
        }
    }

    public void loginCustomer() throws SQLException {
        int command = -1, temp;
        String input;
        ArrayList<Customer> customers = db.getCustomers();
        if(customers.isEmpty()){
            System.out.println("The customer list is empty!");
            return;
        }
        System.out.println("Customer list: ");
        for(int i=0; i<customers.size(); i++){
            System.out.println(i+1+". "+customers.get(i).getName());
        }
        System.out.println("0. Back");
        temp = in.nextInt();
        if(temp==0){
            return;
        }
        CustomerMenu(customers.get(temp-1));
    }

    public ArrayList<Car> popRentedCars(ArrayList<Car> cars) throws SQLException {
        ArrayList ans = new ArrayList<Car>();
        for(int i=0; i<cars.size(); i++){
            if(!db.isRented(cars.get(i))){
                ans.add(cars.get(i));
            }
        }
        return ans;
    }


    public void CustomerMenu(Customer customer) throws SQLException {
        int command =-1;
        while(command!=0){
            System.out.println("1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");
            command = in.nextInt();
            System.out.println();
            switch(command){
                case 0:{
                    break;
                }
                case 1:{
                    if(customer.getRented_car_id()!=0){
                        System.out.println("You've already rented a car!");
                        break;
                    }
                    Company company = chooseCompany();
                    if(company==null){
                        break;
                    }
                    ArrayList<Car> cars = db.getCars(company.getId());
                    if(cars.isEmpty()){
                        System.out.println("No available cars in the '%s' company".formatted(company.getName()));
                        break;
                    }
                    cars = popRentedCars(cars);
                    Collections.sort(cars);
                    System.out.println("Choose a car:");
                    for(int i=0; i<cars.size(); i++){
                        System.out.println(i+1+". "+cars.get(i).getName());
                    }
                    System.out.println("0. Back");
                    int temp = in.nextInt();
                    System.out.println();
                    if(temp==0){
                        break;
                    }
                    Car chosen = cars.get(temp-1);

                    db.rentCar(customer.getId(), chosen.getId());
                    customer.setRented_car_id(chosen.getId());

                    System.out.println("You rented '%s'".formatted(chosen.getName()));
                    break;
                }
                case 2:{
                    int carID = customer.rented_car_id;
                    if(carID==0){
                        System.out.println("You didn't rent a car!");
                        break;
                    }
                    customer.setRented_car_id(0);
                    db.returnCar(customer.getId());
                    System.out.println("You've returned a rented car!");
                    break;
                }
                case 3: {
                    int carID = customer.rented_car_id;
                    if(carID==0){
                        System.out.println("You didn't rent a car!");
                        break;
                    }
                    Car car = db.getCar(carID);
                    int companyID = car.getCompanyID();
                    Company company = db.getCompany(companyID);

                    System.out.println("Your rented car:");
                    System.out.println(car.getName());
                    System.out.println("Company:\n" + company.getName());
                    break;
                }
            }
        }
    }

    public Company chooseCompany() throws SQLException {
        ArrayList<Company> companies = db.getCompanies();
        if(companies.isEmpty()){
            System.out.println("The company list is empty!");
            return null;
        }
        System.out.println("Choose a company: ");
        for(int i=0; i<companies.size(); i++){
            System.out.println(i+1+". "+companies.get(i).getName());
        }
        System.out.println("0. Back");
        int temp = in.nextInt();
        System.out.println();
        if(temp==0){
            return null;
        }
        return companies.get(temp-1);
    }

    public void loginManager() throws SQLException {
        int command = -1, temp;
        String input;
        while(command!=0){
            System.out.println("1. Company list\n" + "2. Create a company\n" + "0. Back");
            command = in.nextInt();
            System.out.println();
            switch(command){
                case 0:{
                    break;
                }
                case 1:{
                    Company company = chooseCompany();
                    if(company==null){
                        break;
                    }
                    companyMenu(company);
                    break;
                }
                case 2:{
                    System.out.println("Enter the company name:");
                    do{
                        input = in.nextLine();
                    } while (input.equals("\n") || input.isEmpty());
                    db.addCompany(input);
                    System.out.println("The company was created!\n");
                    break;
                }
            }
        }
    }

    public void companyMenu(Company comp) throws SQLException {
        int command = -1;
        String input;
        System.out.println("'%s' company: ".formatted(comp.getName()));
        while(command!=0){
            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");
            command = in.nextInt();
            System.out.println();
            switch (command){
                case 0:{
                    break;
                } case 1:{
                    ArrayList<Car> cars = db.getCars(comp.getId());
                    if(cars.isEmpty()){
                        System.out.println("The car list is empty!");
                        break;
                    }
                    System.out.println("Car list:");
                    for(int i=0; i<cars.size(); i++){
                        System.out.println(i+1+". "+cars.get(i).getName());
                    }
                    System.out.println();
                    break;
                } case 2:{
                    System.out.println("Enter the car name: ");
                    do{
                        input = in.nextLine();
                    } while (input.equals("\n") || input.isEmpty());
                    db.addCar(input, comp.getId());
                    System.out.println("The car was added!\n");
                    break;
                }
            }
        }
    }
}
