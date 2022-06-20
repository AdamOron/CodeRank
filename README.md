# CodeRank

As a final project for my Computer Science class, I was required to develop an app in Android Studio. This app could be anything that I choose, and naturally, my choice was some form of social media for programmers.  
My general idea was an app similar to LeetCode, where users can create and solve programming challenges. These challenges and their solutions could then be rated by other users on the platform.  

## Project Structure
### **The root of the source code will be `app/src/main/java/com/example/coderank/`. Any paths/packages that I mention are relative to this root path.**  
* Under `entry` you will find any class relevant to the entrance to the app (MainActivity - the entrypoint of the app, Login & Register activities, e.t.c).
  * This package also contains `entry/home`, which is responsible for the "home screen".  
* Under `post` you will find any class relevant to posts within the app. Generally speaking, there are two types of posts - challenges & submissions.
  * There are several types of challenges, all defined under `post/challenge`.
    * `post/challenge/normal` and `post/challenge/limited` define the two types of challenges.
    * Every challenge has a "Overview" screen and a "Submissions" screen, which are defined as Fragments under `post/challenge/fragments`.
  * All posts are rate-able and comment-able. These behaviors are defined in packages `post/rating` and `post/comment`.
* Under `user` you will find any class relevant to the users and their management.
* Under `sql` you will find any class relevant to the database of the app and its management. The database is an SQLite database which is saved locally.
* Under `syntax` you will find any class relevant to the Syntax Highlighting feature of the app, which is visible on submissions. All code in the app is tokenized and highlighted appropriately.
* Under `utils` you will find miscellaneous utilities for the app.

**If you want to explore the code on your own, `entry/MainActivity.java` would probably be the best place to start.**
