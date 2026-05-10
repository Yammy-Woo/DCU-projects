import java.util.Scanner;

public class Hello {
    String name = "";

    public Hello(String name) {
        this.name = name;
    }

    public static void sayHello() {
        Scanner input = new Scanner(System.in);
        String name = input.nextLine();
        System.out.println(name);
    }

    public String toString() {
        return "Hello, " + this.name;
    }
}