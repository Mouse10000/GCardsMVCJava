create table collectioncards.role
(
    Id   bigint       not null
        primary key,
    Name varchar(255) not null,
    constraint Name
        unique (Name)
);

create table collectioncards.user
(
    Id           bigint       not null
        primary key,
    UserName     varchar(255) not null,
    Email        varchar(255) not null,
    PasswordHash varchar(255) not null,
    constraint Email
        unique (Email),
    constraint UserName
        unique (UserName)
);
create table collectioncards.userrole
(
    Id     bigint auto_increment
        primary key,
    UserId bigint not null,
    RoleId bigint not null,
    constraint userrole_ibfk_1
        foreign key (UserId) references collectioncards.user (Id),
    constraint userrole_ibfk_2
        foreign key (RoleId) references collectioncards.role (Id)
);


create table collectioncards.userrole
(
    UserId bigint not null,
    RoleId bigint not null,
    constraint userrole_ibfk_1
        foreign key (UserId) references collectioncards.user (Id),
    constraint userrole_ibfk_2
        foreign key (RoleId) references collectioncards.role (Id),
    primary key (UserId, RoleId)
);
