import ClayPanel from "@clayui/panel"
import ClayForm, {ClayInput} from '@clayui/form'
import {Liferay} from "../../common/services/liferay/liferay"
import RequiredMask from "../common/RequiredMark"
import LiferayPageSelector from "./LiferayPageSelector"
import CampaignStatusSelector from "./CampaignStatusSelector"

const BasicDetails = ({
                          register,
                          control,
                          setValue,
                          defaultCampaignStatus,
                          campaignStatusListTypeErc
                      }) => {
    return (
        <ClayPanel
            displayTitle={Liferay.Language.get(
                'basic-details'
            )}
            displayType="unstyled"
        >
            <ClayPanel.Body>
                <ClayForm.Group>
                    <label htmlFor="name">{Liferay.Language.get('name')}
                        <RequiredMask/>
                    </label>
                    <ClayInput
                        type="text"
                        {...register("name", {required: true})}
                    />
                </ClayForm.Group>
                <ClayForm.Group>
                    <label htmlFor="description">{Liferay.Language.get('description')}</label>
                    <ClayInput
                        component="textarea"
                        type="text"
                        {...register("description")}
                    />
                </ClayForm.Group>
                <LiferayPageSelector
                    control={control}
                    setValue={setValue}
                />

                <CampaignStatusSelector
                    control={control}
                    setValue={setValue}
                    campaignStatusListTypeErc={campaignStatusListTypeErc}
                    defaultValue={defaultCampaignStatus}
                />
            </ClayPanel.Body>
        </ClayPanel>
    )

}

export default BasicDetails