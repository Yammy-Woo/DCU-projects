import java.util.*;
import java.time.LocalDate;

/** @author Ho Yan Woo */

/** The enum that contains different types of activities */
enum ActivityType {
    /** CreateActivity */
    CREATE,
    /** DeleteActivity */
    DELETE,
    /** LikeActivity */
    LIKE,
    /** ShareActivity */
    SHARE,
    /** FollowActivity */
    FOLLOW,
    /** UnfollowActivity */
    UNFOLLOW,
}

/** The Activity interface */
interface Activity<T> {
    /**
     * Method to get the URI of the activity
     * 
     * @return A string representing the URI
     */
    String getURI();

    /**
     * Method to get the author of the activity i.e. the user who created it
     * 
     * @return A Person representing the author of the activity
     */
    Person getAuthor();

    /**
     * Method to get the corresponding object(StreamObject or Person) of the
     * activity (e.g. a post or a user)
     * 
     * @return A StreamObject representing the object embedded within the activity
     */
    T getObject();

    /**
     * Method to get the audience of the activity i.e. the user(s) who will receive
     * it
     * 
     * @return A list of Person representing the recipients of the activity
     */
    List<Person> getAudience();

    /**
     * Method to get the type of the activity
     * 
     * @return the enum type of the Activity
     */
    ActivityType getType();
}

/** Represents that the author has created and posted a stream object */
class CreateActivity implements Activity<StreamObject> {
    /** The unique global identifiers */
    String URI;
    /** The user who created the activity */
    Person author;
    /** The object embedded within the activity */
    StreamObject streamObject;
    /**
     * The user who will recieve the activity, which is a copy of the stream
     * object's audience
     */
    List<Person> audience;
    /** The type of the activity, CREATE */
    final static ActivityType type = ActivityType.CREATE;

    /**
     * Constructor of CreateActivity with parameters StreamObject streamObject and
     * Person author
     * 
     * @param streamObject The object embedded within the activity
     * @param author       The user who created the activity
     */
    CreateActivity(StreamObject streamObject, Person author) {
        this.URI = author.getURI() + "/activity" + author.getActivitycount() + "/";
        author.setActivitycount(); // Increment the number of activities of the author
        this.author = author;
        this.streamObject = streamObject;
        this.audience = this.streamObject.getAudience(); // The audience is set to a copy of the stream object's
                                                         // audience
        /* Call send() in Outbox to send it to outbox */
        this.author.getOutbox().send(this);
    }

    public String toString() {
        return this.streamObject.toString();
    }

    /* Getters */
    public String getURI() {
        return this.URI;
    }

    public Person getAuthor() {
        return this.author;
    }

    public StreamObject getObject() {
        return this.streamObject;
    }

    public List<Person> getAudience() {
        return this.audience;
    }

    public ActivityType getType() {
        return type;
    }
}

/** Represents that the author has deleted a stream object */
class DeleteActivity implements Activity<StreamObject> {
    /** The unique global identifiers */
    String URI;
    /** The user who created the activity */
    Person author;
    /** The object embedded within the activity */
    StreamObject streamObject;
    /** The user who will recieve the activity (There is none in DeleteActivity) */
    List<Person> audience = null;
    /** The type of the activity, DELETE */
    final static ActivityType type = ActivityType.DELETE;

    /**
     * Constructor of DeleteActivity with parameters StreamObject streamObject and
     * Person author
     * 
     * @param streamObject The object embedded within the activity
     * @param author       The user who created the activity
     */
    DeleteActivity(StreamObject streamObject, Person author) {
        this.URI = author.getURI() + "activity" + author.getActivitycount() + "/";
        author.setActivitycount(); // Increment the number of activities of the author
        this.author = author;
        this.streamObject = streamObject;
        this.streamObject.delete();
    }

    public String toString() {
        String output = "";
        output += this.streamObject.toString();
        return output;
    }

    /* Getters */
    public String getURI() {
        return this.URI;
    }

    public Person getAuthor() {
        return this.author;
    }

    public StreamObject getObject() {
        return this.streamObject;
    }

    public List<Person> getAudience() {
        return this.audience;
    }

    public ActivityType getType() {
        return type;
    }
}

/** Represents that the author has liked a stream object */
class LikeActivity implements Activity<StreamObject> {
    /** The unique global identifiers */
    String URI;
    /** The user who created the activity */
    Person author;
    /** The object embedded within the activity */
    StreamObject streamObject;
    /**
     * The user who will recieve the activity, that is, the author of streamObject
     */
    List<Person> audience;
    /** The type of the activity, LIKE */
    final static ActivityType type = ActivityType.LIKE;

    /**
     * Constructor of LikeActivity with parameters StreamObject streamObject and
     * Person author
     * 
     * @param streamObject The object embedded within the activity
     * @param author       The user who created the activity
     */
    LikeActivity(StreamObject streamObject, Person author) {
        this.URI = author.getName() + "/activity" + author.getActivitycount() + "/";
        author.setActivitycount(); // Increment the number of activities of the author
        this.author = author;
        this.streamObject = streamObject;
        this.audience = new ArrayList<Person>();
        this.audience.add(this.streamObject.getAttributed());
        author.setLiked(this);
        /* Call send() in Outbox to send it to outbox */
        this.author.getOutbox().send(this);
    }

    public String toString() {
        String output = this.author.getName() + " liked the following post:\n";
        output += this.streamObject.toString();
        return output;
    }

    /* Getters */
    public String getURI() {
        return this.URI;
    }

    public Person getAuthor() {
        return this.author;
    }

    public StreamObject getObject() {
        return this.streamObject;
    }

