package com.taxis99.aws

import scala.collection.JavaConversions._

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client

/**
 * Client to handle S3 Interface
 */
class S3Client(accessKey: String, secretKey: String, bucketName: String) {

  def create(): AmazonS3 = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey))

  lazy val client = create()

  def listFiles(prefix: String) = {
    client.listObjects(bucketName, prefix).getObjectSummaries.sortBy(_.getLastModified).reverse
  }

}
