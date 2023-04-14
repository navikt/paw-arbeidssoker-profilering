CREATE TABLE profilering (
                                     id SERIAL PRIMARY KEY,
                                     foedselsnummer VARCHAR(11),
                                     registrerings_id INT NOT NULL,
                                     alder INT NOT NULL,
                                     jobbet_sammenhengende_seks_av_tolv_siste_mnd BOOLEAN NOT NULL,
                                     foreslatt_innsatsgruppe TEXT NOT NULL,
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