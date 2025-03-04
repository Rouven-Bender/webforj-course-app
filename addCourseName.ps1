# addCourseName (course name)
sqlite3 .\db.sqlite "insert into courses(cname) values ('$($args[0])')"