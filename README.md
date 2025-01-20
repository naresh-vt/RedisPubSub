# RedisPubSub
ttt
1. Build the project: mvn clean package"

2. Run the Subscriber with: mvn exec:java -Dexec.mainClass="com.prototype.pubsub.subscriber.MessageSubscriber"

3. You can run as many instances of the subscribers that you want in different terminal. Each instance will act a different subscriber.

4. Run the publisher: mvn exec:java -Dexec.mainClass="com.prototype.pubsub.publisher.MessagePublisher"

5. You can change the publisher and subsciber to pass the redis-channel as configuration. Current version is not supporting this.

6. To run this you need install redis in your local and the redis server should be running on the port : 6379
