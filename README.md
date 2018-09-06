# Pilot

Projet PNSInnov de l'équipe M composée de :

- Nassim Bounouas (Product Owner)
- Loïc Gardaire
- Johann Mortara
- Joël Cancela Vaz

## Description

Pilot est une solution qui aide à la gestion d’un espace de stockage en ligne. Notre solution se greffe sur différents espaces de stockage en ligne déjà existants. Elle est innovante dans la mesure où elle offre à l’utilisateur la possibilité de ranger automatiquement ses fichiers en fonction de règles établies et de les chiffrer s’il le désire.

## Captures d'écran

![Menu](/img/different-drives.png)
![Exemple GDrive](/img/GDrive.png)

## Développé en Java avec

- Jersey (JAX-RS)
- JSF
- Drools (Moteur de règles)

## Compilation et exécution

Nécessite Maven

```bash
mvn compile tomcat7:run-war
```

Puis ouvrez votre navigateur à l'adresse: [http://localhost:9000/Pilot](http://localhost:9000/Pilot)