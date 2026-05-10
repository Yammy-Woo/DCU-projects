import java.util.Scanner;

public class AlarmClock {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Integer alarm_hour = input.nextInt();
        Integer alarm_min = input.nextInt();
        Integer alarm_time = alarm_hour * 60 + alarm_min;

        Integer current_hour, current_min, current_time;
        Integer count = 0;
        do
        {
            current_hour = input.nextInt();
            current_min = input.nextInt();
            current_time = current_hour * 60 + current_min;
            count++;
        } while(current_time < alarm_time);

        System.out.println("false alarms: " + (count - 1));
    }
}