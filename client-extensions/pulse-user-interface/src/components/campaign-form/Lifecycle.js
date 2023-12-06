import ClayPanel from "@clayui/panel"
import {Liferay} from "../../common/services/liferay/liferay"
import ClayDatePickerController from "../react-hook-form/ClayDatePickerController"
import ClayForm from "@clayui/form";

const Lifecycle = ({
                       control,
                       spriteMap,
                       errors,
                       startYear = new Date().getFullYear(),
                       numberOfSelectableYears = 5
                   }) => {
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
            displayTitle={Liferay.Language.get(
                'lifecycle'
            )}
            displayType="unstyled"
            showCollapseIcon={true}
            spritemap={spriteMap}
        >
            <ClayPanel.Body>
                <ClayForm.Group className={`${errors.startDate ? "has-error" : ""}`}>
                    <ClayDatePickerController
                        name="startDate"
                        label={Liferay.Language.get('start-date')}
                        control={control}
                        years={years}
                        spritemap={spriteMap}
                        placeholder={placeholder}/>
                    {errors.startDate && <ClayForm.FeedbackItem>
                        <ClayForm.FeedbackIndicator
                            spritemap={spriteMap}
                            symbol="exclamation-full"
                        />
                        {"The start date is invalid."}
                    </ClayForm.FeedbackItem>}
                </ClayForm.Group>

                <ClayForm.Group className={`${errors.endDate ? "has-error" : ""}`}>
                    <ClayDatePickerController
                        name="endDate"
                        label={Liferay.Language.get('end-date')}
                        control={control}
                        years={years}
                        spritemap={spriteMap}
                        placeholder={placeholder}/>
                    {errors.endDate && <ClayForm.FeedbackItem>
                        <ClayForm.FeedbackIndicator
                            spritemap={spriteMap}
                            symbol="exclamation-full"
                        />
                        {"The end date is invalid."}
                    </ClayForm.FeedbackItem>}
                </ClayForm.Group>
            </ClayPanel.Body>
        </ClayPanel>
    )

}

export default Lifecycle