import {Liferay} from "../../common/services/liferay/liferay"
import ClayForm, {ClayInput} from '@clayui/form'
import ClayPanel from "@clayui/panel"
import {useTranslation} from "react-i18next"

const UtmDetails = ({spriteMap, register}) => {
    const {t} = useTranslation()
    return (
        <ClayPanel
            collapsable
            displayTitle={t('utmDataPanelTitle')}
            displayType="unstyled"
            showCollapseIcon={true}
            spritemap={spriteMap}
        >
            <ClayPanel.Body>
                <ClayForm.Group>
                    <label htmlFor="utmContent">{t('utmContent')}</label>
                    <ClayInput
                        type="text"
                        {...register("utmContent")}
                    />
                </ClayForm.Group>
                <ClayForm.Group>
                    <label htmlFor="utmMedium">{t('utmMedium')}</label>
                    <ClayInput
                        type="text"
                        {...register("utmMedium")}
                    />
                </ClayForm.Group>
                <ClayForm.Group>
                    <label htmlFor="utmSource">{t('utmSource')}</label>
                    <ClayInput
                        type="text"
                        {...register("utmSource")}
                    />
                </ClayForm.Group>
                <ClayForm.Group>
                    <label htmlFor="utmTerm">{t('utmTerm')}</label>
                    <ClayInput
                        type="text"
                        {...register("utmTerm")}
                    />
                </ClayForm.Group>
            </ClayPanel.Body>
        </ClayPanel>
    )
}

export default UtmDetails