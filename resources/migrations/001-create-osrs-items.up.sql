create schema if not exists osrs;

create table if not exists osrs.items
(
    id int not null,
    icon text not null,
    icon_large text not null,
    name text not null,
    description text not null,
    skill text not null,
    xp real not null,
    price text not null,
    last_updated timestamp not null
);

create unique index items_id_uindex
    on osrs.items (id);

create unique index items_name_uindex
    on osrs.items (name);

alter table osrs.items
    add constraint items_pk
        primary key (id);
