// Ce fichier est la "recette" que Jenkins va suivre.
// Il est écrit en Groovy, un langage simple et scripté.

pipeline {
    // 1. Agent : Où doit s'exécuter ce pipeline ?
    // 'any' signifie que Jenkins peut utiliser n'importe quel agent disponible.
    agent any

        // NOUVELLE SECTION : Outils (Tools)
        // C'est ici qu'on déclare les outils dont notre build a besoin.
    tools {
            // On demande à Jenkins d'utiliser un JDK.
            // Le nom 'jdk21' doit correspondre à une configuration dans Jenkins.
            // Nous allons configurer cela dans l'interface web.
            jdk 'jdk21'
    }

    // 2. Stages : Les grandes étapes de notre processus.
    stages {
        // --- Étape 1 : Checkout ---
        stage('Checkout SCM') {
            steps {
                // Récupère le code depuis le dépôt Git configuré.
                checkout scm
                echo 'Code récupéré avec succès.'
            }
        }

        // --- Étape 2 : Build & Test ---
        stage('Build & Test with Maven') {
          options {
                        // On donne 10 minutes à ce stage pour se terminer.
                        // Choisissez une valeur raisonnable pour votre projet.
                        timeout(time: 18, unit: 'MINUTES')
                    }
            steps {
                // Exécute la commande Maven pour compiler et lancer les tests.
                // La phase 'verify' exécute le cycle de vie jusqu'aux tests d'intégration
                // et déclenche la génération du rapport JaCoCo.
                // 'bat' est pour Windows. Sur Mac/Linux, on utiliserait 'sh' pour le conteneur Docker.
                sh './mvnw clean install'
                echo 'Build, tests et génération du rapport de couverture terminés.'
            }
        }
    }

    // 3. Post : Actions à faire à la fin du pipeline.
    post {
        // 'always' s'exécute toujours, que le build réussisse ou échoue.
        always {
                // ON A SIMPLEMENT SUPPRIMÉ LE BLOC JacocoPublisher
                // C'est l'étape fournie par le plugin "Coverage"
                recordCoverage(tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']])
                echo 'Rapport de couverture de code publié.'

                // Nettoie l'espace de travail pour le prochain build.
                cleanWs()
                echo 'Espace de travail nettoyé.'
            }
    }
}