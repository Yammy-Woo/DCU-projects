class Duck {
    private String colour;
    private Size size;
    private double cost;
    private String manufacturer_sign;

    Duck(String colour, Size size, double cost, String manufacturer_sign) {
        this.colour = colour;
        this.size = size;
        this.cost = cost;
        this.manufacturer_sign = manufacturer_sign;
    }

    public String getColour() {
        return colour;
    }
    public Size getSize() {
        return size;
    }
    public double getCost() {
        return cost;
    }
    public String getManufacturer_sign() {
        return manufacturer_sign;
    }
}