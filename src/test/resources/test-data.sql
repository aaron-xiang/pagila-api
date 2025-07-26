-- Create the actor table
CREATE TABLE IF NOT EXISTS actor (
    actor_id SERIAL PRIMARY KEY,
    first_name VARCHAR(45) NOT NULL,
    last_name VARCHAR(45) NOT NULL
);

-- Insert sample data
INSERT INTO actor (first_name, last_name) VALUES ('PENELOPE', 'GUINESS');
INSERT INTO actor (first_name, last_name) VALUES ('NICK', 'WAHLBERG');