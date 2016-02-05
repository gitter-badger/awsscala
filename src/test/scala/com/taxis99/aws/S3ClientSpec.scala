package com.taxis99.aws

import org.mockito.Matchers.anyString
import org.mockito.Mockito._
import org.scalatest.{ Finders, MustMatchers, WordSpec }

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model._

class S3ClientSpec extends WordSpec with MustMatchers {

  object S3Client extends S3Client(accessKey = "@key", secretKey = "@secret", bucketName = "@bucket") {
    override def create() = {

      val client = mock(classOf[AmazonS3])
      val objectListing = mock(classOf[ObjectListing])
      when(objectListing.getObjectSummaries())
        .thenReturn(new java.util.ArrayList[S3ObjectSummary]())
      when(client.listObjects(anyString(), anyString()))
        .thenReturn(objectListing)
      client
    }
  }

  "A S3Client" must {
    "receive nothing on empty bucket" in {
      S3Client.listFiles("prefix") must have size(0)
    }
  }

}