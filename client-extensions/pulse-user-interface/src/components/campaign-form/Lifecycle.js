import ClayPanel from "@clayui/panel"
import {Liferay} from "../../common/services/liferay/liferay"
import ClayDatePickerController from "../react-hook-form/ClayDatePickerController"

const Lifecycle = ({
                       control,
                       spriteMap,
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
                <ClayDatePickerController
                    name="startDate"
                    label={Liferay.Language.get('start-date')}
                    control={control}
                    years={years}
                    spritemap={spriteMap}
                    placeholder={placeholder}/>

                <ClayDatePickerController
                    name="endDate"
                    label={Liferay.Language.get('end-date')}
                    control={control}
                    years={years}
                    spritemap={spriteMap}
                    placeholder={placeholder}/>
            </ClayPanel.Body>
        </ClayPanel>
    )

}

export default Lifecycle