    public List<Person> getAudience() {
        return this.audience;
    }

    public ActivityType getType() {
        return type;
    }
}

/** Represents that the author has shared a stream object */
class ShareActivity implements Activity<StreamObject> {
    /** The unique global identifiers */
    String URI;
    /** The user who created the activity */
    Person author;
    /** The object embedded within the activity */
    StreamObject streamObject;
    /**
     * The user who will recieve the activity, including the followers of this
     * author and the author of the stream object
     */
    List<Person> audience;
    /** The type of the activity, SHARE */
    static final ActivityType type = ActivityType.SHARE;

    /**
     * Constructor of ShareActivity with parameters StreamObject streamObject and
     * Person author
     * 
     * @param streamObject The object embedded within the activity
     * @param author       The user who created the activity
     */
    ShareActivity(StreamObject streamObject, Person author) {
        this.URI = author.getURI() + "/activity" + author.getActivitycount() + "/";
        author.setActivitycount(); // Increment the number of activities of the author
        this.author = author;
        this.streamObject = streamObject;

        this.audience = this.author.getFollowers();
        Person attributed = this.streamObject.getAttributed();
        if (!audience.contains(attributed)) {
            this.audience.add(attributed);
        }

        /* Call send() in Outbox to send it to outbox */
        this.author.getOutbox().send(this);
    }

    public String toString() {
        String output = this.author.getUsername() + " shared a post\n";
        output += "URI: " + this.streamObject.getURI() + "\n";
        output += "Post:\n<" + this.streamObject + ">";
        return output;
    }

    /* Getters */
    public String getURI() {
        return this.URI;
    }

    public Person getAuthor() {
        return this.author;
    }

    public StreamObject getObject() {
        return this.streamObject;
    }

    public List<Person> getAudience() {
        return this.audience;
    }

    public ActivityType getType() {
        return type;
    }
}

/** Represents that the author has followed an actor */
class FollowActivity implements Activity<Person> {
    /** The unique global identifiers */
    String URI;
    /** The user who created the activity */
    Person author;
    /** The user being followed */
    Person target;
    /** The user who will recieve the activity, that is, the user being followed */
    List<Person> audience;
    /** The type of the activity, FOLLOW */
    final static ActivityType type = ActivityType.FOLLOW;

    /**
     * Constructor of FollowActivity with parameters Person target and Person author
     */
    /**
     * @param target The user being followed
     * @param author The user who created the activity
     */
    FollowActivity(Person target, Person author) {
        this.URI = author.getURI() + "/activity" + author.getActivitycount() + "/";
        author.setActivitycount(); // Increment the number of activities of the author
        this.author = author;
        this.target = target;
        this.audience = new ArrayList<Person>();
        this.audience.add(this.target);
        author.setFollowing(target);
        /* Call send() in Outbox to send it to outbox */
        this.author.getOutbox().send(this);
    }

    public String toString() {
        return this.author.getUsername() + " followed " + this.target.getUsername();
    }

    /* Getters */
    public String getURI() {
        return this.URI;
    }

    public Person getAuthor() {
        return this.author;
    }

    public Person getObject() {
        return this.target;
    }

    public List<Person> getAudience() {
        return this.audience;
    }

    public ActivityType getType() {
        return type;
    }
}

/** Represents that the author has unfollowed an actor */
class UnfollowActivity implements Activity<Person> {
    /** The unique global identifiers */
    String URI;
    /** The user who created the activity */
    Person author;
    /** The user being unfollowed */
    Person target;
    /**
     * The user who will recieve the activity, that is, the user being unfollowed
     */
    List<Person> audience;
    /** The type of the activity, UNFOLLOW */
    final static ActivityType type = ActivityType.UNFOLLOW;

    /**
     * Constructor of UnfollowActivity with parameters Person target and Person
     * author
     * 
     * @param target The user being unfollowed
     * @param author The user who created the activity
     */
    UnfollowActivity(Person target, Person author) {
        this.URI = author.getURI() + "/activity" + author.getActivitycount() + "/";
        author.setActivitycount(); // Increment the number of activities of the author
        this.author = author;
        this.target = target;
        this.audience = new ArrayList<Person>();
        this.audience.add(this.target);
        author.setFollowing(target);
        /* Call send() in Outbox to send it to outbox */
        this.author.getOutbox().send(this);
    }

    public String toString() {
        return this.author.getUsername() + " unfollowed " + this.target.getUsername();
    }

    /* Getters */
    public String getURI() {
        return this.URI;
    }

    public Person getAuthor() {
        return this.author;
    }

    public Person getObject() {
        return this.target;
    }

    public List<Person> getAudience() {
        return this.audience;
    }

    public ActivityType getType() {
        return type;
    }
}

/** Receives a message and adds it to the Inbox */
interface ReceiveMessage {
    /**
     * Receives a message and adds it to the Inbox then returns a success / failure
     * message
     * 
     * @param activity The message received
     * @return A success / failure message
     */
    boolean receive(Activity<?> activity);
}

/** Removes and retrieves the next message from inbox */
interface ReadNextMessage {
    /**
     * Removes and retrieves the next message from inbox
     * 
     * @return An Activity, or null if there are no messages
     * @see Inbox#readNext()
     */
    Activity<?> readNext();
}

/** Provides inbox functionality */
class Inbox implements ReceiveMessage, ReadNextMessage {
    /** A queue containing received messages */
    private Queue<Activity<?>> inbox;
    /** The user that this inbox belongs to */
    private Person user;

    /**
     * Constructor of Inbox with parameter Person user
     * 
     * @param user The user that this inbox belongs to
     */
    Inbox(Person user) {
        this.inbox = new LinkedList<Activity<?>>();
        this.user = user;
    }

