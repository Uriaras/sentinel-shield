PS E:\MinecraftMods\SentinelShield> .\gradlew.bat runClient

> Configure project :
Fabric Loom: 1.17.17

> Task :compileClientJava FAILED
E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:6: error: class ShieldTintResolver is public, should be declared in a file named ShieldTintResolver.java
public final class ShieldTintResolver {
             ^
E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTintResolver.java:6: error: duplicate class: com.nythral.sentinel.client.shield.ShieldTintResolver
public final class ShieldTintResolver {
             ^
E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:10: error: cannot find symbol
        public static ShieldTint resolve(ShieldState state) {
                      ^
  symbol:   class ShieldTint
  location: class ShieldTintResolver
E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:35: error: cannot find symbol
        private static ShieldTint fromHex(
                       ^
  symbol:   class ShieldTint
  location: class ShieldTintResolver
E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:15: error: cannot find symbol
                        return ShieldTint.NONE;
                               ^
  symbol:   variable ShieldTint
  location: class ShieldTintResolver
E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:26: error: cannot find symbol
                        return ShieldTint.NONE;
                               ^
  symbol:   variable ShieldTint
  location: class ShieldTintResolver
E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:61: error: cannot find symbol
                return new ShieldTint(
                           ^
  symbol:   class ShieldTint
  location: class ShieldTintResolver
7 errors

[Incubating] Problems report is available at: file:///E:/MinecraftMods/SentinelShield/build/reports/problems/problems-report.html

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':compileClientJava' (registered by plugin class 'org.gradle.api.plugins.JavaBasePlugin').
> Compilation failed; see the compiler output below.
  E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:10: error: cannot find symbol
        public static ShieldTint resolve(ShieldState state) {
                      ^
    symbol:   class ShieldTint
    location: class ShieldTintResolver
  E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:35: error: cannot find symbol
        private static ShieldTint fromHex(
                       ^
    symbol:   class ShieldTint
    location: class ShieldTintResolver
  E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:15: error: cannot find symbol
                        return ShieldTint.NONE;
                               ^
    symbol:   variable ShieldTint
    location: class ShieldTintResolver
  E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:26: error: cannot find symbol
                        return ShieldTint.NONE;
                               ^
    symbol:   variable ShieldTint
    location: class ShieldTintResolver
  E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:61: error: cannot find symbol
                return new ShieldTint(
                           ^
    symbol:   class ShieldTint
    location: class ShieldTintResolver
  E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTintResolver.java:6: error: duplicate class: com.nythral.sentinel.client.shield.ShieldTintResolver
  public final class ShieldTintResolver {
               ^
  E:\MinecraftMods\SentinelShield\src\client\java\com\nythral\sentinel\client\shield\ShieldTint.java:6: error: class ShieldTintResolver is public, should be declared in a file named ShieldTintResolver.java
  public final class ShieldTintResolver {
               ^
  7 errors

* Try:
> Check your code and dependencies to fix the compilation error(s)
> Run with --scan to get full insights from a Build Scan (powered by Develocity).

BUILD FAILED in 1s
3 actionable tasks: 1 executed, 2 up-to-date