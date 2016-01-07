package com.taxis99.aws

/**
 * Helper to handle S3 Interface
 */
@deprecated("Use S3Client instead", since="v0.3.6" )
class S3Helper(accessKey: String, secretKey: String, bucketName: String) extends S3Client(accessKey, secretKey, bucketName)

