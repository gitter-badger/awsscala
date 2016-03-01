package com.taxis99.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.sns.{ AmazonSNS, AmazonSNSClient }
import com.amazonaws.services.sns.model._
import com.taxis99.aws.credentials.AWSCredentialsProvider

/**
 * Client to handle SNS Interface
 */
class SNSClient(topicName: String, snsEndpoint: String)(implicit provider: AWSCredentialsProvider) {

  def create(awsCredentials: AWSCredentials = provider.credentials()): AmazonSNS = new AmazonSNSClient(awsCredentials)

  private lazy val (client, topicArn) = {
    val newClient = create()
    newClient.setEndpoint(snsEndpoint)
    (newClient, newClient.createTopic(new CreateTopicRequest(topicName)).getTopicArn)
  }

  def publish(message: String): String = publishWithSubject(message)

  def publishWithSubject(message: String, subject: String = null): String = {
    val result = client.publish(new PublishRequest()
      .withTopicArn(topicArn)
      .withSubject(subject)
      .withMessage(message))
    result.getMessageId
  }
}