    /**
     * Receives a message and adds it to the Inbox
     * 
     * @return A success / failure message
     */
    public boolean receive(Activity<?> activity) {
        this.inbox.add(activity);
        return this.inbox.contains(activity);
    }

    /**
     * Removes and retrieves the next message from inbox
     * 
     * @return An Activity, or null if there are no messages
     */
    public Activity<?> readNext() {
        if (this.inbox.isEmpty()) {
            return null;
        }
        return this.inbox.remove();
    }

    /**
     * Read and display the next message in Inbox
     * 
     * @return An Activity, or null if there are no messages
     */
    Activity<?> read() {
        Activity<?> inboxActivity = this.readNext();
        if (inboxActivity == null) { // Check if the retrieval succeeded
            return null;
        }

        /* Perform respective action for the read activity */
        switch (inboxActivity.getType()) {
            case LIKE:
                StreamObject likeObject = (StreamObject) inboxActivity.getObject();
                likeObject.setLikes(inboxActivity.getAuthor());
                break;
            case SHARE:
                StreamObject shareObject = (StreamObject) inboxActivity.getObject();
                if (this.user.equals(shareObject.getAttributed())) {
                    shareObject.setShares(inboxActivity.getAuthor());
                }
                break;
            case FOLLOW:
            case UNFOLLOW:
                Person follower = (Person) inboxActivity.getAuthor();
                Person followObject = (Person) inboxActivity.getObject();
                followObject.setFollowers(follower);
                break;
            default:
                break;
        }

        return inboxActivity;
    }

    /**
     * Return the count
     * 
     * @return The count of unread messages in inbox
     */
    public int getCount() {
        return this.inbox.size();
    }
}

/** Sends a message and adds it to the Outbox */
interface SendMessage {
    /**
     * Sends a message and adds it to the Outbox
     * 
     * @param message The message to be sent
     * @return A success / failure message
     */
    boolean send(Activity<?> message);
}

/** Removes and delivers the next message from Outbox */
interface DeliverNextMessage {
    /**
     * Removes and delivers the next message from Outbox
     * 
     * @return an Activity, or null if there are no messages
     */
    Activity<?> deliverNext();
}

/** Provides outbox functionality */
class Outbox implements SendMessage, DeliverNextMessage {
    /** A queue containing sent messages */
    private Queue<Activity<?>> outbox;
    /** The user that this outbox belongs to */
    private Person user;

    /**
     * Constructor of Outbox with parameter Person user
     * 
     * @param user The user that this inbox belongs to
     */
    Outbox(Person user) {
        this.outbox = new LinkedList<Activity<?>>();
        this.user = user;
    }

    /** Sends a message and adds it to the Outbox */
    /** @return A success / failure message */
    public boolean send(Activity<?> message) {
        this.outbox.add(message);
        if (!this.outbox.contains(message)) {
            return false;
        }
        return true;
    }

    /** Removes and delivers the next message from Outbox */
    /** @return An Activity, or null if there are no messages */
    public Activity<?> deliverNext() {
        if (this.outbox.isEmpty()) {
            return null;
        }
        return this.outbox.remove();
    }

    /** Deliver the next draft in Outbox to the selected audience */
    /** @return An Activity, or null if there are no messages */
    Activity<?> delivery() {
        Activity<?> outboxActivity = this.deliverNext(); // Get draft from Outbox
        if (outboxActivity == null) { // Check if the retrieval succeeded
            return null;
        }

        /* Deliver the message to the audience's inboxes */
        List<Person> audience = outboxActivity.getAudience(); // Retrieve a list of audience from the activity
        for (Person target : audience) {
            if (!target.equals(this.user)) {
                target.getInbox().receive(outboxActivity);
            }
        }

        return outboxActivity;
    }

    /** @return The count of unsent messages in outbox */
    public int getCount() {
        return this.outbox.size();
    }
}

/** Represents an actor/user */
class Person {
    /** The unique global identifiers */
    private String URI;
    /** The name of the user */
    private String name;
    /** The username of the user, must be unique */
    private String preferredUsername;
    /** The paragraph of summary/introduction by the user */
    private String summary;
    /** The inbox of the user */
    private Inbox inbox;
    /** The outbox of the user */
    private Outbox outbox;
    /** The list of users following the user */
    private List<Person> followers;
    /** The list of users followed by the user */
    private List<Person> following;
    /** The list of stream objects liked by the user */
    private List<StreamObject> liked;
    /**
     * The count of posts the user has made, used to assign URI for StreamObjects
     * created by the user
     */
    private int postCount = 1;
    /**
     * The count of activities the user has made, used to assign URI for Activities
     * created by the user
     */
    private int activityCount = 1;

    /**
     * Constructor of Person
     * 
     * @param name The name of the user
     */
    Person(String name) {
        this.URI = ClientApp.baseURI + name + "/";
        this.name = name;
        this.preferredUsername = name; // Set username to name by default
        this.summary = null;
        this.inbox = new Inbox(this);
        this.outbox = new Outbox(this);
        this.followers = new ArrayList<Person>();
        this.following = new ArrayList<Person>();
        this.liked = new ArrayList<StreamObject>();
    }

    /**
     * Constructor of Person
     * 
     * @param name              The name of the user
     * @param preferredUsername The username of the user
     * @param summary           The summary text of the user
     */
    Person(String name, String preferredUsername, String summary) {
        this.URI = ClientApp.baseURI + name + "/";
        this.name = name;
        this.preferredUsername = preferredUsername;
        this.summary = summary;
        this.inbox = new Inbox(this);
        this.outbox = new Outbox(this);
        this.followers = new ArrayList<Person>();
        this.following = new ArrayList<Person>();
        this.liked = new ArrayList<StreamObject>();
    }

