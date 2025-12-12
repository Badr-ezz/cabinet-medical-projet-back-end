package ma.cabinet.rendezvous_service.enums;

public enum StatutAttente {
    EN_ATTENTE, // patient présent en salle d'attente
    EN_COURS,   // patient actuellement en consultation
    TERMINE,    // patient passé, consultation terminée
    ABSENT      // patient appelé mais n'est pas venu / a quitté
}
