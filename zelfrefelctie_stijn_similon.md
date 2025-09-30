# V1
## Pre coaching 30/09/2025
### Geschatte Progress (in procent): 15%
### status
We hebben 5 Issues geslopen, 2 zijn nog in progress. Het opzetten van het project heeft momenteel veel tijd in beslag.

De samenwerking verliep vanuit mijn prespectief redelijk vlot. Als er vragen waren onderling werden deze zeker tijdig beantwoord.

## Post coaching
### Feedback
#### Demo
- OOOO... als UUID instellen is mss nog niet de beste oplossing maar tijdelijk nog een goede oplossing
- Zo snel mogelijk overstappen naar een DB
  - beginnen met 1 Entity, dan meer

#### Code review
Restaurant-service:
- OpeningHours aggregate root -> voor overlappende uren
- warning: Uitkijken dat we geen Anemic Domain krijgen
- Restaurant <-> Dish Aggregate Root is goed gedaan

Delivery-service:
- Jpa repo & Domain repo


Opsplitsen wanneer:
- Controller: altijd
- Service: in het begin misschien nog niet maar als er meer specifiekere logica moet gebeuren wel 