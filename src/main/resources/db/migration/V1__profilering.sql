CREATE TABLE profilering (
                                     id SERIAL PRIMARY KEY,
                                     foedselsnummer VARCHAR(11),
                                     innsatsgruppe TEXT NOT NULL,
                                     besvarelse JSONB,
                                     opprettet TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     endret TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX profilering_foedselsnummer_index ON profilering (foedselsnummer);

CREATE OR REPLACE FUNCTION trigger_set_timestamp()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.endret = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER profilering_endret
    BEFORE UPDATE ON profilering
    FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();