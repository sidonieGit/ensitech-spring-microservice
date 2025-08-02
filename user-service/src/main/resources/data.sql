-- Ce fichier sera exécuté au démarrage du user-service

-- Création d'un enseignant
INSERT INTO person (person_type, first_name, last_name, email, created_at, gender) VALUES ('TEACHER', 'Albert', 'Einstein', 'albert.e@ensitech.com', NOW(), 'MALE');

-- Création de deux étudiants
INSERT INTO person (person_type, first_name, last_name, email, matricule, gender, birthday) VALUES ('STUDENT', 'Marie', 'Curie', 'marie.c@ensitech.com', 'ENS-MC001', 'FEMALE', '1867-11-07');
INSERT INTO person (person_type, first_name, last_name, email, matricule, gender, birthday) VALUES ('STUDENT', 'Isaac', 'Newton', 'isaac.n@ensitech.com', 'ENS-IN002', 'MALE', '1643-01-04');