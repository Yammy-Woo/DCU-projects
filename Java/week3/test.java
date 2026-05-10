interface Message {
    String communicate(Message x);
}

class Test implements Message {
    int x;

    public Test(int x) {
        this.x = x;
    }

    public String communicate(Message t1) {
        return tostring(t1.x);
    }

    public static void main(String[] args) {
        Message t1 = new Test();
        System.out.println(t1.communicate());
    }
}