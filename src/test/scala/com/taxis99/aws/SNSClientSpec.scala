package com.taxis99.aws

import org.mockito.Matchers.any
import org.mockito.Mockito.{ mock, times, verify, when }
import org.scalatest.{ BeforeAndAfter, MustMatchers, WordSpec }

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model._

class SNSClientSpec extends WordSpec with MustMatchers with BeforeAndAfter {
  class MockSNSClient(val sns: AmazonSNS = mock(classOf[AmazonSNS])) extends SNSClient(accessKey = "@key", secretKey = "@secret", topicName = "@topic", endpoint = "@snsEndpoint"){
    override def create() = {

      when(sns.createTopic(new CreateTopicRequest("@topic")))
        .thenReturn(new CreateTopicResult().withTopicArn("@topicArn"))
      when(sns.publish(any()))
        .thenReturn(new PublishResult().withMessageId("@messageId"))
      sns
    }
  }

  var snsClient: MockSNSClient = null
  before {
    snsClient = new MockSNSClient()
  }

  "A SNSClient" when {

    "publishes messages" should {

      "create topic on client usage" in {
        snsClient.publish("@message")
        verify(snsClient.sns, times(1)).createTopic(new CreateTopicRequest("@topic"))
      }

      "return messageId of the execution result" in {
        snsClient.publish("@message") must equal("@messageId")
      }

      "send a valid message without subject" in {
        snsClient.publish("@message")
        verify(snsClient.sns, times(1)).publish(new PublishRequest("@topicArn", "@message", null))
      }

      "send a valid message with subject" in {
        snsClient.publishWithSubject("@message", "@subject")
        verify(snsClient.sns, times(1)).publish(new PublishRequest("@topicArn", "@message", "@subject"))

      }
    }
  }

}
