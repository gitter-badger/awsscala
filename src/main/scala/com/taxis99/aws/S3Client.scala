package com.taxis99.aws

import scala.collection.JavaConversions._

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client

/**
 * Client to handle S3 Interface
 */
class S3Client(accessKey: String, secretKey: String, bucketName: String) {

  @deprecated("Use S3Client's create instead", since="v0.3.6" )
  def createClient(): AmazonS3Client = create()

  def create(): AmazonS3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey))

  lazy val client = create()

  def listFiles(prefix: String) = {
    client.listObjects(bucketName, prefix).getObjectSummaries.sortBy(_.getLastModified).reverse
  }

}
