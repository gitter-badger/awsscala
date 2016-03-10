# awsscala

[![Build Status](https://travis-ci.org/99taxis/awsscala.svg?branch=master)](https://travis-ci.org/99taxis/awsscala "Travis CI") [![Coverage Status](https://coveralls.io/repos/github/99taxis/awsscala/badge.svg?branch=master)](https://coveralls.io/github/99taxis/awsscala?branch=master "Coveralls") [![Dependencies](https://app.updateimpact.com/badge/704215565069324288/awsscala.svg?config=compile)](https://app.updateimpact.com/latest/704215565069324288/awsscala) [![Codacy Badge](https://api.codacy.com/project/badge/grade/106d6d09bfe746aa85a1d6c51803e01b)](https://www.codacy.com/app/migmruiz/awsscala)

AWS SDK Java to Scala translations, wrappers and reinventions

[![License](http://img.shields.io/:license-Apache%202-red.svg)](https://github.com/99taxis/awsscala/blob/master/LICENSE "Apache 2.0 Licence")

## Using this library

For sbt builds, add the following to your build.sbt:

```scala
resolvers += "bintray.99taxis OS releases" at "http://dl.bintray.com/content/99taxis/maven"
libraryDependencies += "com.taxis99" %% "awsscala" % "X.Y.Z",
```

The version comes from the corresponding Git tag.

## Instructions for Development

We recommend you to publish your library to a [BinTray](https://bintray.com/) public repository .
To publish your library to BinTray follow these steps:

* [Create a BinTray account](https://bintray.com/)
* In BinTray get your API Key from [your profile page](https://bintray.com/profile/edit)
* From the command line login to BinTray: `activator bintray::changeCredentials`
* Change the version via Git:  `git tag vX.Y.Z`
* Publish your library: `activator +bintray::publish`
                Note: The `+` publishes the cross-versioned (e.g. Scala 2.10 & 2.11) builds.
                
                
To enable others to use your library you can either have them add a new resolver / repository to their build or you can [add your library to Maven Central via jCenter](http://blog.bintray.com/2014/02/11/bintray-as-pain-free-gateway-to-maven-central/).

For the former option do have your scala users add the following to their build.sbt:

```scala
resolvers += "YOUR_BINTRAY_USERNAME" at "http://dl.bintray.com/content/YOUR_BINTRAY_USERNAME/maven"
```

## License

`awsscala` is open source software released under the Apache 2.0 License.

See the [LICENSE](https://github.com/99taxis/awsscala/blob/master/LICENSE) file for details.
