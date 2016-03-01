package com.taxis99.aws.credentials

import com.amazonaws.auth.BasicAWSCredentials

class BasicAWSCredentialsProvider(accessKey: String, secretKey: String) extends AWSCredentialsProvider {

  type AWSCredentialsImpl = BasicAWSCredentials

  def credentials(): AWSCredentialsImpl = new BasicAWSCredentials(accessKey, secretKey)

}

object BasicAWSCredentialsProvider {
  def apply(accessKey: String, secretKey: String): AWSCredentialsProvider = new BasicAWSCredentialsProvider(accessKey, secretKey)
}