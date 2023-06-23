package carsharing;

public class Customer {
    int id, rented_car_id;
    String name;
    public Customer(int id, int rented_car_id, String name){
        this.id=id;
        this.rented_car_id=rented_car_id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRented_car_id() {
        return rented_car_id;
    }

    public void setRented_car_id(int rented_car_id) {
        this.rented_car_id = rented_car_id;
    }
}
