import sbtrelease.ReleaseStateTransformations._

organization := "com.github.fuqiliang"

name := "sbt-protobuf"

scalacOptions := Seq("-deprecation", "-unchecked", "-Xlint", "-Yno-adapted-args")

scalacOptions in (Compile, doc) ++= {
  val tagOrBranch = if(isSnapshot.value) {
    sys.process.Process("git rev-parse HEAD").lines_!.head
  } else {
    "v" + version.value
  }
  Seq(
    "-sourcepath", baseDirectory.value.getAbsolutePath,
    "-doc-source-url", "https://github.com/sbt/sbt-protobuf/blob/" + tagOrBranch + "€{FILE_PATH}.scala"
  )
}

sbtPlugin := true

publishMavenStyle := true

bintrayOrganization := Some("sbt")

bintrayRepository := "sbt-plugin-releases"

bintrayPackage := "sbt-protobuf"

bintrayReleaseOnPublish := false

ScriptedPlugin.scriptedSettings

scriptedBufferLog := false

scriptedLaunchOpts += s"-Dplugin.version=${version.value}"
scriptedLaunchOpts += s"-Dprotoc-jar.version=3.5.0"

crossSbtVersions := Seq("0.13.17", "1.0.4")

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  releaseStepCommandAndRemaining("^ test"),
  releaseStepCommandAndRemaining("^ scripted"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("^ publish"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

//add for sbt-sonatype
useGpg := true
publishTo := sonatypePublishTo.value
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
pomExtra in Global := {
  <url>https://github.com/fuqiliang/sbt-protobuf</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <url>fuqiliang@github.com:fuqiliang/sbt-protobuf.git</url>
      <connection>scm:git:fuqiliang@github.com:fuqiliang/sbt-protobuf.git</connection>
    </scm>
    <developers>
      <developer>
        <id>fuqiliang</id>
        <name>fuqiliang</name>
        <url>https://github.com/fuqiliang/</url>
      </developer>
    </developers>
}