    public String toString() {
        String output = "";
        output += "URI: " + this.URI + "\n";
        output += "Username: " + this.name + "\n";
        output += "Summary: " + this.summary;
        return output;
    }

    /* Getters */
    /** @return String URI */
    public String getURI() {
        return this.URI;
    }

    /** @return String name */
    public String getName() {
        return this.name;
    }

    /** @return String preferredUsername */
    public String getUsername() {
        return this.preferredUsername;
    }

    /** @return String summary */
    public String getSummary() {
        return this.summary;
    }

    /** @return Inbox inbox */
    public Inbox getInbox() {
        return this.inbox;
    }

    /** @return Outbox outbox */
    public Outbox getOutbox() {
        return this.outbox;
    }

    /** @return List of Person followers */
    public List<Person> getFollowers() {
        return this.followers;
    }

    /** @return List of Person following */
    public List<Person> getFollowing() {
        return this.following;
    }

    /** @return List of StreamObject liked */
    public List<StreamObject> getLiked() {
        return this.liked;
    }

    /** @return int postCount */
    public int getPostcount() {
        return postCount;
    }

    /** @return int activityCount */
    public int getActivitycount() {
        return activityCount;
    }

    /**
     * @param postid The count of the post which is part of the URI of the stream
     *               object
     *               (URI of a stream object is defined by: attributedTo.getURI() +
     *               "post" + attributedTo.getPostcount() + "/")
     * @return StreamObject The stream object(post) with the given postid as part of
     *         its URI
     */
    public StreamObject getPost(int postid) {
        return Database.getPost(this.URI + "post" + postid + "/");
    }

    /* Setters */
    /**
     * Update the follower list. Add the given user into the follower list if they
     * are not in the follower list or remove them if they are already in the list
     */
    /** @param follower A user who requested to follow or unfollow this user */
    public void setFollowers(Person follower) {
        // If the user is not in the follower list
        if (!this.followers.contains(follower)) {
            this.followers.add(follower); // Add the user into the follower list
        }
        // If the user is not in the follower list
        else {
            this.followers.remove(follower); // Remove the user from the follower list
        }
    }

    /**
     * Update the following list. Add the given user into the following list if they
     * are not in the following list or remove them if they are already in the list
     */
    /** @param following A user who this user wants to follow or unfollow */
    public void setFollowing(Person following) {
        // If the user is not in the following list
        if (!this.following.contains(following)) {
            this.following.add(following); // Add the user into the following list
        }
        // If the user is not in the following list
        else {
            this.following.remove(following); // Remove the user from the following list
        }
    }

    /** Add new liked stream object into the liked list */
    /** @param liked A LikeActivity */
    public void setLiked(LikeActivity liked) {
        this.liked.add(liked.getObject()); // Add the stream object associated with the LikeActivity into the liked list
    }

    /** Increment the count of posts made by the user */
    public void setPostcount() {
        postCount++;
    }

    /** Increment the count of activities made by the user */
    public void setActivitycount() {
        activityCount++;
    }
}

/** Represents an object, which can be a post or a message */
class StreamObject {
    /** The unique global identifiers */
    private String URI;
    /** The url of the attachment */
    private String attachment;
    /** The author of the stream object */
    private Person attributedTo;
    /** The list of recipients */
    private List<Person> audience;
    /** The list of users who likes the stream object */
    private List<Person> likes;
    /** The list of users who shares the stream object */
    private List<Person> shares;
    /** The content text of the stream object */
    private String content;
    /** The name/title of the stream object */
    private String name;
    /** The publish date of the stream object */
    private LocalDate published;
    /**
     * The delete date of the stream object (null until the stream object is
     * deleted)
     */
    private LocalDate deleted;

    /**
     * Constructor whith specified audience
     * 
     * @param attachment   The url of the attachment
     * @param attributedTo The author of the stream object
     * @param audience     The list of recipients
     * @param name         The name/title of the stream object
     * @param content      The content text of the stream object
     */
    StreamObject(String attachment, Person attributedTo, List<Person> audience, String name, String content) {
        this.URI = attributedTo.getURI() + "post" + attributedTo.getPostcount() + "/";
        attributedTo.setPostcount(); // Increment the number of posts of the author
        this.attachment = attachment;
        this.attributedTo = attributedTo;
        this.audience = audience;
        this.likes = new ArrayList<Person>();
        this.shares = new ArrayList<Person>();
        this.content = content;
        this.name = name;
        this.published = LocalDate.now();
        this.deleted = null;
    }

    /**
     * Constructor with unspecified audience (Audience will be set to the author's
     * followers)
     * 
     * @param attachment   The url of the attachment
     * @param attributedTo The author of the stream object
     * @param name         The name/title of the stream object
     * @param content      The content text of the stream object
     */
    StreamObject(String attachment, Person attributedTo, String name, String content) {
        this.URI = attributedTo.getURI() + "post" + attributedTo.getPostcount() + "/";
        attributedTo.setPostcount(); // Increment the number of posts of the author
        this.attachment = attachment;
        this.attributedTo = attributedTo;
        this.audience = attributedTo.getFollowers();
        this.likes = new ArrayList<Person>();
        this.shares = new ArrayList<Person>();
        this.content = content;
        this.name = name;
        this.published = LocalDate.now();
        this.deleted = null;
    }

    /**
     * Method to delete a post by setting all fields (except published) to null and
     * set the delete datetime to the current datetime
     */
    public void delete() {
        this.attachment = null;
        this.attributedTo = null;
        this.audience = null;
        this.likes = null;
        this.shares = null;
        this.content = null;
        this.name = null;
        this.setDeleted(); // Set deleted to the current datetime
    }

