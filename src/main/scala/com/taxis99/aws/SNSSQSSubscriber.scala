package com.taxis99.aws

import scala.collection.JavaConversions._

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.policy.{ Policy, Principal, Resource, Statement }
import com.amazonaws.auth.policy.Statement.Effect
import com.amazonaws.auth.policy.actions.SQSActions
import com.amazonaws.auth.policy.conditions.ConditionFactory
import com.amazonaws.services.sns.{ AmazonSNS, AmazonSNSAsyncClient }
import com.amazonaws.services.sns.model.{ CreateTopicRequest, SetSubscriptionAttributesRequest, SubscribeRequest }
import com.amazonaws.services.sqs.{ AmazonSQS, AmazonSQSAsyncClient }
import com.amazonaws.services.sqs.model.{ CreateQueueRequest, GetQueueAttributesRequest, QueueAttributeName, SetQueueAttributesRequest }

/**
 * Subscribes SQS queues to SNS topics
 */
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

  def subscribeQueueToTopic(queueName: String, topicName: String, rawMessageDelivery: Boolean = true) {
    val queueUrl = sqsClient.createQueue(new CreateQueueRequest(queueName)).getQueueUrl

    val queueArn = sqsClient.getQueueAttributes(new GetQueueAttributesRequest(queueUrl)
      .withAttributeNames(QueueAttributeName.QueueArn.toString)
    ).getAttributes().get(QueueAttributeName.QueueArn.toString)

    val topicArn = snsClient.createTopic(new CreateTopicRequest(topicName)).getTopicArn

    // Everything below is equivalent of doing
    //
    //    val subscriptionArn = Topics.subscribeQueue(snsClient, sqsClient, topicArn, queueUrl)
    //    snsClient.setSubscriptionAttributes(subscriptionArn, "RawMessageDelivery", rawMessageDelivery.toString)
    //
    // with the exception that this marks the subscription as RawMessageDelivery
    // before allowing the SNS topic in the SQS queue

    val subscribeResult = snsClient.subscribe(new SubscribeRequest()
      .withEndpoint(queueArn)
      .withProtocol("sqs")
      .withTopicArn(topicArn)
    )

    val subscriptionArn = subscribeResult.getSubscriptionArn

    snsClient.setSubscriptionAttributes(new SetSubscriptionAttributesRequest()
      .withSubscriptionArn(subscriptionArn)
      .withAttributeName("RawMessageDelivery")
      .withAttributeValue(rawMessageDelivery.toString)
    )

    val policy = new Policy().withStatements(new Statement(Effect.Allow)
      .withId(s"topic-subscription-$topicArn")
      .withPrincipals(Principal.AllUsers)
      .withActions(SQSActions.SendMessage)
      .withResources(new Resource(queueArn))
      .withConditions(ConditionFactory.newSourceArnCondition(topicArn))
    )

    sqsClient.setQueueAttributes(new SetQueueAttributesRequest()
      .withQueueUrl(queueUrl)
      .withAttributes(Map(QueueAttributeName.Policy.toString -> policy.toJson))
    )
  }

}
