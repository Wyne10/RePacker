plugins {
	`maven-publish`
	alias(libs.plugins.loom)
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(21))
	withSourcesJar()
}

version = findProperty("mod_version").toString()
group = findProperty("maven_group").toString()

base {
	archivesName.set(findProperty("archives_base_name").toString())
}

loom {
	accessWidenerPath.set(file("src/main/resources/repacker.accesswidener"))
}

dependencies {
	minecraft("com.mojang:minecraft:${findProperty("minecraft_version")}")
	mappings("net.fabricmc:yarn:${findProperty("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${findProperty("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${findProperty("fabric_version")}")
}

tasks.withType(net.fabricmc.loom.task.AbstractRunTask::class) {
	javaLauncher = javaToolchains.launcherFor {
		vendor = JvmVendorSpec.JETBRAINS
		languageVersion = JavaLanguageVersion.of(21)
	}
	jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}

tasks {
	processResources {
		inputs.property("version", version)

		filesMatching("fabric.mod.json") {
			expand("version" to version)
		}
	}

	jar {
		from("LICENSE") {
			rename { "${it}_${findProperty("archives_base_name")}"}
		}
	}
}

