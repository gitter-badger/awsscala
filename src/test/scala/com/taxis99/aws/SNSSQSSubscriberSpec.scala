package com.taxis99.aws

import scala.collection.JavaConversions._

import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.{ BeforeAndAfter, Finders, MustMatchers, WordSpec }

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model._
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model._
import com.amazonaws.services.sqs.AmazonSQS

class SNSSQSSubscriberSpec extends WordSpec with MustMatchers with BeforeAndAfter {
  class MockSNSSQSSubscriber(val sns: AmazonSNS, val sqs: AmazonSQS) extends SNSSQSSubscriber(accessKey = "@key", secretKey = "@secret", sqsEndpoint = "@sqsEndpoint", snsEndpoint = "@snsEndpoint"){
    override def createSNSClient() = {
      when(sns.createTopic(new CreateTopicRequest("@topic")))
        .thenReturn(new CreateTopicResult().withTopicArn("@topicArn"))
      when(sns.subscribe(any()))
        .thenReturn(new SubscribeResult().withSubscriptionArn("@subscriptionArn"))
      sns
    }
    override def createSQSClient() = {
      when(sqs.createQueue(new CreateQueueRequest("@queue")))
        .thenReturn(new CreateQueueResult().withQueueUrl("@queueUrl"))
      when(sqs.getQueueAttributes(any()))
        .thenReturn(new GetQueueAttributesResult().withAttributes(Map(QueueAttributeName.QueueArn.toString -> "@queueArn")))
      sqs
    }
  }

  var snsSqsSubscriber: MockSNSSQSSubscriber = null
  before {
    snsSqsSubscriber = new MockSNSSQSSubscriber(mock(classOf[AmazonSNS]), mock(classOf[AmazonSQS]))
  }

  "A SNSSQSSubscriber" when {

    "subscribes a queue to a topic" should {
      val queueName = "@queue"
      val topicName = "@topic"

      "create the expected queue and topic" in {
        snsSqsSubscriber.subscribeQueueToTopic(queueName, topicName)
        verify(snsSqsSubscriber.sqs, times(1)).createQueue(new CreateQueueRequest(queueName))
        verify(snsSqsSubscriber.sns, times(1)).createTopic(new CreateTopicRequest(topicName))
      }

      "call inner subscription methods" in {
        snsSqsSubscriber.subscribeQueueToTopic(queueName, topicName)
        verify(snsSqsSubscriber.sns, times(1)).subscribe(new SubscribeRequest()
          .withEndpoint("@queueArn")
          .withProtocol("sqs")
          .withTopicArn("@topicArn"))
        verify(snsSqsSubscriber.sns, times(1)).setSubscriptionAttributes(any())
        verify(snsSqsSubscriber.sqs, times(1)).setQueueAttributes(any())
      }
    }
  }
}
