import boto3
import sys
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

A = None
B = None
triesCount = 0
while triesCount < timeoutCount:
    try:
        A = sqs.get_queue_by_name(QueueName=sys.argv[1])
        B = sqs.get_queue_by_name(QueueName=sys.argv[2])
        break
    except:
        triesCount += 1
        if (triesCount + 1) == timeoutCount:
            print("Timeout was reached!")
        time.sleep(1)
        continue

while True:
    for message in A.receive_messages():
        value = int(message.body)
        message.delete()
        B.send_message(MessageBody=str(value + 1))
        print(sys.argv[1] + " recieved " + str(value), flush=True)
