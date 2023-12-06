import ClayForm from '@clayui/form'
import ClayButton from '@clayui/button'
import Sheet from "../common/Sheet"
import {Liferay} from "../../common/services/liferay/liferay"
import UtmDetails from "./UtmDetails"
import BasicDetails from "./BasicDetails"
import Lifecycle from "./Lifecycle"
import {useForm} from "react-hook-form"
import {useEffect, useState} from "react";

const USE_LIFERAY_OAUTH2_CLIENT_FOR_POST = process.env.REACT_APP_USE_LIFERAY_OAUTH2_CLIENT_FOR_POST
const USER_AGENT_ERC = process.env.REACT_APP_USER_AGENT_ERC
const PULSE_SERVER_PROTOCOL = process.env.REACT_APP_PULSE_SERVER_PROTOCOL
const PULSE_SERVER_HOSTNAME = process.env.REACT_APP_PULSE_SERVER_HOSTNAME
const PULSE_CAMPAIGN_API_PATH = '/api/campaigns'

const spriteMap = Liferay.Icons.spritemap

const CreateCampaignForm = ({
                                campaignStatusListTypeErc,
                                defaultCampaignStatus
                            }) => {
    campaignStatusListTypeErc = campaignStatusListTypeErc ? campaignStatusListTypeErc : '89201798-3fc2-6d45-d924-316395794c25'
    defaultCampaignStatus = defaultCampaignStatus ? defaultCampaignStatus : 'draft'
    console.log('campaignStatusListTypeErc', campaignStatusListTypeErc)
    console.log('defaultCampaignStatus', defaultCampaignStatus)

    const {register, handleSubmit, control, setValue, reset, formState: {errors}} = useForm()
    const [isSubmitSuccessful, setSubmitSuccessful] = useState(false)
    const resetState = {
        name: null,
        description: null,
        campaignStatus: defaultCampaignStatus,
        startDate: null,
        endDate: null,
        utmContent: null,
        utmMedium: null,
        utmSource: null,
        utmTerm: null,
    }

    useEffect(() => {
        reset(resetState)
    }, [isSubmitSuccessful])

    const onSubmit = (data) => {
        console.log(data);
        console.log(USE_LIFERAY_OAUTH2_CLIENT_FOR_POST);

        const oAuth2Client = Liferay.OAuth2Client.FromUserAgentApplication(USER_AGENT_ERC)
        if (oAuth2Client) {
            console.log('oAuth2Client', oAuth2Client)
            oAuth2Client._getOrRequestToken().then((accessToken) => {
                console.log('accessToken', accessToken)
                const endpoint = `${PULSE_SERVER_PROTOCOL}://${PULSE_SERVER_HOSTNAME}${PULSE_CAMPAIGN_API_PATH}`

                console.log('endpoint', endpoint)
                fetch(endpoint, {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${accessToken.access_token}`
                    },
                    body: JSON.stringify(data)
                })
                    .then(response => {
                        if (response.ok || response.status === 400) {
                            return response.json().then(body => ({
                                status: response.status,
                                ok: response.ok,
                                body: body
                            }))
                        } else {
                            throw new Error('Unauthorized', {cause: response.status})
                        }
                    })
                    .then((response) => {
                        let toastConfig
                        if (response.ok) {
                            setSubmitSuccessful(true)
                            toastConfig = {
                                message: 'The campaign was created successfully',
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
                        console.error(reason)
                    } else {
                        toastConfig = {
                            message: 'An error occurred when creating the campaign',
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
                })
            })
        } else {
            console.log("No OAuth2 client")
        }
    }

    return (
        <div className="campaign-information">
            <ClayForm onSubmit={handleSubmit(onSubmit)}>
                <Sheet title={Liferay.Language.get('campaign-information')} footer={
                    <div className="btn-group-item">
                        <div className="btn-group-item">
                            <ClayButton displayType="primary" type="submit">
                                {Liferay.Language.get('create')}
                            </ClayButton>
                            <ClayButton displayType="secondary" type="button" onClick={() => reset(resetState)}>
                                {Liferay.Language.get('reset')}
                            </ClayButton>
                        </div>
                    </div>
                }>
                    <BasicDetails
                        register={register}
                        control={control}
                        setValue={setValue}
                        defaultCampaignStatus={defaultCampaignStatus}
                        campaignStatusListTypeErc={campaignStatusListTypeErc}
                        spriteMap={spriteMap}
                        errors={errors}
                    />
                    <Lifecycle
                        control={control}
                        spriteMap={spriteMap}
                        errors={errors}
                    />
                    <UtmDetails
                        register={register}
                        spriteMap={spriteMap}
                        errors={errors}
                    />
                </Sheet>
            </ClayForm>
        </div>
    )
}

export default CreateCampaignForm