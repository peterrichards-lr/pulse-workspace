import ClayForm from '@clayui/form'
import ClayButton from '@clayui/button'
import Sheet from "../common/Sheet"
import {Liferay} from "../../common/services/liferay/liferay"
import UtmDetails from "./UtmDetails"
import BasicDetails from "./BasicDetails"
import Lifecycle from "./Lifecycle"
import {useForm} from "react-hook-form"

const spriteMap = Liferay.Icons.spritemap

const CreateCampaignForm = ({
                                campaignStatusListTypeErc,
                                defaultCampaignStatus
                            }) => {
    campaignStatusListTypeErc = campaignStatusListTypeErc ? campaignStatusListTypeErc : '89201798-3fc2-6d45-d924-316395794c25'
    defaultCampaignStatus = defaultCampaignStatus ? defaultCampaignStatus : 'draft'
    console.log('campaignStatusListTypeErc', campaignStatusListTypeErc);
    console.log('defaultCampaignStatus', defaultCampaignStatus);

    const {register, handleSubmit, control, setValue} = useForm()
    const onSubmit = (data) => {
        console.log(data);
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