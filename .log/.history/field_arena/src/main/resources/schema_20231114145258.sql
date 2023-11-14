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
  usernum INT,
  isActive BOOLEAN NOT NULL
);
