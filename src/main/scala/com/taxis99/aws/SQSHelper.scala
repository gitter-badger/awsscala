package com.taxis99.aws

/**
 * Helper to handle SQS Interface
 */
@deprecated("Use SQSClient instead", since="v0.3.6" )
class SQSHelper(accessKey: String, secretKey: String, queueName: String, endpoint: String) extends SQSClient(accessKey, secretKey, queueName, endpoint)
