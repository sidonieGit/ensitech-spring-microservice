package com.project.user_service.models.entities;

import com.project.user_service.enumeration.Gender;
import com.project.user_service.models.entities.model.Course;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Important pour l'héritage !
// @DiscriminatorValue("1") dit à Hibernate : "Si la colonne 'person_type' contient la valeur 1, alors cette ligne est un Teacher".
@DiscriminatorValue("1")
public class Teacher extends Person{
    @Temporal(TemporalType.TIMESTAMP) // Ici on veut la date et l'heure de création.
    private Date createdAt;

    /*
     * -- SUPPRESSION DE LA RELATION JPA --
     * La ligne ci-dessous est supprimée car l'entité Course n'existe pas dans ce microservice.
     * La gestion de la liste des cours se fera au niveau de la couche DTO/Service par appel API.
     * @OneToMany(mappedBy = "teacher", ...)
     * private Set<Course> courses = new HashSet<>();
     */

    // Le reste du constructeur et des champs ne change pas (sauf qu'on enlève 'courses')

    // LE CONSTRUCTEUR EXACT QUE HIBERNATE RECHERCHE
    public Teacher(Long id, String firstName, String lastName, String email, String address, String telephone, Date birthday, Gender gender, Date createdAt) {
        super(id, firstName, lastName, email, address, telephone, birthday, gender);
        this.createdAt = createdAt;
    }

}
