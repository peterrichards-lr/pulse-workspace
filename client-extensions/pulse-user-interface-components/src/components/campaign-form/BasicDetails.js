import ClayPanel from "@clayui/panel"
import ClayForm, {ClayInput} from '@clayui/form'
import {Liferay} from "../../common/services/liferay/liferay"
import RequiredMask from "../common/RequiredMark"
import LiferayPageSelector from "./LiferayPageSelector"
import CampaignStatusSelector from "./CampaignStatusSelector"
import {useTranslation} from "react-i18next"

const BasicDetails = ({
                          register,
                          control,
                          setValue,
                          defaultCampaignStatus,
                          campaignStatusListTypeErc,
                          errors,
                          spriteMap
                      }) => {
    const {t} = useTranslation()

    return (
        <ClayPanel
            displayTitle={t('basicDetailsPanelTitle')}
            displayType="unstyled"
        >
            <ClayPanel.Body>
                <ClayForm.Group className={`${errors.name ? "has-error" : ""}`}>
                    <label htmlFor="name">{t('name')}
                        <RequiredMask/>
                    </label>
                    <ClayInput
                        type="text"
                        {...register("name", {required: true})}
                    />
                    {errors.name && <ClayForm.FeedbackItem>
                        <ClayForm.FeedbackIndicator
                            spritemap={spriteMap}
                            symbol="exclamation-full"
                        />
                        {"The name is required."}
                    </ClayForm.FeedbackItem>}
                </ClayForm.Group>
                <ClayForm.Group>
                    <label htmlFor="description">{t('description')}</label>
                    <ClayInput
                        component="textarea"
                        type="text"
                        {...register("description")}
                    />
                </ClayForm.Group>
                <LiferayPageSelector
                    control={control}
                    setValue={setValue}
                    spritemap={spriteMap}
                    errors={errors}
                />

                <CampaignStatusSelector
                    control={control}
                    setValue={setValue}
                    campaignStatusListTypeErc={campaignStatusListTypeErc}
                    defaultValue={defaultCampaignStatus}
                    spritemap={spriteMap}
                    errors={errors}
                />
            </ClayPanel.Body>
        </ClayPanel>
    )
}

export default BasicDetails