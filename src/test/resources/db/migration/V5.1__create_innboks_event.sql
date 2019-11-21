INSERT INTO INNBOKS(produsent, eventTidspunkt, aktorId, eventId, dokumentId, tekst, link, sistOppdatert, sikkerhetsnivaa, aktiv)
values  ('DittNAV', NOW(), '12345', '125', '100125', 'Dette er en oppgave til brukeren', 'https://nav.no/systemX', NOW(), 4, true),
        ('DittNAV', NOW(), '12345', '659', '100659', 'Dette er en oppgave til brukeren', 'https://nav.no/systemX', NOW(), 4, true),
        ('DittNAV', NOW(), '12345', '659', '100659', 'Dette er en oppgave til brukeren', 'https://nav.no/systemX', NOW(), 4, false),
        ('DittNAV', NOW(), '54321', '558', '100558', 'Dette er en oppgave til brukeren', 'https://nav.no/systemX', NOW(), 4, true);