package com.taxis99.aws

import scala.collection.JavaConverters._

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.sqs.{ AmazonSQS, AmazonSQSClient }
import com.amazonaws.services.sqs.model._
import com.taxis99.aws.credentials.AWSCredentialsProvider

/**
 * Client to handle SQS Interface
 */
abstract class SQSClient(queueName: String, sqsEndpoint: String)(implicit provider: AWSCredentialsProvider) {

  def create(awsCredentials: AWSCredentials = provider.credentials()): AmazonSQS = new AmazonSQSClient(awsCredentials)

  private lazy val (client, queueUrl) = {
    val newClient = create()
    newClient.setEndpoint(sqsEndpoint)
    val newQueueUrl = newClient.createQueue(new CreateQueueRequest(queueName)).getQueueUrl
    (newClient, newQueueUrl)
  }

  def fetchMessage() = fetchMessages(maxNumberOfMessages = 1).headOption

  /**
   * @param maxNumberOfMessages must be between 1 and 10.
   */
  def fetchMessages(maxNumberOfMessages: Int): List[Message] = {
    val request = (new ReceiveMessageRequest(queueUrl)).withMaxNumberOfMessages(maxNumberOfMessages).withAttributeNames("ApproximateReceiveCount", "SentTimestamp")
    client.receiveMessage(request).getMessages().asScala.toList
  }

  def deleteMessage(message: Message) {
    if (message != null) {
      client.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle))
    }
  }

  def deleteMessages(messages: List[Message]) {
    if (messages.nonEmpty) {
      client.deleteMessageBatch(queueUrl, messages.map { message =>
        new DeleteMessageBatchRequestEntry()
          .withId(message.getMessageId)
          .withReceiptHandle(message.getReceiptHandle)
      }.asJava)
    }
  }

  def send(body: String): Unit = {
    client.sendMessage(new SendMessageRequest(queueUrl, body))
  }

  def approximateNumberOfMessages(): Integer = {
    val approxNumOfMessagesAttributeName = QueueAttributeName.ApproximateNumberOfMessages.toString
    client.getQueueAttributes(new GetQueueAttributesRequest(queueUrl, List(approxNumOfMessagesAttributeName).asJava))
      .getAttributes.get(approxNumOfMessagesAttributeName).toInt
  }
}
