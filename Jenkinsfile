// Ce fichier est la "recette" que Jenkins va suivre.
// Il est écrit en Groovy, un langage simple et scripté.

pipeline {
    // 1. Agent : Où doit s'exécuter ce pipeline ?
    // 'any' signifie que Jenkins peut utiliser n'importe quel agent disponible.
    agent any

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
            steps {
                // Exécute la commande Maven pour compiler et lancer les tests.
                // 'bat' est pour Windows. Sur Mac/Linux, on utiliserait 'sh'.
                bat './mvnw clean install'
                echo 'Build et tests terminés.'
            }
        }
    }

    // 3. Post : Actions à faire à la fin du pipeline.
    post {
        // 'always' s'exécute toujours, que le build réussisse ou échoue.
        always {
            // Nettoie l'espace de travail pour le prochain build.
            cleanWs()
            echo 'Espace de travail nettoyé.'
        }
    }
}