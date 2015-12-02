package com.taxis99.aws


import scala.collection.JavaConverters._

import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.AmazonSQSAsyncClient
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sqs.model._

/**
  * Client to handle SQS Interface
  */
class SQSClient (accessKey: String, secretKey: String, queueName: String, endpoint: String) {

  def createClient(): AmazonSQSClient = new AmazonSQSAsyncClient(new BasicAWSCredentials(accessKey, secretKey))

  private lazy val (client, queueUrl) = {
    val newClient = createClient()
    newClient.setEndpoint(endpoint)
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

  def deleteMessage(message: Message): Unit = {
    if (message != null) {
      client.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle))
    }
  }

  def send(body: String): Unit = {
    client.sendMessage(new SendMessageRequest(queueUrl, body))
  }

  def approximateNumberOfMessages(): Integer = {
    client.getQueueAttributes(new GetQueueAttributesRequest(queueUrl, List("ApproximateNumberOfMessages").asJava))
      .getAttributes.get("ApproximateNumberOfMessages").toInt
  }
}