    public String toString() {
        String output = "";
        if (this.deleted != null) {
            output += "This stream object is deleted\n";
            output += "URI: " + this.URI + "\n";
            output += "Delete date: " + this.deleted;
            return output;
        }
        output += "URI: " + this.URI + "\n";
        output += "Name: " + this.name + "\n";
        output += "Content: " + this.content + "\n";

        List<String> temp = new ArrayList<>();
        for (Person p : this.audience) {
            temp.add(p.getUsername());
        }
        output += "Audience: " + temp + "\n";

        output += "Publish date: " + this.published + "\n";
        output += "Delete date: " + this.deleted + "\n";

        temp = new ArrayList<>();
        for (Person p : this.likes) {
            temp.add(p.getUsername());
        }
        output += "Likes: " + temp + "\n";

        temp = new ArrayList<>();
        for (Person p : this.shares) {
            temp.add(p.getUsername());
        }
        output += "Shares: " + temp;
        return output;
    }

    /* Getters */
    /** @return String URI */
    public String getURI() {
        return this.URI;
    }

    /** @return String name */
    public String getName() {
        return this.name;
    }

    /** @return String attachment */
    public String getAttachment() {
        return this.attachment;
    }

    /** @return Person attributedTo */
    public Person getAttributed() {
        return this.attributedTo;
    }

    /** @return List of Person audience */
    public List<Person> getAudience() {
        return this.audience;
    }

    /** @return List of Person likes */
    public List<Person> getLikes() {
        return this.likes;
    }

    /** @return List of Person shares */
    public List<Person> getShares() {
        return this.shares;
    }

    /** @return LocalDate published */
    public LocalDate getPublished() {
        return this.published;
    }

    /** @return LocalDate deleted */
    public LocalDate getDeleted() {
        return this.deleted;
    }

    /* Setters */
    /** Add new user to the likes list */
    /** @param object A user who liked the stream object */
    public void setLikes(Person object) {
        this.likes.add(object);
    }

    /** Add new user to the shares list */
    /** @param object A user who shared the stream object */
    public void setShares(Person object) {
        this.shares.add(object);
    }

    /** Set deleted to the current datetime */
    public void setDeleted() {
        this.deleted = LocalDate.now();
    }
}

/** The client App that handles inboxes and outboxes */
interface App {
    /**
     * Retrieves the inbox
     * 
     * @return The current user's inbox
     */
    Inbox getInbox();

    /**
     * Retrieves the outbox
     * 
     * @return The current user's outbox
     */
    Outbox getOutbox();

    /**
     * Prints a demo of the app in action
     * 
     * @return A string of the demo of the app in action
     */
    String demo();
}

/** The client App used to present a demo of JavaSoc */
public class ClientApp implements App {
    /** The base URI */
    public final static String baseURI = "https://clientapp.com/";
    /** The current user account logged in */
    private Person currentUser;
    /** The inbox of the current account */
    private Inbox myInbox;
    /** The outbox of the current account */
    private Outbox myOutbox;

    /**
     * Retrieves the inbox
     * 
     * @return Inbox myInbox
     */
    public Inbox getInbox() {
        return this.myInbox;
    }

    /**
     * Retrieves the outbox
     * 
     * @return Outbox myOutbox
     */
    public Outbox getOutbox() {
        return this.myOutbox;
    }

    /*
     * Methods that call the responding methods in classes and display contents of
     * the demo
     */
    /**
     * Creates an user and set them to the current user, inbox and outbox
     * 
     * @param user the new Person object
     * @return boolean indicating whether the new user is created
     */
    boolean createUser(Person user) {
        /* Store user into the database if the username is not used yet */
        if (Database.getClients().contains(user)) {
            return false;
        }
        Database.setClients(user); // Create user
        /* Set current user, inbox and outbox */
        this.currentUser = user;
        this.myInbox = user.getInbox();
        this.myOutbox = user.getOutbox();

        return true;
    }

    /**
     * Changes the current user, inbox and outbox
     * 
     * @param username The username of the user
     * @return boolean indicating whether the user with the given username is found
     */
    boolean changeUser(String username) {
        /* Retrieve user if user with the given username exists */
        Person user = Database.getClient(username);
        if (user == null) {
            return false;
        }
        /* Set current user, inbox and outbox */
        this.currentUser = user;
        this.myInbox = user.getInbox();
        this.myOutbox = user.getOutbox();

        return true;
    }

    /**
     * Creates a CreateActivity
     * 
     * @param object The stream object being created
     * @return The created CreateActivity
     */
    Activity<StreamObject> createActivity(StreamObject object) {
        /* Create a CreateActivity */
        Activity<StreamObject> activity = new CreateActivity(object, this.currentUser);
        /* Add the new stream object to database */
        Database.setposts(object);

        return activity;
    }

    /**
     * Creates a DeleteActivity
     * 
     * @param object The stream object being deleted
     * @return The created DeleteActivity
     */
    Activity<StreamObject> deleteActivity(StreamObject object) {
        /* Create a DeleteActivity */
        DeleteActivity activity = new DeleteActivity(object, this.currentUser);

        return activity;
    }

    /**
     * Creates a LikeActivity
     * 
     * @param object The stream object being liked
     * @return The created LikeActivity
     */
    Activity<StreamObject> likeActivity(StreamObject object) {
        /* Create a LikeActivity */
        LikeActivity activity = new LikeActivity(object, this.currentUser);

        return activity;
    }

