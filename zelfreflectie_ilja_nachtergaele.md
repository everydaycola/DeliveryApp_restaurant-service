# Pre coaching 30/09/2025
## Geschatte Progress (in procent): 15%
## status
We hebben 5 van de 43 issues gesloten (12%), Maar ik denk als je de status in user stories uit drukt krijg je een vertekent beeld. Het opzetten van het project, begrijpen van leerstof en aanmaken van files is niet inbegrepen in de issues maar is wel een groot deel van het project. Zo duurt het lang tot je eerste http request werkt en veel minder lang tot je tweede werkt, en zo voort.

De samenwerking verloopt vlot. Ik denk dat ik zelf meer moeite had met het begrijpen van de leerstof en hierdoor achter sta in hoeveelheid werk geleverd. Ik doe delivery en Stijn doet restaurant. Order hebben we momenteel nog niet aangeraakt.

# Post coaching
## Feedback

[zie feedback stijn](./zelfrefelctie_stijn_similon.md)

# V2
## Pre coaching 19/10/2025
### Geschatte Progress (in procent): 70%
### status
#### User Stories
Zowel restaurant als delivery zijn feature-complete. Bij beide moet keycloac nog volledig geimplementeerd worden. En messeging moet beter uitgewerkt worden. In order heb ik een deel van de backend al gedaan. De frontend is nog niet aangeraakt, momenteel staat hier een default vite project. Er zijn wel meerdere api request die via de backend van order tot restaurant moeten gaan, die werken volledig en gaan het maken van de frontend vereenvoudigen.
## Post coaching
### Feedback

 - messeging and security te weinig
 - rest controller, opoen/close: taak voor serive
   - updatedishstate -> publish dish
 - publish dishes taak: naamgeving
 - @atScheduled via spring vor timed events
 - rare comments in restaurantservice
 - intellij java regiuons ipv comment headers
 - voor opening hours: override vervangen door ook schedule
 - domein ook annoteren, in order en delivery.
 - methodes van delivery controller moeten weg.
 - ready -> ready for pickup
 - @repo bij delivery repo moet weg.
 - magic values in app properties
 - queue names in app properties
 - past en current delivies zijn berekende velden