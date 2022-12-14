create database test;

\c test;

create table department (
    name text not null primary key,
    created_at date not null default now()
);

create table employee (
    id bigserial primary key,
    name text not null,
    seniority text not null,
    salary integer not null,
    department text not null references department(name)
);
