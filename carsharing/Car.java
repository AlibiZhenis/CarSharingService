package carsharing;

public class Car implements Comparable<Car>{
    int id, companyID;
    String name;
    public Car(int id, int companyID, String name){
        this.id=id;
        this.companyID=companyID;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public int getCompanyID() {
        return companyID;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Car car) {
        if(id<car.getId()){
            return -1;
        } else if (id>car.getId()) {
            return 1;
        }
        return 0;
    }
}