    /**
     * Creates a ShareActivity
     * 
     * @param object The stream object being shared
     * @return The created ShareActivity
     */
    Activity<StreamObject> shareActivity(StreamObject object) {
        /* Create a ShareActivity */
        ShareActivity activity = new ShareActivity(object, this.currentUser);

        return activity;
    }

    /**
     * Creates a FollowActivity
     * 
     * @param username The username of the user being followed
     * @return The created FollowActivity
     */
    Activity<Person> followActivity(String username) {
        /* Check if the user with the given username exists */
        Person object = Database.getClient(username);
        if (object == null) {
            return null;
        }
        /* Create a FollowActivity */
        FollowActivity activity = new FollowActivity(object, this.currentUser);

        return activity;
    }

    /**
     * Creates a UnfollowActivity
     * 
     * @param username The username of the user being unfollowed
     * @return The created UnfollowActivity
     */
    Activity<Person> unfollowActivity(String username) {
        /* Find the person object with the given username from the following list */
        Person object = null;
        for (Person following : this.currentUser.getFollowing()) {
            if (following.getUsername().equals(username)) {
                object = following;
                break;
            }
        }
        /* Return null if a person object with the username is not found */
        if (object == null) {
            return null;
        }
        /* Create the UnfollowActivity */
        UnfollowActivity activity = new UnfollowActivity(object, this.currentUser);

        return activity;
    }

    /**
     * Delivers the next draft in Outbox to its recipients
     * 
     * @return The next draft message in the outbox queue
     */
    Activity<?> deliverActivity() {
        /* Call Outbox delivery method */
        Activity<?> outboxActivity = this.myOutbox.delivery();
        if (outboxActivity == null) { // Check if the retrieval succeeded
            return null;
        }

        return outboxActivity;
    }

    /**
     * Reads and displays the next message in Inbox
     * 
     * @return The next activity in the inbox queue
     */
    Activity<?> readActivity() {
        Activity<?> inboxActivity = this.myInbox.read();
        if (inboxActivity == null) { // Check if the retrieval succeeded
            return null;
        }

        return inboxActivity;
    }

    /**
     * Check the list associated to the activity according to its type (e.g. LIKE,
     * FOLLOW, SHARE...)
     * 
     * @param activity The activity which is checked
     * @return A string of the list associated to the activity returned by other
     *         check functions (e.g. checkLikes, checkShares...)
     */
    public String checkActivity(Activity<?> activity) {
        /* Check the respective list */
        switch (activity.getType()) {
            case LIKE:
                if (!(activity.getObject() instanceof StreamObject)) {
                    return "ClassCastException";
                }
                return this.checkLikes((StreamObject) activity.getObject());

            case FOLLOW:
            case UNFOLLOW:
                if (!(activity.getObject() instanceof Person)) {
                    return "ClassCastException";
                }
                return this.checkFollowers();

            case SHARE:
                if (!(activity.getObject() instanceof StreamObject)) {
                    return "ClassCastException";
                }
                StreamObject object = (StreamObject) activity.getObject();
                if (this.currentUser.equals(object.getAttributed())) {
                    return this.checkShares(object);
                }

            default:
                return "";
        }
    }

    /**
     * Returns a string of the list of users who liked the stream object
     * 
     * @param likeObject The stream object being checked
     * @return A string of the list of users who liked the stream object
     */
    public String checkLikes(StreamObject likeObject) {
        String output = "";
        output += "Updated likes list:\n";
        int i = 1;
        for (Person p : likeObject.getLikes()) {
            output += i + ".\n";
            output += "<" + p + ">\n";
            i++;
        }
        if (i == 1) {
            output += "None\n";
        }
        return output;
    }

    /**
     * Returns a string of the list of users who shared the stream object
     * 
     * @param shareObject The stream object being checked
     * @return A string of the list of users who shared the stream object
     */
    public String checkShares(StreamObject shareObject) {
        String output = "";
        output += "Updated shares list:\n";
        int i = 1;
        for (Person p : shareObject.getShares()) {
            output += i + ".\n";
            output += "<" + p + ">\n";
            i++;
        }
        if (i == 1) {
            output += "None\n";
        }
        return output;
    }

    /**
     * Returns a string of the list of the current user's liked objects
     * 
     * @return A string of the list of the current user's liked objects
     */
    public String checkLiked() {
        String output = "";
        output += "Updated liked list:\n";
        int i = 1;
        for (StreamObject o : this.currentUser.getLiked()) {
            output += i + ".\n";
            output += "<" + o + ">\n";
            i++;
        }
        if (i == 1) {
            output += "None\n";
        }
        return output;
    }

    /**
     * Returns a string of the list of the current user's followers
     * 
     * @return A string of the list of the current user's followers
     */
    public String checkFollowers() {
        String output = "";
        output += "Updated follower list:\n";
        int i = 1;
        for (Person p : this.currentUser.getFollowers()) {
            output += i + ".\n";
            output += "<" + p + ">\n";
            i++;
        }
        if (i == 1) {
            output += "None\n";
        }
        return output;
    }

    /**
     * Returns a string of the list of the users followed by the current user
     * 
     * @return A string of the list of the users followed by the current user
     */
    public String checkFollowing() {
        String output = "";
        output += "Updated following list:\n";
        int i = 1;
        for (Person p : this.currentUser.getFollowing()) {
            output += i + ".\n";
            output += "<" + p + ">\n";
            i++;
        }
        if (i == 1) {
            output += "None\n";
        }
        return output;
    }

