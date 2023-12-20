import ClayPanel from "@clayui/panel"
import {Liferay} from "../../common/services/liferay/liferay"
import ClayDatePickerController from "../react-hook-form/ClayDatePickerController"
import ClayForm from "@clayui/form"
import {useTranslation} from "react-i18next"

const Lifecycle = ({
                       control,
                       spriteMap,
                       errors,
                       startYear = new Date().getFullYear(),
                       numberOfSelectableYears = 5
                   }) => {
    const {t} = useTranslation()
    const placeholder = "YYYY-MM-DD"
    const lastDate = new Date();
    lastDate.setFullYear(startYear + numberOfSelectableYears)
    const endYear = lastDate.getFullYear()
    const years = {
        end: endYear,
        start: startYear
    }

    return (
        <ClayPanel
            collapsable
            displayTitle={t('lifecyclePanelTitle')}
            displayType="unstyled"
            showCollapseIcon={true}
            spritemap={spriteMap}
        >
            <ClayPanel.Body>
                <ClayForm.Group className={`${errors.startDate ? "has-error" : ""}`}>
                    <ClayDatePickerController
                        name="startDate"
                        label={t('startDate')}
                        control={control}
                        years={years}
                        spritemap={spriteMap}
                        placeholder={placeholder}/>
                    {errors.startDate && <ClayForm.FeedbackItem>
                        <ClayForm.FeedbackIndicator
                            spritemap={spriteMap}
                            symbol="exclamation-full"
                        />
                        {t('startDateInvalid')}
                    </ClayForm.FeedbackItem>}
                </ClayForm.Group>

                <ClayForm.Group className={`${errors.endDate ? "has-error" : ""}`}>
                    <ClayDatePickerController
                        name="endDate"
                        label={t('endDate')}
                        control={control}
                        years={years}
                        spritemap={spriteMap}
                        placeholder={placeholder}/>
                    {errors.endDate && <ClayForm.FeedbackItem>
                        <ClayForm.FeedbackIndicator
                            spritemap={spriteMap}
                            symbol="exclamation-full"
                        />
                        {t('endDateInvalid')}
                    </ClayForm.FeedbackItem>}
                </ClayForm.Group>
            </ClayPanel.Body>
        </ClayPanel>
    )
}

export default Lifecycle