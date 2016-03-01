package com.taxis99.aws.credentials

import com.amazonaws.auth.AWSCredentials

trait AWSCredentialsProvider {

  type AWSCredentialsImpl <: AWSCredentials

  def credentials(): AWSCredentialsImpl

}