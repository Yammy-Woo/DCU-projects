import java.time.LocalDate;
import java.time.Period;

enum State {
    TODO, BEGN, HALT, WAIT, DONE;
}

public class Task {
    // think why private is the WRONG choice here
    // instead, think what should be the access modifier
    private final String title;
    private String description;
    private LocalDate scheduled;
    private LocalDate deadline; // think why this isn't final
    private State state;

    Task(String title, State state) {
        // set title and state
        this.title = title;
        this.state = state;
    }
    
    Task(String title, State state, String description, LocalDate scheduled) {
        this.title = title;
        this.state = state;
        this.description = description;
        this.scheduled = scheduled;
    }

    public String toString() {
        String message = this.title + " (" + this.state + ")";
        if (scheduled != null) {
            message += " scheduled: " + scheduled;
        }
        if (deadline != null) {
            message += " deadline: " + deadline;
        }
        return message;
    }

    // Getter & Setters
    public String getTitle() {
        return title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getScheduled() {
        return scheduled;
    }

    public void setScheduled(LocalDate scheduled) {
        this.scheduled = scheduled;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public static void main(String[] args) {
        // test a simple Task object works correctly
        Task t1 = new Task("Task T1", State.TODO);
        System.out.println(t1);

        /* OUTPUT
        T1 (TODO)
        */

        Task s1 = new RepeatedTask("S1", State.TODO);
        System.out.println(s1);
        s1.setState(State.DONE);
        System.out.println(s1);

        /* OUTPUT
        S1 (TODO)
        S1 (TODO)
        */

        // Check Chores work correctly on DONE -> repeat
        // note s2 is Task but object is type Chore
        Task s2 = new Chore("S2", State.TODO,
            LocalDate.now(),
            LocalDate.now().plus(Period.ofDays(7)));
        System.out.println(s2);
        s2.setState(State.DONE);
        System.out.println(s2);
        // verify the scheduled date has moved by +7 days

        /* OUTPUT
        S2 (TODO) scheduled: 2023-01-26
        S2 (TODO) scheduled: 2023-02-02
        */

        Task t2 = new SharedTask("T2", "Alice");
        System.out.println(t2);

        /* OUTPUT
        T2 (WAIT) shared with: Alice
        */

        Task t3 = new Dependency("T3", State.TODO, t1);
        System.out.println(t3);
        t3.setState(State.DONE);
        System.out.println(t3);
        t1.setState(State.DONE);
        t3.setState(State.DONE);
        System.out.println(t3);

        /* OUTPUT
        T3 (TODO) dependent on: T1 (TODO)
        T3 (TODO) dependent on: T1 (TODO)
        T3 (DONE) dependent on: T1 (DONE)
        */
    }
}

class Chore extends Task {
    LocalDate repeat;

    Chore(String title, State state) {
        super(title, state);
    }

    Chore(String title, State state, LocalDate scheduled, LocalDate repeat) {
        super(title, state);
        setScheduled(scheduled);
        setRepeat(repeat);
    }

    public void setState(State state) {
        super.setState(state);
        if (state == State.DONE) {
            LocalDate repeat_new = repeat.plus(Period.ofDays(7));
            setScheduled(repeat);
            setRepeat(repeat_new);
            setState(State.TODO);
        }
    }

    public LocalDate getRepeat() {
        return repeat;
    }

    public void setRepeat(LocalDate repeat) {
        this.repeat = repeat;
    }
}

class RepeatedTask extends Task {

    RepeatedTask(String title, State state) {
        super(title, state);
    }

    public void setState(State state) {
        super.setState(state);
        if (state == State.DONE) {
            super.setState(State.TODO);
        }
    }
}

class SharedTask extends Task {
    String name;

    SharedTask(String title, String name) {
        super(title, State.WAIT);     
        setName(name);
    }

    public String toString() {
        return(getTitle() + " (" + getState() + ") shared with: " + name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}

class Dependency extends Task {
    Task task;

    Dependency(String title, State state) {
        super(title, state);
    }

    Dependency(String title, State state, Task task) {
        super(title, state);
        this.task = task;
    }

    public String toString() {
        return(getTitle() + " (" + getState() + ") dependent on: " + task);
    }

    public void setState(State state) {
        if (state == State.DONE) {
            if (task.getState() == State.DONE)
            {
                super.setState(state);
            }     
        }
        else{
            super.setState(state);
        }
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task){
        this.task = task;
    }
}