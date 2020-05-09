create schema if not exists osrs;

create table if not exists osrs.items
(
    id int not null,
    icon text not null,
    icon_large text not null,
    name text not null,
    description text not null,
    price text not null,
    last_updated date not null
);

create unique index items_id_uindex
    on osrs.items (id);

create unique index items_icon_uindex
    on osrs.items (icon);

create unique index items_icon_large_uindex
    on osrs.items (icon_large);

create unique index items_name_uindex
    on osrs.items (name);

create unique index items_description_uindex
    on osrs.items (description);

create unique index items_price_uindex
    on osrs.items (price);

alter table osrs.items
    add constraint items_pk
        primary key (id);

