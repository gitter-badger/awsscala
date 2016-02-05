package com.taxis99.aws

import java.io.{ File => JFile }

import scala.collection.JavaConversions._

import org.mockito.Matchers.anyString
import org.mockito.Mockito.{ mock, times, verify, when }
import org.scalatest.{ BeforeAndAfter, MustMatchers, WordSpec }

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model._

class S3ClientSpec extends WordSpec with MustMatchers with BeforeAndAfter {

  class MockS3Client(val s3: AmazonS3 = mock(classOf[AmazonS3])) extends S3Client(accessKey = "@key", secretKey = "@secret", bucketName = "@bucket") {
    override def create() = {
      val objectListing = mock(classOf[ObjectListing])
      when(objectListing.getObjectSummaries())
        .thenReturn(List[S3ObjectSummary]())
      when(s3.listObjects(anyString(), anyString()))
        .thenReturn(objectListing)
      s3
    }
  }

  var s3Client: MockS3Client = null
  before {
    s3Client = new MockS3Client()
  }

  "A S3Client" when {

    "list files" should {

      "receive nothing on empty bucket" in {
        val listFiles = s3Client.listFiles("@prefix")
        verify(s3Client.s3, times(1)).listObjects("@bucket", "@prefix")
        listFiles must have size (0)
      }
    }

    "upload file" should {

      "use inner client putObject" in {
        val file = mock(classOf[JFile])
        s3Client.uploadFile("@key", file)
        verify(s3Client.s3, times(1)).putObject("@bucket", "@key", file)
      }
    }
  }

}