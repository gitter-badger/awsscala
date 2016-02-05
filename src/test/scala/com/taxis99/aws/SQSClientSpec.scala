package com.taxis99.aws

import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.{ Finders, MustMatchers, WordSpec }

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model._

class SQSClientSpec extends WordSpec with MustMatchers {

  object SQSClient extends SQSClient(accessKey = "@key", secretKey = "@secret", queueName = "@queue", endpoint = "@sqsEndpoint") {
    override def create() = {

      val client = mock(classOf[AmazonSQS])
      val queueUrl = "queueUrl"
      when(client.createQueue(any[CreateQueueRequest]()))
        .thenReturn(new CreateQueueResult().withQueueUrl(queueUrl))
      when(client.receiveMessage(any[ReceiveMessageRequest]()))
        .thenReturn(new ReceiveMessageResult().withMessages(new java.util.ArrayList[Message]()))
      client
    }
  }

  "A SQSClient" must {
    "receive nothing on empty list" in {
      SQSClient.fetchMessage must be(None)
    }
  }

}