    /**
     * Prints a demo of the app in action
     * 
     * @return A string of the demo of the app in action
     */
    public String demo() {
        String output = "";
        Activity<?> activity = null;

        /* Create Three users, Tom, Paul and Jimmy, and set current user to Jimmy */
        if (!this.createUser(new Person("Tommy", "Tom", "Hello World!"))) {
            output += "Account already exists!\n";
            return null;
        }
        ;
        output += this.currentUser.getName() + " created a new account\n" + this.currentUser.toString() + "\n";
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "\n";

        if (!this.createUser(new Person("Paul"))) {
            output += "Account already exists!\n";
            return null;
        }
        ;
        output += this.currentUser.getName() + " created a new account\n" + this.currentUser.toString() + "\n";
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "\n";

        if (!this.createUser(new Person("Jimmy"))) {
            output += "Account already exists!\n";
            return null;
        }
        ;
        output += this.currentUser.getName() + " created a new account\n" + this.currentUser.toString() + "\n";
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "\n";

        /* Jimmy creates a FollowActivity to Tom and sends it to Outbox */
        this.followActivity("Tom");
        output += this.currentUser.getUsername() + " adds a FollowActivity to Outbox\n";
        /* Check following list */
        output += this.checkFollowing();
        output += "\n";

        /* Jimmy delivers the FollowActivity in Outbox */
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts in outbox not yet
                                                                          // delivered
        activity = this.deliverActivity();
        output += this.currentUser.getUsername() + " Outbox delivery\n";
        output += activity.toString() + "\n";
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts should decrease by 1 after
                                                                          // delivering the activity
        output += "\n";

        /* Change current user to Paul */
        if (!this.changeUser("Paul")) {
            output += "Account doesn't exist!\n";
        }
        ;
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "You have " + this.myInbox.getCount() + " unread messages.\n";
        output += "\n";

        /* Paul creates a FollowActivity to Jimmy and sends it to Outbox */
        this.followActivity("Jimmy");
        output += this.currentUser.getUsername() + " adds a FollowActivity to Outbox\n";
        /* Check following list */
        output += this.checkFollowing();
        output += "\n";

        /* Paul delivers the FollowActivity in Outbox */
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts in outbox not yet
                                                                          // delivered
        activity = this.deliverActivity();
        output += this.currentUser.getUsername() + " Outbox delivery\n";
        output += activity.toString() + "\n";
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts should decrease by 1 after
                                                                          // delivering the activity
        output += "\n";

        /* Change current user to Tom */
        if (!this.changeUser("Tom")) {
            output += "Account doesn't exist!\n";
        }
        ;
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "You have " + this.myInbox.getCount() + " unread messages.\n";
        output += "\n";

        /* Tom reads message from Inbox */
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message in inbox
        for (int i = 1; this.myInbox.getCount() > 0; i++) {
            output += "Message " + i + " --------------------------------\n";
            activity = this.readActivity();
            output += activity.toString() + "\n"; // Display the message
            output += this.checkActivity(activity);
        }
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message should
                                                                                // decrease by 1 after reading
        output += "\n";

        /* Tom creates a stream object */
        StreamObject object = new StreamObject("https://example.com/", currentUser, "First Post", "Hi everyone");

        /* Tom creates a CreateActivity and sends to Outbox */
        activity = this.createActivity(object);
        output += this.currentUser.getUsername() + " created a CreateActivity to Outbox\n";
        output += activity.toString() + "\n";
        output += "\n";

        /* Tom delivers the CreateActivity in Outbox */
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts in outbox not yet
                                                                          // delivered
        activity = this.deliverActivity();
        output += this.currentUser.getUsername() + " Outbox delivery\n";
        output += activity.toString() + "\n";
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts should decrease by 1 after
                                                                          // delivering the activity
        output += "\n";

        /* Change current user to Jimmy */
        if (!this.changeUser("Jimmy")) {
            output += "Account doesn't exist!\n";
        }
        ;
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "You have " + this.myInbox.getCount() + " unread messages.\n";
        output += "\n";

        /* Jimmy reads message from Inbox */
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message in inbox
        for (int i = 1; this.myInbox.getCount() > 0; i++) {
            output += "Message " + i + " --------------------------------\n";
            activity = this.readActivity();
            output += activity.toString() + "\n"; // Display the message
            output += this.checkActivity(activity);
        }
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message should
                                                                                // decrease by 1 after reading
        output += "\n";

        /* Jimmy creates a LikeActivity to Tom's post and sends to Outbox */
        if (!(activity.getObject() instanceof StreamObject)) {
            output += "ClassCastException\n";
            return null;
        }
        object = (StreamObject) activity.getObject();
        this.likeActivity(object);
        output += this.currentUser.getUsername() + " adds a LikeActivity to Outbox\n";
        /* Check the user's liked list */
        output += this.checkLiked();
        output += "\n";

        /* Jimmy creates a ShareActivity to Tom's post and sends to Outbox */
        if (!(activity.getObject() instanceof StreamObject)) {
            output += "ClassCastException\n";
            return null;
        }
        object = (StreamObject) activity.getObject();
        this.shareActivity(object);
        output += this.currentUser.getUsername() + " adds a ShareActivity to Outbox\n";
        output += "\n";

        /* Jimmy delivers the activities in Outbox */
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts in outbox not yet
                                                                          // delivered
        while (this.myOutbox.getCount() > 0) {
            activity = this.deliverActivity();
            output += this.currentUser.getUsername() + " Outbox delivery" + "\n";
            output += activity.toString() + "\n";
        }
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts should decrease by 1 after
                                                                          // delivering the activity
        output += "\n";

        /* Change current user to Paul */
        if (!this.changeUser("Paul")) {
            output += "Account doesn't exist!\n";
        }
        ;
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "You have " + this.myInbox.getCount() + " unread messages.\n";
        output += "\n";

        /* Paul reads message from Inbox */
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message in inbox
        for (int i = 1; this.myInbox.getCount() > 0; i++) {
            output += "Message " + i + " --------------------------------\n";
            activity = this.readActivity();
            output += activity.toString() + "\n"; // Display the message
            output += this.checkActivity(activity);
        }
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message should
                                                                                // decrease by 1 after reading
        output += "\n";

        /* Paul creates a stream object with Tom as the recipient */
        List<Person> audience = new ArrayList<Person>();
        audience.add(Database.getClient("Tom"));
        object = new StreamObject(null, currentUser, audience, "Hi Tom", "Are you Tommy?");
        output += "\n";

        /* Paul creates a CreateActivity and sends to Outbox */
        this.createActivity(object);
        output += this.currentUser.getUsername() + " created a CreateActivity to Outbox\n";
        /* Check the created object */
        output += object + "\n";
        output += "\n";

        /* Paul delivers the CreateActivity in Outbox */
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts in outbox not yet
                                                                          // delivered
        activity = this.deliverActivity();
        output += this.currentUser.getUsername() + " Outbox delivery\n";
        output += activity.toString() + "\n";
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts should decrease by 1 after
                                                                          // delivering the activity
        output += "\n";

        /* Change current user to Tom */
        if (!this.changeUser("Tom")) {
            output += "Account doesn't exist!\n";
        }
        ;
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "You have " + this.myInbox.getCount() + " unread messages.\n";
        output += "\n";

        /* Tom reads message from Inbox */
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message in inbox
        for (int i = 1; this.myInbox.getCount() > 0; i++) {
            output += "Message " + i + " --------------------------------\n";
            activity = this.readActivity();
            output += activity.toString() + "\n"; // Display the message
            output += this.checkActivity(activity);
        }
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message should
                                                                                // decrease by 1 after reading
        output += "\n";

        /* Tom deletes a post */
        object = this.currentUser.getPost(1);
        if (object == null) {
            output += "Null object returned\n";
            return null;
        }
        /* Check the stream object before deletion */
        output += this.currentUser.getUsername() + " deleted the below stream object:\n";
        output += object.toString() + "\n";
        /* Delete the stream object */
        this.deleteActivity(object);
        /* Check the deleted stream object */
        output += "The stream object after deletion:\n";
        output += object.toString() + "\n";
        output += "\n";

        /* Change current user to Jimmy */
        if (!this.changeUser("Jimmy")) {
            output += "Account doesn't exist!\n";
        }
        ;
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "You have " + this.myInbox.getCount() + " unread messages.\n";
        output += "\n";

        /* Jimmy creates an UnfollowActivity to Tom and sends it to Outbox */
        if (this.unfollowActivity("Tom") == null) {
            output += "User not found\n";
        }
        ;
        output += this.currentUser.getUsername() + " adds a UnfollowActivity to Outbox\n";
        /* Check the following list */
        output += this.checkFollowing();
        output += "\n";

        /* Jimmy delivers the UnfollowActivity in Outbox */
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts in outbox not yet
                                                                          // delivered
        activity = this.deliverActivity();
        output += this.currentUser.getUsername() + " Outbox delivery\n";
        output += activity.toString() + "\n";
        output += "Drafts in Outbox: " + this.myOutbox.getCount() + "\n"; // Number of drafts should decrease by 1 after
                                                                          // delivering the activity
        output += "\n";

        /* Change current user to Tom */
        if (!this.changeUser("Tom")) {
            output += "Account doesn't exist!\n";
        }
        ;
        output += "Welcome, " + this.currentUser.getUsername() + ".\n";
        output += "You have " + this.myInbox.getCount() + " unread messages.\n";
        output += "\n";

        /* Tom reads message from Inbox */
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message in inbox
        for (int i = 1; this.myInbox.getCount() > 0; i++) {
            output += "Message " + i + " --------------------------------\n";
            activity = this.readActivity();
            output += activity.toString() + "\n"; // Display the message
            output += this.checkActivity(activity);
        }
        output += "Unread message in Inbox: " + this.myInbox.getCount() + "\n"; // Number of unread message should
                                                                                // decrease by 1 after reading

        return output;
    }
}

