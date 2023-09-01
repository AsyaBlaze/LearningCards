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

create table cards_data(
	card_id int REFERENCES cards(id_card),
	group_id int REFERENCES groups(id_group),
	PRIMARY KEY (card_id, group_id)
);
