import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("io.papermc.paperweight.userdev") version "1.3.7-SNAPSHOT"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("java")
}

group = "de.amin"
version = "1.2"

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("1.19-R0.1-SNAPSHOT")
}

bukkit {
    main = "de.amin.trading.TradingPlugin"
    apiVersion = "1.18"
    commands {
        register("trade") {
            description = "Command for trading"
            permission = "trading.trade"
        }
    }
    permissions {
        register("trading.trade") {
            description = "Allows a Player to send and receive trading requests"
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}