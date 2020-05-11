alter table osrs.items add column if not exists ha_price real not null default 0;

alter table osrs.items rename column price to ge_price;
