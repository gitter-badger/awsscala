package com.taxis99.aws

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sns.model.CreateTopicRequest
import com.amazonaws.services.sns.util.Topics
import com.amazonaws.services.sns.{AmazonSNS, AmazonSNSAsyncClient}
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSAsyncClient}

class SNSSQSSubscriber {

  class SNSSQSSubscriber (accessKey: String, secretKey: String, sqsEndpoint: String, snsEndpoint: String) {

    def createSNSClient(): AmazonSNS = new AmazonSNSAsyncClient(new BasicAWSCredentials(accessKey, secretKey))
    def createSQSClient(): AmazonSQS = new AmazonSQSAsyncClient(new BasicAWSCredentials(accessKey, secretKey))

    private lazy val (snsClient, sqsClient) = {

      val newSqsClient = createSQSClient()
      newSqsClient.setEndpoint(sqsEndpoint)

      val newSnsClient = createSNSClient()
      newSnsClient.setEndpoint(snsEndpoint)
      (newSnsClient, newSqsClient)
    }

    def subscribeQueueToTopic(queueName: String, topicName: String, rawMessageDelivery: Boolean = true) = {
      val queueUrl = sqsClient.createQueue(new CreateQueueRequest(queueName)).getQueueUrl
      val topicArn = snsClient.createTopic(new CreateTopicRequest(topicName)).getTopicArn
      val subscriptionArn = Topics.subscribeQueue(snsClient, sqsClient, topicArn, queueUrl)
      snsClient.setSubscriptionAttributes(subscriptionArn, "RawMessageDelivery", rawMessageDelivery.toString)
    }
  }

}
