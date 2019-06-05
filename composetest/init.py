import boto3
import time
    
sqs = None
while True:
    try:
        sqs = boto3.resource(service_name='sqs', endpoint_url='http://localstack:4576')
        break
    except:
        time.sleep(1)
        continue
    
A = sqs.create_queue(QueueName="A")
B = sqs.create_queue(QueueName="B")
A.send_message(MessageBody="1")

