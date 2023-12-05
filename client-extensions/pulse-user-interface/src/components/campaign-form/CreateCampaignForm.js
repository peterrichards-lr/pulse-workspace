import ClayForm from '@clayui/form'
import ClayButton from '@clayui/button'
import Sheet from "../common/Sheet"
import {Liferay} from "../../common/services/liferay/liferay"
import UtmDetails from "./UtmDetails"
import BasicDetails from "./BasicDetails"
import Lifecycle from "./Lifecycle"
import {useForm} from "react-hook-form"

const USE_LIFERAY_OAUTH2_CLIENT_FOR_POST = process.env.REACT_APP_USE_LIFERAY_OAUTH2_CLIENT_FOR_POST
const USER_AGENT_ERC = process.env.REACT_APP_USER_AGENT_ERC
const PULSE_SERVER_PROTOCOL =  process.env.REACT_APP_PULSE_SERVER_PROTOCOL
const PULSE_SERVER_HOSTNAME =  process.env.REACT_APP_PULSE_SERVER_HOSTNAME
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

    const {register, handleSubmit, control, setValue} = useForm()
    const onSubmit = (data) => {
        console.log(data);
        console.log(USE_LIFERAY_OAUTH2_CLIENT_FOR_POST);

        const oAuth2Client = Liferay.OAuth2Client.FromUserAgentApplication(USER_AGENT_ERC)
        if (oAuth2Client) {
            console.log('oAuth2Client',oAuth2Client)
            oAuth2Client._getOrRequestToken().then((accessToken) => {
                console.log('accessToken', accessToken)
                const endpoint = `${PULSE_SERVER_PROTOCOL}://${PULSE_SERVER_HOSTNAME}${PULSE_CAMPAIGN_API_PATH}`

                console.log('endpoint', endpoint)
                const campaign = fetch(endpoint, {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${accessToken.access_token}`
                    },
                    body: JSON.stringify(data)
                }).then((response) => {
                    console.log('response', response);
                })
            })
        } else {
            console.log("No OAuth2 client")
        }
    }

    return (
        <div className="campaign-information">
            <ClayForm onSubmit={handleSubmit(onSubmit)}>
                <Sheet title={Liferay.Language.get('campaign-information')}>
                    <BasicDetails
                        register={register}
                        control={control}
                        setValue={setValue}
                        defaultCampaignStatus={defaultCampaignStatus}
                        campaignStatusListTypeErc={campaignStatusListTypeErc}
                    />
                    <Lifecycle
                        control={control}
                        spriteMap={spriteMap}/>
                    <UtmDetails
                        register={register}
                        spriteMap={spriteMap}/>
                    <ClayButton displayType="primary" type="submit">
                        {Liferay.Language.get('create')}
                    </ClayButton>
                </Sheet>
            </ClayForm>
        </div>
    )
}

export default CreateCampaignForm