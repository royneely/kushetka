<p align="center">
  <br>
  <img src='./docs/kushetka_logo.png' width='260'>
  <br>
  <br>
</p> 
<br>

**Kushetka** is a kotlin CouchDb client. 

```kotlin
    val couchDbServer = Kushetka.getServer("http://localhost:5984")
    val catsDb = couchDbServer.getDb("cats")

    data class Cat(
        val name: String,
        val lazinessScore: Int,
        val maliciousDisposition: Boolean = true
    )

    listOf(
        Cat("lucy", 45, true),
        Cat("mittens", 87, true),
        Cat("george", 52, false)
    ).forEach { cat ->
        catsDb.insert(cat, cat.name)
    }

    val mittens = catsDb.findById<Cat>("mittens") ?: error("mittens could not be found")

    assertTrue(mittens.lazinessScore == 87)
```

## Get Started

Add bintray repo to your repositories
```kotlin
repositories {
    maven { setUrl("https://dl.bintray.com/evyy/kushetka") }
}
```

Add dependency as usual
```kotlin
dependencies {
    compile("com.fieldkt:kushetka:0.0.2.DEV")
}
```
