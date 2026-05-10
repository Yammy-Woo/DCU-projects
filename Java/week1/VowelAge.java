import java.util.Scanner;

public class VowelAge {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String name = input.nextLine();
        Integer age = input.nextInt();

        Integer count = 0;

        for(char s : name.toCharArray())
        {
            switch(s)
            {
                case 'a':
                    count++;
                    break;
                case 'e':
                    count++;
                    break;
                case 'i':
                    count++;
                    break;
                case 'o':
                    count++;
                    break;
                case 'u':
                    count++;
                    break;
                case 'A':
                    count++;
                    break;
                case 'E':
                    count++;
                    break;
                case 'I':
                    count++;
                    break;
                case 'O':
                    count++;
                    break;
                case 'U':
                    count++;
                    break;
            }
        }

        String adult;
        if(age < 18)
        {
            adult = "a minor";
        }
        else
        {
            adult = "an adult";
        }

        System.out.println("Hello " + name + ", you have " + count + " vowels, and you are " + adult);
    }
}