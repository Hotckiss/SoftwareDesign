import boto3
import time
    
sqs = None
time.sleep(20)
while True:
    try:
        sqs = boto3.resource(service_name='sqs', endpoint_url='http://localstack:4576',
                             aws_access_key_id="key", aws_secret_access_key="password")
        break
    except:
        time.sleep(1)
        continue
    
A = sqs.create_queue(QueueName="A", Attributes={'DelaySeconds': '3'})
B = sqs.create_queue(QueueName="B", Attributes={'DelaySeconds': '3'})
A.send_message(MessageBody="1")

