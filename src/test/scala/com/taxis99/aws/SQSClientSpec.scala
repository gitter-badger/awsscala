package com.taxis99.aws

import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.{ Finders, MustMatchers, WordSpec }

import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.model.CreateQueueResult
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.amazonaws.services.sqs.model.ReceiveMessageResult

class SQSClientSpec extends WordSpec with MustMatchers {

  object SQSClient extends SQSClient(accessKey = "x", secretKey = "y", queueName = "queue", endpoint = "localhost:9000") {
    override def createClient() = {
      val client = mock(classOf[AmazonSQSClient])
      val queueUrl = "queueUrl"
      when(client.createQueue(any[CreateQueueRequest]()))
        .thenReturn(new CreateQueueResult().withQueueUrl(queueUrl))
      when(client.receiveMessage(any[ReceiveMessageRequest]()))
        .thenReturn(new ReceiveMessageResult().withMessages(new java.util.ArrayList[Message]()))
      client
    }
  }

  "SQSHelper" must {
    "receive nothing on empty list" in {
      SQSClient.fetchMessage must be(None)
    }
  }

}