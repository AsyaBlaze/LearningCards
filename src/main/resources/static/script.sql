create table cards(
	id_card int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	word varchar(150),
	translation varchar(200),
	is_learned boolean default (false),
	examples varchar(300),
	transcription varchar(200)
);

create table groups(
	id_group int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	group_name varchar(36)
);

create table cards_groups(
    id_card int REFERENCES cards(id_card),
    id_group int REFERENCES groups(id_group),
    PRIMARY KEY (id_card, id_group)
);

create table users(
	id_user int REFERENCES users(id_user),
	username varchar(18) CHECK (username >= 5),
	password varchar
);

create table users_cards(
    id_card int REFERENCES cards(id_card),
    id_user int REFERENCES users(id_user),
    PRIMARY KEY (id_card, id_user)
);

create table users_groups(
    id_user int REFERENCES users(id_user),
    id_group int REFERENCES groups(id_group),
    PRIMARY KEY (id_group, id_user)
);