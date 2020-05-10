create table if not exists osrs.products
(
    id int not null,
    produces int not null,
    primary key (id, produces)
);
