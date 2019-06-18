import boto3
import time

time.sleep(20)
sqs = None
triesCount = 0
timeoutCount = 30
while triesCount < timeoutCount:
    try:
        sqs = boto3.resource(service_name='sqs', endpoint_url='http://localstack:4576',
                             aws_access_key_id="key", aws_secret_access_key="password")
        break
    except:
        triesCount += 1
        if (triesCount + 1) == timeoutCount:
            print("Timeout was reached!")
        time.sleep(1)
        continue
    
A = sqs.create_queue(QueueName="A", Attributes={'DelaySeconds': '3'})
B = sqs.create_queue(QueueName="B", Attributes={'DelaySeconds': '3'})
A.send_message(MessageBody="1")

