
create table common_files_tbl (
    id bigint not null auto_increment,
    original_name varchar(255) not null,
    description varchar(255) not null,
    creation_date datetime not null,
    author_name varchar(255) not null,
    size bigint,
    type smallint,
    status smallint,
    module smallint,
    primary key (id)
);
