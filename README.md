# clj-so

Tweet bot to publish clojure/clojurescript questions on [StackOverflow](https://stackoverflow.com) on [Twitter](https://twitter.com/).

## Usage

```shell
$ lein do clean, uberjar

$ java -jar clj-so-0.1.0-standalone.jar [args]
```

## Install on AWS

```shell
aws lambda create-function \
    --function-name clj-so \
    --handler clj-so.aws-lambda.TweetPublisher::publish \
    --runtime java8 \
    --memory 512 \
    --timeout 10 \
    --role arn:aws:iam::123456789:role/lambda_exec_role \
    --zip-file fileb://./target/uberjar/clj-so-0.1.0-SNAPSHOT-standalone.jar

aws lambda delete-function \
    --function-name clj-so

aws lambda update-function-code \
    --function-name clj-so \
    --zip-file fileb://./target/uberjar/clj-so-0.1.0-SNAPSHOT-standalone.jar

# Buckit to save the last update time
aws s3api create-bucket \
    --bucket clojureatso \
    --region ap-southeast-1

aws events put-rule \
    --name clj-so-schedule-rule \
    --schedule-expression 'rate(2 hours)' \
    --region ap-southeast-1

aws lambda add-permission \
    --function-name clj-so \
    --statement-id my-statementid \
    --action 'lambda:InvokeFunction' \
    --principal events.amazonaws.com \
    --source-arn arn:aws:events:ap-southeast-1:123456789:rule/clj-so-schedule-rule \
    --region ap-southeast-1

aws events put-targets \
    --rule clj-so-schedule-rule \
    --targets '{"Id":"1","Arn":"arn:aws:lambda:ap-southeast-1:123456789:function:clj-so"}' \
    --region ap-southeast-1
```

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
