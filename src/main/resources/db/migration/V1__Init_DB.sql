create sequence hibernate_sequence start 2 increment 1;

create table magazine (
                          id int8 not null,
                          contains varchar(2048),
                          filename varchar(255),
                          link varchar(255),
                          name varchar(255),
                          tags varchar(255),
                          user_id int8, primary key (id));

create table user_liked_tags (
                                 user_id int8 not null,
                                 liked_tags_magazines varchar(255));

create table user_role (
                           user_id int8 not null,
                           roles varchar(255));

create table usr (
                     id int8 not null,
                     activation_code varchar(255),
                     active boolean not null,
                     email varchar(255),
                     password varchar(255) not null,
                     username varchar(255) not null,
                     primary key (id));

alter table if exists magazine
    add constraint message_user_fr
    foreign key (user_id) references usr;

alter table if exists user_liked_tags
    add constraint FK7r0hrwfdn2jhosb9wuxx96snt
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;
