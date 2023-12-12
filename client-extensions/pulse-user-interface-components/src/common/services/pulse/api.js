import {Liferay} from "../liferay/liferay";

const USE_LIFERAY_OAUTH2_CLIENT_FOR_POST = process.env.REACT_APP_USE_LIFERAY_OAUTH2_CLIENT_FOR_POST?.toLowerCase() === "true"
const USER_AGENT_ERC = process.env.REACT_APP_USER_AGENT_ERC

const postFetch = async (path, payload = {}) => {
    const oAuth2Client = Liferay.OAuth2Client.FromUserAgentApplication(USER_AGENT_ERC)
    if (!oAuth2Client) {
        throw new Error('Unable to find the oAuth2 client - ' + USER_AGENT_ERC)
    }
    console.debug('oAuth2Client', oAuth2Client)

    console.debug('payload', payload)
    const json = JSON.stringify(payload)
    console.debug('json', json)

    console.debug('Use Liferay.OAuth2Client for POST requests', USE_LIFERAY_OAUTH2_CLIENT_FOR_POST);
    if (USE_LIFERAY_OAUTH2_CLIENT_FOR_POST) {
        return oAuth2Client.fetch(path, {method: "POST", body: json})
    } else {
        const accessToken = await oAuth2Client._getOrRequestToken()
        console.debug('accessToken', accessToken)
        const endpoint = `${oAuth2Client.homePageURL}${path}`
        return fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${accessToken.access_token}`
            },
            body: json
        }).then((response) => {
            if (response.ok) {
                return response.json()
            }
            return Promise.reject(response)
        })
    }
}

const getFetch = (path) => {
    return Liferay.OAuth2Client.FromUserAgentApplication(USER_AGENT_ERC)
        .fetch(path);
}

export {postFetch, getFetch}