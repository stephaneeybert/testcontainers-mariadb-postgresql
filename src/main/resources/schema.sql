drop table if exists post;
CREATE TABLE IF NOT EXISTS post (
    id int primary key,
    user_id INT NOT NULL,
    title VARCHAR(250) NOT NULL,
    body TEXT NOT NULL,
    isbn varchar(50) not null,
    version int not null,
    created_on timestamp,
    updated_on timestamp
);
