# MaBank - Système de Gestion Bancaire

**MaBank** est une application de gestion bancaire développée en Java avec interface graphique Swing. Elle fournit des outils d'administration pour gérer les clients, les employés, les comptes bancaires et les transactions.

## Vue d'ensemble

- **Langage** : Java (JDK 25+)
- **Interface** : Swing (GUI)
- **Stockage** : Tableaux en mémoire (pas de base de données)
- **Architecture** : Orientée objet avec interfaces et classes abstraites

## Fonctionnalités principales

✓ **Gestion des clients** - Créer, modifier, supprimer et lister les clients  
✓ **Gestion des employés** - Gérer les employés, managers et directeurs  
✓ **Gestion des comptes** - Créer différents types de comptes bancaires  
✓ **Transactions** - Dépôts, retraits, virements  
✓ **Rapports** - Génération de statistiques financières  
✓ **Audit** - Historique complet des actions des employés  

## Structure du projet

```
src/
├── Application.java                 # Point d'entrée de l'application
├── gestionnaires/
│   └── BanqueManager.java          # Gestionnaire central (stockage en mémoire)
├── modeles/
│   ├── User.java                   # Classe abstraite parent (Client, Employee)
│   ├── Client.java                 # Client bancaire
│   ├── Employee.java               # Employé avec historique d'audit
│   ├── Employe.java                # Employé de base
│   ├── Manager.java                # Manager supervisant une équipe
│   ├── Directeur.java              # Directeur de région
│   ├── Compte.java                 # Classe abstraite pour comptes
│   ├── CompteInvestissement.java   # Compte investissement
│   ├── CompteClient.java           # Compte spécifique au client
│   ├── CompteEmployee.java         # Compte d'employé
│   ├── Transaction.java            # Classe abstraite pour transactions
│   ├── Depot.java                  # Transaction de dépôt
│   ├── Retrait.java                # Transaction de retrait
│   └── Virement.java               # Transaction de virement
├── interfaces/
│   ├── Auditable.java              # Interface pour l'historique d'audit
│   ├── Assurable.java              # Interface pour l'assurance
│   ├── Authentifiable.java         # Interface d'authentification
│   ├── Notifiable.java             # Interface de notifications
│   └── Placable.java               # Interface pour les placements
├── exceptions/
│   ├── BanqueException.java        # Exception de base
│   ├── AuthenticationException.java
│   ├── ClientExisteException.java
│   ├── CompteInexistantException.java
│   ├── DateInvalideException.java
│   ├── MontantInvalidException.java
│   ├── OperationNonAutoriseeException.java
│   └── SoldeInsufficientException.java
└── ui/
    ├── FenetrePrincipale.java      # Fenêtre principale
    ├── FenetreGestionClients.java
    ├── FenetreGestionComptes.java
    ├── FenetreGestionEmployes.java
    ├── FenetreTransactions.java
    └── FenetreRapports.java
```

## Stockage des données

Toutes les données (clients, employés, comptes, transactions) sont stockées uniquement dans des **tableaux Java en mémoire** gérés par `BanqueManager`.

**Important** : Les données sont perdues à la fermeture de l'application car il n'existe pas de persistance en base de données.

## Architecture et Concepts OOP

### Hiérarchie des classes

**Utilisateurs** :
```
User (id, nom, email)
├── Client
└── Employee (extends User, implements Auditable)
    ├── Employe
    ├── Manager
    └── Directeur
```

**Comptes** :
```
Compte (abstract)
├── CompteInvestissement
├── CompteClient
└── CompteEmployee
```

**Transactions** :
```
Transaction (abstract)
├── Depot
├── Retrait
└── Virement
```

### Concepts implémentés

| Concept | Détails | Exemple |
|---------|---------|----------|
| **Abstraction** | Classes abstraites définissant l'interface | `User`, `Compte`, `Transaction` |
| **Héritage** | Sous-classes spécialisées | `CompteInvestissement` extends `Compte` |
| **Polymorphisme** | Comportement selon le type réel | `compte.calculerInteret()` |
| **Encapsulation** | Attributs privés + getters/setters | Accès contrôlé aux données |
| **Interfaces** | Contrats de comportement | `Auditable`, `Assurable` |
| **Exceptions personnalisées** | Gestion d'erreurs métier | `SoldeInsufficientException` |
| **Collections dynamiques** | Tableaux redimensionnables | `Arrays.copyOf()` |

## Compilation et exécution

### Compiler
```bash
mkdir -p out
find src -name "*.java" | xargs javac -d out
```

### Exécuter
```bash
java -cp out Application
```

## Flux de données

1. **Application.java** lance la fenêtre principale
2. **BanqueManager** gère l'état global (clients, employés, comptes)
3. **UI** (fenêtres Swing) collecte les entrées utilisateur
4. **Modèles** (classes métier) exécutent la logique bancaire
5. **Exceptions personnalisées** signalent les erreurs opérationnelles
6. **Audit** enregistre toutes les actions des employés

## Gestion des erreurs

L'application utilise une hiérarchie d'exceptions personnalisées héritant de `BanqueException` :

- `AuthenticationException` - Erreurs d'authentification
- `ClientExisteException` - Client déjà existant
- `CompteInexistantException` - Compte introuvable
- `MontantInvalidException` - Montant invalide
- `SoldeInsufficientException` - Solde insuffisant
- `OperationNonAutoriseeException` - Opération interdite
- `DateInvalideException` - Date invalide
