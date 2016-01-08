package com.taxis99.aws

import org.scalatest.{ BeforeAndAfter, MustMatchers, WordSpec }
import org.mockito.Mockito._
import org.mockito.Matchers.{ eq => eq2, _ }

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.{ PublishResult, PublishRequest, CreateTopicResult, CreateTopicRequest }

class SNSClientSpec extends WordSpec with MustMatchers with BeforeAndAfter {
  class MockSNSClient(val client: AmazonSNS = mock(classOf[AmazonSNS])) extends SNSClient("@key", "@secret", "@topic", "@endpoint"){
    override def create() = {

      when(client.createTopic(new CreateTopicRequest("@topic")))
        .thenReturn(new CreateTopicResult().withTopicArn("@arn"))
      when(client.publish(any()))
        .thenReturn(new PublishResult().withMessageId("@messageId"))
      client
    }
  }


  var snsClient: MockSNSClient = null
  before {
    snsClient = new MockSNSClient()
  }

  "A SNSClient" when {

    "publishes messages" should {
      "return messageId of the execution result" in {
        snsClient.publish("@message") must equal("@messageId")
      }
      "send a valid message without subject" in {
        snsClient.publish("@message")
        verify(snsClient.client).publish(new PublishRequest("@arn", "@message", null))
      }

      "send a valid message with subject" in {
        snsClient.publishWithSubject("@message", "@subject")
        verify(snsClient.client).publish(new PublishRequest("@arn", "@message", "@subject"))

      }
    }
  }

}
