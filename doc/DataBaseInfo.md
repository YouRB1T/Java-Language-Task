jdbc:postgresql://localhost:5432/phone_book
postgres
postgres

CREATE TABLE persons (
id UUID PRIMARY KEY,                 
first_name VARCHAR(255) NOT NULL,    
last_name VARCHAR(255) NOT NULL      
);

CREATE TABLE numbers (
id UUID PRIMARY KEY,                  
number VARCHAR(50) NOT NULL,        
person_id UUID NOT NULL,     
CONSTRAINT fk_person FOREIGN KEY (person_id)
REFERENCES persons(id) ON DELETE CASCADE
);