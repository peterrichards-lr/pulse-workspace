# pulse-workspace

Pulse as a Microservice Client Extension

## Examples

### Liferay OAuth2 Client

When the following is executed within a Liferay context then the list of URL tokens currently in the Pulse cache (H2 in-memory DB) is returned.

Liferay.OAuth2Client.FromUserAgentApplication("pulse-micro-service-oauth-application-user-agent").fetch("http://localhost:58080/api/url-tokens").then((response) => console.log(response))

The following will create a new campaign within the Pulse cache and also persist it using Liferay Objects.

Liferay.OAuth2Client.FromUserAgentApplication("pulse-micro-service-oauth-application-user-agent").fetch("http://localhost:58080/api/campaigns", {method:"POST",headers:{"Content-Type":"application/json"},body: JSON.stringify({name:"Test",campaignUrl:"/redirect"})}).then((response) => console.log(response))

### Curl

The following CURL command will create a new campaign within the Pulse cache and also persist it using Liferay Objects.

curl \
--verbose \
--header "Content-Type: application/json" \
--header "Authorization: Bearer <token>" \
--request POST \
--data '{"name":"Test","campaignUrl":"/redirect"}' \
http://localhost:58080/api/campaigns