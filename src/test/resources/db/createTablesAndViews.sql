create table if not exists beskjed
(
  id serial not null
    constraint beskjed_pkey
    primary key,
  systembruker      varchar(100),
  eventtidspunkt    timestamp,
  fodselsnummer     varchar(50),
  eventid           varchar(50),
  grupperingsid     varchar(100),
  tekst             varchar(500),
  link              varchar(200),
  sikkerhetsnivaa   integer,
  sistoppdatert     timestamp,
  aktiv             boolean,
  synligfremtil     timestamp,
  uid               varchar(100),
  eksternVarsling   boolean,
  prefererteKanaler varchar(100),
  constraint beskjedeventidprodusent
  unique (eventid, systembruker)
);

create table if not exists oppgave
(
  id serial not null
    constraint oppgave_pkey
    primary key,
  systembruker      varchar(100),
  eventtidspunkt    timestamp,
  fodselsnummer     varchar(50),
  eventid           varchar(50),
  grupperingsid     varchar(100),
  tekst             varchar(500),
  link              varchar(200),
  sikkerhetsnivaa   integer,
  sistoppdatert     timestamp,
  aktiv             boolean,
  eksternVarsling   boolean,
  prefererteKanaler varchar(100),
  constraint oppgaveeventidprodusent
  unique (eventid, systembruker)
);

create table if not exists innboks
(
  id serial not null
    constraint innboks_pkey
    primary key,
  systembruker    varchar(100),
  eventtidspunkt  timestamp,
  fodselsnummer   varchar(50),
  eventid         varchar(50),
  grupperingsid   varchar(100),
  tekst           varchar(500),
  link            varchar(200),
  sikkerhetsnivaa integer,
  sistoppdatert   timestamp,
  aktiv           boolean,
  eksternVarsling   boolean,
  prefererteKanaler varchar(100),
  constraint innbokseventidprodusent
  unique (eventid, systembruker)
);

