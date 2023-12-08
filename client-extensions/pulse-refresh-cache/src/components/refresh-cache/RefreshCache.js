import ClayButton from "@clayui/button";
import {Liferay} from "../../common/services/liferay/liferay";

const USE_LIFERAY_OAUTH2_CLIENT_FOR_POST = process.env.REACT_APP_USE_LIFERAY_OAUTH2_CLIENT_FOR_POST
const USER_AGENT_ERC = process.env.REACT_APP_USER_AGENT_ERC
const PULSE_SERVER_PROTOCOL = process.env.REACT_APP_PULSE_SERVER_PROTOCOL
const PULSE_SERVER_HOSTNAME = process.env.REACT_APP_PULSE_SERVER_HOSTNAME
const PULSE_REFRESH_CACHE_API_PATH = '/api/refresh-cache'

const RefreshCache = () => {
    const handleSubmit = () => {
        console.log(USE_LIFERAY_OAUTH2_CLIENT_FOR_POST)

        const oAuth2Client = Liferay.OAuth2Client.FromUserAgentApplication(USER_AGENT_ERC)
        if (oAuth2Client) {
            console.log('oAuth2Client', oAuth2Client)
            oAuth2Client._getOrRequestToken().then((accessToken) => {
                console.log('accessToken', accessToken)
                const endpoint = `${PULSE_SERVER_PROTOCOL}://${PULSE_SERVER_HOSTNAME}${PULSE_REFRESH_CACHE_API_PATH}`

                console.log('endpoint', endpoint)
                fetch(endpoint, {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${accessToken.access_token}`
                    }
                })
                    .then(response => {
                        if ((response.ok && response.status !== 204) || response.status === 400) {
                            return response.json().then(body => ({
                                status: response.status,
                                ok: response.ok,
                                body: body
                            }))
                        } else if (response.status === 204) {
                            return {
                                status: response.status,
                                ok: response.ok,
                                body: ""
                            }
                        } else {
                            throw new Error('Unauthorized', {cause: response.status})
                        }
                    })
                    .then((response) => {
                        let toastConfig
                        if (response.ok) {
                            toastConfig = {
                                message: 'The cache was refreshed successfully',
                                title: 'Success',
                                type: 'success',
                                toastProps: {
                                    autoClose: 1000,
                                    className: 'alert-success',
                                    style: {top: 0}
                                }
                            }
                        } else if (response.status === 400) {
                            console.debug('response', response);
                            toastConfig = {
                                message: response.body.message,
                                title: 'Error',
                                type: 'warning',
                                toastProps: {
                                    autoClose: 1000,
                                    className: 'alert-warning',
                                    style: {top: 0}
                                }
                            }
                        }
                        Liferay.Util.openToast(toastConfig)
                    }).catch((reason) => {
                    let toastConfig
                    if (reason.cause === 401) {
                        toastConfig = {
                            message: 'The bearer token has expired',
                            title: 'Error',
                            type: 'danger',
                            toastProps: {
                                autoClose: 1000,
                                className: 'alert-danger',
                                style: {top: 0}
                            }
                        }
                    } else {
                        toastConfig = {
                            message: 'An error occurred when refreshing the cache',
                            title: 'Error',
                            type: 'danger',
                            toastProps: {
                                autoClose: 1000,
                                className: 'alert-danger',
                                style: {top: 0}
                            }
                        }
                    }
                    Liferay.Util.openToast(toastConfig)
                    console.error(reason)
                })
            })
        } else {
            console.log("No OAuth2 client")
        }
    }

    return (
        <ClayButton displayType="primary" onClick={handleSubmit}>
            {Liferay.Language.get('refresh-cache')}
        </ClayButton>
    )
}

export default RefreshCache