/** Simulate a database linked to the app */
class Database {
    /** List of Person created */
    private static List<Person> clients = new ArrayList<Person>();
    /** List of StreamObject created */
    private static List<StreamObject> posts = new ArrayList<StreamObject>();

    /**
     * Returns the clients list
     * 
     * @return List of Person clients
     */
    public static List<Person> getClients() {
        return clients;
    }

    /**
     * Updates the clients list with new user
     * 
     * @param client new Person object created
     */
    public static void setClients(Person client) {
        clients.add(client);
    }

    /**
     * Retrieves the client with the given username
     * 
     * @param username The username of the client
     * @return Person client with the given username or null if not found
     */
    public static Person getClient(String username) {
        for (Person client : clients) {
            if (client.getUsername().equals(username)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Returns the posts list
     * 
     * @return List of StreamObject posts
     */
    public static List<StreamObject> getPosts() {
        return posts;
    }

    /**
     * Updates the posts list with new post
     * 
     * @param post new StreamObject object created
     */
    public static void setposts(StreamObject post) {
        posts.add(post);
    }

    /**
     * Retrieves the post with the given URI
     * 
     * @param URI The URI of the post
     * @return post The post with the given URI or null if not found
     */
    public static StreamObject getPost(String URI) {
        for (StreamObject post : posts) {
            if (post.getURI().equals(URI)) {
                return post;
            }
        }
        return null;
    }
}