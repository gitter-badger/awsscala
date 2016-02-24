package com.taxis99.aws

import scala.collection.JavaConverters._

import org.mockito.Matchers.{ any, anyString }
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
      when(sqs.getQueueAttributes(any()))
        .thenReturn(new GetQueueAttributesResult().withAttributes(Map(QueueAttributeName.ApproximateNumberOfMessages.toString -> "1").asJava))
      sqs
    }
  }

  var sqsClient: MockSQSClient = null
  before {
    sqsClient = new MockSQSClient()
  }

  "A SQSClient" when {

    "fetch message" should {

      "create queue on client usage" in {

        sqsClient.fetchMessage
        verify(sqsClient.sqs, times(1)).createQueue(new CreateQueueRequest("@queue"))
      }

      "use inner client receiveMessage" in {
        sqsClient.fetchMessage
        verify(sqsClient.sqs, times(1)).receiveMessage(any[ReceiveMessageRequest]())
      }

      "receive nothing on empty list" in {
        val messages = sqsClient.fetchMessage
        messages must be(None)
      }
    }

    "delete message" should {

      "create queue on client usage" in {
        val messageMock = mock(classOf[Message])
        sqsClient.deleteMessage(messageMock)
        verify(sqsClient.sqs, times(1)).createQueue(new CreateQueueRequest("@queue"))
      }

      "use inner client deleteMessage" in {
        val messageMock = mock(classOf[Message])
        sqsClient.deleteMessage(messageMock)
        verify(sqsClient.sqs, times(1)).deleteMessage(any[DeleteMessageRequest]())
      }
    }
 
    "send message" should {

      "create queue on client usage" in {
        sqsClient.send("@message")
        verify(sqsClient.sqs, times(1)).createQueue(new CreateQueueRequest("@queue"))
      }

      "use inner client sendMessage" in {
        sqsClient.send("@message")
        verify(sqsClient.sqs, times(1)).sendMessage(new SendMessageRequest("@queueUrl", "@message"))
      }
    }
    
    "checks approximateNumberOfMessages" should {

      "create queue on client usage" in {
        sqsClient.approximateNumberOfMessages()
        verify(sqsClient.sqs, times(1)).createQueue(new CreateQueueRequest("@queue"))
      }

      "use inner client getQueueAttributes" in {
        sqsClient.approximateNumberOfMessages()
        verify(sqsClient.sqs, times(1)).getQueueAttributes(any[GetQueueAttributesRequest]())
      }

      "receive a number" in {
        val number = sqsClient.approximateNumberOfMessages()
        number must be(1)
      }
    }

    "delete messages" should {

      "create queue on client usage" in {
        val messageMock = mock(classOf[Message])
        sqsClient.deleteMessages(List(messageMock))
        verify(sqsClient.sqs, times(1)).createQueue(new CreateQueueRequest("@queue"))
      }

      "use inner client deleteMessageBatch" in {
        val messageMock = mock(classOf[Message])
        sqsClient.deleteMessages(List(messageMock))
        verify(sqsClient.sqs, times(1)).deleteMessageBatch(anyString, any[java.util.List[DeleteMessageBatchRequestEntry]]())
      }
    }

  }

}