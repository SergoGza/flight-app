create table airports
(
    id        varchar(3)   not null primary key,
    name      varchar(255) not null,
    country   varchar(255) not null comment 'country where the airport is located',
    latitude  decimal(10, 8) not null comment 'lat of the airport location',
    longitude decimal(10, 8) not null comment 'lon of the airport location'
);

insert into airports (id, name, country, latitude, longitude)
values
    ('ABZ', 'Aberdeen Airport', 'United Kingdom', 57.202713, -2.192333),
    ('EDI', 'Edinburgh Airport', 'United Kingdom', 55.950785, -3.3614532),
    ('GLA', 'Glasgow Airport', 'United Kingdom', 55.869074, -4.435053),
    ('PIK', 'Glasgow Prestwick Airport', 'United Kingdom', 55.508902, -4.610981),
    ('BCN', 'Barcelona Airport', 'Spain', 41.297445, 2.0832940),
    ('IBZ', 'Ibiza Airport', 'Spain', 38.8748594, 1.3712232),
    ('MAH', 'Menorca Airport', 'Spain', 39.86335, 4.220772),
    ('PMI', 'Palma de Mallorca Airport', 'Spain', 39.5517407, 2.73616489);
