A Course Application written with [webforj](https://webforj.com) for the builtwithwebforj contest.

# Manual

## Requirements

### Programms not needed on the server but for the admin
- sqlite3-tools

### Files
These files need to be created for the programm to work
- .../src/main/resources/jwt-signing-secret
in this file you should write a long random string for JWT Token Signing
- .../db.sqlite
create this file with
```bash
#your current folder is .../course-app
sqlite3 db.sqlite
# in the repl
.read schema.sql
.quit
```
this creates an empty database with all the needed tables

## Writing a course
For writing the course I recommend to create a new directory inside of /src/main/resources/courseData with the same name as the url for the course but the name could be anything. The use of spaces isn't recommended though.
Then write the Transcript files, one for each lesson, with Markdown. These will be displayed in a box under the video player. Its the best place for code snippets, links and a written explaination accompanying the lesson.
## Video
For videos the recommended approach is to encode the file as an mp4 with the "web-optimized" checkbox in "Handbrake" set
![Screenshot of the checkbox](/docs/Screenshot%202025-03-08%20200201.png). This will store the information about
the video at the front which will enable the video to be streamed with the html5 video tag using the browsers builtin hls implementation.
Storing the video on the Backend with the frontend is not recommended as storing it in the resources folder will baloon your .jar file sizes and compile/bundle sizes. Instead putting the video in a block storage like s3 and using the link to that file as the value of the video field in the courses.json allows the client to stream the video from a seperate server keeping the bandwidth available on the frontend server

## Starting the service
```bash
mvn jetty:run
```