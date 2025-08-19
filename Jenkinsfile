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
                        timeout(time: 10, unit: 'MINUTES')
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
              // ===================================================================
               // NOUVELLE SECTION : Publication du rapport de couverture de code
               // ===================================================================
               // On utilise le plugin "Coverage" pour lire les rapports JaCoCo.
               // Il faut l'exécuter AVANT de nettoyer l'espace de travail.
                step([
                     $class: 'JacocoPublisher',
                      // Laisse les autres paramètres par défaut
                ])
                publishCoverage adapters: [jacocoAdapter(path: '**/target/site/jacoco/jacoco.xml')]
                echo 'Rapport de couverture de code publié.'

            // Nettoie l'espace de travail pour le prochain build.
            cleanWs()
            echo 'Espace de travail nettoyé.'
        }
    }
}