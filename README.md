[README.md](https://github.com/user-attachments/files/31495430/README.md)
# GateConnect

Plugin Bukkit/Spigot pour réseaux Minecraft sous Velocity ou BungeeCord. GateConnect contrôle la version Minecraft d'un joueur avant de l'autoriser à rejoindre un serveur précis.

Il est conçu pour fonctionner avec **ViaVersion**, un menu tel que **ServerSelectorX**, et peut être utilisé avec **VelocityPteroPower** pour éviter le démarrage inutile d'un serveur incompatible.

## Fonctionnement

Lorsqu'un joueur clique sur un serveur dans le menu :

1. ServerSelectorX exécute la commande `gateconnect <serveur> <joueur>` depuis la console du lobby.
2. GateConnect retrouve le joueur connecté.
3. ViaVersion fournit son numéro de protocole Minecraft.
4. GateConnect compare ce protocole aux valeurs `min` et `max` définies dans `config.yml`.
5. Si la version est incompatible, le joueur reçoit un message et aucune demande de connexion n'est envoyée au proxy.
6. Si la version est compatible, GateConnect envoie une demande `Connect` au proxy via le canal `BungeeCord`.

Ainsi, un joueur en 1.8.8 ne peut pas déclencher le démarrage d'un serveur réservé à la 1.20.6.

## Compatibilité

- Serveurs Bukkit, Spigot, Paper ou PandaSpigot 1.8.8+
- Java 8+
- ViaVersion requis pour la vérification de protocole
- Proxy Velocity ou BungeeCord
- ServerSelectorX recommandé pour les menus
- VelocityPteroPower compatible pour le démarrage automatique des backends

## Installation

1. Compile le projet avec Maven :

   ```bash
   mvn clean package
   ```

2. Récupère le fichier `target/GateConnect.jar`.
3. Place-le dans le dossier `plugins/` de ton lobby Bukkit/Spigot.
4. Installe ViaVersion sur ce même lobby.
5. Redémarre le lobby.
6. Le fichier de configuration est créé dans :

   ```text
   plugins/GateConnect/config.yml
   ```

Au démarrage, la console doit afficher :

```text
[GateConnect] GateConnect actif.
```

### Une version devrait être bloquée mais est autorisée

- Vérifie que ViaVersion est présent et activé sur le lobby.
- Vérifie le protocole vu dans les logs `CONNECT` ou `BLOCKED`.
- Vérifie les valeurs `min` et `max` du serveur dans `plugins/GateConnect/config.yml`.
- Vérifie les majuscules et l'orthographe du nom de serveur.
- Redémarre le lobby après une modification importante de configuration.

### Le serveur démarre alors qu'il devrait être bloqué

GateConnect ne peut empêcher que ses propres demandes `Connect`. Vérifie dans les logs qu'une ligne `BLOCKED` est bien présente et qu'aucune autre commande, file VPP persistante ou plugin ne demande une connexion vers le serveur.

## Compilation avec IntelliJ IDEA

1. Ouvre le projet Maven dans IntelliJ IDEA.
2. Vérifie que le JDK est configuré (Java 17 convient pour compiler une cible Java 8).
3. Dans la fenêtre Maven, ouvre `Lifecycle`.
4. Lance `clean`, puis `package`.
5. Récupère `target/GateConnect.jar`.

## Structure du projet

```text
GateConnect/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── fr/revivemc/gateconnect/
        │       └── GateConnect.java
        └── resources/
            ├── plugin.yml
            └── config.yml
```

## Auteur

Développé pour le réseau **ReviveMC**.
