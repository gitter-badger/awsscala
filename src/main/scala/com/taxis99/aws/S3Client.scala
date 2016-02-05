package com.taxis99.aws

import java.io.{ File => JFile }

import scala.collection.JavaConversions._

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.{ AmazonS3, AmazonS3Client }
import com.amazonaws.services.s3.model._

/**
 * Client to handle S3 Interface
 */
class S3Client(accessKey: String, secretKey: String, bucketName: String) {

  def create(): AmazonS3 = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey))

  private lazy val client = create()

  def uploadFile(key: String, file: JFile): PutObjectResult = client.putObject(bucketName, key, file)

  def listFiles(prefix: String) = {
    client.listObjects(bucketName, prefix).getObjectSummaries.sortBy(_.getLastModified).reverse
  }

}
