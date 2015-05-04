name := "breeze-signal"

version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies  ++= Seq(
  "org.scalanlp" %% "breeze" % "0.11.2",
  // native libraries are not included by default. add this if you want them (as of 0.7)
  // native libraries greatly improve performance, but increase jar sizes.
  "org.scalanlp" %% "breeze-natives" % "0.11.2",
  "com.github.scopt" %% "scopt" % "3.3.0"
)

libraryDependencies += "org.scalanlp" %% "breeze-viz" % "0.11.2" 

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

resolvers += Resolver.sonatypeRepo("public")

shellPrompt in ThisBuild := { 
  state => Project.extract(state).currentRef.project + ">" 
}

shellPrompt := { state => System.getProperty("user.name") + "> " }
