create schema if not exists osrs;

-- items table
create table if not exists osrs.items
(
    id int not null,
    name text not null,
    examine text,
    tradable bool,
    exchange bool,
    members_only bool,
    wiki text,
    value text,
    ge_value text,
    icon text,
    icon_large text,
    last_updated timestamp not null
);

alter table osrs.items
    add constraint items_pk
        primary key (id);

-- recipes table
create table if not exists osrs.recipes
(
    id int not null,
    recipe_id int,
    facilities text,
    tools text,
    ticks text,
    members_only bool,
    last_updated timestamp not null
);

-- materials table
create table if not exists osrs.materials
(
    id int not null,
    recipe_id int,
    name text,
    quantity text,
    last_updated timestamp not null
);

-- skills table
create table if not exists osrs.skills
(
    id int not null,
    recipe_id int,
    name text,
    level text,
    exp text,
    last_updated timestamp not null
);
