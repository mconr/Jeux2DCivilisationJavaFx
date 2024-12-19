![Game](https://github.com/user-attachments/assets/ab8d0980-a772-46ec-af24-f2e2648436e4)# Jeux2DCivilisationJavaFx
Jeux 2D Civilisation JavaFx

# Projet-Approche-Objet
<p align="center">
![Game](https://github.com/user-attachments/assets/dae39285-28b9-40b6-ab14-d7f000866bb1)
![image](https://github.com/user-attachments/assets/2cbed018-8efc-4a5c-83ed-c345122dbe50)
</p>
![GameOver](https://github.com/user-attachments/assets/413738e7-95a3-4f0d-b6d6-1b9767b74970)


## Description
Le jeu que nous proposons est un jeu type "Civilization", dont le but est de maintenir l'économie d'une ville le plus longtemps possible. Vous allez vous occuper de la gestion des ressources, de la construction de bâtiments afin de recolter des ressources, ainsi que la population de votre ville.  
Notre jeu est similaire a "Civilization" mais notre jeu se concentre davantage sur l'aspect économique, mettant l'accent sur la maximisation des ressources pour maintenir la ville plutôt que sur la conquête territoriale. Le joueur cherche à construire les bâtiments de manière stratégique afin de récolter plus ressources. Dans ce sens, notre jeu est moins complexe que "Civilization".

## Caractéristiques
- Trois niveaux de difficulté (Easy, Normal, Hard) avec des ressources initiales différentes.
- Gestion des ressources, des habitants, des travailleurs, et des bâtiments.
- Construction, amélioration, et destruction de bâtiments.
- Possibilité d'améliorer les bâtiments pour augmenter la production et la capacité d'accueil.
- Interface graphique permettant une interaction intuitive avec la ville virtuelle.


## Prérequis
- Java: Version 17 ou supérieure.
- Gradle: Pour la construction du projet.

## Installation et démarrage
### Clonage du dépôt

```shell
git clone git@github.com:mconr/Projet-Approche-Objet.git
```
### Construction avec Gradle

```shell
./gradlew build
```

### Exécution du jeu
```shell
./gradlew run
```


## Utilisation
Une fois le depôt cloné et le projet construit avec Gradle vous pourrez commencer à jouer au jeu.
Le but du jeu est de maintenir une économie stable le plus longtemps possible. La partie se termine lorsque le joueur n'a plus de nourriture pour toute sa population. 

### Instructions pour jouer :
1. **Lancer le jeu :** Après avoir cloné le dépôt, construit le projet avec Gradle, et ouvert le jeu, vous êtes accueilli par l'interface graphique du jeu.
2. **Choix du niveau de difficulté :** Au lancement de la fenêtre de jeu, vous avez le choix entre trois niveaux de difficulté : Easy, Normal, ou Hard. Par exemple, le niveau Easy offre plus de ressources que les niveaux Normal et Hard.
3. **Ressources initiales et bâtiments de départ :** Une fois le niveau de difficulté choisi, vous disposez d'un ensemble initial de ressources ainsi que d'un ou deux bâtiments de départ, en fonction du niveau sélectionné.
4. **Construction de bâtiments :** En haut de la fenêtre du jeu, vous trouverez la liste des bâtiments que vous pouvez construire. Cliquez sur le bâtiment souhaité, puis choisissez l'emplacement sur la grille où vous souhaitez le placer, en fonction des ressources disponibles. Par exemple, vous pouvez construire une maison pour augmenter la capacité d'accueil de votre ville. Sachez que chaque construction prend un certain temps à construire, la maison, par exemple, prend 2 jours à construire.
5. **Gestion des ressources :** Le jeu se déroule au fil des jours. Chaque jour, les bâtiments produisent des ressources, tandis que la population consomme des ressources, en particulier de la nourriture. Vous devez prendre garde à toujours avoir assez de ressources pour nourrir tout votre population.
6. **Amélioration des bâtiments :** Vous avez la possibilité d'améliorer les bâtiments existants pour augmenter leur production et leur capacité d'accueil. Cependant, chaque amélioration a un coût, et il faut prendre en compte les ressources disponibles.
7. **Fin de partie :** La partie se termine lorsque la quantité de nourriture disponible n'est plus suffisante pour nourrir toute la population de la ville. Votre objectif est de maintenir une économie stable le plus longtemps possible.

En suivant ces instructions, vous pouvez explorer les différentes mécaniques du jeu, prendre des décisions stratégiques et tenter de maintenir un équilibre économique le plus longtemps possible. Amusez-vous bien !


