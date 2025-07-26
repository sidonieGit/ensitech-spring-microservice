# Projet Ensitech - Architecture Microservices

Bienvenue dans le dépôt central du projet Ensitech. Ce projet a pour but de construire le système d'information de l'école en utilisant une architecture microservices moderne avec Spring Boot et Spring Cloud.

Ce dépôt contient la base de l'architecture, y compris les services fondamentaux et la configuration partagée.

## Architecture Cible

Le système est décomposé en plusieurs microservices, chacun ayant sa propre responsabilité et sa propre base de données.

### Services Existants

*   **`config-service`**: (À créer) Serveur de configuration centralisé.
*   **`discovery-service`** (Eureka): (À créer) Service de découverte qui permet aux autres services de se trouver.
*   **`gateway-service`**: (À créer) Point d'entrée unique (API Gateway) pour toutes les requêtes externes.
*   **`user-service`**: Gère les utilisateurs (étudiants, enseignants, etc.). **C'est notre service de référence.**

### Services à Ajouter

*   `course-service`: Gestion des cours et des matières.
*   `speciality-service`: Gestion des spécialités et filières.
*   `evaluation-service`: Gestion des notes et évaluations.
*   `academic-year-service`: Gestion des années académiques.
*   `inscription-service`: Gestion des inscriptions des étudiants.
*   `authentication-service`: Gestion de l'authentification et de la sécurité (JWT).

## Prérequis

*   JDK 21 ou supérieur
*   Maven 3.8+
*   Docker (recommandé, pour la base de données et RabbitMQ)
*   Votre IDE favori (IntelliJ IDEA, VS Code...)

## Comment Ajouter un Nouveau Microservice

Pour garantir la cohérence et la compatibilité de tous les services, nous utilisons un **projet parent Maven**. Chaque nouveau microservice doit hériter de ce parent.

Suivez ces étapes pour créer un nouveau service (par exemple, `course-service`) :

### 1. Créer la Structure du Projet

Vous pouvez utiliser [Spring Initializr](https://start.spring.io/) pour générer la base du projet, mais vous devrez **modifier manuellement le `pom.xml`** après la création.

1.  Créez un nouveau dossier `course-service` à la racine de ce projet.
2.  Générez un projet Spring Boot à l'intérieur de ce dossier.

### 2. Configurer le `pom.xml` du nouveau service

C'est l'étape la plus importante. Votre nouveau `pom.xml` doit hériter du `pom.xml` parent qui se trouve à la racine du projet (`ensitech-final/pom.xml`).

Voici un modèle pour le `pom.xml` de `course-service` :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    <modelVersion>4.0.0</modelVersion>

    <!-- ÉTAPE 1: HÉRITAGE DU PARENT -->
    <!-- Cela garantit que vous utilisez les mêmes versions de Spring Boot, Spring Cloud, etc. -->
    <parent>
        <groupId>com.project.ensitech</groupId>
        <artifactId>ensitech-parent</artifactId>
        <version>1.0-SNAPSHOT</version>
        <!-- Le chemin relatif vers le pom.xml parent -->
        <relativePath>../pom.xml</relativePath>
    </parent>

    <!-- ÉTAPE 2: IDENTITÉ DU MICROSERVICE -->
    <artifactId>course-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>course-service</name>
    <description>Microservice pour la gestion des cours</description>

    <!-- ÉTAPE 3: DÉPENDANCES SPÉCIFIQUES -->
    <!-- Le parent gère déjà les versions. Vous n'avez qu'à ajouter les dépendances dont vous avez besoin. -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!-- Ajoutez ici d'autres dépendances comme la BDD, Lombok... -->
    </dependencies>

    <!-- ... le reste du build plugin reste le même ... -->
</project>
```

### 3. Mettre à jour le `pom.xml` parent

Pour que Maven reconnaisse votre nouveau service comme un module du projet global, ajoutez-le à la section `<modules>` du `pom.xml` qui se trouve à la racine (`ensitech-final/pom.xml`).

```xml
<!-- Dans ensitech-final/pom.xml -->
<modules>
    <module>user-service</module>
    <!-- Ajoutez votre nouveau service ici -->
    <module>course-service</module>
</modules>
```

### 4. Lancer et Tester

Vous pouvez maintenant lancer votre nouveau microservice. Il partagera automatiquement la configuration de base (versions, plugins...) avec les autres services, assurant ainsi la stabilité de l'ensemble de l'écosystème.

---

## Comment ça marche ? (La magie du POM Parent)

Le `pom.xml` à la racine du projet agit comme un "chef d'orchestre".

*   **`<dependencyManagement>`**: Il définit une liste de toutes les dépendances possibles avec leurs **versions exactes**. Quand un microservice "enfant" (comme `user-service`) déclare une dépendance **sans spécifier de version**, il hérite automatiquement de la version définie par le parent. C'est ce qui évite les conflits de versions.
*   **`<pluginManagement>`**: De même, il définit la configuration des plugins Maven.
*   **`<modules>`**: Il liste tous les sous-projets (nos microservices), ce qui vous permet de compiler, tester ou packager tous les services en une seule commande depuis la racine (`mvn clean install`).

Cette approche est une **bonne pratique** pour gérer des projets complexes.