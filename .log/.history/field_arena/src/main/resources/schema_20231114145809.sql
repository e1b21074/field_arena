CREATE TABLE userinfo (
  id IDENTITY,
  userName VARCHAR NOT NULL
);

CREATE TABLE cards(
  id IDENTITY,
  cardAttribute VARCHAR NOT NULL,
  cardStrong INT NOT NULL
);

CREATE TABLE rooms(
  id IDENTITY,
  roomName VARCHAR NOT NULL,
  user1 VARCHAR NOT NULL,
  user2 VARCHAR,
  usernum INT,
  isActive BOOLEAN NOT NULL
);
