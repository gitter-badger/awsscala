package com.taxis99.aws.credentials

import org.scalatest.{ MustMatchers, WordSpec }

class BasicAWSCredentialsProviderSpec extends WordSpec with MustMatchers {

  "A BasicAWSCredentialsProvider" when {

    "provides credentials" should {
      val accessKey = "@key"
      val secretKey = "@secret"

      "generate them in AWS format" in {
        val credentials = BasicAWSCredentialsProvider(accessKey, secretKey).credentials()
        credentials.getAWSAccessKeyId() must be(accessKey)
        credentials.getAWSSecretKey() must be(secretKey)
      }
    }
  }
}