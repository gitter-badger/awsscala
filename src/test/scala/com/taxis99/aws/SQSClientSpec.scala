package com.taxis99.aws

import org.mockito.Matchers.any
import org.mockito.Mockito.{ mock, times, verify, when }
import org.scalatest.{ BeforeAndAfter, MustMatchers, WordSpec }

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model._

class SQSClientSpec extends WordSpec with MustMatchers with BeforeAndAfter {

  class MockSQSClient(val sqs: AmazonSQS = mock(classOf[AmazonSQS])) extends SQSClient(accessKey = "@key", secretKey = "@secret", queueName = "@queue", endpoint = "@sqsEndpoint") {
    override def create() = {
      val queueUrl = "@queueUrl"
      when(sqs.createQueue(any[CreateQueueRequest]()))
        .thenReturn(new CreateQueueResult().withQueueUrl(queueUrl))
      when(sqs.receiveMessage(any[ReceiveMessageRequest]()))
        .thenReturn(new ReceiveMessageResult().withMessages(new java.util.ArrayList[Message]()))
      sqs
    }
  }

  var sqsClient: MockSQSClient = null
  before {
    sqsClient = new MockSQSClient()
  }

  "A SQSClient" must {

    "create queue on client usage" in {
      sqsClient.fetchMessage
      verify(sqsClient.sqs, times(1)).createQueue(new CreateQueueRequest("@queue"))
    }

    "receive nothing on empty list" in {
      val messages = sqsClient.fetchMessage
      verify(sqsClient.sqs, times(1)).receiveMessage(any[ReceiveMessageRequest]())
      messages must be(None)
    }
  }

}