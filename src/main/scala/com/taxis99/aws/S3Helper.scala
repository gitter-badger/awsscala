package com.taxis99.aws


@deprecated("Use S3Client instead", since="v0.3.5" )
class S3Helper(accessKey: String, secretKey: String, bucketName: String) extends S3Client(accessKey, secretKey, bucketName)

