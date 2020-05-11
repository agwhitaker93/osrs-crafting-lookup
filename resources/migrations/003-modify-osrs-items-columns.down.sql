alter table osrs.items drop column if exists ha_price;

alter table osrs.items rename column ge_price to price;
