cf login -a https://api.run.pivotal.io -u luwenbin1016@126.com -p ab022803@ -o luwenbin1016 -s space1
cf service 'rabbit1' | grep 'succeeded'
if [ $? -eq 1 ]
	then
	cf create-service CloudAMQP 'Little Lemur' rabbit1
	echo 'create new rabbit mq service rabbit1'
fi

if [ -f "./manifest.yml"]
	then 
	echo 'use manifest.yml to cf push'
	cf push -p ./ackgroundService-0.0.1-SNAPSHOT.jar
	cf push -p BackgroundService-0.0.1-SNAPSHOT.jar --health-check-type=none
else
	echo 'use command line argument to push'
	cf bind-service backgroundService rabbit1
	cf push backgroundService -p BackgroundService-0.0.1-SNAPSHOT.jar --no-route --health-check-type=none
	echo 'backgroundService deployed'
fi