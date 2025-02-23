create table users(
    username text not null primary key,
    pwdhash text not null
);

create table whichCoursesHasUser(
    username text not null,
    cname text not null,
    primary key (username, cname)
);

create table courses(
    cname text not null primary key
);