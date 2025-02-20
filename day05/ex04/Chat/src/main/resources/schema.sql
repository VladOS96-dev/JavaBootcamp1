drop schema if exists chat cascade;
create schema if not exists chat;
create table if not exists chat.user
( id serial primary key ,
  login varchar(20) not null unique,
  password varchar(20) not null
  );
create table if not exists chat.chatroom
( id serial primary key ,
  room_name varchar(20) not null unique,
  owner integer not null,
  foreign key(owner) references chat.user(id)
  );
create table if not exists chat.message
( id serial primary key ,
  author integer not null,
  room integer not null,
  text text  not null ,
 date_time timestamp default  CURRENT_TIMESTAMP,
 foreign key(author) references chat.user(id),
 foreign key(room) references chat.chatroom(id)
  );
create table if not exists  chat.user_chatroom (
    id serial primary key,
    user_id INTEGER NOT NULL,
    chat_id INTEGER NOT NULL,
    foreign key (user_id) references chat.user(id),
    foreign key (chat_id) references chat.chatroom(id)
);