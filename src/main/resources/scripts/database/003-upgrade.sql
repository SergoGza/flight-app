create table flights
(
    id                  bigint(20) unsigned not null auto_increment,
    created             datetime            not null,
    number              varchar(255)        not null comment 'flight number',
    airport_departure_id varchar(3)         not null comment 'departure airport' references airports(id),
    airport_arrival_id   varchar(3)         not null comment 'arrival airport' references airports(id),
    departure_time       timestamp          not null comment 'utc instant of departure time',
    status              varchar(255)        not null comment 'status of the flight',
    capacity            int                not null comment 'passengers capacity for this flight',
    occupancy           int                not null comment 'passengers occupancy for this flight',
    primary key (id)
);

create table flight_images
(
    id           bigint(20) unsigned not null auto_increment,
    filename     varchar(255)        not null comment 'original filename',
    size         int                not null comment 'size of the image in kb',
    content_type varchar(255)        not null comment 'content type of the image',
    resource_id  varchar(36)         not null comment 'public id of the image',
    flight_id    bigint(20) unsigned not null comment 'flight id fk' references flights(id),
    primary key (id)
);

create table flights_with_users
(
    id        bigint(20) unsigned not null auto_increment,
    created   datetime           not null comment 'creation date',
    locator   varchar(36)        not null comment 'locator id',
    flight_id bigint(20) unsigned not null comment 'fk to flight' references flights(id),
    user_id   varchar(13)        not null comment 'fk to users' references users(id),
    primary key (id)
);