[![Build Status](https://travis-ci.org/mcparkournet/impass.svg?branch=master)](https://travis-ci.org/mcparkournet/impass)
[![Download](https://api.bintray.com/packages/mcparkour/maven-public/impass-core/images/download.svg)](https://bintray.com/mcparkour/maven-public/impass-core/_latestVersion)

# Impass

Impass allows you to access unavailable members using dynamic proxy classes. It is used mainly to access implementation members, which are not exposed in the API, such as some CraftBukkit or NMS methods.

## Usage

Add dependency to project:

```kotlin
repositories {
	jcenter()
}

dependencies {
	implementation("net.mcparkour:impass-bukkit:1.0.7")
}
```

Create accessor interface:

```java
import net.mcparkour.impass.annotation.method.Method;
import net.mcparkour.impass.annotation.type.CraftBukkitType;
import net.mcparkour.impass.instance.InstanceAccessor;

@CraftBukkitType("CraftPlayer") //Refers to org.bukkit.craftbukkit.<version>.CraftPlayer
public interface CraftPlayerAccessor extends InstanceAccessor {

	@Method("refreshPlayer")
	void refreshPlayer();
}
```

Create accessor factory:

```java
org.bukkit.Server server = ...
AccessorFactory accessorFactory = new BukkitAccessorFactory(server);
```

Create instance of accessor and invoke defined there method:

```java
AccessorFactory accessorFactory = ...

org.bukkit.entity.Player player = ...
CraftPlayerAccessor accessor = accessorFactory.createInstanceAccessor(CraftPlayerAccessor.class, player);

accessor.refreshPlayer(); //Invokes CraftPlayer#refreshPlayer()
```
