package com.taxis99.aws

import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.{ Finders, MustMatchers, WordSpec }

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.S3ObjectSummary

class S3ClientSpec extends WordSpec with MustMatchers {

  object S3Client extends S3Client(accessKey = "x", secretKey = "y", bucketName = "bucket") {
    override def createClient() = {
      val client = mock(classOf[AmazonS3Client])
      val objectListing = mock(classOf[ObjectListing])
      when(objectListing.getObjectSummaries())
        .thenReturn(new java.util.ArrayList[S3ObjectSummary]())
      when(client.listObjects(anyString(), anyString()))
        .thenReturn(objectListing)
      client
    }
  }

  "S3Client" must {
    "receive nothing on empty bucket" in {
      S3Client.listFiles("prefix") must have size(0)
    }
  }

}