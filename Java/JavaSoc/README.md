This assignment aims to implement a set of classes and methods to simulate communication between actors.
My work consists of classes including:
- Different types of Activities (CreateActivity, DeleteActivity, LikeActivity, ShareActivity, FollowActivity and UnfollowActivity)
- Stream object (StreamObject)
- Actor (Person)
- Inbox and Outbox

Highlights of my work:
I implemented all the required activity classes and an additional class for the SHARE activity.
I implemented classes for Inbox and Outbox using queues and methods to send and receive messages.
The corresponding data will change when activities are read, e.g. the follower list will be updated when a FollowActivity is received.
I implemented StreamObject class with all required fields.
I used ArrayLists to represent the list of Person who liked or shared the post.
I implemented a Person class with all required fields.
I used ArrayLists to represent the list of followers, following and liked posts.
The classes generate unique URIs using a combination of a base URI, the user name and the number of posts/activities created by the user.
I used JavaDoc comments to generate well-organized documentation for my code.

What it lacks or suffers:
I did not do enough research and planning before starting my work, so there might be some inaccurate or inefficient design.
For example, I changed Activity into a generic class halfway so that the getObject() can return either a Person or a StreamObject.
It ended up increasing quite a lot of workload to adjust the code for the change, and I'm not sure if it is a good idea to do so.

Future work:
- More Activities such as blocking users, accepting or rejecting follow requests
- Public and Private account
  Anyone can follow a public account without request and view posts from a public account. 
  Users can only follow a private account after the owner accepts their follow request. 
  Non-followers can not view the posts of a private account.
- More Stream object types
  The current StreamObject class is basically representing posts. 
  I would like to add a type "message", which represents inbox messages.
  Inbox messages are similar to posts but not visible to other users and may have different audiences.
  
Testing:
I implemented a series of methods which create corresponding objects and display output messages when called in demo().
They will display what objects are created and some updated fields, e.g. check the following list after creating a FollowActivity.
In the demo(), I call the methods to showcase different classes and methods of my work.
For example, createActivity() will create a CreateActivity object.
Also, I implemented a Database class to simulate a database and temporarily store the list of created users and posts so that I can retrieve them for testing more easily.

Sources:
I mainly learnt about ActivityPub through https://www.w3.org/TR/activitypub/ and project slides.
I finished all the work on my own.