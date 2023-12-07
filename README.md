# pulse-workspace

Pulse as a Microservice Client Extension

## Order of Deployment

In order to honour the dependencies and avoid exceptions during deployment, you need to deploy the client extensions in the following order.

1. pulse-batch-object-dependencies
2. pulse-batch-object-definition
3. pulse-batch-object-entry
4. pulse-micro-service
5. pulse-reporting
6. pulse-user-interface

*You can deploy the pulse-micro-service before the pulse-object-definition to avoid exceptions in the logs suggesting the Object Actions are not defined but these will resolve themselves once the microservice is deployed.*

When the microservice is started it will attempt to refresh the cache from the Liferay Object definitions so this needs to be started once Liferay DXP is started.

## Testing the microservice

### Liferay OAuth2 Client

When the following is executed within a Liferay context then the list of URL tokens currently in the Pulse cache (H2 in-memory DB) is returned.

Liferay.OAuth2Client.FromUserAgentApplication("pulse-micro-service-oauth-application-user-agent").fetch("http://localhost:58080/api/url-tokens").then((response) => console.log(response))

The following will create a new campaign within the Pulse cache and also persist it using Liferay Objects.

Liferay.OAuth2Client.FromUserAgentApplication("pulse-micro-service-oauth-application-user-agent").fetch("http://localhost:58080/api/campaigns", {method:"POST",headers:{"Content-Type":"application/json"},body: JSON.stringify({name:"Test",campaignUrl:"/redirect"})}).then((response) => console.log(response))

**At this time there are issues with the  Liferay OAuth2 Client. It does not pass the Bearer token when the headers are overridden and in the case of this example, the content type needs to be set to application/json in order to be accepted by the microservice API.
In order to work around this, the _getOrRequestToken() method is being used and this does not refresh the Bearer token which can lead to 401 responses**

### Curl

The following CURL command will create a new campaign within the Pulse cache and also persist it using Liferay Objects.

curl \
--verbose \
--header "Content-Type: application/json" \
--header "Authorization: Bearer <token>" \
--request POST \
--data '{"name":"Test","campaignUrl":"/redirect"}' \
http://localhost:58080/api/campaigns